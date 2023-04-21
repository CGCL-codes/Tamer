package com.tce.lang;

/**
 * The TNumberConstants class provides constants for numeric
 * classes in the <code>com.tce.lang</code> package.
 *
 * @author  <A HREF="http://www.voicenet.com/~hsk0/tce">Howard S. Kapustein</A>
 * @version 0.9.1
 * @since   0.9.1
 * @see com.tce.lang.TInteger
 * @see com.tce.lang.TLong
 */
final class TNumberConstants {

    /**
     * This class cannot be instantiated.
     */
    private TNumberConstants() {
    }

    /**
     * All possible chars for representing a number as a String
     */
    public static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /**
     * Array of chars to lookup the char for the digit in the tenth's
     * place for a two digit, base ten number.  The char can be got by
     * using the number as the index.
     * <P>
     * NOTE: Oddly named due to the code derivation from JDK.
     */
    public static final char[] RADIX_TEN_TENTHS = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9' };

    /**
     * Array of chars to lookup the char for the digit in the unit's
     * place for a two digit, base ten number.  The char can be got by
     * using the number as the index.
     * <P>
     * NOTE: Oddly named due to the code derivation from JDK.
     */
    public static final char[] RADIX_TEN_UNITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /**
     * Number of bits per byte.
     */
    public static final int BITS_PER_BYTE = 8;

    /**
     * Byte.MIN_VALUE as a string.
     */
    public static final String BYTE_MIN_AS_STRING = "-128";

    /**
     * Length of Byte.MIN_VALUE as a string.
     */
    public static final int BYTE_MIN_AS_STRING_LENGTH = 4;

    /**
     * Number of bits per short.
     */
    public static final int BITS_PER_SHORT = 16;

    /**
     * Short.MIN_VALUE as a string.
     */
    public static final String SHORT_MIN_AS_STRING = "-32768";

    /**
     * Length of Short.MIN_VALUE as a string.
     */
    public static final int SHORT_MIN_AS_STRING_LENGTH = 6;

    /**
     * Number of bits per int.
     */
    public static final int BITS_PER_INT = 32;

    /**
     * Integer.MIN_VALUE as a string.
     */
    public static final String INT_MIN_AS_STRING = "-2147483648";

    /**
     * Length of Integer.MIN_VALUE as a string.
     */
    public static final int INT_MIN_AS_STRING_LENGTH = 11;

    /**
     * Number of bits per long.
     */
    public static final int BITS_PER_LONG = 64;

    /**
     * Long.MIN_VALUE as a string.
     */
    public static final String LONG_MIN_AS_STRING = "-9223372036854775808";

    /**
     * Length of Long.MIN_VALUE as a string.
     */
    public static final int LONG_MIN_AS_STRING_LENGTH = 20;
}
