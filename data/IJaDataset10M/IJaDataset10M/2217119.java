package com.sun.org.apache.commons.logging;

/**
 * LogConfigurationException
 *
 * Bridging com.sun.org.apache.logging.
 *
 */
public class LogConfigurationException extends RuntimeException {

    /**
     * 
     */
    public LogConfigurationException() {
        super();
    }

    /**
     * @param message
     */
    public LogConfigurationException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public LogConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public LogConfigurationException(Throwable cause) {
        super(cause);
    }
}
