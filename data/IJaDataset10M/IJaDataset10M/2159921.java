package nuts.core.lang;

/**
 * utility class for string escape
 */
public class StringEscapeUtils extends org.apache.commons.lang.StringEscapeUtils {

    /**
	 * escapeSqlLike
	 * @param str string
	 * @return escaped string
	 */
    public static String escapeSqlLike(String str) {
        final char esc = '~';
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == esc) {
                result.append(esc);
                result.append(esc);
                continue;
            }
            if (c == '%' || c == '_') {
                result.append(esc);
                result.append(c);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
