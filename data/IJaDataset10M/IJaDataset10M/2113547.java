package com.sun.corba.se.impl.presentation.rmi;

/**
 * Checked exception containing information about an
 * an IDL type validation.
 */
public class IDLTypeException extends Exception {

    public IDLTypeException() {
    }

    public IDLTypeException(String message) {
        super(message);
    }
}
