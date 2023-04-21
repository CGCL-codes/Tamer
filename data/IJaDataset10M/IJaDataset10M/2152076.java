package org.itsocial.framework.exception;

public class ConfigurationException extends ApplicationException {

    public ConfigurationException(String code, String message) {
        super(code, message);
    }

    public ConfigurationException(String errCode) {
        super(errCode);
    }

    public ConfigurationException() {
    }

    private static final long serialVersionUID = 1L;
}
