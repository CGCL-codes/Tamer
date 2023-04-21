package dshub;

/**
 *
 * @author Thanks To Mr. Pretorian for providing 
 *
 * (PD) 2003 The Bitzi Corporation
 * Please see http://bitzi.com/publicdomain for more info.
 *
 * Base32.java
 *
 */
public class Base32 {

    private static final String base32Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    private static final byte[] base32Lookup = { 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };

    private static final String errorCanonicalLength = "non canonical Base32 string length";

    private static final String errorCanonicalEnd = "non canonical bits at end of Base32 string";

    private static final String errorInvalidChar = "invalid character in Base32 string";

    /**
     * Encode an array of binary bytes into a Base32 string.
     * Should not fail (the only possible exception is that the
     * returned string cannot be allocated in memory)
     */
    public static String encode(final byte[] bytes) {
        StringBuffer base32 = new StringBuffer((bytes.length * 8 + 4) / 5);
        int currByte, digit, i = 0;
        while (i < bytes.length) {
            currByte = bytes[i++] & 255;
            base32.append(base32Chars.charAt(currByte >> 3));
            digit = (currByte & 7) << 2;
            if (i >= bytes.length) {
                base32.append(base32Chars.charAt(digit));
                break;
            }
            currByte = bytes[i++] & 255;
            base32.append(base32Chars.charAt(digit | (currByte >> 6)));
            base32.append(base32Chars.charAt((currByte >> 1) & 31));
            digit = (currByte & 1) << 4;
            if (i >= bytes.length) {
                base32.append(base32Chars.charAt(digit));
                break;
            }
            currByte = bytes[i++] & 255;
            base32.append(base32Chars.charAt(digit | (currByte >> 4)));
            digit = (currByte & 15) << 1;
            if (i >= bytes.length) {
                base32.append(base32Chars.charAt(digit));
                break;
            }
            currByte = bytes[i++] & 255;
            base32.append(base32Chars.charAt(digit | (currByte >> 7)));
            base32.append(base32Chars.charAt((currByte >> 2) & 31));
            digit = (currByte & 3) << 3;
            if (i >= bytes.length) {
                base32.append(base32Chars.charAt(digit));
                break;
            }
            currByte = bytes[i++] & 255;
            base32.append(base32Chars.charAt(digit | (currByte >> 5)));
            base32.append(base32Chars.charAt(currByte & 31));
        }
        return base32.toString();
    }

    /**
     * Decode a Base32 string into an array of binary bytes.
     * May fail if the parameter is a non canonical Base32 string
     * (the only other possible exception is that the
     * returned array cannot be allocated in memory)
     */
    public static byte[] decode(final String base32) throws IllegalArgumentException {
        switch(base32.length() % 8) {
            case 1:
            case 3:
            case 6:
                throw new IllegalArgumentException(errorCanonicalLength);
        }
        byte[] bytes = new byte[base32.length() * 5 / 8];
        int offset = 0, i = 0, lookup;
        byte nextByte, digit;
        while (i < base32.length()) {
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            nextByte = (byte) (digit << 3);
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            bytes[offset++] = (byte) (nextByte | (digit >> 2));
            nextByte = (byte) ((digit & 3) << 6);
            if (i >= base32.length()) {
                if (nextByte != (byte) 0) {
                    throw new IllegalArgumentException(errorCanonicalEnd);
                }
                break;
            }
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            nextByte |= (byte) (digit << 1);
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            bytes[offset++] = (byte) (nextByte | (digit >> 4));
            nextByte = (byte) ((digit & 15) << 4);
            if (i >= base32.length()) {
                if (nextByte != (byte) 0) {
                    throw new IllegalArgumentException(errorCanonicalEnd);
                }
                break;
            }
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            bytes[offset++] = (byte) (nextByte | (digit >> 1));
            nextByte = (byte) ((digit & 1) << 7);
            if (i >= base32.length()) {
                if (nextByte != (byte) 0) {
                    throw new IllegalArgumentException(errorCanonicalEnd);
                }
                break;
            }
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            nextByte |= (byte) (digit << 2);
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            bytes[offset++] = (byte) (nextByte | (digit >> 3));
            nextByte = (byte) ((digit & 7) << 5);
            if (i >= base32.length()) {
                if (nextByte != (byte) 0) {
                    throw new IllegalArgumentException(errorCanonicalEnd);
                }
                break;
            }
            lookup = base32.charAt(i++) - '2';
            if (lookup < 0 || lookup >= base32Lookup.length) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            digit = base32Lookup[lookup];
            if (digit == -1) {
                throw new IllegalArgumentException(errorInvalidChar);
            }
            bytes[offset++] = (byte) (nextByte | digit);
        }
        return bytes;
    }
}
