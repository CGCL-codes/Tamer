package com.kitten.exceptions;

public class KittenCannotImportException extends Exception {

    private String message;

    public KittenCannotImportException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
