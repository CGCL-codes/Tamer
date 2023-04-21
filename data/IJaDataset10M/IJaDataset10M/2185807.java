package com.hifiremote.jp1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * The Class Hex.
 */
public class Hex implements Cloneable, Comparable<Hex> {

    /**
   * Instantiates a new hex.
   */
    public Hex() {
        data = new short[0];
    }

    /**
   * Instantiates a new hex.
   * 
   * @param length
   *          the length
   */
    public Hex(int length) {
        data = new short[length];
    }

    /**
   * Instantiates a new hex.
   * 
   * @param text
   *          the text
   */
    public Hex(String text) {
        data = parseHex(text);
    }

    /**
   * Instantiates a new hex.
   * 
   * @param data
   *          the data
   */
    public Hex(short[] data) {
        this.data = data;
    }

    /**
   * Instantiates a new hex.
   * 
   * @param newData
   *          the new data
   * @param offset
   *          the offset
   * @param length
   *          the length
   */
    public Hex(short[] newData, int offset, int length) {
        data = new short[length];
        System.arraycopy(newData, offset, data, 0, length);
    }

    /**
   * Instantiates a new hex.
   * 
   * @param h
   *          the h
   */
    public Hex(Hex h) {
        data = new short[h.data.length];
        System.arraycopy(h.data, 0, data, 0, data.length);
    }

    /**
   * Instantiates a new hex.
   * 
   * @param h
   *          the h
   * @param offset
   *          the offset
   * @param length
   *          the length
   */
    public Hex(Hex h, int offset, int length) {
        data = new short[length];
        System.arraycopy(h.data, offset, data, 0, Math.min(length, h.length() - offset));
        if (h.length() - offset < length) Arrays.fill(data, h.length() - offset, length, (short) 0);
    }

    /**
   * Length.
   * 
   * @return the int
   */
    public int length() {
        return data.length;
    }

    /**
   * Gets the data.
   * 
   * @return the data
   */
    public short[] getData() {
        return data;
    }

    /**
   * Sets the.
   * 
   * @param data
   *          the data
   */
    public void set(short[] data) {
        this.data = data;
    }

    /**
   * Sets the.
   * 
   * @param text
   *          the text
   */
    public void set(String text) {
        data = parseHex(text);
    }

    /**
   * Gets the.
   * 
   * @param offset
   *          the offset
   * @return the int
   */
    public int get(int offset) {
        return get(data, offset);
    }

    /**
   * Gets the.
   * 
   * @param data
   *          the data
   * @param offset
   *          the offset
   * @return the int
   */
    public static int get(short[] data, int offset) {
        return (data[offset] << 8) | data[offset + 1];
    }

    public static Integer get(Short[] data, int offset) {
        if (data[offset] == null || data[offset + 1] == null) {
            return null;
        }
        return (data[offset] << 8) | data[offset + 1];
    }

    /**
   * Put.
   * 
   * @param value
   *          the value
   * @param offset
   *          the offset
   */
    public void put(int value, int offset) {
        put(value, data, offset);
    }

    /**
   * Put.
   * 
   * @param value
   *          the value
   * @param data
   *          the data
   * @param offset
   *          the offset
   */
    public static void put(int value, short[] data, int offset) {
        data[offset] = (short) ((value >> 8) & 0xFF);
        data[offset + 1] = (short) (value & 0xFF);
    }

    public static void put(Integer value, Short[] data, int offset) {
        if (value == null) {
            data[offset] = null;
            data[offset + 1] = null;
        } else {
            data[offset] = (short) ((value >> 8) & 0xFF);
            data[offset + 1] = (short) (value & 0xFF);
        }
    }

    public static void semiPut(Integer value, Short[] data, int offset, int half) {
        if (value == null) {
            data[offset + 1 + half] = null;
            if (data[offset + 2 - half] == null) data[offset] = null;
        } else {
            int shift = 4 * half;
            if (data[offset] == null) data[offset] = 0;
            data[offset] = (short) ((data[offset] & 0x0F << shift) | ((value & 0xF00) >> shift + 4));
            data[offset + 1 + half] = (short) (value & 0xFF);
        }
    }

