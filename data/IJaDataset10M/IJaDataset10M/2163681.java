package org.yaffa.commons.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.yaffa.commons.CommonsConstants;
import org.yaffa.commons.utils.StringUtil;

/**
 * 
 * TODO: Make it thread safe!
 * 
 * @author zgobolos.at.sf.net
 *
 */
public final class Messages {

    /** Holds the properties. */
    private static final Map<String, ResourceBundle> BUNDLES = new HashMap<String, ResourceBundle>();

    /** Holds the properties identifiers. */
    private static final List<String> PACKAGE_NAMES = new ArrayList<String>();

    /** The delimiter between the <code>propertiesName</code> and the key */
    private static final String PROPERTIES_FILE_NAME = "messages";

    /**
	 * 
	 * @param key
	 * @param propertiesIdentifier
	 * @return
	 */
    public static String getString(String packageName, String key) {
        return Messages.getString(packageName, key, null);
    }

    /**
	 * 
	 * @param packageName
	 * @param key
	 * @param substitute
	 * @return
	 */
    public static String getString(String packageName, String key, String[] substitute) {
        if (null == packageName || null == key) return null;
        if (!Messages.PACKAGE_NAMES.contains(packageName)) loadProperties(packageName);
        ResourceBundle bundle = Messages.BUNDLES.get(packageName);
        String message = null;
        if (null != bundle) message = bundle.getString(key);
        if (null != message && null != substitute && 0 < substitute.length) message = Messages.substitutePlaceholders(message, substitute);
        return message;
    }

    /**
	 * 
	 * @param message
	 * @param substitute
	 * @return
	 */
    private static String substitutePlaceholders(String message, String[] substitute) {
        Map<String, String> substitutionMap = new HashMap<String, String>();
        for (int i = 0; i < substitute.length; i++) {
            substitutionMap.put(String.valueOf(i + 1), substitute[i]);
        }
        return StringUtil.substitute(message, substitutionMap);
    }

    /**
	 * 
	 * @param propertiesIdentifier
	 */
    private static void loadProperties(String packageName) {
        ResourceBundle bundle = ResourceBundle.getBundle(packageName + CommonsConstants.PACKAGE_NAME_DELIMITER + Messages.PROPERTIES_FILE_NAME);
        Messages.BUNDLES.put(packageName, bundle);
    }

    public static String makeKey(String classFQName, String key) {
        return classFQName + CommonsConstants.PACKAGE_NAME_DELIMITER + key;
    }
}
