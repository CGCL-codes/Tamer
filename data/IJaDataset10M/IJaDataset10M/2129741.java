package de.doubleSlash.speedTracker;

/**
 * Basic functionality for implementiung new GPSAdapter backends
 * @author cbalzer
 */
public abstract class GPSAdapter {

    /**
	 * Current connection status
	 */
    protected boolean isConnected = false;

    /**
	 * Receives GPS events
	 */
    protected GPSListener listener = null;

    /**
	 * Sets up new GPSAdapter
	 * @param listener GPSListener
	 */
    public GPSAdapter(GPSListener listener) {
        this.listener = listener;
    }

    /**
	 * Returns the current connection status
	 * @return
	 */
    public boolean isConnected() {
        return this.isConnected;
    }

    /**
	 * Connect to GPSAdapter
	 */
    public abstract void connect();

    /**
	 * Reconnect to the GPSAdapter if the connection is lost
	 */
    public abstract void reconnect();

    /**
	 * Close the GPSAdapter connection
	 */
    public abstract void close();

    /**
	 * call cancel event
	 */
    public abstract void canceled();
}
