package org.androidsoft.coloring.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import org.androidsoft.coloring.R;
import org.androidsoft.coloring.ui.activity.AbstractColoringActivity;

public class ColoringButton extends View {

    public ColoringButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setClickable(true);
    }

    public ColoringButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.coloringButtonStyle);
    }

    public ColoringButton(Context context) {
        this(context, null);
    }

    public static int getPreferredWidth() {
        return AbstractColoringActivity.getDisplayWitdh() / 8;
    }

    public static int getPreferredHeight() {
        return AbstractColoringActivity.getDisplayHeight() / 8;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        _touchPushed = isPushed(e, _touchPushed);
        return super.onTouchEvent(e);
    }

    public boolean isPushedDown() {
        return _touchPushed;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private boolean isPushed(MotionEvent e, boolean original) {
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                invalidate();
                return false;
            default:
                return original;
        }
    }

    private int measureWidth(int measureSpec) {
        return getMeasurement(measureSpec, getPreferredWidth());
    }

    private int measureHeight(int measureSpec) {
        return getMeasurement(measureSpec, getPreferredHeight());
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement = 0;
        switch(MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    private boolean _touchPushed;
}
