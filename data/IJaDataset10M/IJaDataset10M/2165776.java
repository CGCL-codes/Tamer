package net.sf.traser.common;

import net.sf.traser.configuration.ConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import net.sf.traser.configuration.AbstractConfigurable;
import net.sf.traser.utils.ResourceLocator;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * Helper class for internationalizing labels and error messages.
 * When used in configmanager, point the Interface class element
 * to the inner Builder class.
 * @author karnokd, 2008.01.08.
 * @version $Revision 1.0$
 */
public final class LabelManager {

    /**
     * The read only map of labels.
     */
    private final Map<String, String> labels = new HashMap<String, String>();

    /**
     * The locale.
     */
    private final Locale locale;

    /**
     * The base name.
     */
    private final String name;

    /**
     * Debug mode flag. Throws exception if the label
     * does not exists.
     */
    private boolean debugMode;

    /**
     * Private constructor. Initializes the final fields.
     * @param name the base file name
     * @param locale the locale
     */
    private LabelManager(String name, Locale locale) {
        this.name = name;
        this.locale = locale;
    }

    /** Java XML properties file entry element. */
    private static final QName PROPERTIES_ENTRY = new QName("entry");

    /** Java XML properties file key attribute. */
    private static final QName PROPERTIES_KEY = new QName("key");

    /**
     * Load the properties from a Java XML properties file.
     */
    private void load() {
        try {
            InputStream in = findLabelFile();
            if (in != null) {
                StAXOMBuilder builder = new StAXOMBuilder(in);
                try {
                    OMElement root = builder.getDocumentElement();
                    Iterator<?> it = root.getChildrenWithName(PROPERTIES_ENTRY);
                    while (it.hasNext()) {
                        OMElement e = (OMElement) it.next();
                        labels.put(e.getAttributeValue(PROPERTIES_KEY), e.getText());
                    }
                } finally {
                    if (builder != null) {
                        builder.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (XMLStreamException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * Find a label file based on the name and locale.
     * @return the input stream if null
     */
    private InputStream findLabelFile() {
        String variant = locale.getVariant();
        String country = locale.getCountry();
        String language = locale.getLanguage();
        String[] names = { name + "_" + language + "_" + country + "_" + variant + ".xml", name + "_" + language + "_" + country + ".xml", name + "_" + language + ".xml", name + "_en_US.xml", name + "_en.xml", name + ".xml" };
        for (String s : names) {
            InputStream in = ResourceLocator.findAsStream(s);
            if (in != null) {
                return in;
            }
        }
        return null;
    }

    /**
     * Get a label labelManager for a name and locale.
     * @param name the label file base name
     * @param locale the locale
     * @return the label labelManager object
     */
    public static LabelManager getLabelManager(String name, Locale locale) {
        LabelManager result = new LabelManager(name, locale);
        result.load();
        return result;
    }

    /**
     * @return returns the Label labelManager for the default locale and language file.
     */
    public static LabelManager getLabelManager() {
        return getLabelManager("conf/TraserLabels", Locale.getDefault());
    }

    /**
     * Get a label labelManager for the name and the default locale.
     * @param name the label file base name
     * @return the label labelManager object. Never null.
     */
    public static LabelManager getLabelManager(String name) {
        return getLabelManager(name, Locale.getDefault());
    }

    /**
     * Get the value of a key.
     * @param key the key
     * @return the value or null
     */
    public String get(String key) {
        String result = labels.get(key);
        if (debugMode && result == null) {
            throw new IllegalArgumentException("Undefined label: " + key);
        }
        return result;
    }

    /**
     * Get the value of a key or return the def if not present.
     * @param key the key
     * @param def the default value
     * @return the value or the default value
     */
    public String get(String key, String def) {
        String result = labels.get(key);
        return result != null ? result : def;
    }

    /**
     * Return a formatted string based on the value of the key.
     * @param key the key
     * @param params the parameters for the formatting
     * @return the formatted string or null
     */
    public String format(String key, Object... params) {
        String value = get(key);
        if (value != null) {
            return String.format(value, params);
        }
        return null;
    }

    /**
     * Interface for a factory that provides a label labelManager.
     * @author karnokd, 2008.01.08.
     * @version $Revision 1.0$
     */
    public interface LabelManagerProvider {

        /**
         * @return returns a non-null label labelManager.
         */
        LabelManager getManager();
    }

    /**
     * A ConfigManager aware builder class.
     * The following attributes are checked:<br>
     * <li>name (required): the name of the label file without extension</li>
     * <li>language (optional): the language code (2 chars lovercase)</li>
     * <li>country (optional): the country code (2 chars uppercase)</li>
     * <li>variant (optional): optional variant</li>
     * If none of the optional attributes are present, de default locale will be used
     * @author karnokd, 2008.01.08.
     * @version $Revision 1.0$
     */
    public static final class Builder extends AbstractConfigurable implements LabelManagerProvider {

        /**
         * The label labelManager.
         */
        private LabelManager labelManager;

        /** The optional variant attribute. */
        private static final QName VARIANT = new QName("variant");

        /** The optional country attribute. */
        private static final QName COUNTRY = new QName("country");

        /** The required language attribute. */
        private static final QName LANGUAGE = new QName("language");

        /** The required label file name without extension attribute. */
        private static final QName NAME = new QName("name");

        /** The optional debug mode flag. */
        private static final QName DEBUGMODE = new QName("debugmode");

        /**
         * {@inheritDoc}
         */
        @Override
        public void configure() {
            String variant = configuration.getAttributeValue(VARIANT);
            String country = configuration.getAttributeValue(COUNTRY);
            String language = configuration.getAttributeValue(LANGUAGE);
            String name = configuration.getAttributeValue(NAME);
            boolean debugMode = "true".equals(configuration.getAttributeValue(DEBUGMODE));
            if (name == null) {
                throw new ConfigurationException("No name attribute defined");
            } else if (variant != null && country == null) {
                throw new ConfigurationException("No country defined  but variant defined.");
            } else if (country != null && language == null) {
                throw new ConfigurationException("No language defined but country defined.");
            }
            Locale locale = null;
            if (variant != null) {
                locale = new Locale(language, country, variant);
            } else if (country != null) {
                locale = new Locale(language, country);
            } else if (language != null) {
                locale = new Locale(language);
            } else {
                locale = Locale.getDefault();
            }
            labelManager = LabelManager.getLabelManager(name, locale);
            labelManager.debugMode = debugMode;
        }

        /**
         * @return the Label labelManager
         */
        @Override
        public LabelManager getManager() {
            return labelManager;
        }
    }

    /**
     * @return the debugMode
     */
    public boolean isDebugMode() {
        return debugMode;
    }
}
