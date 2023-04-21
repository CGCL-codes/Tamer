package com.ouroboroswiki.core;

public class AuthenticationException extends Exception {

    private static final long serialVersionUID = 0;

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
