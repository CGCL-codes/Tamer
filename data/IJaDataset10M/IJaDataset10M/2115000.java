package org.ladybug.core.users;

/**
 * @author Aurelian Pop
 */
public class UserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserException(final String message) {
        super(message);
    }
}
