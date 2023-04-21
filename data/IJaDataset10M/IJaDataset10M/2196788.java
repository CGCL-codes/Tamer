package de.consolewars.api.exception;

public class ConsolewarsAPIException extends Exception {

    public ConsolewarsAPIException(String message) {
        super(message);
    }

    public ConsolewarsAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
