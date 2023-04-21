package com.volantis.mcs.bundles;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;

public class BundleUtilities {

    private static String mark = "(c) Volantis Systems Ltd 2000. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(BundleUtilities.class);

    /**
     * Locate a bundle using the basename specified, using a default locale, 
     * looking up bundle classes via the Thread's context class loader.
     * 
     * @param baseName
     * @return the bundle
     */
    public static EnhancedBundle getBundle(String baseName) {
        return getBundle(baseName, Locale.getDefault());
    }

    /**
     * Locate a bundle using the basename specified, using specified locale, 
     * looking up bundle classes via the Thread's context class loader.
     * 
     * @param baseName
     * @return the bundle
     */
    public static EnhancedBundle getBundle(String baseName, Locale locale) {
        Locale defaultLocale = Locale.getDefault();
        EnhancedBundle bundle = null;
        EnhancedBundle root;
        root = lookFor(baseName, null);
        if (root == null) {
            throw new MissingResourceException("Can't find bundle for base name " + baseName + ", locale " + locale, baseName, null);
        }
        bundle = lookFor(baseName, root, locale);
        if (bundle == root && !locale.equals(defaultLocale)) {
            bundle = lookFor(baseName, root, defaultLocale);
        }
        return bundle;
    }

    private static EnhancedBundle lookFor(String baseName, EnhancedBundle parent, Locale locale) {
        String language = locale.getLanguage();
        boolean languageSet = (language.length() != 0);
        String country = locale.getCountry();
        boolean countrySet = (country.length() != 0);
        String variant = locale.getVariant();
        boolean variantSet = (variant.length() != 0);
        String name = baseName;
        EnhancedBundle bundle;
        if (!languageSet && !countrySet && !variantSet) {
            return parent;
        }
        name = name + "_" + language;
        bundle = lookFor(name, parent);
        if (bundle == null) {
            return parent;
        }
        parent = bundle;
        if (!countrySet && !variantSet) {
            return parent;
        }
        name = name + "_" + country;
        bundle = lookFor(name, parent);
        if (bundle == null) {
            return parent;
        }
        parent = bundle;
        if (!variantSet) {
            return parent;
        }
        name = name + "_" + variant;
        bundle = lookFor(name, parent);
        if (bundle == null) {
            return parent;
        }
        return bundle;
    }

    private static EnhancedBundle lookFor(String bundleName, EnhancedBundle parent) {
        String resourceName = bundleName.replace('.', '/') + ".properties";
        InputStream inputStream;
        AbstractEnhancedBundle bundle = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        inputStream = cl.getResourceAsStream(resourceName);
        if (inputStream != null) {
            try {
                bundle = new EnhancedPropertyBundle(inputStream, resourceName);
                bundle.setParent(parent);
                inputStream.close();
                if (logger.isDebugEnabled()) {
                    logger.debug("Found " + resourceName);
                }
            } catch (IOException e) {
            }
        }
        return bundle;
    }

    public static Image getImage(EnhancedBundle bundle, String key) {
        return (Image) bundle.getObject(key, Image.class, null);
    }

    public static ImageIcon getImageIcon(EnhancedBundle bundle, String key) {
        return (ImageIcon) bundle.getObject(key, ImageIcon.class, null);
    }
}
