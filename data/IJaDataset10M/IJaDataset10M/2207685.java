package de.zeiban.loppe.dbcore;

public class DbAccessException extends RuntimeException {

    public DbAccessException() {
        super();
    }

    public DbAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbAccessException(String message) {
        super(message);
    }

    public DbAccessException(Throwable cause) {
        super(cause);
    }
}
