package org.in4ama.documentautomator.exception;

public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Throwable t) {
        super(msg, t);
    }
}
