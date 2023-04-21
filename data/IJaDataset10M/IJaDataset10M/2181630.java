package ch.articlefox.utils.servlet;

import java.net.URLDecoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * These servlet tools help to retrieve parameters inside a HTTP request or to
 * dump such a request.
 * 
 * @author Lukas Blunschi
 */
public class ServletTools {

    private static Log log = LogFactory.getLog(ServletTools.class);

    /**
	 * Decode the given URI using UTF-8 charset.
	 * 
	 * @param encoded encoded URI.
	 * @return decoded URI, or null if failure occured.
	 */
    public static String decodeURI(String encoded) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(encoded, "utf-8");
        } catch (Exception e) {
            log.warn("Failure while decoding URI (" + encoded + "): " + e.getMessage());
            decoded = null;
        }
        return decoded;
    }

    /**
	 * Remove path information from a given filename.
	 * 
	 * @param filename filename to remove path information from.
	 * @return filename without path information.
	 */
    public static String removePathInformation(String filename) {
        filename = filename.replace('\\', '/');
        int pos = filename.lastIndexOf("/");
        String name = null;
        if (pos == -1) {
            name = filename;
        } else {
            name = filename.substring(pos + 1);
        }
        return name;
    }
}
