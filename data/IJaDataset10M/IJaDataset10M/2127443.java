package howbuy.android.palmfund.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import android.view.View.OnTouchListener;

public class CustomSliderView extends FrameLayout implements OnTouchListener {

    private float mTouchXPosition;

    protected ImageView mThumbImageView, mSliderBarImageView;

    protected Bitmap mThumbBitmap;

    protected Bitmap mSliderBarBitmap;

    protected int mThumbResourceId;

    protected int mSliderBarResourceId;

    protected int mMinValue = 0;

    protected int mMaxValue = 100;

    protected float mTargetValue = 0;

    protected int mSliderLeftPosition, mSliderRightPosition;

    protected OnTouchListener mDelegateOnTouchListener;

    /**
	 * Default constructors. Just tell Android that we're doing custom drawing
	 * and that we want to listen to touch events.
	 */
    public CustomSliderView(Context context) {
        super(context);
        setWillNotDraw(false);
        setOnTouchListener(this);
    }

    public CustomSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setOnTouchListener(this);
    }

    public CustomSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        this.setOnTouchListener(this);
    }

    public void setDelegateOnTouchListener(OnTouchListener onTouchListener) {
        mDelegateOnTouchListener = onTouchListener;
    }

    public void setResourceIds(int thumbResourceId, int sliderBarResourceId) {
        mThumbResourceId = thumbResourceId;
        mSliderBarResourceId = sliderBarResourceId;
        mThumbImageView = null;
        mSliderBarImageView = null;
    }

    public boolean onTouch(View view, MotionEvent event) {
        return false;
    }

    public void setRange(int min, int max) {
        mMinValue = min;
        mMaxValue = max;
    }

    public void setScaledValue(int value) {
        mTargetValue = value;
        invalidate();
    }

    /**
	 * @return The actual value of the thumb position, scaled to the min and max
	 *         value
	 */
    public int getScaledValue() {
        return (int) mMinValue + (int) ((mMaxValue - mMinValue) * getPercentValue());
    }

    /**
	 * @return The percent value of the current thumb position.
	 */
    public float getPercentValue() {
        float fillWidth = mSliderBarImageView.getWidth();
        float relativeTouchX = mTouchXPosition - mSliderBarImageView.getLeft();
        float percentage = relativeTouchX / fillWidth;
        return percentage;
    }

    /**
	 * 
	 * @param percentValue
	 *            between 0 to 1.0f
	 */
    public void setPercentValue(float percentValue) {
        float position = mSliderLeftPosition + percentValue * (mSliderRightPosition - mSliderLeftPosition - mThumbBitmap.getWidth());
        mTouchXPosition = position;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mThumbImageView == null) {
            mThumbImageView = (ImageView) this.getChildAt(1);
            this.removeView(mThumbImageView);
            if (mThumbResourceId > 0) {
                mThumbBitmap = BitmapFactory.decodeResource(getContext().getResources(), mThumbResourceId);
                mThumbImageView.setImageBitmap(mThumbBitmap);
            }
            mThumbImageView.setDrawingCacheEnabled(true);
            mThumbBitmap = mThumbImageView.getDrawingCache(true);
        }
        if (mSliderBarImageView == null) {
            mSliderBarImageView = (ImageView) this.getChildAt(0);
            this.removeView(mSliderBarImageView);
            if (mSliderBarResourceId > 0) {
                mSliderBarBitmap = BitmapFactory.decodeResource(getContext().getResources(), mSliderBarResourceId);
                mSliderBarImageView.setImageBitmap(mSliderBarBitmap);
            }
            mSliderBarImageView.setDrawingCacheEnabled(true);
            mSliderBarBitmap = mSliderBarImageView.getDrawingCache(true);
            mSliderLeftPosition = mSliderBarImageView.getLeft();
            if (mSliderBarImageView != null && mSliderBarBitmap != null) {
                mSliderRightPosition = mSliderBarImageView.getLeft() + mSliderBarBitmap.getWidth();
            }
        }
        if (mTargetValue > 0) {
            float fillWidth = mSliderBarImageView.getMeasuredWidth();
            float range = (mMaxValue - mMinValue);
            mTouchXPosition = ((mTargetValue - mMinValue) / range) * fillWidth;
            mTargetValue = 0;
        }
        if (mTouchXPosition < mSliderLeftPosition) mTouchXPosition = mSliderLeftPosition; else if (mTouchXPosition > mSliderRightPosition) mTouchXPosition = mSliderRightPosition;
        if (mSliderBarBitmap != null) canvas.drawBitmap(mSliderBarBitmap, mSliderLeftPosition, mSliderBarImageView.getTop(), null);
        if (mThumbBitmap != null) canvas.drawBitmap(mThumbBitmap, mTouchXPosition - mThumbBitmap.getWidth() / 2, mThumbImageView.getTop(), null);
    }
}
