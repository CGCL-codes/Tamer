package com.mitester.sipserver.headervalidation;

public class MissingMandatoryHeaderException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public MissingMandatoryHeaderException() {
        super();
    }

    public MissingMandatoryHeaderException(String message) {
        super(message);
    }
}
