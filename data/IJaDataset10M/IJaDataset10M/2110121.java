package lpg.util;

import java.io.Closeable;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.EnumSet;

public class Utility {

    public static Charset getCharset() {
        String charsetName = System.getProperty("file.encoding");
        if (charsetName != null) {
            try {
                return Charset.forName(charsetName);
            } catch (IllegalCharsetNameException e) {
                System.out.println("***Error: Illegal charset name \"" + charsetName + "\".");
            } catch (UnsupportedCharsetException e) {
                System.out.println("***Error: Unsupported charset \"" + charsetName + "\".");
            }
        }
        return Charset.defaultCharset();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable t) {
            }
        }
    }

    public static int checkQualifiedIdentifier(String str) {
        int numDots = 0;
        boolean isStart = true;
        for (int i = 0, n = str.length(); i < n; i++) {
            char c = str.charAt(i);
            if (isStart) {
                if (!Character.isJavaIdentifierStart(c)) {
                    return -1;
                }
                isStart = false;
            } else {
                if (c == '.') {
                    numDots++;
                    isStart = true;
                } else if (!Character.isJavaIdentifierPart(c)) {
                    return -1;
                }
            }
        }
        return isStart ? -1 : numDots;
    }

    public static String getPackageName(String str) {
        int index = str.lastIndexOf('.');
        if (index != -1) {
            return str.substring(0, index);
        }
        return null;
    }

    public static String escape(String str, char delimiter) {
        if (str == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        buf.append(delimiter);
        for (int i = 0, n = str.length(); i < n; i++) {
            char c = str.charAt(i);
            switch(c) {
                case '\b':
                    buf.append("\\b");
                    break;
                case '\t':
                    buf.append("\\t");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '\f':
                    buf.append("\\f");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                case '"':
                    buf.append("\\\"");
                    break;
                case '\'':
                    buf.append("\\'");
                    break;
                default:
                    buf.append(c);
            }
        }
        buf.append(delimiter);
        return new String(buf);
    }

    public static String toLowerCase(String str) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0, n = str.length(); i < n; i++) {
            buf.append(toLowerCase(str.charAt(i)));
        }
        return new String(buf);
    }

    public static char toLowerCase(char c) {
        if (c == '_') {
            return '-';
        }
        return Character.toLowerCase(c);
    }

    public static int prefixMatch(String str1, String str2) {
        int n = Math.min(str1.length(), str2.length());
        for (int i = 0; i < n; i++) {
            if (str1.charAt(i) != str2.charAt(i)) return i;
        }
        return n;
    }

    static final String LINE_SEPARATOR;

    static {
        String separator = System.getProperty("line.separator");
        LINE_SEPARATOR = (separator == null) ? "\n" : separator;
    }

    private Utility() {
    }
}
