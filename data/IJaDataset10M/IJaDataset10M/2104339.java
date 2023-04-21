package com.manning.aip.brewmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class Splash extends BrewMapActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            startActivity(new Intent(Splash.this, Main.class));
        }
        return true;
    }
}
