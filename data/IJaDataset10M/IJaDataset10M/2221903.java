package net.sf.josceleton.connection.api;

/**
 * @since 0.1
 */
public interface Connector {

    /**
	 * Opens a connection to the default osceleton port.
	 * 
	 * @since 0.1
	 */
    Connection openConnection();

    /**
	 * @since 0.1
	 */
    Connection openConnectionOnPort(final int port);
}
