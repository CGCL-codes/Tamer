package net.sourceforge.javautil.common;

/**
 * Utilities related to byte's and byte arrays.
 * 
 * @author ponder
 * @author $Author: ponderator $
 * @version $Id: ByteUtil.java 2757 2011-02-15 15:01:42Z ponderator $
 */
public class ByteUtil {

    public static final int ONE_BYTE_INTEGER = 0xFF;

    public static final int TWO_BYTE_INTEGER = 0xFFFF;

    public static final int THREE_BYTE_INTEGER = 0xFFFFFF;

    public static final int FOUR_BYTE_INTEGER = Integer.MAX_VALUE;

    public static final long FIVE_BYTE_LONG = 0xFFFFFFFFFFL;

    public static final long SIX_BYTE_LONG = 0xFFFFFFFFFFFFL;

    public static final long SEVEN_BYTE_LONG = 0xFFFFFFFFFFFFFFL;

    public static final long EIGHT_BYTE_LONG = Long.MAX_VALUE;

    /**
	 * Assumes using a little bytes as possible, as little as 1 or as many as 4.
	 * 
	 * @see #translate(int, boolean)
	 */
    public static byte[] translate(int value) {
        return translate(value, 1, 4);
    }

    /**
	 * @param value The integer value
	 * @param minBytes The minimum amount of bytes that the number should be stored in
	 * @param maxBytes The maximum amount of bytes that the number should be stored in
	 * @return An array of 1-4 byte(s) that contains the integer representation, with the last byte containing the highest value
	 */
    public static byte[] translate(int value, int minBytes, int maxBytes) {
        if (value <= ONE_BYTE_INTEGER && minBytes < 2) {
            return new byte[] { (byte) value };
        } else if (value <= TWO_BYTE_INTEGER && minBytes < 3 && maxBytes > 1) {
            return new byte[] { (byte) value, (byte) (value >>> 8) };
        } else if (value <= THREE_BYTE_INTEGER && minBytes < 4 && maxBytes > 2) {
            return new byte[] { (byte) value, (byte) (value >>> 8), (byte) (value >>> 16) };
        } else if (maxBytes == 4) {
            return new byte[] { (byte) value, (byte) (value >>> 8), (byte) (value >>> 16), (byte) (value >>> 24) };
        } else {
            throw new IllegalArgumentException("Could not fit number in lesser bytes: " + value + ", max:" + maxBytes);
        }
    }

    /**
	 * NOTE: This will decode the values according to the positions provided by the {@link #translate(int)} method.
	 * 
	 * @param values The translated values that were an <code>int</code>
	 * @return The integer value
	 * 
	 * @see #translate(int)
	 */
    public static int translate(byte[] values) {
        if (values.length == 4) {
            return (values[3] << 24) + ((values[2] & 0xFF) << 16) + ((values[1] & 0xFF) << 8) + (values[0] & 0xFF);
        } else if (values.length == 3) {
            return ((values[2] & 0xFF) << 16) + ((values[1] & 0xFF) << 8) + (values[0] & 0xFF);
        } else if (values.length == 2) {
            return ((values[1] & 0xFF) << 8) + (values[0] & 0xFF);
        } else if (values.length == 1) {
            return (values[0] & 0xFF);
        } else if (values.length == 0) {
            return 0;
        }
        throw new IllegalArgumentException("Integers can only be stored in maximum of 4-byte arrays: " + values.length);
    }

    public static byte[] translate(long value) {
        return translate(value, 1, 8);
    }

    /**
	 * @param value The integer value
	 * @param minBytes The minimum amount of bytes that the number should be stored in
	 * @param maxBytes The maximum amount of bytes that the number should be stored in
	 * @return An array of 1-4 byte(s) that contains the integer representation, with the last byte containing the highest value
	 */
    public static byte[] translate(long value, int minBytes, int maxBytes) {
        byte[] values = new byte[8];
        int needed = 0;
        for (int i = 0; i < 8; i++) {
            values[i] = (byte) (value >>> (i * 8));
            if (values[i] != 0) {
                needed = i + 1;
            }
        }
        if (needed > maxBytes) {
            throw new IllegalArgumentException("Could not fit number in lesser bytes: " + value + ", max:" + maxBytes + " needed: " + needed);
        }
        if (needed <= minBytes) {
            byte[] shortened = new byte[needed];
            System.arraycopy(values, 0, shortened, 0, needed);
            values = shortened;
        } else if (maxBytes < values.length) {
            byte[] shortened = new byte[maxBytes];
            System.arraycopy(values, 0, shortened, 0, maxBytes);
            values = shortened;
        }
        return values;
    }

    /**
	 * NOTE: This will decode the values according to the positions provided by the {@link #translate(int)} method.
	 * 
	 * @param values The translated values that were an <code>int</code>
	 * @return The integer value
	 * 
	 * @see #translate(int)
	 */
    public static long translateLong(byte[] values) {
        if (values.length > 8) {
            throw new IllegalArgumentException("A long can only be stored in a maximum of 8 bytes");
        }
        long value = 0;
        for (int i = 0; i < values.length; i++) {
            value += (values[i] & 0xFFL) << (i * 8);
        }
        return value;
    }

    /**
	 * @param hexString The string of hex characters
	 * @return A byte array representing the hex string
	 * 
	 * @see StringUtil#hexify(byte[])
	 */
    public static byte[] dehexify(String hexString) {
        int length = hexString.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
        }
        return data;
    }

    /**
	 * Copy the contents of the array into a new array.
	 * 
	 * @param original The original byte array
	 * @return The new array with the same contents
	 */
    public static byte[] copy(byte[] original) {
        byte[] copy = new byte[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
	 * @param src The byte array to compare
	 * @param dst The byte array to compare to
	 * @return True if both byte array contain the same amount of bytes
	 * and the exact same bytes in each array position.
	 */
    public static boolean equals(byte[] src, byte[] dst) {
        if (src.length != dst.length) return false;
        for (int i = 0; i < src.length; i++) if (src[i] != dst[i]) return false;
        return true;
    }

    /**
	 * @param original The original array
	 * @param offset The offset for the beginning of the slice
	 * @param length The length of the slice
	 * @return An array of the specified length containing the sliced contents 
	 */
    public static byte[] getSlice(byte[] original, int offset, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(original, offset, bytes, 0, length);
        return bytes;
    }

    /**
	 * @param slice The array slice
	 * @param target The target array
	 * @param offset The offset in the target array
	 * @return The length of the slice
	 */
    public static int putSlice(byte[] slice, byte[] target, int offset) {
        System.arraycopy(slice, 0, target, offset, slice.length);
        return slice.length;
    }
}
