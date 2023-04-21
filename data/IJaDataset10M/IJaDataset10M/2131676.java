package com.esl.exception;

public class DBException extends ESLSystemException {

    public DBException() {
        super();
    }

    public DBException(String message) {
        this("dbException", message);
    }

    public DBException(String errorCode, String message) {
        super(errorCode, message);
    }
}
