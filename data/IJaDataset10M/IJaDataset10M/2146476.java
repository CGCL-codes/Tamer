package com.sibyl.ui.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ActivityTransition extends Animation {

    private boolean mReverse;

    /**
     * Constructor of ActivityTransition: constructs a new transition
     * 
     * @param reversed  sense of the transition
     */
    public ActivityTransition(boolean reverse) {
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float alpha = interpolatedTime;
        if (!mReverse) {
            alpha = 1 - interpolatedTime;
        }
        t.setAlpha(alpha);
    }
}
