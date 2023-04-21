package org.jostraca.comp.antlr;

public class SemanticException extends RecognitionException {

    public SemanticException(String s) {
        super(s);
    }

    /** @deprecated As of ANTLR 2.7.2 use {@see #SemanticException(char, String, int, int) } */
    public SemanticException(String s, String fileName, int line) {
        this(s, fileName, line, -1);
    }

    public SemanticException(String s, String fileName, int line, int column) {
        super(s, fileName, line, column);
    }
}
