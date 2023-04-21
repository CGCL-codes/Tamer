package uk.ac.essex.common.lang;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.io.File;
import java.util.*;

/**
 * Class to provide translation facilities, using locales, resource bundles and
 * properties files. This class is a variation on a singleton class, all references to it
 * should be obtained by using the getInstance() method, which returns (the same)
 * static instance. However a singleton will be created for each resource base name.
 * This allows you to have multiple subsystems each with their own resource base name
 * and corresponding file. Yet at the same time still treat this class as a singleton.
 * This is simply impleemted as a HashMap mapping resource base name to Translator
 * instances. If the hashmap doesn't contain the resource base name then a translator is
 * instantiated otherwise you get the old one.- <br><br>
 *
 * Locales and resource bundles basically just provide a hash
 * map that maps keys to values. For each available locale their is a different
 * hashmap. These different maps have the same keys but differnt sets of values,
 * there is one for each language available. <br> <br>
 *
 * By default this will search for the resources on the classpath and will use the
 * default resource base name of <b>"Resources"</b>. The only default locale it will
 * search for is {@link Locale#ENGLISH}
 *
 *
 * @see PropertyResourceBundle
 * @see Locale
 * @author Laurence Smith
 *
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class MultiTranslator {

    /** The singleton */
    private static MultiTranslator translator;

    /** A hashmap mapping Locale --> MutablePropertyResourceBundle */
    private HashMap localesToBundles;

    /** A list of all the available Locales / languages */
    private List locales;

    /** The log4j logger */
    private static transient Log logger = LogFactory.getLog(MultiTranslator.class);

    /** */
    private ClassLoader classLoaderToUse = this.getClass().getClassLoader();

    /** The default locale to use this is {@link Locale#ENGLISH} */
    public static Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private MutablePropertyResourceBundle mutablePropertyResourceBundle;

    private Set resourceBaseNameSet = new HashSet();

    /** */
    private File directoryToLoadFrom;

    /**
     * Defaults to just have the english locale
     */
    private MultiTranslator() {
    }

    /**
     * Given a locale and a STRING key this returns the string for that key using the
     * locale obtained by calling {@link #DEFAULT_LOCALE}.
     * @param key
     * @return
     */
    public String get(String key) {
        return get(DEFAULT_LOCALE, key);
    }

    /**
     * Given a locale and a STRING key this returns the string for that key using that
     * locale. The locale given should be available ie/ have it's own properties
     * file. All available translations are searched (those in the database & from
     * the properties file)
     * @param locale - The locale to use to 'translate' the given key
     * @param key - The key of the required translation
     */
    public String get(Locale locale, String key) {
        ResourceBundle r = (ResourceBundle) localesToBundles.get(locale);
        if (r != null) return r.getString(key); else return null;
    }

    /**
     *
     */
    public boolean supportsLanguage(Locale locale) {
        for (Iterator iterator = locales.iterator(); iterator.hasNext(); ) {
            if (locale.equals(iterator.next())) return true;
        }
        return false;
    }

    /**
     * Adds a an available locale and then for each resource base name tries to load the relevent
     * properties file.
     * @param locale
     */
    public void addLocale(Locale locale) {
        locales.add(locale);
        for (Iterator iterator = resourceBaseNameSet.iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            initResource(name, locale);
        }
    }

    /**
     * Adds a an available resource base name and then for each locale tries to load the relevent
     * properties file.
     * @param resourceBaseName
     */
    public void addResourceBundle(String resourceBaseName) {
        resourceBaseNameSet.add(resourceBaseName);
        for (Iterator iterator = locales.iterator(); iterator.hasNext(); ) {
            Locale locale = (Locale) iterator.next();
            initResource(resourceBaseName, locale);
        }
    }

    /**
     * If resources are loaded from the classpath, then this determines which
     * class loader to use to achieve this. By default this class will just use
     * this classes class loader.
     *
     * @param classLoaderToUse
     */
    public void setClassLoaderToUse(ClassLoader classLoaderToUse) {
        this.classLoaderToUse = classLoaderToUse;
    }

    /**
     *
     * @param directory
     * @throws Exception - If the file is not a directory or is null
     */
    public void setDirectoryToLoadFrom(File directory) throws Exception {
        if (directory == null || !directory.isDirectory()) throw new Exception();
        directoryToLoadFrom = directory;
    }

    /**
     * Used for testing
     */
    public static void main(String[] args) {
        try {
            Locale loc = DEFAULT_LOCALE;
            MultiTranslator t = MultiTranslator.getInstance();
            logger.debug("Value = " + t.get(loc, "GREETING"));
        } catch (Exception eX) {
        }
    }

    /**
     * This uses the default locales
     * @return Translator - A new (static) instance of Translator
     */
    public static synchronized MultiTranslator getInstance() {
        if (translator == null) translator = new MultiTranslator();
        return translator;
    }

    /**
     *
     * @param defaultLocale
     */
    public static void setDefaultLocale(Locale defaultLocale) {
        DEFAULT_LOCALE = defaultLocale == null ? Locale.ENGLISH : defaultLocale;
    }

    /**
     * This is used to initialise a resource base.
     * @param resourceBaseName
     * @param locale
     */
    private void initResource(String resourceBaseName, Locale locale) {
        if (directoryToLoadFrom == null) loadResourcesFromClassPath(resourceBaseName, locale); else loadResourcesFromDirectory(resourceBaseName, locale);
    }

    /**
     * Load the resource strings from the properties files located on this classes classpath
     * @param resourceBaseName
     * @param locale
     */
    private void loadResourcesFromClassPath(String resourceBaseName, Locale locale) {
    }

    /**
     * Load the resource strings from the properties files located in {@link #directoryToLoadFrom}
     * @param resourceBaseName
     * @param locale
     */
    private void loadResourcesFromDirectory(String resourceBaseName, Locale locale) {
    }

    /**
     * Gets the ending for the file name
     * @param locale
     * @return
     */
    private String getEnding(Locale locale) {
        String ending;
        String country = locale.getCountry();
        String endingPrefix = "_" + locale.getLanguage();
        String endingPostFix = null;
        if (country != null && !country.equals("")) endingPostFix = "_" + country + ".properties"; else endingPostFix = ".properties";
        ending = endingPrefix + endingPostFix;
        return ending;
    }
}
