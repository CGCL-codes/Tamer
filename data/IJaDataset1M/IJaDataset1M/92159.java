package android.view.animation;

import android.internal.Assert;

public abstract class Animation {

    public interface AnimationListener {
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        Assert.NOT_IMPLEMENTED();
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Assert.NOT_IMPLEMENTED();
    }

    public void setDuration(long durationMillies) {
        Assert.NOT_IMPLEMENTED();
    }

    public void setFillAfter(boolean fillAfter) {
        Assert.NOT_IMPLEMENTED();
    }

    public void setInterpolator(Interpolator i) {
        Assert.NOT_IMPLEMENTED();
    }

    public void setAnimationListener(AnimationListener listener) {
        Assert.NOT_IMPLEMENTED();
    }
}
