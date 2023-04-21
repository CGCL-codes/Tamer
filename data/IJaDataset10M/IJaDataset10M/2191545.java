package org.statcato.calculator;

/**
 * A token in a mathematical expression accepted by the Statcato calculator.
 * 
 * @author Margaret Yau
 * @version %I%, %G%
 * @since 1.0
 */
public class Token {

    public static final int UNDEFINED = -1;

    public static final int ADD = 0;

    public static final int SUBTRACT = 1;

    public static final int MULTIPLY = 2;

    public static final int DIVIDE = 3;

    public static final int POWER = 4;

    public static final int NEGATE = 5;

    public static final int LEFTPAREN = 6;

    public static final int RIGHTPAREN = 7;

    public static final int NUMBER = 8;

    public static final int FUNCTION = 9;

    public static final int COLUMN = 10;

    public static final int VARIABLE = 11;

    public static final int CONSTANT = 12;

    public static final int QUOTE = 13;

    public static final int LAST = 14;

    /**
     * Array of string representations of the tokens.
     */
    public static final String[] STRINGS = { "+", "-", "*", "/", "^", "-", "(", ")", "number", "function", "column", "variable", "constant", "'", "terminal" };

    /**
     * Returns the string representation of the given token type.
     * 
     * @param i token type
     * @return string representation of the given token type
     */
    public static String toString(int i) {
        if (i < 0 || i > LAST) return "UNKNOWN";
        return STRINGS[i];
    }
}
