package org.kwantu.wesemo.controller;

public class UserNotAuthenticatedException extends WesemoServiceException {

    private static final long serialVersionUID = 1L;

    public UserNotAuthenticatedException(String email) {
        super("User with email '" + email + "' does not exist or the password given is invalid");
    }
}
