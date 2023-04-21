package com.mossdev.android.moteapp;

import com.mossdev.android.moteapp.comms.Commands;
import com.mossdev.android.moteapp.listeners.ButtonOnClickListener;
import com.mossdev.android.moteapp.listeners.TouchpadTouchListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * @author Andrew Moss
 *
 */
public class MoteAppTouchpadActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touchpad);
        ImageButton back_button = (ImageButton) findViewById(R.id.TouchBack);
        back_button.setOnClickListener(new ButtonOnClickListener(Commands.BACK));
        View touchview = findViewById(R.id.TouchView);
        touchview.setOnTouchListener(new TouchpadTouchListener());
    }
}
