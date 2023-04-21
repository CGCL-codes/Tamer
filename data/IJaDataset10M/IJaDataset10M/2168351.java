package com.reserveamerica.jirarmi.exceptions;

/**
 * @author bstasyszyn
 */
public class AuthenticationException extends JiraException {

    private static final long serialVersionUID = -3760501703192562901L;

    public AuthenticationException(String message) {
        super(message);
    }
}
