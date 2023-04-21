package jmodnews.parsing;

public class ParserException extends Exception {

    public ParserException(String toparse, int position, String message) {
        super("Unable to parse \"" + toparse + "\" (at position " + position + "): " + message);
    }
}
