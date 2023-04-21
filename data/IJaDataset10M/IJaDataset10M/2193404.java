package com.netx.generics.R1.translation;

public class ParseException extends RuntimeException {

    private final Position _p;

    public ParseException(String message, Position p) {
        super(message);
        _p = p;
    }

    public ParseException(String message) {
        this(message, null);
    }

    public ParseException(int line, String message) {
        this("in line " + line + ": " + message, null);
    }

    public ParseException(String header, ErrorList el, MessageFormatter mf) {
        super(header + mf.format(el.getErrors().get(0)));
        _p = el.getErrors().get(0).getPosition();
    }

    public Position getPosition() {
        return _p;
    }
}
