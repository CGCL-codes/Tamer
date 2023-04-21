package tab.util;

import java.util.List;

public class StringUtil {

    private static final String BLANK_STR = " ";

    public static final String ljust(String str, int length) {
        if (str == null) {
            return ljust(BLANK_STR, length);
        } else {
            if (length <= str.length()) return str; else return repeat(BLANK_STR, length - str.length()) + str;
        }
    }

    public static final String rjust(String str, int length) {
        if (str == null) {
            return rjust(BLANK_STR, length);
        } else {
            if (length <= str.length()) return str; else return str + repeat(BLANK_STR, length - str.length());
        }
    }

    public static final String repeat(String str, int length) {
        if (str == null) {
            return repeat(BLANK_STR, length);
        } else {
            if (length > 1) {
                StringBuffer buffer = new StringBuffer();
                while (length > 0) {
                    buffer.append(str);
                    length--;
                }
                return buffer.toString();
            } else return str;
        }
    }

    public static final String join(String joinner, String[] strs) {
        if (joinner == null) throw new IllegalArgumentException();
        if (strs == null || strs.length == 0) {
            throw new IllegalArgumentException();
        }
        if (strs.length == 1) {
            return strs[0];
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, n = strs.length; i < n; i++) {
            buffer.append(strs[i]);
            if (i < n - 1) {
                buffer.append(joinner);
            }
        }
        return buffer.toString();
    }

    public static final String join(String joinner, List<String> strs) {
        return join(joinner, strs.toArray(new String[0]));
    }
}
