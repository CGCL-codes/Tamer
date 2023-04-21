package fhire.exceptions;

/**
 * 
 * @author Joerg Doppelreiter, Johann Zagler, Robert Maierhofer
 *
 */
public class PasswordNotValidException extends Exception {

    /**
     * 
     */
    public PasswordNotValidException() {
        super();
    }

    /**
     * @param message
     */
    public PasswordNotValidException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public PasswordNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public PasswordNotValidException(Throwable cause) {
        super(cause);
    }
}
