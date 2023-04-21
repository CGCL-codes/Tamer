package parser.simple;

import lexer.interfaces.LexerToken;

/**
 * @author Daniel Kristensen
 *
 */
public class Times implements LexerToken {

    @Override
    public String getRegex() {
        return "\\*";
    }

    @Override
    public String toString() {
        return "*";
    }
}
