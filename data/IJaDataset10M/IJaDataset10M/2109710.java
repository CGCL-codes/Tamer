package haframework.gui.effect;

import haframework.events.TouchEvent;

/**
 * @author	hjb
 * @desc	this class is used to implement the ui effect that drag & slide ui widget in 1d
 */
public class DragSlide {

    public static final int SlideDir_hor = 1;

    public static final int SlideDir_ver = 2;

    protected float m_minVal = 0;

    protected float m_maxVal = 0;

    protected int m_dir = SlideDir_hor;

    /**
	 * @desc	constructor
	 */
    public DragSlide(float minVal, float maxVal, int dir) {
        m_minVal = minVal;
        m_maxVal = maxVal;
        m_dir = dir;
    }

    /**
	 * @desc	pull touch event
	 * @param	evt
	 */
    public void PullEvent(TouchEvent evt) {
    }

    /**
	 * @desc	frame function
	 * @param	elapsed
	 */
    public void Frame(float elapsed) {
    }

    /**
	 * @desc	return the current position
	 * @return
	 */
    public float GetPosition() {
        return 0.0f;
    }
}
