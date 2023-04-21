package net.sourceforge.freejava.annotation.util;

public class AnnotationParseUtil {

    protected static final String[] EMPTY_STRING_ARRAY = {};

    protected static String join(String[] array, String separator) {
        StringBuffer buf = new StringBuffer(array.length * 20);
        for (int i = 0; i < array.length; i++) {
            String s = array[i];
            if (s != null && s.length() != 0) {
                if (buf.length() != 0) buf.append(separator);
                buf.append(s);
            }
        }
        return buf.toString();
    }
}
