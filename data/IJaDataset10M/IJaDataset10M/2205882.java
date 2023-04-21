package com.malethan.seemorej;

public class ErrorInitializingControllerException extends RuntimeException {

    private static final long serialVersionUID = 4910402795918910692L;

    public ErrorInitializingControllerException(String controllerClassname, Exception e) {
        super("An exception was thrown attempting to instantiate '" + controllerClassname + "'", e);
    }
}
