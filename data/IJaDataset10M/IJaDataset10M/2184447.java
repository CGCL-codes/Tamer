package com.android.zweibo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class LogoActivity extends Activity {

    /** Called when the activity is first created. */
    private ImageView mainImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        mainImage = (ImageView) this.findViewById(R.id.logo);
        AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
        alpha.setDuration(3000);
        mainImage.setAnimation(alpha);
        alpha.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(intent);
                LogoActivity.this.finish();
            }
        });
    }
}
