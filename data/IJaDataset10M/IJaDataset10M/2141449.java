package com.chronogears.bsodroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class BsodDroidActivity extends Activity {

    private static final String STATE_ORIENTATION = "orientation";

    private static final int MENU_ABOUT = 1;

    private static final int MENU_SELECT = 2;

    private static final int MENU_FLIP = 3;

    private int orientation = ActivityInfo.SCREEN_ORIENTATION_USER;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        orientation = getPreferences(MODE_PRIVATE).getInt(STATE_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_USER);
        setRequestedOrientation(orientation);
        if (state != null) {
            state.getInt(STATE_ORIENTATION);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        int bsod = intent.getIntExtra("bsod", -1);
        startBsod(bsod);
    }

    private void startBsod(int bsod) {
        setContentView(new BsodDroidView(this, bsod));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SELECT, 0, "Select BSOD");
        menu.add(0, MENU_FLIP, 0, "Flip Orientation");
        menu.add(0, MENU_ABOUT, 0, "About");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_ABOUT:
                setContentView(R.layout.about);
                return true;
            case MENU_SELECT:
                Intent intent = new Intent(this, TimebombActivity.class);
                startActivity(intent);
                finish();
                return true;
            case MENU_FLIP:
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_USER || orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; else orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putInt(STATE_ORIENTATION, orientation);
                editor.commit();
                setRequestedOrientation(orientation);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
