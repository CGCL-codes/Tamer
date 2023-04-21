package com.googlecode.jplurk.exception;

import com.googlecode.jplurk.net.Result;

public class RequestFailureException extends Exception {

    private static final long serialVersionUID = 2405924251357922059L;

    public RequestFailureException() {
        super("request is failure. please check the log messages.");
    }

    public RequestFailureException(String cause) {
        super(cause);
    }

    public RequestFailureException(Result result) {
        super("request is failed: " + result);
    }
}
