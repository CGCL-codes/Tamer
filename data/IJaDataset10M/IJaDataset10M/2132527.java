package com.beardediris.ajaqs.ex;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