    public static Integer semiGet(Short[] data, int offset, int half) {
        if (data[offset] == null || data[offset + 1 + half] == null) {
            return null;
        } else {
            int shift = 4 * half;
            return (data[offset] << shift + 4 & 0xF00) | data[offset + 1 + half];
        }
    }

    public void set(short value, int offset) {
        data[offset] = value;
    }

    /**
   * Copy from src
   * 
   * @param src
   *          the src
   */
    public void put(Hex src) {
        put(src, 0);
    }

    /**
   * Copy from src to index
   * 
   * @param src
   *          the src
   * @param index
   *          the index
   */
    public void put(Hex src, int index) {
        put(src.data, index);
    }

    /**
   * Copy from src to index
   * 
   * @param src
   *          the src
   * @param index
   *          the index
   */
    public void put(short[] src, int index) {
        put(src, data, index);
    }

    /**
   * Copy src into dest at index
   * 
   * @param src
   *          the src
   * @param dest
   *          the dest
   * @param index
   *          the index into dest
   */
    public static void put(Hex src, short[] dest, int index) {
        put(src.data, dest, index);
    }

    /**
   * Copy src into dest at index
   * 
   * @param src
   *          the src
   * @param dest
   *          the dest
   * @param index
   *          the index into dest
   */
    public static void put(short[] src, short[] dest, int index) {
        int length = src.length;
        if ((index + length) > dest.length) length = dest.length - index;
        System.arraycopy(src, 0, dest, index, length);
    }

    /**
   * Parses the hex.
   * 
   * @param text
   *          the text
   * @return the short[]
   */
    public static short[] parseHex(String text) {
        short[] rc = null;
        int length = 0;
        int space = text.indexOf(' ');
        if ((space == -1) && (text.length() > 2)) {
            if ((text.length() & 1) == 1) text = "0" + text;
            length = text.length() / 2;
            rc = new short[length];
            for (int i = 0; i < length; i++) {
                int offset = i * 2;
                String temp = text.substring(offset, offset + 2);
                rc[i] = Short.parseShort(temp, 16);
            }
        } else {
            StringTokenizer st = new StringTokenizer(text, " _.$h\n\r\t");
            length = st.countTokens();
            rc = new short[length];
            parseHex(text, rc, 0);
        }
        return rc;
    }

