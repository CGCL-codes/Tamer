package org.moonwave.dconfig.dao;

/**
 *
 * @author jonathan
 */
public class IncompatibleTypeException extends RuntimeException {

    public IncompatibleTypeException() {
        super();
    }

    public IncompatibleTypeException(String message) {
        super(message);
    }
}
