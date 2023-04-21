package com.beardediris.ajaqs.ex;

public class DuplicateIdException extends Exception {

    public DuplicateIdException(String msg) {
        super(msg);
    }

    public DuplicateIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
