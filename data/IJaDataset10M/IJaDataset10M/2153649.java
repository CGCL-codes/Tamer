package org.judo.database.exceptions;

public class DatabaseConnectionException extends Exception {

    public DatabaseConnectionException() {
    }

    public DatabaseConnectionException(String arg0) {
        super(arg0);
    }

    public DatabaseConnectionException(Throwable cause) {
        super(cause);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
