package com.continuent.tungsten.manager.exception;

public class DirectoryNotFoundException extends Exception {

    public DirectoryNotFoundException() {
    }

    public DirectoryNotFoundException(String message) {
        super(message);
    }

    public DirectoryNotFoundException(Throwable cause) {
        super(cause);
    }

    public DirectoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
