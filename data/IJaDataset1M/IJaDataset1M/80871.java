package org.mbari.vars.services;

import org.mbari.vars.mini.CoreException;

/**
 *
 * @author brian
 */
public class UserPersistenceException extends CoreException {

    public UserPersistenceException() {
    }

    public UserPersistenceException(String message) {
        super(message);
    }

    public UserPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPersistenceException(Throwable cause) {
        super(cause);
    }
}
