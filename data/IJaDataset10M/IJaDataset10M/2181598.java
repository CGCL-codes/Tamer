package org.omg.tacsit.controller;

/**
 * ViewportChangeListener is the type of object that is notified about a
 * Viewport that it is registered for notification of changes
 */
public interface ViewportChangeListener {

    /**
	 * This method should be called by the Viewport where this
	 * ViewportChangeListener is registered in case a Viewport is changed.
	 * 
	 * The details of the change can be obtained through the given
	 * ViewportChangeEvent.
	 * 
	 */
    void viewportChanged(ViewportChangeEvent event);
}
