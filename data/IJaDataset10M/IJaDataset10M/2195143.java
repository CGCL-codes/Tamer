package org.riverock.common.config;

public class ConfigException extends RuntimeException {

    public ConfigException() {
        super();
    }

    public ConfigException(String s) {
        super(s);
    }

    public ConfigException(String s, Throwable cause) {
        super(s, cause);
    }
}
