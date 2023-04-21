package com.samples.frameanimationxml;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FrameAnimationActivity extends Activity {

    AnimationDrawable mAnim;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageView image = (ImageView) findViewById(R.id.image);
        image.setBackgroundResource(R.anim.android_anim);
        mAnim = (AnimationDrawable) image.getBackground();
        final Button btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mAnim.start();
            }
        });
        final Button btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mAnim.stop();
            }
        });
    }
}
