package org.pixory.pxfoundation;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * a facade on top of the java.util.prefs system. Compensates for the
 * mind-numbing design flaw in Preferences that ties 'user' to an arbitrary
 * notion defined by the underlying system. Since a user is explicitly specified
 * here, this system can be used for server apps that carry their own notion of
 * user. A user string must comprise 'word' characters, i.e. regex [a-zA-Z_0-9].
 * A keypath must comprise elements of 'word' characters, separated by
 * KEY_PATH_SEPARATOR_STRING
 */
public class PXConfiguration extends Object {

    private static final Log LOG = LogFactory.getLog(PXConfiguration.class);

    public static final String NOTIFICATION_NAME_DID_CHANGE = "did.change";

    /**
	 * these are the keys in the change notifcation userInfo Map
	 */
    public static final String NOTIFICATION_CONFIGURATION_USER = "user";

    public static final String NOTIFICATION_CONFIGURATION_DOMAIN = "domain";

    public static final String NOTIFICATION_CONFIGURATION_NODE_PATH = "nodePath";

    public static final String NOTIFICATION_CONFIGURATION_KEY = "key";

    public static final String NOTIFICATION_CONFIGURATION_VALUE = "value";

    /** use in keyPaths passed to this class */
    public static final String KEY_PATH_SEPARATOR_STRING = ".";

    private static final char KEY_PATH_SEPARATOR_CHAR = KEY_PATH_SEPARATOR_STRING.charAt(0);

    private static final char PREFERENCES_KEY_PATH_SEPARATOR_CHAR = '/';

    private static final String DEFAULT_USER = "default";

    private static final String DEFAULT_DOMAIN = "default";

    private static final Pattern USER_PATTERN = Pattern.compile("\\w+");

    private static final Pattern DOMAIN_PATTERN = Pattern.compile("\\w+");

    private static final Pattern KEY_PATH_PATTERN = Pattern.compile("[\\w,\\.]+");

    private PXConfiguration() {
    }

    public static void validateUser(String user) throws IllegalArgumentException {
        if (user != null) {
            if (!USER_PATTERN.matcher(user).matches()) {
                throw new IllegalArgumentException(user + " is an invalid user string");
            } else if (user.equalsIgnoreCase(DEFAULT_USER)) {
                throw new IllegalArgumentException(user + " is a reserved user string");
            }
        }
    }

    public static String getString(String keyPath, String user, String domain, String dfault) {
        String getString = null;
        if (keyPath != null) {
            String aNodeKeyPath = PXPathUtility.pathByRemovingLastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            String aKey = PXPathUtility.lastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            getString = getPreferencesString(user, domain, aNodeKeyPath, aKey, dfault);
        } else {
            throw new IllegalArgumentException("PXConfiguration.getString(String,String,String) does not accept null " + " keyPath ");
        }
        return getString;
    }

    public static String getString(Object object, String key, String user, String domain, String dfault) {
        String getString = null;
        if ((object != null) && (key != null)) {
            String aNodeKeyPath = PXObjectUtility.getInstanceClassName(object);
            getString = getPreferencesString(user, domain, aNodeKeyPath, key, dfault);
        } else {
            throw new IllegalArgumentException("PXConfiguration.getString(Object, String,String,String)" + " does not accept null object or key ");
        }
        return getString;
    }

    /** you are not allowed to put null values; use remove */
    public static void putString(String keyPath, String value, String user, String domain) {
        if ((keyPath != null) && (value != null)) {
            String aNodeKeyPath = PXPathUtility.pathByRemovingLastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            String aKey = PXPathUtility.lastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            putPreferencesString(user, domain, aNodeKeyPath, aKey, value);
        } else {
            throw new IllegalArgumentException("PXConfiguration.putString(String,String,String) does not accept null " + " keyPath or value ");
        }
    }

    public static void putString(Object object, String key, String value, String user, String domain) {
        if ((object != null) && (key != null) && (value != null)) {
            String aNodeKeyPath = PXObjectUtility.getInstanceClassName(object);
            putPreferencesString(user, domain, aNodeKeyPath, key, value);
        } else {
            throw new IllegalArgumentException("PXConfiguration.putString(Object,String,String,String)" + " does not accept null object, key, or value");
        }
    }

    public static boolean getBoolean(String keyPath, String user, String domain, boolean dfault) {
        boolean getBoolean = dfault;
        if (keyPath != null) {
            String aNodeKeyPath = PXPathUtility.pathByRemovingLastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            String aKey = PXPathUtility.lastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            getBoolean = getPreferencesBoolean(user, domain, aNodeKeyPath, aKey, dfault);
        } else {
            throw new IllegalArgumentException("PXConfiguration.getBoolean(String,String,boolean) does not accept null " + " keyPath ");
        }
        return getBoolean;
    }

