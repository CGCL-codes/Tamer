package org.toobsframework.pres.url;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlMappingUtil {

    private static final String[] NO_STRINGS = new String[0];

    private static Pattern regexHttp;

    static {
        regexHttp = Pattern.compile("(?:(?:https?)://|www\\.)[^/]*");
    }

    /**
   * Break a pattern into its components, separated by '/'
   * @param pathPattern is the pattern
   * @return a string of parts
   */
    public static String[] tokenizePath(String pathPattern) {
        String[] tokens;
        if (pathPattern != null) {
            Matcher matcher = regexHttp.matcher(pathPattern);
            pathPattern = matcher.replaceFirst("");
            StringTokenizer tok = new StringTokenizer(pathPattern, "/");
            tokens = new String[tok.countTokens()];
            int i = 0;
            while (tok.hasMoreTokens()) {
                tokens[i++] = tok.nextToken();
            }
        } else {
            tokens = NO_STRINGS;
        }
        return tokens;
    }
}
