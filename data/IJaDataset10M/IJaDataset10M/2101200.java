package info.jonclark.database;

/**
 * @author Jonathan
 */
public class DatabaseException extends Exception {

    /**
     * Create a new DatabaseException with given message
     * 
     * @param message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Create a new DatabaseException with given message
     * and cause of exception
     * 
     * @param message
     * @param cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
