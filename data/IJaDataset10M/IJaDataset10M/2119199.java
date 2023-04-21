package freemind.main;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import freemind.common.NamedObject;
import freemind.common.TextTranslator;
import freemind.main.FreeMindMain.VersionInformation;

/**
 * @author Dimitri Polivaev
 * 12.07.2005
 */
public class Resources implements TextTranslator {

    private FreeMindMain main;

    static Resources resourcesInstance = null;

    private HashMap countryMap;

    private Logger logger = null;

    private Resources(FreeMindMain frame) {
        this.main = frame;
        if (logger == null) logger = main.getLogger(this.getClass().getName());
    }

    public static void createInstance(FreeMindMain frame) {
        if (resourcesInstance == null) resourcesInstance = new Resources(frame);
    }

    public URL getResource(String resource) {
        return main.getResource(resource);
    }

    public String getResourceString(String resource) {
        return main.getResourceString(resource);
    }

    public String getResourceString(String key, String resource) {
        return main.getResourceString(key, resource);
    }

    public static Resources getInstance() {
        return resourcesInstance;
    }

    public String getFreemindDirectory() {
        return main.getFreemindDirectory();
    }

    public VersionInformation getFreemindVersion() {
        return main.getFreemindVersion();
    }

    public int getIntProperty(String key, int defaultValue) {
        return main.getIntProperty(key, defaultValue);
    }

    /**
     * @param key Property key
     * @return the boolean value of the property resp. the default.
     */
    public boolean getBoolProperty(String key) {
        String boolProperty = getProperty(key);
        return Tools.safeEquals("true", boolProperty);
    }

    public Properties getProperties() {
        return main.getProperties();
    }

    public String getProperty(String key) {
        return main.getProperty(key);
    }

    public ResourceBundle getResources() {
        return main.getResources();
    }

    public HashMap getCountryMap() {
        if (countryMap == null) {
            String[] countryMapArray = new String[] { "de", "DE", "en", "UK", "en", "US", "es", "ES", "es", "MX", "fi", "FI", "fr", "FR", "hu", "HU", "it", "CH", "it", "IT", "nl", "NL", "no", "NO", "pt", "PT", "ru", "RU", "sl", "SI", "uk", "UA", "zh", "CN" };
            countryMap = new HashMap();
            for (int i = 0; i < countryMapArray.length; i = i + 2) {
                countryMap.put(countryMapArray[i], countryMapArray[i + 1]);
            }
        }
        return countryMap;
    }

    public java.util.logging.Logger getLogger(String forClass) {
        return main.getLogger(forClass);
    }

    public void logException(Throwable e) {
        logException(e, "");
    }

    public void logException(Throwable e, String comment) {
        logger.log(Level.SEVERE, "An exception occured: " + comment, e);
    }

    public String format(String resourceKey, Object[] messageArguments) {
        MessageFormat formatter = new MessageFormat(getResourceString(resourceKey));
        String stringResult = formatter.format(messageArguments);
        return stringResult;
    }

    public NamedObject createTranslatedString(String key) {
        String fs = getResourceString(key);
        return new NamedObject(key, fs);
    }

    public String getText(String pKey) {
        return getResourceString(pKey);
    }
}
