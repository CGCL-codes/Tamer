package org.freelords.network.command.exception;

/** Thrown the Server'services if a network command command is invalid or not well formed. */
public class InvalidCommandException extends Exception {

    private static final long serialVersionUID = -7470505145939658282L;

    public InvalidCommandException(String message) {
        super(message);
    }
}
