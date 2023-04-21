package org.javadatabasemigrations.exceptions;

public class MigrationException extends RuntimeException {

    public MigrationException() {
        super();
    }

    public MigrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MigrationException(final String message) {
        super(message);
    }

    public MigrationException(final Throwable cause) {
        super(cause);
    }
}
