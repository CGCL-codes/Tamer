package com.db4o.nb.api.exception;

/**
 *
 * @author klevgert
 */
public class Db4oConfigurationException extends Db4oProviderException {

    /** Creates a new instance of Db4oConfigurationException */
    public Db4oConfigurationException() {
        super();
    }

    public Db4oConfigurationException(String message) {
        super(message);
    }

    public Db4oConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public Db4oConfigurationException(Throwable cause) {
        super(cause);
    }
}
