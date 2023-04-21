package org.javizy.local;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.javizy.utils.JavizyToolBox;
import org.javizy.xml.XmlToMap;
import org.xml.sax.SAXException;

public class Localization extends Hashtable<String, String> {

    public static final String EXIT = "exit";

    public static final String WAS_MINIMIZED = "wasminimized";

    public static final String TXT_WAS_MINIMIZED = "wasminimized.txt";

    public static final String MINIMIZE = "minimize";

    public static final String UNMINIMIZE = "unminimize";

    public static final String APPNAME = "appname";

    private static Hashtable<Locale, Localization> s_locales = new Hashtable<Locale, Localization>();

    private static Localization s_localization = null;

    private Locale locale = Locale.getDefault();

    /**
     * 
     * @param locale
     */
    private Localization(Locale locale) {
        System.err.println("Try to load '" + locale + "'");
        this.locale = locale;
    }

    /**
     * 
     * @param french
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws NumberFormatException
     */
    public static void load(Locale locale) throws NumberFormatException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        loadXmlFile(locale.getClass().getResourceAsStream(JavizyToolBox.getPath(Localization.class, locale.toString() + ".xml")));
    }

    /**
     * Load localization
     * 
     * @param domain
     * @param xmlFile
     * @throws NumberFormatException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void loadXmlFile(InputStream xmlFile) throws NumberFormatException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        Object ret = XmlToMap.decode(xmlFile);
        String lang = (String) XmlToMap.getParameters(ret, "props.lang");
        String country = (String) XmlToMap.getParameters(ret, "props.country");
        Object strings = XmlToMap.getParameters(ret, "strings");
        if (strings instanceof Map) {
            Locale locale = new Locale(lang, country);
            Localization localization = s_locales.get(locale);
            if (localization == null) {
                localization = new Localization(locale);
                s_locales.put(locale, localization);
            }
            @SuppressWarnings("unchecked") Map<String, String> stringMap = (Map<String, String>) strings;
            localization.putAll(stringMap);
            if (s_localization == null) {
                changeLocalization(localization.locale);
            }
        }
    }

    /**
     * 
     * @param locale
     */
    public static void changeLocalization(Locale locale) {
        Localization localization = (Localization) s_locales.get(locale);
        if (localization != null) {
            s_localization = localization;
        }
        Locale.setDefault(locale);
    }

    /**
     *
     */
    public static void changeLocalization() {
        Localization.changeLocalization(Locale.getDefault());
    }

    /**
     * 
     * @param key
     * @return
     */
    public static String getString(String key) {
        if (s_localization != null) {
            String ret = (String) s_localization.get(key);
            if (ret == null) {
                return "?" + key + "?";
            }
            return ret;
        }
        throw new java.util.MissingResourceException("No localization defined", Localization.class.getName(), key);
    }

    /**
     * 
     * @param txtWasMinimized
     * @param context
     * @return
     */
    public static String getString(String key, Map<String, String> context) {
        String ret = getString(key);
        for (Map.Entry<String, String> entry : context.entrySet()) {
            ret = ret.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue());
        }
        return ret;
    }

    /**
     * 
     * @return
     */
    public static Locale getLocale() {
        return s_localization.locale;
    }
}
