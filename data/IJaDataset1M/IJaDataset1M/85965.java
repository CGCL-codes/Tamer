package org.monome.pages.ableton;

/**
 * Stores the state of a single clip slot.
 * 
 * @author Tom Dinchak 
 *
 */
public class AbletonClip {

    /**
	 * Indicates an empty clip slot. 
	 */
    public static final int STATE_EMPTY = 0;

    /**
	 * Indicates that the clip has been stopped.
	 */
    public static final int STATE_STOPPED = 1;

    /**
	 * Indicates that the clip is playing. 
	 */
    public static final int STATE_PLAYING = 2;

    /**
	 * Indicates that the clip has been triggered for playing or the empty clip slot has been triggered for recording. 
	 */
    public static final int STATE_TRIGGERED = 3;

    /**
	 * The current state of the clip.
	 */
    private int state;

    /**
	 * The length of the clip in bars.
	 */
    private float length;

    /**
	 * The current playhead position in the clip.
	 */
    private float position;

    public AbletonClip() {
        state = 0;
        setLength(0.0f);
        setPosition(0.0f);
    }

    /**
	 * Sets the current state of the clip.
	 * 
	 * @param state AbletonClip.STATE_EMPTY, AbletonClip.STATE_STOPPED, AbletonClip.STATE_PLAYING, or AbletonClip.STATE_TRIGGERED 
	 */
    public void setState(int state) {
        this.state = state;
    }

    /**
	 * Gets the current state of the clip.
	 * 
	 * @return state AbletonClip.STATE_EMPTY, AbletonClip.STATE_STOPPED, AbletonClip.STATE_PLAYING, or AbletonClip.STATE_TRIGGERED
	 */
    public int getState() {
        return state;
    }

    /**
	 * Sets the length of the clip.
	 * 
	 * @param length The new length.
	 */
    public void setLength(float length) {
        this.length = length;
    }

    /**
	 * Gets the length of the clip.
	 * 
	 * @return The length of the clip.
	 */
    public float getLength() {
        return length;
    }

    /**
	 * Sets the current playhead position of the clip.
	 * 
	 * @param position The current playhead position of the clip.
	 */
    public void setPosition(float position) {
        this.position = position;
    }

    /**
	 * Gets the current playhead position of the clip.
	 * 
	 * @return The current playhead position of the clip
	 */
    public float getPosition() {
        return position;
    }
}
