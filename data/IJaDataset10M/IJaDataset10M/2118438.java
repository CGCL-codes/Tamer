package com.elibera.gateway.elements;

public class ParsingErrorException extends Exception {

    public ParsingErrorException() {
    }

    public ParsingErrorException(String arg0) {
        super(arg0);
    }

    public ParsingErrorException(Throwable arg0) {
        super(arg0);
    }

    public ParsingErrorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
