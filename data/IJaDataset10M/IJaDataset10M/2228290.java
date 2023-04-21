package org.phoneid.keepassj2me;

import java.util.Date;

/**
 * Tools for slicing and dicing Java and KeePass data types.
 * 
 * @author Bill Zwicky <wrzwicky@pobox.com>
 */
public class Types {

    /**
   * Read a 32-bit value.
   * 
   * @param buf
   * @param offset
   * @return
   */
    public static int readInt(byte buf[], int offset) {
        return (buf[offset + 0] & 0xFF) + ((buf[offset + 1] & 0xFF) << 8) + ((buf[offset + 2] & 0xFF) << 16) + ((buf[offset + 3] & 0xFF) << 24);
    }

    /**
   * Write a 32-bit value.
   * 
   * @param val
   * @param buf
   * @param offset
   */
    public static void writeInt(int val, byte[] buf, int offset) {
        buf[offset + 0] = (byte) (val & 0xFF);
        buf[offset + 1] = (byte) ((val >>> 8) & 0xFF);
        buf[offset + 2] = (byte) ((val >>> 16) & 0xFF);
        buf[offset + 3] = (byte) ((val >>> 24) & 0xFF);
    }

    /**
   * Read an unsigned 16-bit value.
   * 
   * @param buf
   * @param offset
   * @return
   */
    public static int readShort(byte[] buf, int offset) {
        return (buf[offset + 0] & 0xFF) + ((buf[offset + 1] & 0xFF) << 8);
    }

    /** Read an unsigned byte */
    public static int readUByte(byte[] buf, int offset) {
        return ((int) buf[offset] & 0xFF);
    }

    /**
   * Return len of null-terminated string (i.e. distance to null)
   * within a byte buffer.
   * 
   * @param buf
   * @param offset
   * @return
   */
    public static int strlen(byte[] buf, int offset) {
        int len = 0;
        while (buf[offset + len] != 0) len++;
        return len;
    }

    /**
   * Copy a sequence of bytes into a new array.
   * 
   * @param b - source array
   * @param offset - first byte
   * @param len - number of bytes
   * @return new byte[len]
   */
    public static byte[] extract(byte[] b, int offset, int len) {
        byte[] b2 = new byte[len];
        System.arraycopy(b, offset, b2, 0, len);
        return b2;
    }

    /**
   * Unpack date from 5 byte format.
   * The five bytes at 'offset' are unpacked to a java.util.Date instance.
   */
    public static Date readTime(byte[] buf, int offset) {
        int dw1 = readUByte(buf, offset);
        int dw2 = readUByte(buf, offset + 1);
        int dw3 = readUByte(buf, offset + 2);
        int dw4 = readUByte(buf, offset + 3);
        int dw5 = readUByte(buf, offset + 4);
        int year = (dw1 << 6) | (dw2 >> 2);
        int month = ((dw2 & 0x00000003) << 2) | (dw3 >> 6);
        int day = (dw3 >> 1) & 0x0000001F;
        int hour = ((dw3 & 0x00000001) << 4) | (dw4 >> 4);
        int minute = ((dw4 & 0x0000000F) << 2) | (dw5 >> 6);
        int second = dw5 & 0x0000003F;
        return null;
    }
}
