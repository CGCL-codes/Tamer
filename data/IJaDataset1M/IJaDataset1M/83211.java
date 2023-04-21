package org.limmen.crs.exception;

public class ConfigurationException extends Exception {

    private static final long serialVersionUID = -4704036775706214245L;

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
