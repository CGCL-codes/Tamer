package com.consciouscode.alex.reader;

import java.io.IOException;
import java.io.Reader;

/**
 * 
 */
public class Tokenizer {

    private static final int NOT_STARTED = -2;

    public static final String OPEN_PAREN = "(";

    public static final String CLOSE_PAREN = ")";

    private final Reader myReader;

    private int myCurrentChar = NOT_STARTED;

    private StringBuilder myTokenBuilder = new StringBuilder(80);

    /**
     * @param input must not be <code>null</code>.
     */
    public Tokenizer(Reader input) {
        myReader = input;
    }

    /**
     * Pulls the next token from the input source.
     * 
     * @return <code>null</code> at end of input.
     * @throws IOException if there's a problem reading from the source.
     */
    public String next() throws IOException {
        if (myCurrentChar == NOT_STARTED) myCurrentChar = myReader.read();
        while (Character.isWhitespace(myCurrentChar)) {
            myCurrentChar = myReader.read();
        }
        if (myCurrentChar == '(') {
            myCurrentChar = myReader.read();
            return OPEN_PAREN;
        }
        if (myCurrentChar == ')') {
            myCurrentChar = myReader.read();
            return CLOSE_PAREN;
        }
        if (myCurrentChar == -1) {
            return null;
        }
        do {
            myTokenBuilder.append((char) myCurrentChar);
            myCurrentChar = myReader.read();
        } while (myCurrentChar != '(' && myCurrentChar != ')' && !Character.isWhitespace(myCurrentChar) && myCurrentChar != -1);
        String token = myTokenBuilder.toString();
        myTokenBuilder.setLength(0);
        return token;
    }
}
