package be.lassi.lanbox.tools;

import static be.lassi.util.Util.newArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Splits up a string in tokens.
 */
public class Tokenizer {

    private final String string;

    private final ListIterator<Token> iterator;

    private final List<Token> tokens = newArrayList();

    /**
     * Constructs a new instance.
     *
     * @param string the string to be splitted up in tokens
     */
    public Tokenizer(final String string) {
        this.string = string;
        StringBuilder b = new StringBuilder();
        int offset = 0;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (' ' == c) {
                if (b.length() > 0) {
                    Token token = new Token(offset, b.toString());
                    tokens.add(token);
                }
                b = new StringBuilder();
            } else {
                if (b.length() == 0) {
                    offset = i;
                }
                b.append(c);
            }
        }
        if (b.length() > 0) {
            Token token = new Token(offset, b.toString());
            tokens.add(token);
        }
        iterator = tokens.listIterator();
    }

    /**
     * Gets the next token.
     *
     * @return the next token
     */
    public Token next() {
        return iterator.next();
    }

    /**
     * Gets the previous token.
     *
     * @return the previous token
     */
    public Token previous() {
        return iterator.previous();
    }

    /**
     * Indicates whether there are more tokens.
     *
     * @return true if there are more tokens
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Gets the complete string.
     *
     * @return the complete string
     */
    public String getString() {
        return string;
    }

    /**
     * Indicates whether the string contains a token that matches given string.
     *
     * @param tokenString the string to compare with
     * @return true if the string contains given string
     */
    public boolean contains(final String tokenString) {
        boolean result = false;
        for (Token token : tokens) {
            if (token.getString().equals(tokenString)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
