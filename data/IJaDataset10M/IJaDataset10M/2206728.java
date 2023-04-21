package acmsoft.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class is used to simplify working with parametrized resources.
 * Resource strings will be formatted according to
 * java.text.MessageFormat rules
 */
public final class FormattableResourceBundle extends ResourceBundle {

    private static final Hashtable m_hshFormatableBundles = new Hashtable();

    public FormattableResourceBundle(ResourceBundle base) {
        m_rb = base;
    }

    /**
     * Returns resource bundle
     * @param strName - file of properties or class to retrieve resources from
     */
    public static FormattableResourceBundle getFormattableBundle(String strName) {
        return getFormattableBundle(strName, false);
    }

    /**
     * Returns resource bundle
     * @param strName - file of properties or class to retrieve resources from
     * @param bRefresh - if true, we always create new bundle
     */
    public static FormattableResourceBundle getFormattableBundle(String strName, boolean bRefresh) {
        FormattableResourceBundle result = bRefresh ? null : (FormattableResourceBundle) m_hshFormatableBundles.get(strName);
        if (result == null) {
            result = new FormattableResourceBundle(NLSResourceBundleLoader.getNLSBundle(strName));
            m_hshFormatableBundles.put(strName, result);
        }
        return result;
    }

    /**
     * Returns resource bundle for given locale
     * @param strName - file of properties or class to retrieve resources from
     * @param loc - locale required
     */
    public static FormattableResourceBundle getFormattableBundle(String strName, Locale loc) {
        return getFormattableBundle(strName, loc, false);
    }

    /**
     * Returns resource bundle for given locale
     * @param strName - file of properties or class to retrieve resources from
     * @param loc - locale required
     * @param bRefresh - if true, we always create new bundle
     */
    public static FormattableResourceBundle getFormattableBundle(String strName, Locale loc, boolean bRefresh) {
        StringWithLocale strloc = new StringWithLocale(strName, loc);
        FormattableResourceBundle result = bRefresh ? null : (FormattableResourceBundle) m_hshFormatableBundles.get(strloc);
        if (result == null) {
            result = new FormattableResourceBundle(NLSResourceBundleLoader.getNLSBundle(strName, loc));
            m_hshFormatableBundles.put(strloc, result);
        }
        return result;
    }

    private ResourceBundle m_rb;

    protected Object handleGetObject(String str) {
        return m_rb.getObject(str);
    }

    public Enumeration getKeys() {
        return m_rb.getKeys();
    }

    /**
     * returns formatted resource string
     */
    public String getString(String strKey, Object[] obValues) {
        for (int iCount = obValues.length - 1; iCount >= 0; iCount--) {
            if (obValues[iCount] == null) {
                obValues[iCount] = "";
            }
        }
        try {
            String str = m_rb.getString(strKey);
            if (str == null) {
                return null;
            }
            return MessageFormatter.format(str, obValues);
        } catch (MissingResourceException ex) {
            return "Internal error. Message with id '" + strKey + "' cannot be retrieved";
        }
    }

    public String getString(String strKey, Object obValue0) {
        Object[] obValues = { obValue0 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1) {
        Object[] obValues = { obValue0, obValue1 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2) {
        Object[] obValues = { obValue0, obValue1, obValue2 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2, Object obValue3) {
        Object[] obValues = { obValue0, obValue1, obValue2, obValue3 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2, Object obValue3, Object obValue4) {
        Object[] obValues = { obValue0, obValue1, obValue2, obValue3, obValue4 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2, Object obValue3, Object obValue4, Object obValue5) {
        Object[] obValues = { obValue0, obValue1, obValue2, obValue3, obValue4, obValue5 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2, Object obValue3, Object obValue4, Object obValue5, Object obValue6) {
        Object[] obValues = { obValue0, obValue1, obValue2, obValue3, obValue4, obValue5, obValue6 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2, Object obValue3, Object obValue4, Object obValue5, Object obValue6, Object obValue7) {
        Object[] obValues = { obValue0, obValue1, obValue2, obValue3, obValue4, obValue5, obValue6, obValue7 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2, Object obValue3, Object obValue4, Object obValue5, Object obValue6, Object obValue7, Object obValue8) {
        Object[] obValues = { obValue0, obValue1, obValue2, obValue3, obValue4, obValue5, obValue6, obValue7, obValue8 };
        return getString(strKey, obValues);
    }

    public String getString(String strKey, Object obValue0, Object obValue1, Object obValue2, Object obValue3, Object obValue4, Object obValue5, Object obValue6, Object obValue7, Object obValue8, Object obValue9) {
        Object[] obValues = { obValue0, obValue1, obValue2, obValue3, obValue4, obValue5, obValue6, obValue7, obValue8, obValue9 };
        return getString(strKey, obValues);
    }
}

/**
 * this internal class is used to "key" bundles in hashtable
 */
final class StringWithLocale {

    private String m_str;

    private Locale m_loc = Locale.getDefault();

    StringWithLocale(String str, Locale loc) {
        if (str == null) {
            m_str = "";
        } else {
            m_str = str;
        }
        if (loc == null) {
            m_loc = Locale.getDefault();
        } else {
            m_loc = loc;
        }
    }

    public int hashCode() {
        return m_str.hashCode() + m_loc.hashCode();
    }

    public boolean equals(Object p1) {
        if (p1 == null) {
            return false;
        }
        if (!(p1 instanceof StringWithLocale)) {
            return false;
        }
        StringWithLocale s1 = (StringWithLocale) p1;
        return (m_str.equals(s1.m_str)) && (m_loc.equals(s1.m_loc));
    }
}
