package com.jmg.gesture.event;

import com.jmg.gesture.Gesture;

/**
 * GesturePerformedListener interface used to listen for valid gestures
 * being performed.
 * 
 * @author  Christopher Martin
 * @version $Id: GesturePerformedListener.java 4 2007-04-12 16:55:33Z GentlemanHal $
 */
public interface GesturePerformedListener {

    /**
     * Called when a valid mouse gesture is performed.
     * 
     * @param when The time this event happened.
     * @param gesture The <code>Gesture</code> that was performed.
     */
    public void gesturePerformed(long when, Gesture gesture);
}
