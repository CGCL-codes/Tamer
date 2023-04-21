package gnu.java.util.regex;

/**
 * Defines the interface used internally so that different types of source
 * text can be accessed in the same way.  Built-in concrete classes provide
 * support for String, StringBuffer, InputStream and char[] types.
 * A class that is CharIndexed supports the notion of a cursor within a
 * block of text.  The cursor must be able to be advanced via the move()
 * method.  The charAt() method returns the character at the cursor position
 * plus a given offset.
 *
 * @author <A HREF="mailto:wes@cacas.org">Wes Biggs</A>
 */
public interface CharIndexed {

    /**
     * Defines a constant (0xFFFF was somewhat arbitrarily chosen)
     * that can be returned by the charAt() function indicating that
     * the specified index is out of range.
     */
    char OUT_OF_BOUNDS = '￿';

    /**
     * Returns the character at the given offset past the current cursor
     * position in the input.  The index of the current position is zero.
     * It is possible for this method to be called with a negative index.
     * This happens when using the '^' operator in multiline matching mode
     * or the '\b' or '\<' word boundary operators.  In any case, the lower
     * bound is currently fixed at -2 (for '^' with a two-character newline).
     *
     * @param index the offset position in the character field to examine
     * @return the character at the specified index, or the OUT_OF_BOUNDS
     *   character defined by this interface.
     */
    char charAt(int index);

    /**
     * Shifts the input buffer by a given number of positions.  Returns
     * true if the new cursor position is valid.
     */
    boolean move(int index);

    /**
     * Returns true if the most recent move() operation placed the cursor
     * position at a valid position in the input.
     */
    boolean isValid();

    /**
     * Returns another CharIndexed containing length characters to the left
     * of the given index. The given length is an expected maximum and
     * the returned CharIndexed may not necessarily contain so many characters.
     */
    CharIndexed lookBehind(int index, int length);

    /**
     * Returns the effective length of this CharIndexed
     */
    int length();

    /**
     * Sets the REMatch last found on this input.
     */
    void setLastMatch(REMatch match);

    /**
     * Returns the REMatch last found on this input.
     */
    REMatch getLastMatch();

    /**
     * Returns the anchor.
     */
    int getAnchor();

    /**
     * Sets the anchor.
     */
    void setAnchor(int anchor);
}