    public static boolean getBoolean(Object object, String key, String user, String domain, boolean dfault) {
        boolean getBoolean = dfault;
        if ((object != null) && (key != null)) {
            String aNodeKeyPath = PXObjectUtility.getInstanceClassName(object);
            getBoolean = getPreferencesBoolean(user, domain, aNodeKeyPath, key, dfault);
        } else {
            throw new IllegalArgumentException("PXConfiguration.getBoolean(Object, String,String,boolean)" + " does not accept null object or key ");
        }
        return getBoolean;
    }

    public static void putBoolean(String keyPath, boolean value, String user, String domain) {
        if (keyPath != null) {
            String aNodeKeyPath = PXPathUtility.pathByRemovingLastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            String aKey = PXPathUtility.lastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            putPreferencesBoolean(user, domain, aNodeKeyPath, aKey, value);
        } else {
            throw new IllegalArgumentException("PXConfiguration.putBoolean(String,boolean,String) does not accept null " + " keyPath ");
        }
    }

    public static void putBoolean(Object object, String key, boolean value, String user, String domain) {
        if ((object != null) && (key != null)) {
            String aNodeKeyPath = PXObjectUtility.getInstanceClassName(object);
            putPreferencesBoolean(user, domain, aNodeKeyPath, key, value);
        } else {
            throw new IllegalArgumentException("PXConfiguration.putString(Object,String,boolean,String)" + " does not accept null object or key");
        }
    }

    public static int getInt(String keyPath, String user, String domain, int dfault) {
        int getInt = dfault;
        if (keyPath != null) {
            String aNodeKeyPath = PXPathUtility.pathByRemovingLastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            String aKey = PXPathUtility.lastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            getInt = getPreferencesInt(user, domain, aNodeKeyPath, aKey, dfault);
        } else {
            throw new IllegalArgumentException("PXConfiguration.getInt(String,String,int) does not accept null " + " keyPath ");
        }
        return getInt;
    }

    public static int getInt(Object object, String key, String user, String domain, int dfault) {
        int getInt = dfault;
        if ((object != null) && (key != null)) {
            String aNodeKeyPath = PXObjectUtility.getInstanceClassName(object);
            getInt = getPreferencesInt(user, domain, aNodeKeyPath, key, dfault);
        } else {
            throw new IllegalArgumentException("PXConfiguration.getInt(Object, String,String,int)" + " does not accept null object or key ");
        }
        return getInt;
    }

    public static void putInt(String keyPath, int value, String user, String domain) {
        if (keyPath != null) {
            String aNodeKeyPath = PXPathUtility.pathByRemovingLastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            String aKey = PXPathUtility.lastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            putPreferencesInt(user, domain, aNodeKeyPath, aKey, value);
        } else {
            throw new IllegalArgumentException("PXConfiguration.putInt(String,int,String) does not accept null " + " keyPath ");
        }
    }

    public static void putInt(Object object, String key, int value, String user, String domain) {
        if ((object != null) && (key != null)) {
            String aNodeKeyPath = PXObjectUtility.getInstanceClassName(object);
            putPreferencesInt(user, domain, aNodeKeyPath, key, value);
        } else {
            throw new IllegalArgumentException("PXConfiguration.putInt(Object,String,int,String)" + " does not accept null object or key");
        }
    }

    public static void remove(String keyPath, String user, String domain) {
        if (keyPath != null) {
            String aNodeKeyPath = PXPathUtility.pathByRemovingLastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            String aKey = PXPathUtility.lastPathComponent(keyPath, KEY_PATH_SEPARATOR_STRING);
            removePreferencesValue(user, domain, aNodeKeyPath, aKey);
        } else {
            throw new IllegalArgumentException("PXConfiguration.remove(String,String) does not accept null " + " keyPath ");
        }
    }

    public static void remove(Object object, String key, String user, String domain) {
        if ((key != null) && (object != null)) {
            String aNodeKeyPath = PXObjectUtility.getInstanceClassName(object);
            removePreferencesValue(user, domain, aNodeKeyPath, key);
        } else {
            throw new IllegalArgumentException("PXConfiguration.remove(Object, String,String)" + " does not accept null object or key ");
        }
    }

    private static String getPreferencesString(String user, String domain, String nodeKeyPath, String key, String dfault) {
        String getPreferencesString = null;
        if (key != null) {
            Preferences aNode = getNode(user, domain, nodeKeyPath);
            getPreferencesString = aNode.get(key, dfault);
        }
        return getPreferencesString;
    }

