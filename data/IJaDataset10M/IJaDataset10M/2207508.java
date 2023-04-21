package com.road.user;

public class UserException extends Exception {

    public UserException(Exception e) {
        super(e);
    }

    public UserException(String msg, Exception e) {
        super(msg, e);
    }
}
