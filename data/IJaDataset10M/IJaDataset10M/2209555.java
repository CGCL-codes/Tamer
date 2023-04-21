package com.google.code.jahath.common;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HexDump {

    public static void hexDump(StringBuilder buffer, byte[] data, int offset, int length) {
        for (int start = 0; start < length; start += 16) {
            for (int i = 0; i < 16; i++) {
                int index = start + i;
                if (index < length) {
                    String hex = Integer.toHexString(data[offset + start + i] & 0xFF);
                    if (hex.length() < 2) {
                        buffer.append('0');
                    }
                    buffer.append(hex);
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
                if (i == 7) {
                    buffer.append(' ');
                }
            }
            buffer.append(" |");
            for (int i = 0; i < 16; i++) {
                int index = start + i;
                if (index < length) {
                    int b = data[offset + index] & 0xFF;
                    if (32 <= b && b < 128) {
                        buffer.append((char) b);
                    } else {
                        buffer.append('.');
                    }
                } else {
                    buffer.append(' ');
                }
            }
            buffer.append('|');
            buffer.append('\n');
        }
    }

    public static void log(Logger log, Level level, String label, byte[] data, int offset, int length) {
        StringBuilder dump = new StringBuilder(label);
        dump.append('\n');
        HexDump.hexDump(dump, data, offset, length);
        log.log(level, dump.toString());
    }
}
