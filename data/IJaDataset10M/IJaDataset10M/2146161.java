package se392.ateam2006.exceptions;

/**
 * Exception thrown when an invalid value is used in the system
 * @author Ateam (Matthew Bennett, Claire Melton, Shingai Manyiwa, John Adderley)
 * @version 25/03/07
 */
public class InvalidValueException extends Exception {

    /**
     * Default constructor
     */
    public InvalidValueException() {
        super();
    }

    /**
     * Create a new InvalidValueException
     * @param msg - an error message
     */
    public InvalidValueException(String msg) {
        super(msg);
    }

    /**
     * Create a new InvalidValueException
     * @param cause - the cause of the exception
     */
    public InvalidValueException(Throwable cause) {
        super(cause);
    }

    /**
     * Create a new InvalidValueException
     * @param msg - an error message
     * @param cause - the cause of the exception
     */
    public InvalidValueException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
