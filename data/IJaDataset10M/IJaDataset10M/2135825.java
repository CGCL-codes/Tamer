package com.jpmorrsn.fbp.engine;

public class ComponentException extends Exception {

    private int intError;

    public ComponentException(final int intErrNo) {
        intError = intErrNo;
    }

    public ComponentException(final String strMessage) {
        super(strMessage);
    }

    @Override
    public String toString() {
        return "Component exception - value: " + intError;
    }

    public int getValue() {
        return intError;
    }
}
