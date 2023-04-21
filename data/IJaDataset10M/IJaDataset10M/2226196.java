package net.allblog.testbed;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class SearchUtil {

    public SearchUtil() {
    }

    public static int countMatches(String s, String k) {
        int count = 0;
        int idx = 0;
        while ((idx = s.indexOf(k, idx)) >= 0) {
            count += 1;
            idx += 1;
        }
        return count;
    }

    public static byte[] getBytes(String str, String type) throws UnsupportedEncodingException {
        if (type != "" && type != null) {
            return str.getBytes(type);
        } else {
            return str.getBytes();
        }
    }

    public static String getString(byte[] bytes, String type) throws UnsupportedEncodingException {
        if (type != "" && type != null) return new String(bytes, type); else return new String(bytes);
    }

    public static String getSource(String allblogLink) {
        return allblogLink.replaceFirst("http://link.allblog.net/\\d*/", "");
    }

    public static String getContents(InputStream is) throws IOException {
        byte[] buffer = new byte[4096];
        OutputStream outputStream = new ByteArrayOutputStream();
        while (true) {
            int read = is.read(buffer);
            if (read == -1) {
                break;
            }
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
        String s = outputStream.toString();
        return s;
    }
}
