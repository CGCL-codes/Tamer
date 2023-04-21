package org.jdesktop.swingx.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author joshy
 */
public abstract class StringUtils {

    public static String[] regexSearch(String source, String pattern) {
        Pattern pat = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher matcher = pat.matcher(source);
        matcher.find();
        String[] list = new String[matcher.groupCount() + 1];
        for (int i = 0; i <= matcher.groupCount(); i++) {
            list[i] = matcher.group(i);
        }
        return list;
    }
}
