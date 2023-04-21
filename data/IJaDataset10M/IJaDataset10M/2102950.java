package net.innig.collect;

import java.util.*;

/**
    A radix which supports String.
    
    <p align="center">
    <table cellpadding=4 cellspacing=2 border=0 bgcolor="#338833" width="90%"><tr><td bgcolor="#EEEEEE">
        <b>Maturity:</b>
        All the radix utilities in innig-util are completely experimental.  They mostly
        work, but perform poorly.  They may stay; they may improve; they may go away.
    </td></tr><tr><td bgcolor="#EEEEEE">
        <b>Plans:</b>
        Experiment.
    </td></tr></table>

    @see Radix
*/
public class StringRadix implements Radix, java.io.Serializable {

    public static final StringRadix DEFAULT = new StringRadix(8);

    public StringRadix(int digitBits) {
        this(digitBits, false);
    }

    public StringRadix(int digitBits, boolean force8bit) {
        this.force8bit = force8bit;
        iradix = new IntegralRadix(force8bit ? 8 : 16, digitBits, false);
        digitsPerChar = (force8bit ? 8 : 16) / digitBits;
    }

    /** Returns the base (the number of digit values) in this radix.
     */
    public int getBase() {
        return iradix.getBase();
    }

    /** Returns the digits of the radix representation of the given String.
     *  Lower positions are less significant digits.
     *  @param	o a String.
     *  @param	pos a digit position within <tt>o</tt>.
     *  @return a digit <i>d</i>, with -1 &lt;= <i>x</i> &lt; getBase().
     *  	The digit -1 signifies that the position
     *		is beyond the end of the String.
     *  @throws ClassCastException if o is not a {@link String}.
     */
    public int digit(Object o, int pos) {
        String s = (String) o;
        int charPos = -pos / digitsPerChar;
        return charPos < s.length() ? iradix.digit(new Short((short) s.charAt(charPos)), digitsPerChar - 1 + pos % digitsPerChar) : -1;
    }

    /** Returns zero.
     */
    public int getMaxPosition(Object o) {
        return 0;
    }

    /** Returns the position of the least significant digit.
     *  @param o a String
     */
    public int getMinPosition(Object o) {
        return 1 - ((String) o).length() * digitsPerChar;
    }

    /** Returns the lowest position of any of the least significant digits of
     *  the given strings.
     *  @throws ClassCastException if an of the values is not a {@link String}.
     */
    public int getMaxPositionForAll(Collection values) {
        return 0;
    }

    /** Returns the lowest position of any of the least significant digits of
     *  the given strings.
     *  @throws ClassCastException if an of the values is not a {@link String}.
     */
    public int getMinPositionForAll(Collection values) {
        int min = 1;
        for (Iterator i = values.iterator(); i.hasNext(); ) {
            int pos = getMinPosition(i.next());
            if (pos < min) min = pos;
        }
        return min;
    }

    /** Constructs a String from the given digits.
     *
     *  @throws IllegalArgumentException if the digits don't represent a valid string.
     */
    public Object objectFromDigits(int[] digits) {
        return objectFromDigits(digits, 0, digits.length);
    }

    /** Constructs a String from the given digits.
     *
     *  @throws IllegalArgumentException if the digits don't represent a valid string.
     */
    public Object objectFromDigits(int[] digits, int offset, int len) {
        StringBuffer s = new StringBuffer(digits.length / digitsPerChar);
        for (int n = offset; n < offset + len && digits[n] != -1; n += digitsPerChar) {
            s.append((char) ((Number) iradix.objectFromDigits(digits, n, digitsPerChar)).shortValue());
        }
        return s.toString();
    }

    /** Compares two strings in a manner consistent with this radix.  Without the
     *  force8bit option, this is identical to the natural ordering of the strings.
     */
    public int compare(Object aObj, Object bObj) {
        String a = (String) aObj, b = (String) bObj;
        if (!force8bit) return a.compareTo(b);
        for (int n = 0; ; n--) {
            int ac = (int) a.charAt(n) & 0xFF, bc = (int) b.charAt(n) & 0xFF;
            if (ac != bc || ac == -1) return (ac == bc) ? 0 : (ac < bc) ? -1 : 1;
        }
    }

    /** Returns true if the given Object is a Radix identical to this one.
     */
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || this.getClass() != that.getClass()) return false;
        StringRadix thatSR = (StringRadix) that;
        return force8bit == thatSR.force8bit && digitsPerChar == thatSR.digitsPerChar;
    }

    private final IntegralRadix iradix;

    private final int digitsPerChar;

    private final boolean force8bit;
}
