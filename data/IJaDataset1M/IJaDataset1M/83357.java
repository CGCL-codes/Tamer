package com.gwtaf.core.shared.util;

public final class HexDump {

    private final int bytesPerRow;

    private final boolean charCol;

    public HexDump(int bytesPerRow, boolean charCol) {
        if (bytesPerRow < 1) throw new IllegalArgumentException("Invalid parameter bytesPerRow=" + bytesPerRow);
        this.bytesPerRow = bytesPerRow;
        this.charCol = charCol;
    }

    public HexDump() {
        this(8, true);
    }

    public String dump(byte[] data) {
        return dump(data, 0, data.length);
    }

    public static String dumpHex(byte[] data) {
        return new HexDump().dump(data);
    }

    public static byte[] convert(char[] data) {
        byte[] bytes = new byte[data.length];
        for (int i = 0; i < data.length; ++i) bytes[i] = (byte) data[i];
        return bytes;
    }

    public String dump(byte[] data, int off, int len) {
        StringBuilder buff = new StringBuilder();
        dump(buff, data, off, len);
        return buff.toString();
    }

    public void dump(StringBuilder buff, byte[] data, int off, int len) {
        StringBuilder content = new StringBuilder();
        int pos = off;
        while (pos < len) {
            buff.append("  ");
            int b = 0;
            while (b < bytesPerRow && pos < len) {
                byte val = data[pos];
                if (charCol) content.append((char) (val >= 32 && val <= 127 ? val : '.'));
                buff.append(formatNibble(val >> 4));
                buff.append(formatNibble(val));
                if ((b & 7) == 7) buff.append("  "); else buff.append(' ');
                ++b;
                ++pos;
            }
            if (charCol) {
                while (b < bytesPerRow) {
                    buff.append("   ");
                    if ((b++ & 7) == 7) buff.append(' ');
                }
                buff.append("  ");
                buff.append(content);
                content = new StringBuilder();
            }
            buff.append('\n');
        }
        buff.append('\n');
    }

    public char formatNibble(int i) {
        i = i & 0xf;
        if (i < 10) return (char) ('0' + i); else return (char) ('A' + i - 10);
    }
}
