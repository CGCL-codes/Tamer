package org.sepp.exceptions;

public class AuthenticationException extends Exception {

    static final long serialVersionUID = 82000006;

    public AuthenticationException(String message) {
        super(message);
    }
}
