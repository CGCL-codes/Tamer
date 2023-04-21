package lebah.util;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class F {

    public static final String MAILSERVER = "202.190.118.120";

    public static String str(String s, int len) {
        String result = s;
        if (s.length() > len) {
            result = s.substring(0, len);
        }
        return result;
    }
}
