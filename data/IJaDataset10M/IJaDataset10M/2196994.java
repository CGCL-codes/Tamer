package au.edu.diasb.chico.mvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import au.edu.diasb.chico.config.ImpossibleException;

/**
 * Utility methods for manipulating URLs.
 * 
 * @author scrawley
 */
public class UrlUtils {

    private UrlUtils() {
    }

    /**
	 * Rewrite the URL provided to add an extra query parameter.  We assume
	 * that the URL is well-formed, and do the job by simple String bashing.
	 * The parameter name and value should be supplied as regular strings and
	 * they will be URL encoded (%-encoded) as required.
	 * 
	 * @param url the URL to be rewritten
	 * @param name the parameter name
	 * @param value the parameter value
	 * @return the URL with the extra parameter.
	 */
    public static String addParameter(String url, String name, String value) {
        int queryPos = url.indexOf('?');
        int fragPos = url.indexOf('#');
        try {
            String eName = URLEncoder.encode(name, "UTF-8");
            String eValue = URLEncoder.encode(value, "UTF-8");
            if (fragPos == -1) {
                if (queryPos == -1) {
                    return url + '?' + eName + '=' + eValue;
                } else {
                    return url + '&' + eName + '=' + eValue;
                }
            } else {
                if (queryPos == -1) {
                    return url.substring(0, fragPos) + '?' + eName + '=' + eValue + url.substring(fragPos);
                } else {
                    return url.substring(0, fragPos) + '&' + eName + '=' + eValue + url.substring(fragPos);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            throw new ImpossibleException(ex);
        }
    }
}
