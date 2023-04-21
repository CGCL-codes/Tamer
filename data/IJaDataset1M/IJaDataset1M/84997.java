package com.googlecode.pondskum.config;

public final class ConfigFileLoaderException extends RuntimeException {

    private static final long serialVersionUID = 5050950582745844528L;

    public ConfigFileLoaderException(final String message) {
        super(message);
    }

    public ConfigFileLoaderException(final Throwable cause) {
        super(cause);
    }

    public ConfigFileLoaderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
