package reqtrack.util;

import javax.naming.*;
import javax.servlet.*;
import org.apache.commons.logging.*;

/**
 * This class contains static methods that support internationalization and localization.
 * 
 * @author michael
 * @version $Revision: 1.3 $
 * @since reqtrack 1.0
 */
public class Localize {

    /** Logger */
    private static Log log = LogFactory.getLog("reqtrack.util.Localize");

    /** The cached docDir, lazily initialized by getDocDir */
    private static String docDir;

    /**
     * Identifies a two-letter locale string which has a corresponding directory
     * containing documentation.  The method extracts the language from the request
     * object, and looks for a corresponding directory under "/docs".  If it exists,
     * this String is returned, otherwise "en" which exists.
     * 
     * @param request the request object from which we extract the Locale information.
     * @return a language String that is guaranteed to have a corresponding directory under /docs.
     */
    public static String getDocDir(ServletRequest request) {
        if (docDir != null) return docDir;
        String language = request.getLocale().getLanguage();
        try {
            String jndiPrefix = "java:comp/Resources/docs/";
            Context initCtx = new InitialContext();
            initCtx.lookup(jndiPrefix + language);
            docDir = language;
        } catch (NamingException e) {
            log.info("User requested language " + language + ", but not available.", e);
        }
        if (docDir == null) docDir = "en";
        return docDir;
    }
}
