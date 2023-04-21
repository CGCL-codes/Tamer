package taygalove_shepherd.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {

    private StringUtil() {
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmptyTrimmed(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String toDisplayString(String s) {
        return toDisplayString(s, "null");
    }

    /**
     * @param nameForNull for example, "null", or "absent", or "may be absent", etc.
     */
    public static String toDisplayString(String s, String nameForNull) {
        if (s == null) return nameForNull; else return "\"" + s + "\"";
    }

    public static String makeEmpty(String s) {
        if (s == null) return "";
        return s;
    }

    public static String makeNull(String s) {
        if (s != null && s.length() == 0) return null;
        return s;
    }

    /**
     * Converts strings. Examples:
     * <p>
     * "LaunchDate" --> "Launch Date";<br>
     * "BasicURLBuilderImpl" --> "Basic URL Builder Impl"<br>
     * "HTTP" --> "HTTP"<br>
     * "URLConnection" --> "URL Connection"<br>
     * "YyyyYYY" --> "Yyyy YYY"<br>
     * <p>
     * Digits are treated exactly like uppercase letters.
     * <p>
     * Underscores are treated exactly like lowercase letters, and replaced by spaces upon return.
     * <p>
     * The behavior is undefined for strings starting with lowercase letter
     * and/or containing spaces and/or delimiters.
     */
    public static String convertCamelStringToSpaceDelimited(String camelString) {
        char[] ca = camelString.toCharArray();
        StringBuffer sb = new StringBuffer(ca.length * 2);
        boolean uppercase = true;
        int uppercaseCount = 0;
        int start = 0;
        int i = 0;
        for (; i < ca.length; i++) {
            char c = ca[i];
            if (uppercase && (Character.isUpperCase(c) || Character.isDigit(c))) {
                uppercaseCount++;
                continue;
            }
            if (!uppercase && (Character.isLowerCase(c) || c == '_')) {
                continue;
            }
            if (uppercase) {
                if (uppercaseCount == 1) {
                    int len = i - 1 - start;
                    if (len > 0) {
                        sb.append(ca, start, len);
                        start = i - 1;
                    }
                } else {
                    int len = i - 1 - start;
                    if (len > 0) {
                        sb.append(ca, start, len);
                        start = i - 1;
                    }
                    sb.append(' ');
                }
                uppercase = false;
                continue;
            }
            sb.append(ca, start, i - start).append(' ');
            start = i;
            uppercase = true;
            uppercaseCount = 1;
        }
        sb.append(ca, start, i - start);
        return sb.toString().replace('_', ' ');
    }

    /** test */
    public static void main(String[] args) {
        testCamelConverter0("LaunchDate", "Launch Date");
        testCamelConverter0("BasicURLBuilderImpl", "Basic URL Builder Impl");
        testCamelConverter0("HTTP", "HTTP");
        testCamelConverter0("URLConnection", "URL Connection");
        testCamelConverter0("XxxxXXX", "Xxxx XXX");
        testCamelConverter0("ErrorListener2", "Error Listener 2");
        testCamelConverter0("HTTP11Connector", "HTTP11 Connector");
        testCamelConverter0("IOException", "IO Exception");
        testCamelConverter0("launchSomethingBAD", "");
        testCamelConverter0("launchSomethingBAD()", "");
        testCamelConverter0(" Launch Something BAD ()", "");
        testCamelConverter0("Launch Something BAD", "");
    }

    private static void testCamelConverter0(String from, String to) {
        String toActual = convertCamelStringToSpaceDelimited(from);
        System.out.println(toDisplayString(from) + " --> " + toDisplayString(toActual) + " -- " + (to.equals(toActual) ? "right." : "WRONG !"));
    }

    public static String join(Object[] objects, String separator) {
        int length = objects.length;
        if (length == 0) return "";
        StringBuffer buf = new StringBuffer().append(objects[0]);
        for (int i = 1; i < length; i++) {
            buf.append(separator).append(objects[i]);
        }
        return buf.toString();
    }

    public static String join(Iterator objects, String separator) {
        StringBuffer buf = new StringBuffer();
        if (objects.hasNext()) buf.append(objects.next());
        while (objects.hasNext()) {
            buf.append(separator).append(objects.next());
        }
        return buf.toString();
    }

    public static List<String> split(String s, String stringTokenizerDelimiters) {
        StringTokenizer st = new StringTokenizer(s, stringTokenizerDelimiters, false);
        List<String> ll = new LinkedList<String>();
        while (st.hasMoreElements()) {
            ll.add(st.nextToken());
        }
        return ll;
    }
}
