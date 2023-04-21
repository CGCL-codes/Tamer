package org.spirit.util.text;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Generic text manipulation utilities.
 * 
 * @author bbrown
 */
public class TextUtils {

    /**
	 * Using the java API URL class, extract the http/https
	 * hostname.
	 * 
	 * e.g: http://www.google.com/search will return http://www.google.com
	 * 
	 * @return
	 */
    public static String getHTTPHostname(final String urlStr) {
        try {
            URL url = new URL(urlStr);
            String curHostname = url.getHost();
            String scheme = url.getProtocol();
            String fullNewURL = "";
            if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                fullNewURL = scheme + "://" + curHostname;
                return fullNewURL;
            } else {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException e) {
            return "invalid-hostname";
        }
    }
}
