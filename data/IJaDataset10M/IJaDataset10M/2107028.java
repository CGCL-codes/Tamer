package com.totsp.restaurant;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.totsp.restaurant.data.Review;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Show Review detail for review item user selected.
 * 
 * @author charliecollins
 */
public class ReviewDetail extends Activity {

    private static final int MENU_CALL = 0;

    private static final int MENU_MAP = 1;

    private static final int MENU_WEB = 2;

    private String link;

    private TextView location;

    private TextView name;

    private TextView phone;

    private TextView rating;

    private TextView review;

    private ImageView reviewImage;

    private Bitmap imageBitmap;

    private String imageLink;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (imageBitmap != null) {
                reviewImage.setImageBitmap(imageBitmap);
            } else {
                reviewImage.setImageResource(R.drawable.no_review_image);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.review_detail);
        this.name = (TextView) findViewById(R.id.name_detail);
        this.rating = (TextView) findViewById(R.id.rating_detail);
        this.location = (TextView) findViewById(R.id.location_detail);
        this.phone = (TextView) findViewById(R.id.phone_detail);
        this.review = (TextView) findViewById(R.id.review_detail);
        this.reviewImage = (ImageView) findViewById(R.id.review_image);
        RestaurantFinderApplication application = (RestaurantFinderApplication) getApplication();
        Review currentReview = application.getCurrentReview();
        this.link = currentReview.link;
        this.imageLink = currentReview.imageLink;
        this.name.setText(currentReview.name);
        this.rating.setText(currentReview.rating);
        this.location.setText(currentReview.location);
        this.review.setText(currentReview.content);
        if ((currentReview.phone != null) && !currentReview.phone.equals("")) {
            this.phone.setText(currentReview.phone);
        } else {
            this.phone.setText("NA");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {

            public void run() {
                if ((imageLink != null) && !imageLink.equals("")) {
                    try {
                        URL url = new URL(imageLink);
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        imageBitmap = BitmapFactory.decodeStream(bis);
                    } catch (IOException e) {
                        Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, ReviewDetail.MENU_WEB, 0, R.string.menu_web_review).setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(0, ReviewDetail.MENU_MAP, 1, R.string.menu_map_review).setIcon(android.R.drawable.ic_menu_mapmode);
        menu.add(0, ReviewDetail.MENU_CALL, 2, R.string.menu_call_review).setIcon(android.R.drawable.ic_menu_call);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case MENU_WEB:
                if ((this.link != null) && !this.link.equals("")) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.link));
                    startActivity(intent);
                } else {
                    Toast.makeText(ReviewDetail.this, "Error, cannot parse web link", Toast.LENGTH_LONG).show();
                }
                return true;
            case MENU_MAP:
                if ((this.location.getText() != null) && !this.location.getText().equals("")) {
                    String intentAction = "geo:0,0?q=" + this.parseAddress(this.location.getText().toString());
                    try {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentAction));
                        this.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(ReviewDetail.this, "Error, unable to use geo action, Google Maps API add on not present?", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ReviewDetail.this, "Error, cannot parse location", Toast.LENGTH_LONG).show();
                }
                return true;
            case MENU_CALL:
                if ((this.phone.getText() != null) && !this.phone.getText().equals("") && !this.phone.getText().equals("NA")) {
                    String phoneString = parsePhone(this.phone.getText().toString());
                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneString));
                    startActivity(intent);
                } else {
                    Toast.makeText(ReviewDetail.this, "Error, cannot parse phone number", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private String parsePhone(final String p) {
        String temp = p;
        temp = temp.replaceAll("\\D", "");
        temp = temp.replaceAll("\\s", "");
        return temp.trim();
    }

    private String parseAddress(final String a) {
        String temp = a;
        temp = temp.replaceAll("\\s", "+");
        return temp.trim();
    }
}
