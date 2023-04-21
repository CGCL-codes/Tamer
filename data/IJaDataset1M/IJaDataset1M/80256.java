package a_vcard.android.text;

import java.util.regex.Pattern;
import java.util.Iterator;

public class TextUtils {

    private TextUtils() {
    }

    public static void getChars(CharSequence s, int start, int end, char[] dest, int destoff) {
        Class c = s.getClass();
        if (c == String.class) ((String) s).getChars(start, end, dest, destoff); else if (c == StringBuffer.class) ((StringBuffer) s).getChars(start, end, dest, destoff); else if (c == StringBuilder.class) ((StringBuilder) s).getChars(start, end, dest, destoff); else if (s instanceof GetChars) ((GetChars) s).getChars(start, end, dest, destoff); else {
            for (int i = start; i < end; i++) dest[destoff++] = s.charAt(i);
        }
    }

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0) return true; else return false;
    }
}
