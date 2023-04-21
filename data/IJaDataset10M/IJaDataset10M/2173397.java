package net.sourceforge.mnxj;

/**
 * @author mfx (mfx at linux dot se)
 * @version 0.8.5
 * @since 0.8.4
 */
public class Strings {

    /**
     * Checks if a given <tt>String</tt> is <tt>null</tt> or empty
     *
     * @param string
     * @return <tt>true</tt> if the <tt>String</tt> is <tt>null</tt> or empty
     */
    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.equals(""));
    }

    /**
     * Returns the subString pre the first delimeter.
     *
     * @param string any </tt>String</tt>
     * @param delimeter the delimeter <tt>String</tt>
     * @return the substring pre the first occurrence of the delimeter in string
     */
    public static String getPreFirstDelimeter(String string, String delimeter) {
        int d = string.indexOf(delimeter);
        if (d == -1) return null;
        return string.substring(0, d);
    }

    /**
     * Returns the subString post the first delimeter.
     *
     * @param string any </tt>String</tt>
     * @param delimeter the delimeter <tt>String</tt>
     * @return the substring post the first occurrence of the delimeter in string
     */
    public static String getPostDelimeter(String string, String delimeter) {
        if (isNullOrEmpty(delimeter)) return string;
        int delimeterIndex = string.indexOf(delimeter) + delimeter.length();
        if (delimeterIndex == 0 || (delimeterIndex) >= string.length()) return null;
        return string.substring(delimeterIndex);
    }

    /**
     * Returns the subString pre the last delimeter.
     *
     * @param string any </tt>String</tt>
     * @param delimeter the delimeter <tt>String</tt>
     * @return the substring pre the last occurrence of the delimeter in string
     */
    public static String getPreLastDelimeter(String string, String delimeter) {
        int delimeterIndex = string.lastIndexOf(delimeter);
        if (delimeterIndex == 0 || (delimeterIndex) >= string.length()) return null;
        if (delimeterIndex == -1) return string;
        return string.substring(0, delimeterIndex);
    }
}
