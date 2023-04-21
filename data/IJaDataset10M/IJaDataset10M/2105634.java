package edu.washington.assist.animation;

import java.util.EventListener;

public interface AnimationListener extends EventListener {

    /**
	 * Performed whenever the animation state is changed.
	 */
    public void animationRunning(boolean isRunning);

    /**
	 * Performed whenever the playback rate is changed.
	 */
    public void animationRateChanged(double newRate);

    public void timeZoomChanged();

    public void clusterKValueChanged(int newValue);

    public void tailLengthChanged(long newValue);
}
