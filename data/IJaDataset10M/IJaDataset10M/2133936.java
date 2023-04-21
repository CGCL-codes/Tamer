package at.ac.ait.enviro.dssos.util;

import java.net.URL;

/**
 * Contains methods for URL manipulation
 * @author Arndt Bonitz
 */
public class URLHandler {

    /**
     * Adds a Query to an URL
     * @param url
     *      A valid URL, e.g. <code>http://services.example.com/sos</code>
     * @param query
     *      An HTTP GET query, e.g. <code>REQUEST=GetCapabilities</code>
     * @return
     *      The original URL with the appended queries,
     *      e.g. <code>http://services.example.com/sos?REQUEST=GetCapabilities</code>
     *
     */
    public static final String addQuery(URL url, String... query) {
        String delim;
        if (url.getQuery() == null) {
            delim = "?";
        } else if (url.getQuery().length() == 0) {
            delim = "";
        } else {
            delim = "&";
        }
        StringBuilder newURL = new StringBuilder(url.toString());
        for (String q : query) {
            newURL.append(delim).append(q);
            delim = "&";
        }
        return newURL.toString();
    }
}
