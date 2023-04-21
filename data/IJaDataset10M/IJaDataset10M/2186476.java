package org.dhcp4java.server.config;

/**
 * Thrown to indicate there was a problem starting the DHCP Server.
 * 
 * @author Stephan Hadinger
 * @version 0.71
 */
public class ConfigException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * 
	 *
	 */
    public ConfigException() {
        super();
    }

    /**
     * @param message
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
