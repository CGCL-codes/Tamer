package com.gabysoft.edixml.edixml.exceptions;

public final class AlphabeticException extends ValueCheckerException {

    public AlphabeticException(char character) {
        super("character " + pretty(character) + " not in [A-Za-z]");
    }
}
