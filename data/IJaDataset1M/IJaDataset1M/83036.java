package org.reward4j.service;

/**
 * Will be thrown if a {@link User} could not be found by the
 * {@link UserResolver}.
 * 
 * @author Peter Kehren <mailto:kehren@eyeslide.de>
 */
public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = -3177505119076944494L;

    public UserNotFoundException(String userId) {
        super("user with id '" + userId + "' could not be found");
    }

    public UserNotFoundException() {
        super("user could not be found");
    }
}
