package com.edbaskerville.gridsweeper;

public class AdapterException extends Exception {

    private static final long serialVersionUID = 1L;

    public AdapterException() {
        super();
    }

    public AdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdapterException(String message) {
        super(message);
    }

    public AdapterException(Throwable cause) {
        super(cause);
    }
}
