package org.expasy.jpl.bio.exceptions;

public class JPLSequenceIndexOutOfBoundException extends ArrayIndexOutOfBoundsException {

    private static final long serialVersionUID = 1L;

    public JPLSequenceIndexOutOfBoundException() {
    }

    public JPLSequenceIndexOutOfBoundException(String message) {
        super(message);
    }
}
