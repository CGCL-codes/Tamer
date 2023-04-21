package com.malethan.seemorej;

public class NoSuchControllerException extends RuntimeException {

    private static final long serialVersionUID = 8371894050977512909L;

    public NoSuchControllerException(String controllerName) {
        super(controllerName);
    }
}
