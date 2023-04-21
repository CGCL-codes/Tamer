package com.tce.lang;

import com.tce.lang.IComparable;
import com.tce.lang.TNumberConstants;

/**
 * The TLong class complements the <code>java.lang.Long</code>
 * class providing higher performance static methods, additional functionality
 * and can be used as mutable data type.
 *
 * @author  <A HREF="http://www.voicenet.com/~hsk0/tce">Howard S. Kapustein</A>
 * @version 0.9.1
 * @since   0.9.1
 * @see com.tce.lang.TInteger
 * @see java.lang.Long
 */
public final class TLong extends Number implements IComparable {

    /**
     * The minimum value a TLong can have.
     */
    public static final long MIN_VALUE = Long.MIN_VALUE;

    /**
     * The maximum value a TLong can have.
     */
    public static final long MAX_VALUE = Long.MAX_VALUE;

    /**
     * The Class object representing the primitive type long.
     */
    public static final Class TYPE = long.class;

    private static final long serialVersionUID = 3937969611676040767L;

    /**
     * The value of the TLong.
     *
     * @serial
     */
    private long m_value = 0;

    /**
     * Constructs a TLong object initialized to zero.
     */
    public TLong() {
    }

    /**
     * Constructs a TLong object initialized to the specified long value.
     *
     * @param value     the initial value of the TLong
     */
    public TLong(long value) {
        m_value = value;
    }

    /**
     * Constructs a TLong object initialized to the value specified by the
     * String parameter. The radix is assumed to be 10.
     *
     * @param s         the String to be converted to a TLong
     * @exception       NumberFormatException If the String does not contain a parsable long.
     */
    public TLong(String s) throws NumberFormatException {
        m_value = parseLong(s);
    }

    /**
     * Sets the value of this TLong to the specified long value.
     */
    public void setValue(long value) {
        m_value = value;
    }

    /**
     * Returns the value of this TLong as a byte.
     */
    public byte byteValue() {
        return (byte) m_value;
    }

    /**
     * Returns the value of this TLong as a short.
     */
    public short shortValue() {
        return (short) m_value;
    }

    /**
     * Returns the value of this TLong as an int.
     */
    public int intValue() {
        return (int) m_value;
    }

    /**
     * Returns the value of this TLong as a long.
     */
    public long longValue() {
        return m_value;
    }

    /**
     * Returns the value of this TLong as a float.
     */
    public float floatValue() {
        return (float) m_value;
    }

    /**
     * Returns the value of this TLong as a double.
     */
    public double doubleValue() {
        return (double) m_value;
    }

    /**
     * Returns a String object representing this TLong's value.
     */
    public String toString() {
        return TLong.toString(m_value);
    }

    /**
     * Returns a hashcode for this TLong.
     */
    public int hashCode() {
        return (int) m_value;
    }

    /**
     * Compares this object to the specified object.
     *
     * @param obj       the object to compare with
     * @return          true if the objects are the same; false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        return compareTo(obj) == 0;
    }

    /**
     * Compares two TLongs numerically.
     *
     * @param   value   the <code>TLong</code> to be compared.
     * @return  the value <code>0</code> if the argument TLong is equal to
     *          this TLong; a value less than <code>0</code> if this TLong
     *          is numerically less than the TLong argument; and a
     *          value greater than <code>0</code> if this TLong is
     *          numerically greater than the TLong argument (signed comparison).
     */
    public int compareTo(TLong value) {
        long cmp = m_value - value.m_value;
        return (cmp < 0) ? -1 : (cmp > 0) ? 1 : 0;
    }

    /**
     * Compares this TLong to another Object.  If the Object is a TLong,
     * this function behaves like <code>compareTo(TLong)</code>.
     * it throws a <code>ClassCastException</code> (as TLongs are comparable
     * only to other TLongs).
     *
     * @param   o the <code>Object</code> to be compared.
     * @return  the value <code>0</code> if the argument is a TLong
     *              numerically equal to this TLong; a value less than
     *              <code>0</code> if the argument is a TLong numerically
     *          greater than this TLong; and a value greater than
     *              <code>0</code> if the argument is a TLong numerically
     *              less than this TLong.
     * @exception <code>ClassCastException</code> if the argument is not a
     *              <code>TLong</code>.
     * @see     com.tce.lang.IComparable
     */
    public int compareTo(Object o) {
        return compareTo((TLong) o);
    }

