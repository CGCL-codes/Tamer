package edu.uiuc.ncsa.security.core.exceptions;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Nov 5, 2010 at  11:45:13 AM
 */
public class ConfigurationException extends GeneralException {

    public ConfigurationException() {
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
