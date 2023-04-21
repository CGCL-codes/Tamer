package softwareengineering.tokeniser;

/**
 * Objects of type token contains data relating to a single atomic
 * item in and HTML source document. These can be string-literal,
 * whitespace sequences, or special tokens.
 *
 * Objects of type Token are immutable - they and their contents can
 * not be modified once instantiated.
 *
 * @version 1.00, 08 June 2009
 */
public class Token {

    /**
     * The text that this token represents (the characters in the
     * HTML file that where parsed to become this token.)
     */
    private final String text;

    /**
     * Whether or not this token contains whitespace. If true the text
     * should contain only whitespace, if false then text should
     * contain no whitespace.
     */
    private final boolean whiteSpace;

    /**
     * Whether or not the text represents a special sequence (eg ">").
     */
    private final boolean special;

    /**
     * The line in the file that this token was found
     */
    private final int line;

    /**
     * The column in the file that this token was found
     */
    private final int column;

    /**
     * Construct a new token, instantiating it's fields to those
     * given in the parameters.
     * 
     * @param newText   The string representation of this token
     * @param newLine   The line number it was found in the file
     * @param newColumn The column number it was found in the file
     * @param newWhiteSpace Whether it is whitespace or not
     * @param newSpecial    Whether it is a special char or not
     * @throws IllegalArgumentException If any of the parameters are
     *          percieved to be outside their normal range.
     */
    public Token(String newText, int newLine, int newColumn, boolean newWhiteSpace, boolean newSpecial) {
        if (newText == null || newText.length() == 0) throw new IllegalArgumentException("Text argument must not be empty or null.");
        if (newLine < 0) throw new IllegalArgumentException("Line argument must be a possitive integer.");
        if (newColumn < 0) throw new IllegalArgumentException("Column argument must be a possitive integer.");
        text = newText;
        line = newLine;
        column = newColumn;
        whiteSpace = newWhiteSpace;
        special = newSpecial;
    }

    /**
     * Accessor returning the column number that this token was found
     * in the source HTML file.
     *
     * @return The column number of this token
     */
    public int getColumn() {
        return column;
    }

    /**
     * Accessor returning the line number that this token was found
     * in the source HTML file.
     *
     * @return The line number of this token
     */
    public int getLine() {
        return line;
    }

    /**
     * Accessor returning the string representation of this token, ie
     * the text that was found in the source HTML file.
     *
     * @return The string value of this token
     */
    public String getText() {
        return text;
    }

    /**
     * Accessor returning whether or not this token is either entirely
     * composed of whitespace characters, or entirely composed of
     * non-whitespace characters.
     *
     * @return True if this token is whitespace, false otherwise.
     */
    public boolean isWhiteSpace() {
        return whiteSpace;
    }

    /**
     * Accessor returning if this token is a special characters
     * sequence or not. A special character sequence is a
     * non-whitespace sequence that has soe special meaning to the
     * Parser class. For example "<", ">", and "&".
     * 
     * @return True if this token is special, false otherwise.
     */
    public boolean isSpecial() {
        return special;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Token(");
        if (whiteSpace) buf.append("WHITESPACE");
        if (special) buf.append("SPECIAL");
        buf.append("@").append(line).append(":").append(column);
        buf.append(" \"").append(text).append("\")");
        return buf.toString();
    }
}
