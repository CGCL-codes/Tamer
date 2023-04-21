package com.akcess.exception;

public class Usuario_tiene_rolException extends Exception {

    public Usuario_tiene_rolException(String message) {
        super(message);
    }

    public Usuario_tiene_rolException(Throwable cause) {
        super(cause);
    }
}
