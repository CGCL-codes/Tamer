package de.carne.nio;

import java.nio.ByteOrder;

/**
 * This class provides the necessary functions to access raw byte data with Java types.
 */
public final class Data {

    /**
	 * Size of a short (2 bytes)
	 */
    public static final int SIZE_SHORT = 2;

    /**
	 * Size of a int (4 bytes)
	 */
    public static final int SIZE_INT = 4;

    /**
	 * Size of a long (8 bytes)
	 */
    public static final int SIZE_LONG = 8;

    /**
	 * Align int value.
	 * 
	 * @param i The int value to align.
	 * @param alignment The alignment to apply.
	 * @return The aligned int value.
	 */
    public static int alignTo(int i, int alignment) {
        return (i + (alignment - 1)) & ~(alignment - 1);
    }

    /**
	 * Align long value.
	 * 
	 * @param l The long value to align.
	 * @param alignment The alignment to apply.
	 * @return The aligned long value.
	 */
    public static long alignTo(long l, int alignment) {
        return (l + (alignment - 1)) & ~(alignment - 1);
    }

    /**
	 * Convert byte data (2 bytes) to a short value.
	 * 
	 * @param order The byte order to use.
	 * @param data The byte data to convert.
	 * @return The corresponding short value.
	 */
    public static short toShort(ByteOrder order, byte[] data) {
        return toShort(order, data, 0);
    }

    /**
	 * Convert byte data (2 bytes) to a short value.
	 * 
	 * @param order The byte order to use.
	 * @param data The byte data to convert.
	 * @param offset The first byte to convert.
	 * @return The corresponding short value.
	 */
    public static short toShort(ByteOrder order, byte[] data, int offset) {
        assert order != null;
        assert data != null;
        assert offset + SIZE_SHORT <= data.length;
        short s = 0;
        if (ByteOrder.LITTLE_ENDIAN.equals(order)) {
            s = (short) (((data[offset] & 0xff) | ((data[offset + 1] & 0xff) << 8)) & 0xffff);
        } else {
            s = (short) (((data[offset + 1] & 0xff) | ((data[offset] & 0xff) << 8)) & 0xffff);
        }
        return s;
    }

    /**
	 * Convert a short value to byte data (2 bytes).
	 * 
	 * @param order The byte order to use.
	 * @param s The short value to convert.
	 * @param data The array receiving the byte data.
	 */
    public static void fromShort(ByteOrder order, short s, byte[] data) {
        fromShort(order, s, data, 0);
    }

    /**
	 * Convert a short value to byte data (2 bytes).
	 * 
	 * @param order The byte order to use.
	 * @param s The short value to convert.
	 * @param data The array receiving the byte data.
	 * @param offset The first byte to write.
	 */
    public static void fromShort(ByteOrder order, short s, byte[] data, int offset) {
        assert order != null;
        assert data != null;
        assert offset + SIZE_SHORT <= data.length;
        if (ByteOrder.LITTLE_ENDIAN.equals(order)) {
            data[offset] = (byte) (s & 0xff);
            data[offset + 1] = (byte) ((s >> 8) & 0xff);
        } else {
            data[offset + 1] = (byte) (s & 0xff);
            data[offset] = (byte) ((s >> 8) & 0xff);
        }
    }

    /**
	 * Swap the bytes of a short value (2 bytes).
	 * 
	 * @param s The short value to swap.
	 * @return The swapped short value.
	 */
    public static short swapShort(short s) {
        return (short) (((s << 8) & 0xff00) | ((s >> 8) & 0xff));
    }

    /**
	 * Convert byte data (4 bytes) to an int value.
	 * 
	 * @param order The byte order to use.
	 * @param data The byte data to convert.
	 * @return The corresponding int value.
	 */
    public static int toInt(ByteOrder order, byte[] data) {
        return toInt(order, data, 0);
    }

    /**
	 * Convert byte data (4 bytes) to an int value.
	 * 
	 * @param order The byte order to use.
	 * @param data The byte data to convert.
	 * @param offset The first byte to convert.
	 * @return The corresponding int value.
	 */
    public static int toInt(ByteOrder order, byte[] data, int offset) {
        assert order != null;
        assert data != null;
        assert offset + SIZE_INT <= data.length;
        int i = 0;
        if (ByteOrder.LITTLE_ENDIAN.equals(order)) {
            i = (data[offset] & 0xff) | ((data[offset + 1] & 0xff) << 8) | ((data[offset + 2] & 0xff) << 16) | ((data[offset + 3] & 0xff) << 24);
        } else {
            i = (data[offset + 24] & 0xff) | ((data[offset + 16] & 0xff) << 8) | ((data[offset + 8] & 0xff) << 16) | ((data[offset] & 0xff) << 24);
        }
        return i;
    }

    /**
	 * Convert an int value to byte data (4 bytes).
	 * 
	 * @param order The byte order to use.
	 * @param i The int value to convert.
	 * @param data The array receiving the byte data.
	 */
    public static void fromInt(ByteOrder order, int i, byte[] data) {
        fromInt(order, i, data, 0);
    }

