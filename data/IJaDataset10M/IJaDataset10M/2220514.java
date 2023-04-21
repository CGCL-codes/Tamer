package com.vitria.test.client.worker.common;

public class ConfigurationInvalidException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConfigurationInvalidException() {
        super();
    }

    public ConfigurationInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationInvalidException(String message) {
        super(message);
    }

    public ConfigurationInvalidException(Throwable cause) {
        super(cause);
    }
}
