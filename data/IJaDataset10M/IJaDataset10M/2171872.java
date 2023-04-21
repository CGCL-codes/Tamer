package org.lwjgl.util.model;

import java.io.Serializable;

/**
 * $Id: Frame.java 1245 2004-06-12 20:28:34Z matzon $
 * The base class for animation frames.
 * @author $Author: matzon $
 * @version $Revision: 1245 $
 */
public abstract class Frame implements Serializable, Comparable {

    public static final long serialVersionUID = 1L;

    /** Frame time */
    private final float time;

    /** User-defined action to occur after this frame has been used. May be null */
    private final String action;

    /**
	 * C'tor
	 * @param time
	 * @param action
	 */
    public Frame(float time, String action) {
        this.time = time;
        this.action = action;
    }

    /**
	 * @return the frame time
	 */
    public final float getTime() {
        return time;
    }

    public int compareTo(Object o) {
        if (o == null) {
            return 0;
        }
        if (!(o instanceof Frame)) {
            return 0;
        }
        Frame f = (Frame) o;
        if (f.time == time) {
            return 0;
        } else if (f.time > time) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
	 * Gets the user-defined animation action. This can be processed by whatever
	 * is animating the model to perform some special action after the frame is
	 * used. For example, you could use "stop" to stop the animation, or "rewind"
	 * to repeat the animation ad infinitum.
	 * @return String, or null, for no action
	 */
    public final String getAction() {
        return action;
    }
}