    /**
	 * Convert an int value to byte data (4 bytes).
	 * 
	 * @param order The byte order to use.
	 * @param i The int value to convert.
	 * @param data The array receiving the byte data.
	 * @param offset The first byte to write.
	 */
    public static void fromInt(ByteOrder order, int i, byte[] data, int offset) {
        assert order != null;
        assert data != null;
        assert offset + SIZE_INT <= data.length;
        if (ByteOrder.LITTLE_ENDIAN.equals(order)) {
            data[offset] = (byte) (i & 0xff);
            data[offset + 1] = (byte) ((i >> 8) & 0xff);
            data[offset + 2] = (byte) ((i >> 16) & 0xff);
            data[offset + 3] = (byte) ((i >> 24) & 0xff);
        } else {
            data[offset + 3] = (byte) (i & 0xff);
            data[offset + 2] = (byte) ((i >> 8) & 0xff);
            data[offset + 1] = (byte) ((i >> 16) & 0xff);
            data[offset] = (byte) ((i >> 24) & 0xff);
        }
    }

    /**
	 * Swap the bytes of an int value (4 bytes).
	 * 
	 * @param i The int value to swap.
	 * @return The swapped int value.
	 */
    public static int swapInt(int i) {
        return ((i << 24) & 0xff000000) | ((i << 8) & 0xff0000) | ((i >> 8) & 0xff00) | ((i >> 24) & 0xff);
    }

    /**
	 * Convert byte data (8 bytes) to a long value.
	 * 
	 * @param order The byte order to use.
	 * @param data The byte data to convert.
	 * @return The corresponding long value.
	 */
    public static long toLong(ByteOrder order, byte[] data) {
        return toLong(order, data, 0);
    }

    /**
	 * Convert byte data (8 bytes) to a long value.
	 * 
	 * @param order The byte order to use.
	 * @param data The byte data to convert.
	 * @param offset The first byte to convert.
	 * @return The corresponding long value.
	 */
    public static long toLong(ByteOrder order, byte[] data, int offset) {
        assert order != null;
        assert data != null;
        assert offset + SIZE_LONG <= data.length;
        long l = 0;
        if (ByteOrder.LITTLE_ENDIAN.equals(order)) {
            l = (data[offset] & 0xffl) | ((data[offset + 1] & 0xffl) << 8) | ((data[offset + 2] & 0xffl) << 16) | ((data[offset + 3] & 0xffl) << 24) | ((data[offset + 4] & 0xffl) << 32) | ((data[offset + 5] & 0xffl) << 40) | ((data[offset + 6] & 0xffl) << 48) | ((data[offset + 7] & 0xffl) << 56);
        } else {
            l = (data[offset + 7] & 0xffl) | ((data[offset + 6] & 0xffl) << 8) | ((data[offset + 5] & 0xffl) << 16) | ((data[offset + 4] & 0xffl) << 24) | ((data[offset + 3] & 0xffl) << 32) | ((data[offset + 2] & 0xffl) << 40) | ((data[offset + 1] & 0xffl) << 48) | ((data[offset] & 0xffl) << 56);
        }
        return l;
    }

    /**
	 * Convert a long value to byte data (8 bytes).
	 * 
	 * @param order The byte order to use.
	 * @param l The long value to convert.
	 * @param data The array receiving the byte data.
	 */
    public static void fromLong(ByteOrder order, long l, byte[] data) {
        fromLong(order, l, data, 0);
    }

    /**
	 * Convert a long value to byte data (8 bytes).
	 * 
	 * @param order The byte order to use.
	 * @param l The long value to convert.
	 * @param data The array receiving the byte data.
	 * @param offset The first byte to write.
	 */
    public static void fromLong(ByteOrder order, long l, byte[] data, int offset) {
        assert data != null;
        assert offset + SIZE_LONG <= data.length;
        if (ByteOrder.LITTLE_ENDIAN.equals(order)) {
            data[offset] = (byte) (l & 0xffl);
            data[offset + 1] = (byte) ((l >> 8) & 0xffl);
            data[offset + 2] = (byte) ((l >> 16) & 0xffl);
            data[offset + 3] = (byte) ((l >> 24) & 0xffl);
            data[offset + 4] = (byte) ((l >> 32) & 0xffl);
            data[offset + 5] = (byte) ((l >> 40) & 0xffl);
            data[offset + 6] = (byte) ((l >> 48) & 0xffl);
            data[offset + 7] = (byte) ((l >> 56) & 0xffl);
        } else {
            data[offset + 7] = (byte) (l & 0xffl);
            data[offset + 6] = (byte) ((l >> 8) & 0xffl);
            data[offset + 5] = (byte) ((l >> 16) & 0xffl);
            data[offset + 4] = (byte) ((l >> 24) & 0xffl);
            data[offset + 3] = (byte) ((l >> 32) & 0xffl);
            data[offset + 2] = (byte) ((l >> 40) & 0xffl);
            data[offset + 1] = (byte) ((l >> 48) & 0xffl);
            data[offset] = (byte) ((l >> 56) & 0xffl);
        }
    }

    /**
	 * Swap the bytes of a long value (8 bytes).
	 * 
	 * @param l The long value to swap.
	 * @return The swapped long value.
	 */
    public static long swapLong(long l) {
        return ((l << 56) & 0xff00000000000000l) | ((l << 40) & 0xff000000000000l) | ((l << 24) & 0xff0000000000l) | ((l << 8) & 0xff00000000l) | ((l >> 8) & 0xff000000l) | ((l >> 24) & 0xff0000l) | ((l >> 40) & 0xff00l) | ((l >> 56) & 0xffl);
    }
}
