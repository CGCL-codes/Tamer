package com.sun.j2ee.blueprints.activitysupplier.pomessagebean;

public class InvalidDocumentException extends java.lang.Exception {

    private java.lang.String message;

    public InvalidDocumentException(java.lang.String message) {
        super(message);
        this.message = message;
    }

    public java.lang.String getMessage() {
        return message;
    }
}
