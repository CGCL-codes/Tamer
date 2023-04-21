package xades4j.utils;

/**
 * Utility methods for strings.
 * @author Luís
 */
public class StringUtils {

    public static boolean isNullOrEmptyString(String s) {
        return null == s || s.isEmpty();
    }

    public static boolean allNullOrEmptyStrings(String... srts) {
        for (int i = 0; i < srts.length; i++) {
            if (!isNullOrEmptyString(srts[i])) return false;
        }
        return true;
    }

    public static boolean differentStringsIfNotNullNorEmpty(String str1, String str2) {
        return str1 != null && str2 != null && !str1.isEmpty() && !str2.isEmpty() && !str1.equals(str2);
    }
}
