package de.searchworkorange.lib.database;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class NoSQLDriverDisposerIsSetException extends Exception {

    public NoSQLDriverDisposerIsSetException(Throwable cause) {
        super(cause);
    }

    public NoSQLDriverDisposerIsSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSQLDriverDisposerIsSetException(String message) {
        super(message);
    }

    public NoSQLDriverDisposerIsSetException() {
    }
}