    private static boolean getPreferencesBoolean(String user, String domain, String nodeKeyPath, String key, boolean dfault) {
        boolean getPreferencesBoolean = dfault;
        if (key != null) {
            Preferences aNode = getNode(user, domain, nodeKeyPath);
            getPreferencesBoolean = aNode.getBoolean(key, dfault);
        }
        return getPreferencesBoolean;
    }

    private static int getPreferencesInt(String user, String domain, String nodeKeyPath, String key, int dfault) {
        int getPreferencesInt = dfault;
        if (key != null) {
            Preferences aNode = getNode(user, domain, nodeKeyPath);
            getPreferencesInt = aNode.getInt(key, dfault);
        }
        return getPreferencesInt;
    }

    private static void putPreferencesString(String user, String domain, String nodeKeyPath, String key, String value) {
        if (key != null) {
            Preferences aNode = getNode(user, domain, nodeKeyPath);
            aNode.put(key, value);
            notifyChangeListeners(user, domain, nodeKeyPath, key, value);
        }
    }

    private static void putPreferencesBoolean(String user, String domain, String nodeKeyPath, String key, boolean value) {
        if (key != null) {
            Preferences aNode = getNode(user, domain, nodeKeyPath);
            aNode.putBoolean(key, value);
            notifyChangeListeners(user, domain, nodeKeyPath, key, new Boolean(value));
        }
    }

    private static void putPreferencesInt(String user, String domain, String nodeKeyPath, String key, int value) {
        if (key != null) {
            Preferences aNode = getNode(user, domain, nodeKeyPath);
            aNode.putInt(key, value);
            notifyChangeListeners(user, domain, nodeKeyPath, key, new Integer(value));
        }
    }

    private static void removePreferencesValue(String user, String domain, String nodeKeyPath, String key) {
        if (key != null) {
            Preferences aNode = getNode(user, domain, nodeKeyPath);
            if (aNode.get(key, null) != null) {
                aNode.remove(key);
                notifyChangeListeners(user, domain, nodeKeyPath, key, null);
            }
        }
    }

    private static Preferences getNode(String user, String domain, String nodeKeyPath) {
        Preferences getNode = Preferences.userRoot();
        String aCompleteNodePath = null;
        if (user != null) {
            validateUser(user);
        } else {
            user = DEFAULT_USER;
        }
        aCompleteNodePath = "{" + user + "}";
        if (domain != null) {
            validateDomain(domain);
        } else {
            domain = DEFAULT_DOMAIN;
        }
        aCompleteNodePath += KEY_PATH_SEPARATOR_STRING + "[" + domain + "]";
        if (nodeKeyPath != null) {
            validateKeyPath(nodeKeyPath);
            aCompleteNodePath += KEY_PATH_SEPARATOR_STRING + nodeKeyPath;
        }
        if (aCompleteNodePath != null) {
            aCompleteNodePath = aCompleteNodePath.replace(KEY_PATH_SEPARATOR_CHAR, PREFERENCES_KEY_PATH_SEPARATOR_CHAR);
            getNode = getNode.node(aCompleteNodePath);
        }
        return getNode;
    }

    private static void validateKeyPath(String keyPath) throws IllegalArgumentException {
        if (keyPath != null) {
            if (!KEY_PATH_PATTERN.matcher(keyPath).matches()) {
                throw new IllegalArgumentException(keyPath + " is an invalid keyPath string");
            }
        }
    }

    private static void validateDomain(String domain) throws IllegalArgumentException {
        if (domain != null) {
            if (!DOMAIN_PATTERN.matcher(domain).matches()) {
                throw new IllegalArgumentException(domain + " is an invalid domain string");
            }
        }
    }

    private static void notifyChangeListeners(String user, String domain, String nodeKeyPath, String key, Object value) {
        Map aUserInfo = new HashMap(5);
        aUserInfo.put(NOTIFICATION_CONFIGURATION_USER, user);
        aUserInfo.put(NOTIFICATION_CONFIGURATION_DOMAIN, domain);
        aUserInfo.put(NOTIFICATION_CONFIGURATION_NODE_PATH, nodeKeyPath);
        aUserInfo.put(NOTIFICATION_CONFIGURATION_KEY, key);
        aUserInfo.put(NOTIFICATION_CONFIGURATION_VALUE, value);
        SZNotification.Center aCenter = SZNotification.Center.defaultCenter();
        SZNotification aNotification = new SZNotification(NOTIFICATION_NAME_DID_CHANGE, null, aUserInfo);
        aCenter.postNotification(aNotification);
    }
}