    /**
     * Creates a string representation of the first argument in the
     * radix specified by the second argument.
     * <p>
     * If the radix is smaller than <code>Character.MIN_RADIX</code> or
     * larger than <code>Character.MAX_RADIX</code>, then the radix
     * <code>10</code> is used instead.
     * <p>
     * If the first argument is negative, the first element of the
     * result is the ASCII minus character <code>'-'</code>. If the first
     * argument is not negative, no sign character appears in the result.
     * The following ASCII characters are used as digits:
     * <ul><code>
     *   0123456789abcdefghijklmnopqrstuvwxyz
     * </code></ul>
     *
     * @param   i       a long.
     * @param   radix   the radix.
     * @return  a string representation of the argument in the specified radix.
     * @see     java.lang.Character#MAX_RADIX
     * @see     java.lang.Character#MIN_RADIX
     * @see     #toString(long)
     * @see     #toHexString(long)
     * @see     #toOctalString(long)
     * @see     #toBinaryString(long)
     */
    public static String toString(long i, int radix) {
        if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) radix = 10;
        if (radix == 10) {
            return toString(i);
        }
        char buf[] = new char[TNumberConstants.BITS_PER_LONG + 1];
        boolean negative = (i < 0);
        int charPos = TNumberConstants.BITS_PER_LONG;
        if (!negative) {
            i = -i;
        }
        while (i <= -radix) {
            buf[charPos--] = TNumberConstants.DIGITS[-(int) (i % radix)];
            i = i / radix;
        }
        buf[charPos] = TNumberConstants.DIGITS[-(int) i];
        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (TNumberConstants.BITS_PER_LONG + 1 - charPos));
    }

    /**
     * Creates a string representation of the long argument as an
     * unsigned long in base&nbsp;16.
     * <p>
     * The unsigned long value is the argument plus 2<sup>64</sup> if
     * the argument is negative; otherwise, it is equal to the argument.
     * This value is converted to a string of ASCII digits in hexadecimal
     * (base&nbsp;16) with no extra leading <code>0</code>s.
     *
     * @param   i   a long.
     * @return  the string representation of the unsigned long value
     *          represented by the argument in hexadecimal (base&nbsp;16).
     */
    public static String toHexString(long i) {
        return toUnsignedString(i, 4);
    }

    /**
     * Creates a string representation of the long argument as an
     * unsigned long in base 8.
     * <p>
     * The unsigned long value is the argument plus 2<sup>64</sup> if
     * the argument is negative; otherwise, it is equal to the argument.
     * This value is converted to a string of ASCII digits in octal
     * (base&nbsp;8) with no extra leading <code>0</code>s.
     *
     * @param   i   a long
     * @return  the string representation of the unsigned long value
     *          represented by the argument in octal (base&mnsp;8).
     */
    public static String toOctalString(long i) {
        return toUnsignedString(i, 3);
    }

    /**
     * Creates a string representation of the long argument as an
     * unsigned long in base&nbsp;2.
     * <p>
     * The unsigned long value is the argument plus 2<sup>64</sup>if
     * the argument is negative; otherwise it is equal to the argument.
     * This value is converted to a string of ASCII digits in binary
     * (base&nbsp;2) with no extra leading <code>0</code>s.
     *
     * @param   i   a long.
     * @return  the string representation of the unsigned long value
     *          represented by the argument in binary (base&nbsp;2).
     */
    public static String toBinaryString(long i) {
        return toUnsignedString(i, 1);
    }

    /**
     * Convert the long to an unsigned number.
     */
    private static String toUnsignedString(long i, int shift) {
        StringBuffer bp = new StringBuffer((shift >= 3) ? TNumberConstants.LONG_MIN_AS_STRING_LENGTH : TNumberConstants.BITS_PER_LONG);
        int radix = 1 << shift;
        long mask = (long) (radix - 1);
        do {
            bp.append(TNumberConstants.DIGITS[(int) (i & mask)]);
            i >>>= shift;
        } while (i != 0);
        return bp.reverse().toString();
    }

    /**
     * Returns a new String object representing the specified long. The radix
     * is assumed to be 10.
     *
     * @param   i   a long to be converted.
     * @return  a string representation of the argument in base&nbsp;10.
     * @see     #toString(long, int)
     * @see     #toHexString(long)
     * @see     #toOctalString(long)
     * @see     #toBinaryString(long)
     */
    public static String toString(long i) {
        if (i == TLong.MIN_VALUE) {
            return TNumberConstants.LONG_MIN_AS_STRING;
        }
        boolean negative = (i < 0);
        if (negative) {
            i = -i;
        }
        StringBuffer tmp = new StringBuffer(TNumberConstants.LONG_MIN_AS_STRING_LENGTH + 1);
        do {
            int digit = (int) (i % 100);
            tmp.append(TNumberConstants.RADIX_TEN_UNITS[digit]);
            if (digit > 10) tmp.append(TNumberConstants.RADIX_TEN_TENTHS[digit]);
        } while ((i /= 100) != 0);
        if (negative) tmp.append('-');
        return tmp.reverse().toString();
    }

    /**
     * Parses the string argument as a signed long in the radix
     * specified by the second argument. The characters in the string
     * must all be digits of the specified radix (as determined by
     * whether <code>Character.digit</code> returns a
     * nonnegative value), except that the first character may be an
     * ASCII minus sign <code>'-'</code> to indicate a negative value.
     * The resulting long value is returned.
     *
     * @param      s   the <code>String</code> containing the long.
     * @param      radix   the radix to be used.
     * @return     the long represented by the string argument in the
     *             specified radix.
     * @exception  NumberFormatException  if the string does not contain a
     *               parsable long.
     */
    public static long parseLong(String s, int radix) throws NumberFormatException {
        return parseLong(s, radix, false);
    }

    /**
     *
     */
    private static long parseLong(String s, int radix, boolean decode) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
        }
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
        }
        int slen = s.length();
        if (slen <= 0) {
            throw new NumberFormatException(s);
        }
        char[] ch = s.toCharArray();
        long result = 0;
        boolean negative = false;
        int index = 0;
        long limit;
        if (ch[0] == '-') {
            negative = true;
            limit = TLong.MIN_VALUE;
            index++;
        } else {
            limit = -TLong.MAX_VALUE;
        }
        if (decode && !negative) {
            if (ch[index] == '0') {
                try {
                    if (Character.toUpperCase(ch[index + 1]) == 'X') {
                        index += 2;
                        radix = 16;
                        if (index >= slen) throw new NumberFormatException("string empty");
                    } else {
                        radix = 8;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            } else {
                if (ch[index] == '#') {
                    index++;
                    radix = 16;
                    if (index >= slen) throw new NumberFormatException("string empty");
                } else {
                    radix = 10;
                }
            }
        }
        long multmin = limit / radix;
        if (index < slen) {
            int digit = Character.digit(ch[index++], radix);
            if (digit < 0) throw new NumberFormatException(s);
            result = -digit;
        }
        while (index < slen) {
            int digit = Character.digit(ch[index++], radix);
            if (digit < 0) throw new NumberFormatException(s);
            if (result < multmin) throw new NumberFormatException(s);
            result *= radix;
            if (result < limit + digit) throw new NumberFormatException(s);
            result -= digit;
        }
        if (!negative) {
            return -result;
        } else if (index > 1) {
            return result;
        } else {
            throw new NumberFormatException(s);
        }
    }

    /**
     * Parses the string argument as a signed decimal long. The
     * characters in the string must all be decimal digits, except that
     * the first character may be an ASCII minus sign <code>'-'</code> to
     * indicate a negative value.
     *
     * @param      s   a string.
     * @return     the long represented by the argument in decimal.
     * @exception  NumberFormatException  if the string does not contain a
     *               parsable long.
     */
    public static long parseLong(String s) throws NumberFormatException {
        return parseLong(s, 10, false);
    }

    /**
     * Returns a new TLong object initialized to the value of the
     * specified String.  Throws an exception if the String cannot be
     * parsed as a long.
     *
     * @param      s   the string to be parsed.
     * @return     a newly constructed <code>TLong</code> initialized to the
     *             value represented by the string argument in the specified
     *             radix.
     * @exception  NumberFormatException  if the <code>String</code> does not
     *               contain a parsable long.
     */
    public static TLong valueOf(String s, int radix) throws NumberFormatException {
        return new TLong(parseLong(s, radix));
    }

    /**
     * Returns a new TLong object initialized to the value of the
     * specified String.  Throws an exception if the String cannot be
     * parsed as a long. The radix is assumed to be 10.
     *
     * @param      s   the string to be parsed.
     * @return     a newly constructed <code>TLong</code> initialized to the
     *             value represented by the string argument.
     * @exception  NumberFormatException  if the string does not contain a
     *               parsable long.
     */
    public static TLong valueOf(String s) throws NumberFormatException {
        return new TLong(parseLong(s, 10));
    }

    /**
     * Determines the long value of the system property with the
     * specified name.
     * <p>
     * The first argument is treated as the name of a system property.
     * System properties are accessible through <code>getProperty</code>
     * and , a method defined by the <code>System</code> class. The
     * string value of this property is then interpreted as an integer
     * value and an <code>TLong</code> object representing this value is
     * returned. Details of possible numeric formats can be found with
     * the definition of <code>getProperty</code>.
     * <p>
     * If there is no property with the specified name, or if the
     * property does not have the correct numeric format, then
     * <code>null</code> is returned.
     *
     * @param   nm   property name.
     * @return  the <code>TLong</code> value of the property.
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static TLong getLong(String nm) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPropertyAccess(nm);
        return getLong(nm, null);
    }

    /**
     * Determines the integer value of the system property with the
     * specified name.
     * <p>
     * The first argument is treated as the name of a system property.
     * System properties are accessible through <code>getProperty</code>
     * and , a method defined by the <code>System</code> class. The
     * string value of this property is then interpreted as a long
     * value and an <code>TLong</code> object representing this value is
     * returned. Details of possible numeric formats can be found with
     * the definition of <code>getProperty</code>.
     * <p>
     * If there is no property with the specified name, or if the
     * property does not have the correct numeric format, then a
     * <code>TLong</code> object that represents the value of the
     * second argument is returned.
     *
     * @param   nm   property name.
     * @param   val   default value.
     * @return  the <code>TLong</code> value of the property.
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static TLong getLong(String nm, long val) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPropertyAccess(nm);
        TLong result = getLong(nm, null);
        return (result == null) ? new TLong(val) : result;
    }

    /**
     * Determines the long value of the system property with the
     * specified name.
     * <p>
     * The first argument is treated as the name of a system property.
     * System properties are accessible through <code>getProperty</code>
     * and , a method defined by the <code>System</code> class. The
     * string value of this property is then interpreted as a long
     * value and an <code>TLong</code> object representing this value is
     * returned.
     * <p>
     * If the property value begins with "<code>0x</code>" or
     * "<code>#</code>", not followed by a minus sign, the rest
     * of it is parsed as a hexadecimal long exactly as for the method
     * <code>TLong.valueOf</code> with radix 16.
     * <p>
     * If the property value begins with "<code>0</code>" then
     * it is parsed as an octal long exactly as for the method
     * <code>TLong.valueOf</code> with radix 8.
     * <p>
     * Otherwise the property value is parsed as a decimal long
     * exactly as for the method <code>TLong.valueOf</code> with radix 10.
     * <p>
     * The second argument is the default value. If there is no property
     * of the specified name, or if the property does not have the
     * correct numeric format, then the second argument is returned.
     *
     * @param   nm   property name.
     * @param   val   default value.
     * @return  the <code>TLong</code> value of the property.
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     * @see     #valueOf(String, long)
     * @see     #valueOf(String)
     */
    public static TLong getLong(String nm, TLong val) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPropertyAccess(nm);
        String v = System.getProperty(nm);
        if (v != null) {
            try {
                return TLong.decode(v);
            } catch (NumberFormatException e) {
            }
        }
        return val;
    }

    /**
     * Decodes a <code>String</code> into an <code>TLong</code>.  Accepts
     * decimal, hexadecimal, and octal numbers, in the following formats:
     * <pre>
     *     [-]    <decimal constant>
     *     [-] 0x     <hex constant>
     *     [-] #      <hex constant>
     *     [-] 0    <octal constant>
     * </pre>
     *
     * The constant following an (optional) negative sign and/or "radix
     * specifier" is parsed as by the <code>TLong.parseLong</code> method
     * with the specified radix (10, 8 or 16).  This constant must be positive
     * or a NumberFormatException will result.  The result is made negative if
     * first character of the specified <code>String</code> is the negative
     * sign.  No whitespace characters are permitted in the
     * <code>String</code>.
     *
     * @param     nm the <code>String</code> to decode.
     * @return    the <code>TLong</code> represented by the specified string.
     * @exception NumberFormatException  if the <code>String</code> does not
     *            contain a parsable integer.
     * @see #decodeInt(String)
     * @see #parseInt(String, long)
     */
    public static TLong decode(String nm) throws NumberFormatException {
        return new TLong(parseLong(nm, 10, true));
    }

    /**
     * Decodes a <code>String</code> into an <code>long</code>.  Accepts
     * decimal, hexadecimal, and octal numbers, in the following formats:
     * <pre>
     *     [-]    <decimal constant>
     *     [-] 0x     <hex constant>
     *     [-] #      <hex constant>
     *     [-] 0    <octal constant>
     * </pre>
     *
     * The constant following an (optional) negative sign and/or "radix
     * specifier" is parsed as by the <code>TLong.parseLong</code> method
     * with the specified radix (10, 8 or 16).  This constant must be positive
     * or a NumberFormatException will result.  The result is made negative if
     * first character of the specified <code>String</code> is the negative
     * sign.  No whitespace characters are permitted in the
     * <code>String</code>.
     *
     * @param     nm the <code>String</code> to decode.
     * @return    the <code>long</code> represented by the specified string.
     * @exception NumberFormatException  if the <code>String</code> does not
     *            contain a parsable integer.
     * @see #decode(String)
     * @see #parseInt(String, long)
     */
    public static long decodeLong(String nm) throws NumberFormatException {
        return parseLong(nm, 10, true);
    }
}
