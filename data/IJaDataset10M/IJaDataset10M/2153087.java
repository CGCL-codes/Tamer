package com.akivasoftware.comp.exception;

public class UnknownFactoryException extends Exception {

    /** Constructor - No arg calls to super */
    public UnknownFactoryException() {
        super();
    }

    /** Constructor - int ID */
    public UnknownFactoryException(int ID) {
        super("Unknown factory key - " + ID);
    }
}
