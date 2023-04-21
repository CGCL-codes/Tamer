package org.rascalli.framework.commons.number;

/**
 * <p>
 * The UnsignedShort class wraps a numeric value with 2 bytes (16 bits) of
 * length. The bits are interpreted as an unsigned short (which is not supported
 * as a primitive type by the Java platform).
 * </p>
 * 
 * <p>
 * The API of this class mimics the API of other subclasses of {@link Number},
 * such as {@link Byte} or {@link Short}. However, since there is no need to
 * maintain backward compatibility, the public constructors have been omitted
 * (the use of these constructors is discouraged for the Java platform Number
 * classes).
 * </p>
 * 
 * <p>
 * Furthermore, in addition to those APIs, this class contains methods for
 * reading/writing an UnsignedShort from/to a byte[]:
 * </p>
 * 
 * <p>
 * <ul>
 * <li>{@link #valueOf(byte[])}</li>
 * <li>{@link #valueOf(byte[], int)}</li>
 * <li>{@link #toByteArray()}</li>
 * <li>{@link #toByteArray(int)}</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Note that this class cannot be subclassed.
 * </p>
 * 
 * <p>
 * Note: Instances of UnsignedShort are immutable.
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: christian $<br/> $Date: 2007-05-30
 * 13:46:54 +0200 (Mi, 30 Mai 2007) $<br/> $Revision: 2446 $
 * </p>
 * 
 * @author Christian Schollum
 */
public class UnsignedShort extends Number implements Comparable<UnsignedShort> {

    /**
     * 
     */
    private static final long serialVersionUID = 5704258754982439188L;

    /**
     * The number of bytes an unsigned short consists of, {@value}.
     */
    public static final int NUMBER_OF_BYTES = 2;

    /**
     * The number of bits used to represent an unsigned short value in binary
     * form, {@value}.
     */
    public static final int SIZE = NUMBER_OF_BYTES * Byte.SIZE;

    /**
     * A constant holding the maximum value an unsigned short can have, {@value}.
     */
    public static final int MAX_VALUE = (1 << SIZE) - 1;

    /**
     * A constant holding the minimum value an insigned short can have, {@value}.
     */
    public static final int MIN_VALUE = 0;

    /**
     * The numeric value of this UnsignedShort.
     */
    private final int intValue;

    /**
     * <p>
     * Constructs a newly allocated UnsignedShort object that represents the
     * specified numeric value.
     * </p>
     * 
     * <p>
     * Note: it is assumed that the {@code intValue} is already masked (i.e. the
     * 16 most significant bits are set to 0).
     * </p>
     * 
     * @param intValue
     *            the numeric value to be represented by the UnsignedShort
     *            object.
     */
    private UnsignedShort(int intValue) {
        assert isMasked(intValue) : "param intValue is not masked, this indicates an error in the invoked static factory method";
        this.intValue = intValue;
    }

    /**
     * Asserts that the given int has been masked using {@link #maskInt(int)}.
     * 
     * @param i
     *            an int value.
     * @return {@code true} if the int value has been masked, {@code false}
     *         otherwise.
     */
    private boolean isMasked(int i) {
        return (MAX_VALUE & i) == i;
    }

    /**
     * Extracts the relevant bits from the given int value. These are the
     * lowest-order {@value #SIZE} bits of the int.
     * 
     * @param i
     *            an int value.
     * @return The relevant bits of the given int value.
     */
    private static int maskInt(int i) {
        return MAX_VALUE & i;
    }

    /**
     * <p>
     * This method returns an unsigned short equal to the value of:
     * </p>
     * 
     * <p>
     * <code>
     *   UnsignedShort.parseUnsignedShort(s, 10))
     * </code>
     * </p>
     * 
     * @see #parseUnsignedShort(String, int)
     * @param s
     *            the String containing the unsigned short representation to be
     *            parsed, in decimal notation.
     * @return the unsigned short represented by the string argument in the
     *         radix 10.
     * @throws NumberFormatException
     *             if the String does not contain a parsable unsigned short.
     */
    public static int parseUnsignedShort(String s) throws NumberFormatException {
        return parseUnsignedShort(s, 10);
    }

