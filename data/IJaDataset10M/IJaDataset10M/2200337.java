package net.azib.ipscan.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Labels class for localization, based on PropertyResourceBundle.
 * It adds some special methods for loading of images by IDs.
 * 
 * It is a singleton, so use getInstance() in order to use this class.
 * 
 * Use initialize() to create an instance of this class.
 * 
 * @author Anton Keks
 */
public final class Labels {

    private static final Logger LOG = Logger.getLogger(Labels.class.getName());

    private static Labels instance;

    PropertyResourceBundle labels, labelsFallback;

    Locale locale;

    static {
        initialize(Locale.getDefault());
    }

    private Labels() {
    }

    public static final Labels getInstance() {
        return instance;
    }

    /**
	 * Initializes the internal locale-specific data.
	 * The files messages_lang.properties and messages.properties are searched for from the classpath.
	 * This method must be called prior to using this class.
	 */
    public static void initialize(Locale locale) {
        if (instance != null && locale.equals(instance.locale)) {
            return;
        }
        instance = new Labels();
        instance.locale = locale;
        InputStream labelsStream = null;
        try {
            labelsStream = Labels.class.getClassLoader().getResourceAsStream("messages.properties");
            if (labelsStream == null) {
                throw new MissingResourceException("Labels not found!", Labels.class.getName(), "messages");
            }
            instance.labelsFallback = new PropertyResourceBundle(labelsStream);
            labelsStream.close();
        } catch (IOException e) {
            throw new MissingResourceException(e.toString(), Labels.class.getName(), "messages");
        }
        try {
            labelsStream = Labels.class.getClassLoader().getResourceAsStream("messages_" + locale.getLanguage() + ".properties");
            instance.labels = new PropertyResourceBundle(labelsStream);
            labelsStream.close();
        } catch (Exception e) {
            instance.labels = instance.labelsFallback;
        }
    }

    /**
	 * Retrieves an InputStream to load the image, specified by a key in resource file.
	 * @param key
	 */
    public InputStream getImageAsStream(String key) {
        String imagePath = get(key);
        return getClass().getClassLoader().getResourceAsStream(imagePath);
    }

    /**
	 * Retrieves a String specified by the label key
	 * @param key
	 */
    public String get(String key) {
        try {
            return labels.getString(key);
        } catch (MissingResourceException e) {
            String text = labelsFallback.getString(key);
            LOG.warning("Used fallback label for " + key);
            return text;
        }
    }

    /**
	 * A shortened form of Labels.getLabel().get()
	 */
    public static String getLabel(String key) {
        return getInstance().get(key);
    }
}
