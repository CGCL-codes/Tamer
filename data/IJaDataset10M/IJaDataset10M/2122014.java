package com.androidengineer.themes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * This activity's theme is set to a custom theme in the onCreate() method.
 * By default, there is no theme, but pressing the buttons will change the
 * theme dynamically.
 *
 * @author Matt Quigley
 *
 */
public class Themes extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button1:
                Utils.changeToTheme(this, Utils.THEME_DEFAULT);
                break;
            case R.id.button2:
                Utils.changeToTheme(this, Utils.THEME_WHITE);
                break;
            case R.id.button3:
                Utils.changeToTheme(this, Utils.THEME_BLUE);
                break;
        }
    }
}
