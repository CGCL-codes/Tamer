package net.sf.metaprint2d.mol2;

public class ParseTool {

    /**
     * Quicker than <tt>s.substring(st, st+len).trim()</tt> and works for
     * <tt>st+len >= s.length()</tt>.
     *
     * @param s		String
     * @param st	Start pos
     * @param len	Length
     * @return
     */
    public static String trimmedSubstring(String s, int st, int len) {
        int l = s.length();
        if (l < st) {
            return "";
        }
        int end = Math.min(st + len, l) - 1;
        while (st < end && s.charAt(st) <= ' ') {
            st++;
        }
        while (end > st && s.charAt(end) <= ' ') {
            end--;
        }
        return s.substring(st, end + 1);
    }

    public static String parseString(String s, int start, int len) {
        s = trimmedSubstring(s, start, len);
        return s;
    }

    public static String[] splitLine(String line, int... splits) {
        String[] sections = new String[splits.length];
        int length = line.length();
        for (int i = 0, indx = 0; i < splits.length; i++) {
            if (length < indx) {
                for (; i < splits.length; i++) {
                    sections[i] = "";
                }
            } else {
                int end = Math.min(indx + splits[i], length);
                sections[i] = line.substring(indx, end);
                indx += splits[i];
            }
        }
        return sections;
    }

    public static int parseInt(String text) {
        int mag;
        int left;
        if ('-' == text.charAt(0)) {
            mag = -1;
            left = 1;
        } else {
            mag = 1;
            left = 0;
        }
        int val = 0;
        for (int i = text.length() - 1; i >= left; i--) {
            char c = text.charAt(i);
            int cval = (((int) c) - '0');
            if (cval < 0 || cval > 9) {
                throw new IllegalArgumentException("Illegal character: " + c);
            }
            val += mag * cval;
            mag *= 10;
        }
        return val;
    }

    public static int parseInt(String s, int start, int len) {
        s = trimmedSubstring(s, start, len);
        return s.length() == 0 ? 0 : parseInt(s);
    }

    public static Integer integerValue(String text) {
        return text.length() == 0 ? null : Integer.valueOf(parseInt(text));
    }

    public static Integer integerValue(String s, int start, int len) {
        s = trimmedSubstring(s, start, len);
        return integerValue(s);
    }

    public static short parseShort(String text) {
        short mag;
        int left;
        if ('-' == text.charAt(0)) {
            mag = -1;
            left = 1;
        } else {
            mag = 1;
            left = 0;
        }
        short val = 0;
        for (int i = text.length() - 1; i >= left; i--) {
            char c = text.charAt(i);
            int cval = (((int) c) - '0');
            if (cval < 0 || cval > 9) {
                throw new IllegalArgumentException("Illegal character: " + c);
            }
            val += mag * cval;
            mag *= 10;
        }
        return val;
    }

    public static Short shortValue(String text) {
        return text.length() == 0 ? null : Short.valueOf(parseShort(text));
    }

    public static double parseDouble(String text) {
        double sign;
        int left;
        int right;
        if ('-' == text.charAt(0)) {
            sign = -1d;
            left = 1;
        } else {
            sign = 1d;
            left = 0;
        }
        int dp = text.indexOf('.');
        double val = 0;
        if (dp == -1) {
            right = text.length() - 1;
        } else {
            double mag = sign * 0.1;
            for (int i = dp + 1, n = text.length(); i < n; i++) {
                char c = text.charAt(i);
                int cval = (((int) c) - '0');
                if (cval < 0 || cval > 9) {
                    throw new IllegalArgumentException("Illegal character: " + c);
                }
                val += mag * cval;
                mag /= 10;
            }
            right = dp - 1;
        }
        double mag = sign;
        for (int i = right; i >= left; i--) {
            char c = text.charAt(i);
            int cval = (((int) c) - '0');
            if (cval < 0 || cval > 9) {
                throw new IllegalArgumentException("Illegal character: " + c);
            }
            val += mag * cval;
            mag *= 10;
        }
        return val;
    }

    public static double parseDouble(String s, int start, int len) {
        s = trimmedSubstring(s, start, len);
        return s.length() == 0 ? 0 : parseDouble(s);
    }

    public static Double doubleValue(String text) {
        return text.length() == 0 ? null : Double.valueOf(parseDouble(text));
    }

    public static Double doubleValue(String s, int start, int len) {
        s = trimmedSubstring(s, start, len);
        return doubleValue(s);
    }

    public static String toString(Integer i) {
        StringBuilder text = new StringBuilder();
        String prefix;
        if (i < 0) {
            prefix = "-";
            i = -i;
        } else {
            prefix = "";
        }
        while (i > 0) {
            int x = i;
            i /= 10;
            text.append((char) ('0' + x - (i * 10)));
        }
        return text.length() == 0 ? "0" : text.append(prefix).reverse().toString();
    }
}
