package net.sf.grotag.parse;

import net.sf.grotag.common.Tools;

/** Tokenizer for a single line of an AmigaGuide document */
public class LineTokenizer {

    public enum Type {

        CLOSE_BRACE, COMMAND, INVALID, OPEN_BRACE, SPACE, STRING, TEXT
    }

    private enum State {

        IN_COMMAND, IN_COMMAND_BRACE, IN_TEXT
    }

    private static final int NO_COLUMN = -1;

    private State parserState;

    private String text;

    private int lineNumber;

    private int column;

    private int columnOpenBrace;

    private String token;

    private Type type;

    private Tools tools;

    private boolean insertCloseBrace;

    private AbstractSource source;

    private MessagePool messagePool;

    /**
     * Create a new LineTokenizer for the line <code>newText</code> read from
     * the input at <code>newLineNumber</code>.
     * 
     * The tokenizer already performs some corrections on the input to guarantee
     * a certain syntactical correctness. In particular, it ensures:
     * <ul>
     * <li>Strings in commands have a terminating quote.
     * <li>Command have a terminating curly brace.
     * <li>All escape sequences are proper.
     * </ul>
     * 
     * @param newLineNumber
     *                the number of the line when read from the input file,
     *                starting with 0
     */
    public LineTokenizer(AbstractSource newSource, int newLineNumber, String newText) {
        assert newSource != null;
        assert newLineNumber >= 0;
        assert newText != null;
        tools = Tools.getInstance();
        messagePool = MessagePool.getInstance();
        source = newSource;
        lineNumber = newLineNumber;
        text = tools.withoutTrailingWhiteSpace(newText);
        column = 0;
        columnOpenBrace = NO_COLUMN;
        parserState = State.IN_TEXT;
        type = Type.INVALID;
    }

    /**
     * The type of the current token.
     */
    public Type getType() {
        if (type == Type.INVALID) {
            throw new IllegalStateException("getType() must be called only when there is a token available");
        }
        return type;
    }

    private void fireWarning(String message) {
        assert message != null;
        fireWarning(message, getColumn());
    }

    private void fireWarning(String message, int messageColumn) {
        assert message != null;
        assert messageColumn >= 0;
        messagePool.add(source, getLine(), getColumn(), message);
    }

    /**
     * Similar to <code>Character.isWhitespace</code> but does not consider
     * formfeed (decimal ASCII 12) to be whitespace.
     */
    private boolean isWhitespace(char some) {
        return ((some == ' ') || (some == '\t'));
    }

    private boolean atSignIsCommand(int atSignColumn) {
        assert atSignColumn >= 0;
        boolean result = false;
        char atSign = text.charAt(atSignColumn);
        int textLength = text.length();
        assert atSign == '@' : "character at column " + atSignColumn + " must be " + tools.sourced("@") + " but is " + tools.sourced(atSign);
        if (atSignColumn < textLength - 1) {
            char charAfterAtSign = text.charAt(atSignColumn + 1);
            boolean charAfterAtSignIsOpenBrace = (charAfterAtSign == '{');
            if ((atSignColumn > 0) || charAfterAtSignIsOpenBrace) {
                if (charAfterAtSignIsOpenBrace && (atSignColumn < textLength - 2)) {
                    char charAfterOpenBrace = text.charAt(atSignColumn + 2);
                    result = !isWhitespace(charAfterOpenBrace);
                }
            } else {
                result = !isWhitespace(charAfterAtSign);
            }
        }
        return result;
    }

    public void advance() {
        if (!hasNext()) {
            throw new IllegalStateException("cannot advance past end of line number " + getLine());
        }
        char some;
        if (insertCloseBrace) {
            assert parserState == State.IN_COMMAND_BRACE : "parserState must be " + State.IN_COMMAND_BRACE + " but is " + parserState;
            insertCloseBrace = false;
            some = '}';
        } else {
            some = text.charAt(column);
            column += 1;
        }
        token = "" + some;
        type = Type.INVALID;
        if (isWhitespace(some)) {
            while (hasChars() && isWhitespace(text.charAt(column))) {
                token += text.charAt(column);
                column += 1;
            }
            type = Type.SPACE;
        } else if ((parserState == State.IN_TEXT) && (some < 32)) {
            token = "?";
            type = Type.TEXT;
            fireWarning("replaced invisible character with code " + ((int) some) + " by " + tools.sourced(token));
        } else if ((parserState == State.IN_TEXT) && (some == '@') && atSignIsCommand(column - 1)) {
            parserState = State.IN_COMMAND;
            type = Type.COMMAND;
        } else if ((parserState == State.IN_COMMAND) && (some == '{')) {
            columnOpenBrace = column;
            parserState = State.IN_COMMAND_BRACE;
            type = Type.OPEN_BRACE;
        } else if (((parserState == State.IN_COMMAND) || (parserState == State.IN_COMMAND_BRACE)) && (some == '"')) {
            int quoteColumn = column;
            do {
                try {
                    token += text.charAt(column);
                } catch (StringIndexOutOfBoundsException error) {
                    System.err.println("(" + lineNumber + ":" + column + ") ");
                    System.err.println("  text = " + tools.sourced(text));
                    System.err.println("  token = " + tools.sourced(token));
                    throw error;
                }
                column += 1;
            } while (hasChars() && (text.charAt(column) != '"'));
            if (!hasChars()) {
                fireWarning("appended missing trailing quote", quoteColumn);
            } else {
                column += 1;
            }
            token += "\"";
            type = Type.STRING;
        } else if ((parserState == State.IN_COMMAND_BRACE) && (some == '}')) {
            assert columnOpenBrace != NO_COLUMN : "columnOpenBrace must have been set earlier";
            token = "" + some;
            columnOpenBrace = NO_COLUMN;
            parserState = State.IN_TEXT;
            type = Type.CLOSE_BRACE;
        } else {
            boolean afterBackslash = (some == '\\');
            if (some == '@') {
                fireWarning("inserting backslash before dangling \"@\"");
                assert token.equals("@");
                token = "\\@";
            }
            while (hasChars() && (text.charAt(column) > 32) && !((parserState == State.IN_COMMAND_BRACE) && (text.charAt(column) == '}')) && !((parserState == State.IN_TEXT) && (text.charAt(column) == '@') && !afterBackslash && atSignIsCommand(column))) {
                some = text.charAt(column);
                if (afterBackslash) {
                    if ((some != '\\') && (some != '@')) {
                        fireWarning("inserted backslash before dangling backslash with " + tools.sourced(some) + " instead of \"\\\" or \"@\"");
                        token += '\\';
                    }
                    token += some;
                    afterBackslash = false;
                } else if (some == '\\') {
                    token += some;
                    afterBackslash = true;
                } else if (some == '@') {
                    fireWarning("inserted backslash before dangling \"@\"");
                    token += "\\" + some;
                } else {
                    token += some;
                }
                column += 1;
            }
            if (afterBackslash) {
                fireWarning("appended backslash after dangling backslash at end of token");
                token += '\\';
            }
            type = Type.TEXT;
        }
        if (!hasNext() && (parserState == State.IN_COMMAND_BRACE)) {
            insertCloseBrace = true;
        }
        assert type != Type.INVALID : "token type must be set";
    }

    private boolean hasChars() {
        return (column < text.length());
    }

    public boolean hasNext() {
        return hasChars() || insertCloseBrace;
    }

    public int getLine() {
        return lineNumber;
    }

    public int getColumn() {
        return column;
    }

    public String getToken() {
        return token;
    }
}