    /**
     * <p>
     * Parses the string argument as an unsigned short in the radix specified by
     * the second argument.
     * </p>
     * 
     * <p>
     * See {@link Integer#parseInt(String, int)} for details.
     * </p>
     * 
     * <p>
     * In addition to the requirements specified by
     * {@link Integer#parseInt(String, int)}, the number contained in the
     * string must conform to the following requirements:
     * <ul>
     * <li>The number must be unsigned.</li>
     * <li>The number must be in the range [{@link #MIN_VALUE},
     * {@link #MAX_VALUE}].</li>
     * </ul>
     * </p>
     * 
     * @param s
     *            the String containing the unsigned short representation to be
     *            parsed.
     * @param radix
     *            the radix to be used while parsing s.
     * @return the unsigned short represented by the string argument in the
     *         specified radix.
     * @throws NumberFormatException
     *             if the String does not contain a parsable unsigned short.
     */
    public static int parseUnsignedShort(String s, int radix) throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i < MIN_VALUE || i > MAX_VALUE) {
            throw new NumberFormatException("value must be an unsigned number in the range [" + MIN_VALUE + ", " + MAX_VALUE + "], but is " + i);
        }
        return maskInt(i);
    }

    /**
     * <p>
     * Constructs a newly allocated UnsignedShort object that represents the
     * specified numeric value.
     * </p>
     * 
     * <p>
     * Note: Only the {@value #SIZE} lowest-order bits of the {@code intValue}
     * are used. All higher-order bits are ignored. This is equivalent to a cast
     * operation (e.g. (short) Integer.MAX_VALUE == -1).
     * </p>
     * 
     * @param intValue
     *            the numeric value to be represented by the UnsignedShort
     *            object.
     */
    public static UnsignedShort valueOf(int i) {
        return new UnsignedShort(maskInt(i));
    }

    /**
     * <p>
     * Constructs a newly allocated UnsignedShort object by parsing the
     * specified string with the {@link #parseUnsignedShort(String)} method.
     * </p>
     * 
     * @param s
     *            a string containing an unsigned short in decimal notation.
     */
    public static UnsignedShort valueOf(String s) {
        return new UnsignedShort(parseUnsignedShort(s));
    }

    /**
     * <p>
     * Constructs a newly allocated UnsignedShort object by parsing the
     * specified string with the {@link #parseUnsignedShort(String, int)}
     * method.
     * </p>
     * 
     * @param s
     *            a string containing an unsigned short.
     */
    public static UnsignedShort valueOf(String s, int radix) {
        return new UnsignedShort(parseUnsignedShort(s, radix));
    }

    /**
     * <p>
     * Interprets the first two bytes of the specified byte[] as an unsigned
     * short.
     * <p>
     * 
     * <p>
     * An invocation of this method {@code valueOf(b)} is equivalent to an
     * invacation of {@code valueOf(b, 0)}.
     * </p>
     * 
     * @param byteArray
     * @return an UnsignedShort with a numerical value equivalent to the first
     *         two bytes of the specified {@code byte[]}.
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code byteArray.length < 2}.
     */
    public static UnsignedShort valueOf(byte[] byteArray) throws ArrayIndexOutOfBoundsException {
        return valueOf(byteArray, 0);
    }

    /**
     * Interprets two bytes from the specified byte[] as an unsigned short. The
     * {@code offset} parameter indicates which two bytes to read from the
     * array.
     * 
     * @param byteArray
     *            a byte[] containing at least 2 bytes.
     * @param offset
     *            an offset to the byte[] indicating where to start reading.
     * @return an UnsignedShort with a numerical value equivalent to two bytes
     *         of the specified {@code byte[]}, starting at {@code offset}.
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code offset < 0} or
     *             {@code byteArray.length - offset < 2}.
     */
    public static UnsignedShort valueOf(byte[] byteArray, int offset) throws ArrayIndexOutOfBoundsException {
        int intValue = 0;
        intValue |= byteArray[offset] & 0xFF;
        intValue <<= 8;
        intValue |= byteArray[offset + 1] & 0xFF;
        return new UnsignedShort(intValue);
    }

    /**
     * Compares this object to the specified object. The result is true if and
     * only if the argument is not null and is an UnsignedShort object that
     * contains the same numeric value as this object.
     * 
     * @param obj
     *            the object to compare with.
     * @return true if the objects are equal; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnsignedShort)) {
            return false;
        }
        UnsignedShort other = (UnsignedShort) obj;
        return intValue() == other.intValue();
    }

    /**
     * Returns a hash code for this UnsignedShort.
     * 
     * @return a hash code value for this object, equal to the numeric value
     *         represented by this UnsignedShort object.
     */
    @Override
    public int hashCode() {
        return intValue();
    }

    /**
     * Returns the numeric value of this UnsignedShort as an {@code int}.
     * 
     * @return the numeric value represented by this object after conversion to
     *         type {@code int}.
     */
    @Override
    public int intValue() {
        return intValue;
    }

    /**
     * Returns the numeric value of this UnsignedShort as an {@code long}.
     * 
     * @return the numeric value represented by this object after conversion to
     *         type {@code long}.
     */
    @Override
    public long longValue() {
        return (long) intValue();
    }

    /**
     * Returns the numeric value of this UnsignedShort as an {@code float}.
     * 
     * @return the numeric value represented by this object after conversion to
     *         type {@code float}.
     */
    @Override
    public float floatValue() {
        return (float) intValue();
    }

    /**
     * Returns the numeric value of this UnsignedShort as an {@code double}.
     * 
     * @return the numeric value represented by this object after conversion to
     *         type {@code double}.
     */
    @Override
    public double doubleValue() {
        return (double) intValue();
    }

    /**
     * <p>
     * Returns a String object representing this UnsignedShort's numeric value.
     * The value is converted to decimal representation and returned as a
     * string, exactly as if the numeric value were given as an argument to the
     * {@link #toString(int)} method.
     * </p>
     * 
     * @return a string representation of the value of this object in base 10.
     */
    @Override
    public String toString() {
        return toString(intValue());
    }

    /**
     * <p>
     * Returns a String object representing the specified unsigned short. The
     * argument is converted to unsigned decimal representation and returned as
     * a string, exactly as if the argument and radix 10 were given as arguments
     * to the {@link #toString(int, int)} method.
     * </p>
     * 
     * @param intValue
     * @return a string representation of the value of the specified unsigned
     *         short in base 10.
     */
    public static String toString(int intValue) {
        return toString(intValue, 10);
    }

    /**
     * <p>
     * Returns a String object representing the specified unsigned short. The
     * argument is converted to an unsigned representation appropriate for the
     * specified {@code radix}.
     * </p>
     * 
     * @see Integer#toString(int, int) for details.
     * 
     * @param intValue
     * @return a string representation of the value of the specified unsigned
     *         short in the specified radix.
     */
    public static String toString(int intValue, int radix) {
        int unsignedShort = new UnsignedShort(intValue).intValue();
        return Integer.toString(unsignedShort, radix);
    }

    /**
     * <p>
     * Converts this UnsignedShort into a byte array.
     * </p>
     * 
     * <p>
     * An invocation of this method such as
     * {@code UnsignedShort#valueOf(i)#toByteArray()} is equivalent to an
     * invocation of {@code UnsignedShort#toByteArray(i)}.
     * </p>
     * 
     * @see #toByteArray(int) for details.
     * 
     * @return A byte array of length {@value #SIZE} containing the two bytes of
     *         this UnsignedShort.
     */
    public byte[] toByteArray() {
        return toByteArray(intValue());
    }

    /**
     * <p>
     * Converts the two least significant bytes of the given int into a byte
     * array, most significant byte first.
     * </p>
     * 
     * <p>
     * Example: If i == 0x12345678, byte[0] == 0x56 and byte[1] == 0x78.
     * </p>
     * 
     * @param i
     *            an int value.
     * @return A byte array of length {@value #SIZE} containing the two least
     *         significant bytes of the given int.
     */
    public static byte[] toByteArray(int i) {
        byte[] byteArray = new byte[NUMBER_OF_BYTES];
        byteArray[0] = (byte) ((i & 0xFF00) >> 8);
        byteArray[1] = (byte) (i & 0x00FF);
        return byteArray;
    }

    /**
     * <p>
     * Compares two Byte objects numerically.
     * </p>
     * 
     * @param other
     *            the UnsignedShort to be compared.
     * @return the value 0 if this UnsignedShort is equal to the argument
     *         UnsignedShort; a value less than 0 if this UnsignedShort is
     *         numerically less than the argument UnsignedShort ; and a value
     *         greater than 0 if this UnsignedShort is numerically greater than
     *         the argument UnsignedShort.
     */
    public int compareTo(UnsignedShort other) {
        if (intValue() < other.intValue()) return -1;
        if (intValue() > other.intValue()) return 1;
        return 0;
    }

    /**
     * <p>
     * Decodes a String into an UnsignedShort.
     * </p>
     * 
     * <p>
     * See {@link Integer#decode(String)} for details.
     * </p>
     * 
     * @param nm
     *            the String to decode.
     * @return an UnsignedShort object holding the int value represented by nm.
     * @throws NumberFormatException
     *             if the String does not contain a parsable integer.
     */
    public static UnsignedShort decode(String nm) throws NumberFormatException {
        return valueOf(Integer.decode(nm));
    }
}
