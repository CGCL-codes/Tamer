package com.pandmservices.core;

public class TodoException extends Exception {

    private String msg;

    public TodoException(String message) {
        msg = message;
    }

    public String toString() {
        return msg;
    }
}
