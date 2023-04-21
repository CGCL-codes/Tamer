package com.songoo;

import android.os.Bundle;
import android.view.MotionEvent;
import com.songoo.activity.IStepActivity;
import com.songoo.activity.StepActivity;
import com.songoo.step.Level1Step;

/**
 * @author Martinien
 *
 */
public class GameActivity extends StepActivity implements IStepActivity {

    private Level1Step _level1Step;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Initialize();
    }

    private void Initialize() {
        initializeLevel1Activity();
    }

    private void initializeLevel1Activity() {
        _level1Step = new Level1Step(this);
        addView(_level1Step);
        _level1Step.onStepNext = new Runnable() {

            public void run() {
            }
        };
        _level1Step.onStepPrevious = new Runnable() {

            public void run() {
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        _level1Step.Start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
