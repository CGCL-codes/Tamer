package com.m4f.business.service.exception;

public class VotesLimitExceedException extends Exception {

    public VotesLimitExceedException() {
        super();
    }

    public VotesLimitExceedException(String message, Throwable cause) {
        super(message, cause);
    }

    public VotesLimitExceedException(String message) {
        super(message);
    }

    public VotesLimitExceedException(Throwable cause) {
        super(cause);
    }
}
