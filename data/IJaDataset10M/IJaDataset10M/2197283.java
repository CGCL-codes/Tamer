package org.archive.configuration;

public class ConfigurationException extends Exception {

    private static final long serialVersionUID = 5883308732750297463L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final String message, final Throwable e) {
        super(message, e);
    }

    public ConfigurationException(final Throwable e) {
        super(e);
    }
}
