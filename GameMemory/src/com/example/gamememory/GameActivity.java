package com.example.gamememory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GameActivity extends Activity {

	int bear = 0x7f020000;
	int cardback = 0x7f020001;
	int eagle = 0x7f020002;
	int elephant = 0x7f020003;

	int jiraph = 0x7f020005;
	int zebra = 0x7f020007;
	int wolf = 0x7f020006;

	ArrayList<Integer> imagesNumbers;
	ArrayList<Integer> updateViewCards;

	LinearLayout ln;
	ImageView img;

	Timer timer = new Timer();

	boolean firstClicked = true;
	boolean itsAMatch = false;

	int firstCardClickedTag;
	int secondCardClickedTag;
	int clickCounter;
	int matchCounter;

	ImageView cardA, cardB;

	private final Handler myHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		imagesNumbers = new ArrayList<Integer>();
		imagesNumbers.add(bear);
		imagesNumbers.add(eagle);
		imagesNumbers.add(elephant);
		imagesNumbers.add(jiraph);
		imagesNumbers.add(zebra);
		imagesNumbers.add(wolf);
		imagesNumbers.add(bear);
		imagesNumbers.add(eagle);
		imagesNumbers.add(elephant);
		imagesNumbers.add(jiraph);
		imagesNumbers.add(zebra);
		imagesNumbers.add(wolf);

		cardA = new ImageView(this);
		cardB = new ImageView(this);

		int currentImage;

		ln = (LinearLayout) findViewById(R.id.ln1);

		for (int i = 0; i < 3; i++) {
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LayoutParams(5, 5));
			row.setOrientation(LinearLayout.HORIZONTAL);

			for (int j = 0; j < 4; j++) {
				currentImage = i * 4 + j;

				img = new ImageView(this);

				img.setImageResource(cardback);

				img.setTag(currentImage);

				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						clickCounter++;

						int number = (Integer) v.getTag();
						final ImageView imgView = (ImageView) v;

						imgView.setImageResource(imagesNumbers.get(number));
						// First image clicked
						if (firstClicked == true) {

							cardA = imgView;
							cardA.setClickable(false);

							firstCardClickedTag = number;
							firstClicked = false;
							// Second image clicked
						} else {
							cardB = imgView;
							cardB.setClickable(false);

							secondCardClickedTag = number;
							// if first image equals to the second image (by
							// intValue) then it's a match.
							if (imagesNumbers.get(number).intValue() == imagesNumbers
									.get(firstCardClickedTag).intValue()) {
								matchCounter++;
								cardA.setClickable(true);
								cardB.setClickable(true);
								// if you get 6 matches you win!
								if (matchCounter == 6) {
									myHandler.post(new Runnable() {

										@Override
										public void run() {
											new AlertDialog.Builder(
													GameActivity.this)

													.setTitle("You Win!")

													.setMessage(
															"Game completed with total of "
																	+ clickCounter
																	+ " clicks!")
													.setCancelable(true)
													.setNeutralButton(
															"Play again",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	onRestart();
																	dialog.cancel();

																}
															})

													.create().show();

										}
									});
								} else {

									// Single match
									Toast.makeText(getBaseContext(),
											"it's a match!", Toast.LENGTH_SHORT)
											.show();

									firstClicked = true;
									itsAMatch = true;
								}
								// if there wasn't a match then a timer set to
								// the first and second card clicked to change
								// the picture back to the card.
							} else {
								cardB = imgView;

								timer.schedule(new TimerTask() {

									@Override
									public void run() {
										try {
											myHandler.post(new Runnable() {

												@Override
												public void run() {

													cardA.setImageResource(cardback);
													cardB.setImageResource(cardback);
												}
											});
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
								}, 1000);

								cardA.setClickable(true);
								cardB.setClickable(true);

								Toast.makeText(getBaseContext(),
										"Sorry no match at this time!",
										Toast.LENGTH_SHORT).show();
								firstClicked = true;
								itsAMatch = false;
								secondCardClickedTag = number;
							}
						}

					}

				});

				row.addView(img, new LinearLayout.LayoutParams(150, 200));
			}

			ln.addView(row, new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
		}
		
		//Randomize cards
		long seed = System.nanoTime();
		Collections.shuffle(imagesNumbers, new Random(seed));

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	//Restart the game
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Intent i = new Intent(GameActivity.this, GameActivity.class);
		startActivity(i);
		finish();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
