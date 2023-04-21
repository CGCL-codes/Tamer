package br.unb.syntainia.parsers;

public class ParserException extends Exception {

    private static final long serialVersionUID = 3960466358642794637L;

    public ParserException() {
        super();
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }
}