    /**
   * Parses the hex.
   * 
   * @param text
   *          the text
   * @param data
   *          the data
   * @param offset
   *          the offset
   */
    public static void parseHex(String text, short[] data, int offset) {
        StringTokenizer st = new StringTokenizer(text, " _.$h\n\r\t", true);
        short value = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals(" ") || token.equals("$") || token.equals("h") || token.equals("\n") || token.equals("\r") || token.equals("\t")) value = 0; else if (token.equals("_")) value = ADD_OFFSET; else if (token.equals(".")) value = NO_MATCH; else data[offset++] = (short) (value | Short.parseShort(token, 16));
        }
    }

    /**
   * As string.
   * 
   * @param value
   *          the value
   * @return the string
   */
    public static String asString(int value) {
        StringBuilder buff = new StringBuilder(2);
        String str = Integer.toHexString(value & 0xFF);
        if (str.length() < 2) buff.append('0');
        buff.append(str);
        return buff.toString().toUpperCase();
    }

    /**
   * To raw string.
   * 
   * @return the string
   */
    public String toRawString() {
        if (data == null) return null;
        StringBuilder rc = new StringBuilder(3 * data.length);
        for (int i = 0; i < data.length; i++) {
            int val = data[i];
            int masked = val & 0x00FF;
            if (i > 0) {
                char sep = ' ';
                int flag = val & 0xFF00;
                if (flag == NO_MATCH) sep = ','; else if (flag == ADD_OFFSET) sep = '_';
                rc.append(sep);
            }
            String str = Integer.toHexString(masked);
            if (masked < 16) rc.append('0');
            rc.append(str);
        }
        return rc.toString().toUpperCase();
    }

    /**
   * To string.
   * 
   * @param data
   *          the data
   * @return the string
   */
    public static String toString(short[] data) {
        return toString(data, -1);
    }

    /**
   * To string.
   * 
   * @param data
   *          the data
   * @param breakAt
   *          the break at
   * @return the string
   */
    public static String toString(short[] data, int breakAt) {
        return toString(data, breakAt, 0, data.length);
    }

    /**
   * To string.
   * 
   * @param data
   *          the data
   * @param breakAt
   *          the break at
   * @param offset
   *          the offset
   * @param length
   *          the length
   * @return the string
   */
    public static String toString(short[] data, int breakAt, int offset, int length) {
        if (data == null) return "null";
        StringBuilder rc = new StringBuilder(4 * data.length);
        int breakCount = breakAt;
        int last = offset + length;
        for (int i = offset; i < last; ++i) {
            if (breakCount == 0) {
                rc.append('\n');
                breakCount = breakAt;
            } else if (i > offset) rc.append(' ');
            --breakCount;
            String str = Integer.toHexString(data[i] & 0xFF);
            if (str.length() < 2) rc.append('0');
            rc.append(str);
        }
        return rc.toString().toUpperCase();
    }

    /**
   * To string.
   * 
   * @param data
   *          the data
   * @return the string
   */
    public static String toString(int[] data) {
        if (data == null) return null;
        StringBuilder rc = new StringBuilder(4 * data.length);
        for (int i = 0; i < data.length; ++i) {
            if (i > 0) rc.append(' ');
            String str = Integer.toHexString(data[i]);
            if (str.length() < 2) rc.append('0');
            rc.append(str);
        }
        return rc.toString().toUpperCase();
    }

    public static String getRemoteSignature(short[] data) {
        int len = data.length;
        if (len < 2) {
            return null;
        }
        int sigStart = ((data[0] + data[1]) == 0xFF) ? 2 : 0;
        if (len < sigStart + 8) {
            return null;
        }
        char[] sig = new char[8];
        short val;
        int i = 0;
        for (; i < 8 && ((val = data[sigStart + i]) >= 0x20 && val < 0x80); ++i) {
            sig[i] = (char) val;
        }
        for (; i < 8; ++i) {
            sig[i] = '_';
        }
        return new String(sig);
    }

    public String toString() {
        return toString(data);
    }

    /**
   * To string.
   * 
   * @param breakAt
   *          the break at
   * @return the string
   */
    public String toString(int breakAt) {
        return toString(data, breakAt);
    }

    /**
   * To string.
   * 
   * @param offset
   *          the offset
   * @param length
   *          the length
   * @return the string
   */
    public String toString(int offset, int length) {
        return toString(data, -1, offset, length);
    }

    /**
   * To string.
   * 
   * @param data
   *          the data
   * @param offset
   *          the offset
   * @param length
   *          the length
   * @return the string
   */
    public static String toString(short[] data, int offset, int length) {
        return toString(data, -1, offset, length);
    }

    public void print(PrintWriter pw) throws IOException {
        print(pw, 0);
    }

    public void print(PrintWriter pw, int base) throws IOException {
        print(pw, data, base);
    }

    public static void print(PrintWriter pw, short[] data) throws IOException {
        print(pw, data, 0);
    }

    public static void print(PrintWriter pw, short[] data, int base) throws IOException {
        for (int i = 0; i < data.length; i += 16) {
            pw.printf("%04X:", i + base);
            for (int j = 0; j < 16 && (i + j) < data.length; ++j) pw.printf("  %02X", data[i + j] & 0xFF);
            pw.println();
        }
    }

    public boolean equals(Object obj) {
        Hex aHex = (Hex) obj;
        if (this == aHex) return true;
        if (data.length != aHex.data.length) return false;
        for (int i = 0; i < data.length; i++) {
            if ((data[i] & 0xFF) != (aHex.data[i] & 0xFF)) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(short[] a, short[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; ++i) {
            if ((a[i] & 0xFF) != (b[i] & 0xFF)) {
                System.err.println(String.format("%04X: %X != %X", i, a[i], b[i]));
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int rc = 0;
        if (data.length == 0) return 0;
        int multiplier = (int) Math.pow(31, data.length - 1);
        for (int i = 0; i < data.length; i++) {
            rc += (data[i] & 0xFF) * multiplier;
            multiplier /= 31;
        }
        return rc;
    }

    public int compareTo(Hex o) {
        int rc;
        int compareLen;
        short[] otherData = o.data;
        if (data.length < otherData.length) {
            compareLen = data.length;
            rc = -1;
        } else if (data.length == otherData.length) {
            compareLen = data.length;
            rc = 0;
        } else {
            compareLen = otherData.length;
            rc = 1;
        }
        for (int i = 0; i < compareLen; i++) {
            int v1 = data[i] & 0xFF;
            int v2 = otherData[i] & 0xFF;
            if (v1 < v2) {
                rc = -1;
                break;
            } else if (v1 > v2) {
                rc = 1;
                break;
            }
        }
        return rc;
    }

    /**
   * Index of.
   * 
   * @param needle
   *          the needle
   * @return the int
   */
    public int indexOf(Hex needle) {
        return indexOf(needle.data);
    }

    /**
   * Index of.
   * 
   * @param needle
   *          the needle
   * @param start
   *          the start
   * @return the int
   */
    public int indexOf(Hex needle, int start) {
        return indexOf(needle.data, start);
    }

    /**
   * Index of.
   * 
   * @param needle
   *          the needle
   * @return the int
   */
    public int indexOf(short[] needle) {
        return indexOf(needle, 0);
    }

    /**
   * Index of.
   * 
   * @param needle
   *          the needle
   * @param start
   *          the start
   * @return the int
   */
    public int indexOf(short[] needle, int start) {
        int index = start;
        int last = data.length - needle.length;
        while (index <= last) {
            boolean match = true;
            for (int i = 0; i < needle.length; i++) {
                if (needle[i] != data[index + i]) {
                    match = false;
                    break;
                }
            }
            if (match) return index;
            index++;
        }
        return -1;
    }

    /**
   * Sub hex.
   * 
   * @param index
   *          the index
   * @return the hex
   */
    public Hex subHex(int index) {
        return subHex(index, data.length - index);
    }

    /**
   * Sub hex.
   * 
   * @param index
   *          the index
   * @param len
   *          the len
   * @return the hex
   */
    public Hex subHex(int index, int len) {
        return subHex(data, index, len);
    }

    /**
   * Sub hex.
   * 
   * @param src
   *          the src
   * @param index
   *          the index
   * @param len
   *          the len
   * @return the hex
   */
    public static Hex subHex(short[] src, int index, int len) {
        short[] dest = new short[len];
        System.arraycopy(src, index, dest, 0, len);
        return new Hex(dest);
    }

    /**
   * Apply mask.
   * 
   * @param mask
   *          the mask
   * @return the hex
   */
    public Hex applyMask(Hex mask) {
        if (data.length != mask.data.length) throw new IllegalArgumentException("Mask length doesn't equal data length");
        short[] result = new short[data.length];
        for (int i = 0; i < data.length; ++i) result[i] = (short) (data[i] & mask.data[i]);
        return new Hex(result);
    }

    protected Object clone() throws CloneNotSupportedException {
        Hex rc = (Hex) super.clone();
        rc.data = (short[]) data.clone();
        return rc;
    }

    /** The data. */
    private short[] data = null;

    /** The N o_ match. */
    public static short NO_MATCH = 0x100;

    /** The AD d_ offset. */
    public static short ADD_OFFSET = 0x200;
}
