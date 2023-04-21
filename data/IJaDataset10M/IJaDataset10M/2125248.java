package org.webstrips.core;

public class InterruptedRuntimeException extends RuntimeException {

    public InterruptedRuntimeException(InterruptedException e) {
        super(e);
    }

    private static final long serialVersionUID = 1L;
}
