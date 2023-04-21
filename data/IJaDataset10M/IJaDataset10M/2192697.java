package com.android.mms;

/**
 * A generic exception that is thrown by checking content restriction.
 */
public class ContentRestrictionException extends RuntimeException {

    private static final long serialVersionUID = 516136015813043499L;

    public ContentRestrictionException() {
        super();
    }

    public ContentRestrictionException(String msg) {
        super(msg);
    }

    public ContentRestrictionException(Exception cause) {
        super(cause);
    }
}
