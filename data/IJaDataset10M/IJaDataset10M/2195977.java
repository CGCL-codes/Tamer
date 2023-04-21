package com.sun.jdi.request;

/**
 * Thrown to indicate a duplicate event request.
 *
 * @author Robert Field
 * @since  1.3
 */
public class DuplicateRequestException extends RuntimeException {

    public DuplicateRequestException() {
        super();
    }

    public DuplicateRequestException(String s) {
        super(s);
    }
}
