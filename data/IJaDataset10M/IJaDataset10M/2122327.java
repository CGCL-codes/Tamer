package ru.bluesteel.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class URLDecoder {

    public static String decode(String s) {
        if (s == null) return null;
        boolean needToChange = false;
        StringBuffer sb = new StringBuffer();
        int numChars = s.length();
        int i = 0;
        byte[] bytes = null;
        ByteArrayInputStream buf;
        DataInputStream reader;
        String read = "";
        while (i < numChars) {
            char c = s.charAt(i);
            switch(c) {
                case '+':
                    sb.append(' ');
                    i++;
                    needToChange = true;
                    break;
                case '%':
                    try {
                        bytes = new byte[(numChars - i) / 3 + 2];
                        int pos = 2;
                        while (((i + 2) < numChars) && (c == '%')) {
                            bytes[pos++] = (byte) Integer.parseInt(s.substring(i + 1, i + 3), 16);
                            i += 3;
                            if (i < numChars) c = s.charAt(i);
                        }
                        if ((i < numChars) && (c == '%')) throw new IllegalArgumentException();
                        bytes[0] = (byte) (((pos - 2) >> 8) & 0xFF);
                        bytes[1] = (byte) ((pos - 2) & 0xFF);
                        buf = new ByteArrayInputStream(bytes, 0, pos);
                        reader = new DataInputStream(buf);
                        try {
                            read = reader.readUTF();
                        } catch (IOException ioe) {
                        }
                        sb.append(read);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                    needToChange = true;
                    break;
                default:
                    sb.append(c);
                    i++;
                    break;
            }
        }
        return (needToChange ? sb.toString() : s);
    }
}
