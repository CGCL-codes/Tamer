package com.jetigy.tach.common.lifecycle;

/**
 * Startable.java
 * <p>
 * An interface that specifies a single method: start(), used 
 * to control objects that can be started.
 * @author Tim Osten
 *
 */
public interface Startable {

    /**
	 * Start the object's process
	 */
    public void start();
}
