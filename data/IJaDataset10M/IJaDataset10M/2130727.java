package pl.tyszka.rolemanager.rbac;

import pl.tyszka.rolemanager.exceptions.RepositoryException;

/**
 * This is an exception that is thrown whenever an attempt is made to 
 * delete the user when a user is not found. 
 * 
 * @author  <a href="mailto:daras@users.sourceforge.net">Dariusz Tyszka</a>
 * @version $Revision: 1.5 $
 * 
 * @since 1.0
 */
public class NoSuchUserException extends RepositoryException {

    /**
     * Constructs a new <code>NoSuchUserException</code> 
     * with no detail message.
     */
    public NoSuchUserException() {
        super();
    }

    /**
     * Constructs a new <code>UserNotFoundException</code> 
     * with the specified detail message.
     * 
     * @param message The detail message, or <code>null</code> if no detail message.
     */
    public NoSuchUserException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>UserNotFoundException</code> 
     * with the specified detail message and cause.
     * 
     * @param message The detail message, or <code>null</code> if no detail message.
     * @param cause The nested failure cause.
     */
    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new <code>UserNotFoundException</code> 
     * with the specified cause.
     * 
     * @param cause The nested failure cause.
     */
    public NoSuchUserException(Throwable cause) {
        super(cause);
    }
}
