package com.osgisamples.congress.business;

public class RegistrantNotFoundForCongressException extends RuntimeException {

    private static final long serialVersionUID = 3886386115859087947L;

    public RegistrantNotFoundForCongressException(final String message) {
        super(message);
    }
}
