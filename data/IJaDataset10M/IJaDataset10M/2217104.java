package org.nuclearbunny.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * The <code>URLMatcher</code> class provides utilities for finding URLs
 * contained within String buffers and for determining if a string is
 * a valid URL.
 *
 * @author David C. Gibbons
 * @since 0.84
 */
public class URLMatcher {

    /**
     * This <code>Pattern</code> object can be used to match for URL strings
     * within a larger string buffer.
     */
    public static final Pattern URL_PATTERN = Pattern.compile("(?s)((?:\\w+://|\\bwww\\.[^.])\\S+)");

    /**
     * Attempts to convert the specified string into a valid URL object.
     *
     * @param candidate
     * @return a valid URL, or <code>null</code> if the candidate was not valid
     */
    public static URL getURL(String candidate) {
        URL url = null;
        try {
            url = new URL(candidate);
        } catch (MalformedURLException ex) {
            url = null;
        }
        try {
            if (url == null) {
                url = new URL("http://" + candidate);
            }
        } catch (MalformedURLException ex) {
            url = null;
        }
        return url;
    }
}
