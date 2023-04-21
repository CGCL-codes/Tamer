package se.perfectfools.localizer.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class handles i18n for ezloc itself.
 * 
 * @author P&aring;l Brattberg &lt; <a href="mailto:pal@eminds.se">pal@eminds.se </a>&gt;
 * @version $Id: Messages.java,v 1.2 2004/11/13 13:34:48 pal Exp $
 */
public class Messages {

    private static final String BUNDLE_NAME = "messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    protected Messages() {
    }

    /**
     * Get a translated string for the current locale.
     * 
     * @param key The key for the String to translate
     * @return The translated string, or <code>!<em>key</em>!</code> if no
     * translation was found.
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
