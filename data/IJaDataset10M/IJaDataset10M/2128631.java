package com.jeronimo.eko.core.config;

/**
 * An exception thrown if a service received a bad config
 * Copyright Jerome Bonnet (c) 2003
 */
@SuppressWarnings("serial")
public abstract class ConfigException extends Exception {

    public ConfigException() {
        super();
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
