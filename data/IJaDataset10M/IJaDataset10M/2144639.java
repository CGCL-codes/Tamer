package uk.ac.essex.ia.lang;

import org.apache.log4j.Category;
import uk.ac.essex.common.io.ResourceLoader;
import uk.ac.essex.common.lang.LanguageManager;
import uk.ac.essex.common.lang.MutablePropertyResourceBundle;
import uk.ac.essex.ia.IAConstants;
import uk.ac.essex.ia.lang.xml.AvailableLocales;
import uk.ac.essex.ia.lang.xml.Resource;
import uk.ac.essex.ia.lang.xml.Resources;
import uk.ac.essex.ia.pref.GlobalPreferences;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

/**
 *
 * <br>
 * Date: 15-Jul-2002 <br>
 * 
 * @author Laurence Smith
 * 
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class LanguageManagerImpl implements LanguageManager {

    /** The singleton instance */
    private static LanguageManager ourInstance;

    /** The log4j logger */
    private static transient Category logger = Category.getInstance(LanguageManagerImpl.class);

    private AvailableLocales availableLocales;

    private Locale[] locales;

    /** Maps locale --> MutablePropertyResourceBundle */
    private HashMap localeToMutableBundle = new HashMap();

    private Locale DEFAULT_LOCALE = new Locale("en");

    /** */
    protected Preferences preferences;

    private String defaultLocaleLang;

    /** */
    private LanguageManagerImpl() {
        preferences = Preferences.userNodeForPackage(LanguageManagerImpl.class);
        defaultLocaleLang = preferences.get(GlobalPreferences.LANGUAGE, "en");
        initLocales();
        initResources();
    }

    /**
     *
     * @param s
     * @return
     */
    public String get(String s) {
        final String translated = get(DEFAULT_LOCALE, s);
        if (logger.isDebugEnabled()) {
            logger.debug("Getting string key = " + s + " value = " + translated);
        }
        return translated;
    }

    /**
     *
     * @param s
     * @return
     */
    public String get(Locale locale, String s) {
        ResourceBundle resourceBundle = (ResourceBundle) localeToMutableBundle.get(locale);
        return resourceBundle.getString(s);
    }

    /**
     *
     * @param locale
     */
    public void setDefaultLocale(Locale locale) {
        DEFAULT_LOCALE = locale;
        preferences.put(GlobalPreferences.LANGUAGE, locale.getLanguage());
    }

    /**
     *
     * @return
     */
    public Locale getDefaultLocale() {
        logger.debug("DEfault locale = " + DEFAULT_LOCALE);
        return DEFAULT_LOCALE;
    }

    /**
     *
     * @return
     */
    public Locale[] getAvailableLocales() {
        return locales;
    }

    public boolean isSupported(Locale locale) {
        return false;
    }

    /**
     *
     * @return LanguageManagerImpl - The singleton instance
     */
    public static synchronized LanguageManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new LanguageManagerImpl();
        }
        return ourInstance;
    }

    /**
     *
     * @param argStrings
     */
    public static void main(String[] argStrings) {
        LanguageManager languageManager = LanguageManagerImpl.getInstance();
        logger.debug("File = " + languageManager.get("FILE"));
        logger.debug("Explorer pane = " + languageManager.get("SHOW_EXPLORER_PANE"));
    }

    /**
     *
     */
    private void initLocales() {
        try {
            FileReader reader = getFileReader(IAConstants.AVAILABLE_LOCALES_FILE);
            availableLocales = AvailableLocales.unmarshal(reader);
            uk.ac.essex.ia.lang.xml.Locale[] xmlLocales = availableLocales.getLocale();
            locales = new Locale[xmlLocales.length];
            for (int i = 0; i < xmlLocales.length; i++) {
                uk.ac.essex.ia.lang.xml.Locale locale = xmlLocales[i];
                String language = locale.getLanguage();
                String country = locale.getCountry();
                if (country == null) locales[i] = new Locale(language); else locales[i] = new Locale(language, country);
                if (language.equals(defaultLocaleLang)) {
                    DEFAULT_LOCALE = locales[i];
                    logger.debug("Set default locale to " + locales[i]);
                }
                logger.info("Loaded locale = " + locales[i]);
            }
        } catch (Exception e) {
            logger.warn("Failed to load availble locales file: " + IAConstants.AVAILABLE_LOCALES_FILE + " from classpath");
            logger.warn("Using default locale only");
            if (logger.isDebugEnabled()) logger.error("ERROR: ", e);
            locales = new Locale[1];
            locales[0] = DEFAULT_LOCALE;
        }
    }

    /**
     * Initialise all the resource bundles
     */
    private void initResources() {
        try {
            FileReader fileReader = getFileReader(IAConstants.AVAILABLE_RESOURCES_FILE);
            Resources resourceWrapper = Resources.unmarshal(fileReader);
            Resource[] resources = resourceWrapper.getResource();
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                initResource(resource.getName());
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) logger.error("ERROR: ", e);
            String message = "Failed to load any availeble resources xml file, the application cannot continue";
            logger.fatal(message, e);
            JOptionPane.showMessageDialog(null, message, "Fatal Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @param resourceBaseName
     */
    private void initResource(String resourceBaseName) {
        logger.info("Initialising resource base name = " + resourceBaseName);
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            PropertyResourceBundle propertyResourceBundle = (PropertyResourceBundle) ResourceBundle.getBundle(resourceBaseName, locale);
            MutablePropertyResourceBundle resourceBundle = (MutablePropertyResourceBundle) localeToMutableBundle.get(locale);
            if (resourceBundle == null) resourceBundle = new MutablePropertyResourceBundle();
            resourceBundle.addPropertyResourceBundle(propertyResourceBundle);
            localeToMutableBundle.put(locale, resourceBundle);
        }
    }

    /**
     * Gets a file reader to the fileToReadFrom, this should be a path relative
     * to a directory on the classpath.
     * @param fileToReadFrom
     * @return
     * @throws FileNotFoundException
     */
    private FileReader getFileReader(String fileToReadFrom) throws FileNotFoundException {
        URL fileLocation = ResourceLoader.getResource(fileToReadFrom);
        logger.info("Loading file = " + fileLocation.getPath());
        File f = new File(fileLocation.getPath());
        FileReader reader = new FileReader(f);
        return reader;
    }
}
