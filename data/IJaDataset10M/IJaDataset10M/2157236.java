package com.ait.actors;

public class ControlException extends Exception {

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
