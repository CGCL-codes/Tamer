package com.artofsolving.jodconverter.openoffice.connection;

public class OpenOfficeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OpenOfficeException(String message) {
        super(message);
    }

    public OpenOfficeException(String message, Throwable cause) {
        super(message, cause);
    }
}
