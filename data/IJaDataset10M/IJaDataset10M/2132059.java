package net.sourceforge.antme;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class is used to assist in the unit testing of the
 * system.  When the Language system is put into test mode,
 * and instance of this class is used as the system resource
 * bundle instead of the standard <code>PropertyResourceBundle</code>
 * driven off the <code>AntMEText.properties</code> file.
 * <p>
 * This class returns strings as follows:
 * <ol>
 * <li>
 * When a request is made for a string, it checks the standard
 * property bundle to see if the string exists.  If it does
 * not exist, a <code>null</code> is returned.  Based on the
 * default behavior of <code>ResourceBundle</code>, this will
 * generally cause an exception to be thrown.
 * </li>
 * <li>
 * If the string does exist, it is examined to see if it has
 * any "substitution points" in it.  If it does not, the "key"
 * value is returned.  This means that, for parameter-less
 * messages, the key will be returned in place of the locale-specific
 * string.  (This allows the behavior of the unit tests to be 
 * independent of the locale in which they're run.)
 * </li>
 * <li>
 * If there are substitution points in the string, the same
 * set of substitution points are added to the end of the key,
 * separated by colons.  This results in a returned template
 * that has the same set of substitution points, but whose
 * format is entirely predictable, again making unit tests
 * behave consistently.
 * </li>
 * </ol>
 * 
 * @author khunter
 *
 */
public class TestTextBundle extends ResourceBundle {

    private ResourceBundle _realResourceBundle;

    /**
	 * Default constructor.
	 *
	 */
    public TestTextBundle() {
        _realResourceBundle = ResourceBundle.getBundle(Language.TEXT_BUNDLE);
    }

    /**
     * Returns an enumeration of the keys supported by this resource bundle.
     * 
     * @return An <code>Enumeration</code>  of the keys supported by this resource bundle.
     *
     */
    public Enumeration getKeys() {
        return _realResourceBundle.getKeys();
    }

    /**
     * Gets an object for the given key from this resource bundle.
     * Returns null if this resource bundle does not contain an
     * object for the given key.
     *
     * @param key the key for the desired object
     * @exception NullPointerException if <code>key</code> is <code>null</code>
     * @return the object for the given key, or null
     * @see 
     */
    protected Object handleGetObject(String key) {
        String realString;
        try {
            realString = _realResourceBundle.getString(key);
        } catch (MissingResourceException ex) {
            return null;
        }
        if (realString.indexOf('{') < 0) {
            return key;
        }
        String result = key;
        for (int i = 0; i < SUBSTITUTION_POINTS.length; i++) {
            if (realString.indexOf(SUBSTITUTION_POINTS[i]) >= 0) {
                result = result + ":" + SUBSTITUTION_POINTS[i];
            }
        }
        return result;
    }

    private static final String[] SUBSTITUTION_POINTS = { "{0}", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}" };
}
