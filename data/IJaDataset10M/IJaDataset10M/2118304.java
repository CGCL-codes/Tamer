package au.edu.jcu.haldbus.exceptions;

/**
 * Exceptions of this type are thrown when trying to add an element in a list
 * which already exists (or to remove an element in a list which does not
 * exists).
 * 
 * @author gilles
 */
public class AddRemoveElemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AddRemoveElemException() {
        super();
    }

    public AddRemoveElemException(String message) {
        super(message);
    }

    public AddRemoveElemException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddRemoveElemException(Throwable cause) {
        super(cause);
    }
}
