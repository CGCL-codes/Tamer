package sears.tools;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import sears.gui.resources.SearsResources;

/**
 * Class SearsResourceBundle.
 * <br><b>Summary:</b><br>
 * This class permits to access to the string resources in the choosen locale.
 */
public class SearsResourceBundle {

    /** Languages supported by Sears, array used by the options dialog */
    public static final String[][] LOCALES = { { "en_US", "English" }, { "fr_FR", "Français" }, { "it_IT", "Italiano" }, { "es_LA", "Spanish" }, { "nl_NL", "Nederlands" } };

    /**The locale choosen*/
    private Locale locale;

    /**The resource bundle that correspond to the locale.*/
    private ResourceBundle resourceBundle;

    /**The instance of SearsResourceBundle.*/
    private static SearsResourceBundle instance;

    /**The name of the resource bundles.*/
    private static final String RESOURCE_NAME = "MessageBundle";

    /**
     * Constructor SearsResourceBundle.
     * <br><b>Summary:</b><br>
     * Constructor of the class.
     */
    public SearsResourceBundle() {
        this(new Locale("en", "US"));
    }

    /**
     * Constructor SearsResourceBundle.
     * <br><b>Summary:</b><br>
     * Constructor of the class.
     */
    public SearsResourceBundle(Locale locale) {
        instance = this;
        setLocale(locale);
    }

    /**
     * Method setLocale.
     * <br><b>Summary:</b><br>
     * Change the locale to the specified one.
     * @param locale        The new locale to set.
     */
    public static void setLocale(Locale locale) {
        instance.locale = locale;
        instance.resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
    }

    /**
     * Method getResource.
     * <br><b>Summary:</b><br>
     * This method permits to return a resources associated to the given key in the resource bundle.
     * It returns "", if key is not found in resource bundle.
     * @param key               The key of the value to found.
     * @return  <b>String</b>   The resources associated to the given key in the resource bundle.
     */
    public static String getResource(String key) {
        return getResource(key, "");
    }

    /**
     * Method getResource.
     * <br><b>Summary:</b><br>
     * This method permits to return a resources associated to the given key in the resource bundle.
     * It returns the default value, if key is not found in resource bundle.
     * @param key               The key of the value to found.
     * @param defaultValue      The default value to return, if key is not found.
     * @return  <b>String</b>   The resources associated to the given key in the resource bundle.
     */
    public static String getResource(String key, String defaultValue) {
        String result = defaultValue;
        result = instance.resourceBundle.getString(key);
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Method getLocale.
     * <br><b>Summary:</b><br>
     * Return the Locale.
     * @return  <b>Locale</b>       The Locale.
     */
    public static Locale getLocale() {
        return instance.locale;
    }

    /**
     * Converts a 'locale' string ("en_US") to a proper string to display
     * ("English") using <tt>LOCALES</tt> class constant 
     * @param 	locale 	the string to convert
     * @return			the converted string or null if there's no entries in <tt>LOCALES</tt> class constant
     */
    public static String getStringForLocaleString(String locale) {
        String str = null;
        if (locale != null) {
            for (int i = 0; i < LOCALES.length; i++) {
                if (locale.contentEquals(LOCALES[i][0])) {
                    str = LOCALES[i][1];
                }
            }
        }
        return str;
    }

    /**
	 * Gets the icon which correspond to the locale string
	 * @param locale	the locale string ("en_US)
	 * @return			the corresponding icon or a blank icon if there's no resources for this locale
	 */
    public static Icon getIconForLocaleString(String locale) {
        return SearsResources.getIcon(locale.substring(0, locale.indexOf("_")));
    }

    /**
	 * Returns an array of string which contains all the language names
	 * available on Sears.
	 * <br>This method use the <tt>LOCALES</tt> constant to do that
	 * @return	an array of language names
	 */
    public static String[] getAllAvailableLanguage() {
        String[] locales = new String[LOCALES.length];
        for (int i = 0; i < LOCALES.length; i++) {
            locales[i] = LOCALES[i][0];
        }
        return locales;
    }
}
