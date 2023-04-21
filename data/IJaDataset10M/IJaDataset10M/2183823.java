package org.alcibiade.sculpt.parser;

public class ParseError extends ParsingException {

    private static final long serialVersionUID = 1L;

    public ParseError(int row, int column, String message) {
        super(row, column, message);
    }
}
