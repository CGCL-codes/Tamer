package de.searchworkorange.lib.database;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class DatabaseServerNotReachableException extends Exception {

    public DatabaseServerNotReachableException(Throwable cause) {
        super(cause);
    }

    public DatabaseServerNotReachableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseServerNotReachableException(String message) {
        super(message);
    }

    public DatabaseServerNotReachableException() {
    }
}
