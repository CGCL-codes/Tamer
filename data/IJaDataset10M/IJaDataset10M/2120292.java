package rubbish.db.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���K�\�����[�e�B���e�B
 *
 * @author $Author: winebarrel $
 * @version $Revision: 1.3 $
 */
public class RegexUtils {

    protected static Map patterns = new HashMap();

    protected static Pattern getPattern(String regex) {
        return getPattern(regex, 0);
    }

    protected static Pattern getPattern(String regex, int i_flags) {
        Map patterns_par_regex = (Map) patterns.get(regex);
        if (patterns_par_regex == null) {
            patterns_par_regex = new HashMap();
            patterns.put(regex, patterns_par_regex);
        }
        Integer flags = new Integer(i_flags);
        Pattern pattern = (Pattern) patterns_par_regex.get(flags);
        if (pattern == null) {
            pattern = Pattern.compile(regex, i_flags);
            patterns_par_regex.put(flags, pattern);
        }
        return pattern;
    }

    public static Matcher matcher(String regex, CharSequence input) {
        Pattern pattern = getPattern(regex);
        return pattern.matcher(input);
    }

    public static Matcher matcher(String regex, int flags, CharSequence input) {
        Pattern pattern = getPattern(regex, flags);
        return pattern.matcher(input);
    }

    public static boolean matches(String regex, CharSequence input) {
        Pattern pattern = getPattern(regex);
        return pattern.matcher(input).matches();
    }

    public static boolean matches(String regex, int flags, CharSequence input) {
        Pattern pattern = getPattern(regex, flags);
        return pattern.matcher(input).matches();
    }

    public static String[] split(String regex, CharSequence input) {
        Pattern pattern = getPattern(regex);
        return pattern.split(input);
    }

    public static String[] split(String regex, int flags, CharSequence input) {
        Pattern pattern = getPattern(regex, flags);
        return pattern.split(input);
    }

    public static String[] split(String regex, CharSequence input, int limit) {
        Pattern pattern = getPattern(regex);
        return pattern.split(input, limit);
    }

    public static String[] split(String regex, int flags, CharSequence input, int limit) {
        Pattern pattern = getPattern(regex, flags);
        return pattern.split(input, limit);
    }

    public static boolean lookingAt(String regex, CharSequence input) {
        Pattern pattern = getPattern(regex);
        return pattern.matcher(input).lookingAt();
    }

    public static boolean lookingAt(String regex, int flags, CharSequence input) {
        Pattern pattern = getPattern(regex, flags);
        return pattern.matcher(input).lookingAt();
    }

    public static String replaceAll(String regex, CharSequence input, String replacement) {
        Pattern pattern = getPattern(regex);
        return pattern.matcher(input).replaceAll(replacement);
    }

    public static String replaceAll(String regex, int flags, CharSequence input, String replacement) {
        Pattern pattern = getPattern(regex, flags);
        return pattern.matcher(input).replaceAll(replacement);
    }

    public static String replaceFirst(String regex, CharSequence input, String replacement) {
        Pattern pattern = getPattern(regex);
        return pattern.matcher(input).replaceAll(replacement);
    }

    public static String replaceFirst(String regex, int flags, CharSequence input, String replacement) {
        Pattern pattern = getPattern(regex, flags);
        return pattern.matcher(input).replaceAll(replacement);
    }

    public static String escapeReplacementString(String src) {
        if (src == null || (src.indexOf('$') < 0 && src.indexOf('\\') < 0)) {
            return src;
        }
        char[] cs = src.toCharArray();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == '$' || cs[i] == '\\') {
                buf.append('\\');
            }
            buf.append(cs[i]);
        }
        return buf.toString();
    }
}
