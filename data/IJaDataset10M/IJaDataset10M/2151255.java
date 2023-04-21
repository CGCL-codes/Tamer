package com.jes.classfinder.helpers.file;

/**
 * @author John Dickerson
 *
 */
public class FileHelperException extends Exception {

    public FileHelperException() {
        super();
    }

    public FileHelperException(String message) {
        super(message);
    }

    public FileHelperException(Throwable cause) {
        super(cause);
    }

    public FileHelperException(String message, Throwable cause) {
        super(message, cause);
    }
}
