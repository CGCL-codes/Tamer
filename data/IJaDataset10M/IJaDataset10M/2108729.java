package org.evertree.scroom.authentication.model.exception;

public class LoginAlreadyExistsException extends Exception {

    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;

    public LoginAlreadyExistsException(String login) {
        super("The login '" + login + "' already exists.");
    }
}
