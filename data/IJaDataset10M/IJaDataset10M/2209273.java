package com.csol.util;

/**
 * 
 */
public class ConfigurationException extends Exception {

    /**
	 * Constructor for ConfigurationException.
	 */
    public ConfigurationException() {
        super();
    }

    /**
	 * Constructor for ConfigurationException.
	 * @param message
	 */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
	 * Constructor for ConfigurationException.
	 * @param message
	 * @param cause
	 */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor for ConfigurationException.
	 * @param cause
	 */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
