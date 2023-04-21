package com.chinacnit.eduboard.activity;

import com.chinacnit.eduboard.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class HomeActivity extends Activity implements OnTouchListener {

    LinearLayout drawingSurface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        drawingSurface = (LinearLayout) findViewById(R.id.drawingSurface);
        drawingSurface.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int a = event.getAction();
        switch(a) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return false;
    }
}
