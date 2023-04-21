package com.android.gopens;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GopensTest extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bitmap[] bmImg = MapRetriever.getMaps(45.647371, 5.860171, 10);
            ImageView imView = new ImageView(this);
            imView.setImageBitmap(bmImg[3]);
            RelativeLayout l = new RelativeLayout(this);
            l.setLayoutParams(new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            l.addView(imView);
            setContentView(l);
        } catch (Exception e) {
            TextView tv = new TextView(this);
            tv.setText("Exception : " + e);
            setContentView(tv);
        }
    }
}
