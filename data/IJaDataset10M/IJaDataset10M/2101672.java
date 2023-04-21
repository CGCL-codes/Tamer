package com.myJava.file.driver.cache;

public class NonExistingEntryException extends Exception {

    private static final long serialVersionUID = 77780100234L;

    public NonExistingEntryException() {
        super();
    }

    public NonExistingEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistingEntryException(String message) {
        super(message);
    }

    public NonExistingEntryException(Throwable cause) {
        super(cause);
    }
}
