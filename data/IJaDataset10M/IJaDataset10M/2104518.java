package com.ahm.exception;

public class AhmSystemException extends AhmBaseException {

    public AhmSystemException(String msg) {
        super(msg);
    }

    public AhmSystemException(String msg, Exception bex) {
        super(msg, bex);
    }
}
