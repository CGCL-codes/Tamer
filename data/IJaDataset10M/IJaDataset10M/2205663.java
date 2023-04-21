package org.dmd.dmc.presentation;

/**
 * The DmcPresentationTrackerIF defines a component that is responsible for tracking
 * the status of a component that implements the DmcPresentationIF. Generally,
 * components that implement this interface 
 */
public interface DmcPresentationTrackerIF {

    /**
	 * Adds the specified presentation implementation to the tracker. The tracker
	 * should also set itself on the presentation by calling its setTracker() method.
	 * @param dpi
	 */
    public void track(DmcPresentationIF dpi);

    /**
	 * Adds a listener that's interested in the tracker's state of readiness.
	 * @param listener
	 */
    public void addReadyListener(DmcReadyListenerIF listener);

    /**
	 * Adds a listener that's interested in whether anything has changed.
	 * @param listener
	 */
    public void addChangeListener(DmcChangeListenerIF listener);

    /**
	 * Adds a listener that's interested in any value change in a presenter.
	 * @param listener
	 */
    public void addValueChangeListener(DmcValueChangeListenerIF listener);

    /**
	 * Indicates that the presentation component is "ready". Exactly what "ready" means
	 * is implementation specific.
	 * @param dpi
	 */
    public void isReady(DmcPresentationIF dpi);

    /**
	 * Indicates that the presentation component is "not ready". Exactly what "not ready" means
	 * is implementation specific.
	 * @param dpi
	 */
    public void isNotReady(DmcPresentationIF dpi);

    /**
	 * Calling this method will cause the tracker to query all of its presentation implementations
	 * to determine their current state.
	 */
    public void startTracking();

    /**
	 * Calling this method will call the reset method on all presentations; this will
	 * cause them all to display their original values.
	 */
    public void reset();

    /**
	 * Sets whether the tracker is in debug mode or not.
	 * @param d true for debug.
	 */
    public void debug(boolean d);

    /**
	 * @return the tracker's debug state.
	 */
    public boolean debug();
}
