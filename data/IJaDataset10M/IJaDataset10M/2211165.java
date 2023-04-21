package frost.util.gui.translation;

import java.io.*;
import java.util.*;

/**
 * Because the Java PropertyResourceBundle does still not support
 * UTF-8 properties files, we use our own bundle.
 */
public class FrostResourceBundle {

    private static final String BUILDIN_BUNDLE_NAME = "/i18n/langres";

    protected static final String EXTERNAL_BUNDLE_DIR = "localdata/i18n/";

    protected Map<String, String> bundle;

    protected FrostResourceBundle parentBundle = null;

    /**
     * Load the root bundle.
     */
    public FrostResourceBundle() {
        final String resource = BUILDIN_BUNDLE_NAME + ".properties";
        bundle = FrostResourceBundleReader.loadBundle(resource);
    }

    public FrostResourceBundle getParentBundle() {
        return parentBundle;
    }

    /**
     * Load build-in bundle for localeName (de,en,...), and use parent bundle as fallback.
     */
    public FrostResourceBundle(final String localeName, final FrostResourceBundle parent, final boolean isExternal) {
        parentBundle = parent;
        if (localeName.length() == 0) {
            bundle = new HashMap<String, String>();
        } else if (isExternal == false) {
            final String resource = BUILDIN_BUNDLE_NAME + "_" + localeName + ".properties";
            bundle = FrostResourceBundleReader.loadBundle(resource);
        } else {
            final String filename = EXTERNAL_BUNDLE_DIR + "langres_" + localeName + ".properties";
            final File file = new File(filename);
            if (file.isFile()) {
                bundle = FrostResourceBundleReader.loadBundle(file);
            } else {
                bundle = new HashMap<String, String>();
            }
        }
    }

    /**
     * Load external bundle file, without fallback. For tests of new properties files.
     */
    public FrostResourceBundle(final File bundleFile) {
        bundle = FrostResourceBundleReader.loadBundle(bundleFile);
    }

    /**
     * Load bundle for File, with fallback. For uses user properties files.
     */
    public FrostResourceBundle(final File bundleFile, final FrostResourceBundle parent) {
        parentBundle = parent;
        bundle = FrostResourceBundleReader.loadBundle(bundleFile);
    }

    public String getString(final String key) throws MissingResourceException {
        String value;
        value = bundle.get(key);
        if (value == null) {
            if (parentBundle != null) {
                value = parentBundle.getString(key);
            } else {
                throw new MissingResourceException("Key is missing: '" + key + "'", "FrostResourceBundle", key);
            }
        }
        return value;
    }

    public Collection<String> getKeys() {
        return bundle.keySet();
    }
}
