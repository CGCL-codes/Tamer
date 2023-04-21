package acmsoft.util;

/**
 * this class works with resources loading
 */
public final class ResourceManager {

    /** Name of package whose resources are to be managed */
    private String m_strPackageName = null;

    /** The name of file with resources in current package */
    private String m_strResourceFileName = "";

    private NLSLocalizator m_DefaultLocalizator = NLSLocalizator.getDefaultLocalizator();

    private FormattableResourceBundle m_DefaultBundle;

    /** Here resource bundles for all localizations will be stored*/
    private FormattableResourceBundle[] m_bundles = new FormattableResourceBundle[NLSLocalizator.getLocalizatorsCount()];

    public boolean m_bResourcesLoaded = false;

    public ResourceManager(Class aClass, String strResourceFileName) {
        this(StringUtils.getClassPackageName(aClass), strResourceFileName);
    }

    public ResourceManager(String strPackageName, String strResourceFileName) {
        m_strPackageName = strPackageName;
        m_strResourceFileName = strResourceFileName;
        loadResources();
    }

    private synchronized FormattableResourceBundle loadResources(NLSLocalizator loc) {
        FormattableResourceBundle bundle = FormattableResourceBundle.getFormattableBundle(m_strPackageName + "." + m_strResourceFileName, loc.getLocale());
        if (bundle != null) {
            m_bundles[loc.getID()] = bundle;
        }
        return bundle;
    }

    private synchronized void loadResources() {
        m_bResourcesLoaded = false;
        try {
            NLSLocalizator defaultlLocalizator = NLSLocalizator.getDefaultLocalizator();
            FormattableResourceBundle bundle = FormattableResourceBundle.getFormattableBundle(m_strPackageName + "." + m_strResourceFileName, defaultlLocalizator.getLocale());
            if (bundle != null) {
                m_bundles[defaultlLocalizator.getID()] = bundle;
                m_DefaultBundle = bundle;
                m_bResourcesLoaded = true;
            }
        } catch (Exception ignored) {
        }
    }

    public synchronized FormattableResourceBundle getBundle(NLSLocalizator loc) {
        FormattableResourceBundle bundle = m_bundles[loc.getID()];
        if (bundle == null) {
            bundle = loadResources(loc);
        }
        return bundle;
    }

    public String getResourceString(String strID) {
        String strResource;
        FormattableResourceBundle bundle = m_DefaultBundle;
        if (bundle == null) {
            System.err.println("Cannot load resources for package " + m_strPackageName + " and locale " + m_DefaultLocalizator.getPrimaryName() + ".");
            strResource = "";
        } else {
            try {
                strResource = bundle.getString(strID);
            } catch (Exception e) {
                System.err.println("Request for resource string '" + strID + "' from package " + m_strPackageName + " failed.");
                strResource = "";
            }
        }
        return strResource;
    }

    public String getResourceString(NLSLocalizator loc, String strID) {
        String strResource;
        FormattableResourceBundle bundle = m_bundles[loc.getID()];
        if (bundle == null) {
            bundle = loadResources(loc);
        }
        if (bundle == null) {
            System.err.println("Cannot load resources for package " + m_strPackageName + " and locale " + loc.getPrimaryName() + ".");
            strResource = "";
        } else {
            try {
                strResource = bundle.getString(strID);
            } catch (Exception e) {
                System.err.println("Request for resource string '" + strID + "' from package " + m_strPackageName + " failed.");
                strResource = "";
            }
        }
        return strResource;
    }

    public String getResourceString(String strID, Object[] obValues) {
        return getResourceString(m_DefaultBundle, m_DefaultLocalizator, strID, obValues);
    }

    public String getResourceString(NLSLocalizator loc, String strID, Object[] obValues) {
        FormattableResourceBundle bundle = m_bundles[loc.getID()];
        if (bundle == null) {
            bundle = loadResources(loc);
        }
        return getResourceString(bundle, loc, strID, obValues);
    }

    String getResourceString(UnlocalizedMessage msg) {
        return getResourceString(m_DefaultBundle, m_DefaultLocalizator, msg.getResourceID(), msg.getParameters());
    }

    public String getResourceString(NLSLocalizator loc, UnlocalizedMessage msg) {
        FormattableResourceBundle bundle = m_bundles[loc.getID()];
        if (bundle == null) {
            bundle = loadResources(loc);
        }
        return getResourceString(bundle, loc, msg.getResourceID(), msg.getParameters());
    }

    private String getResourceString(FormattableResourceBundle bundle, NLSLocalizator loc, String strID, Object[] obValues) {
        String strResource;
        if (bundle == null) {
            System.err.println("Cannot load resources for package " + m_strPackageName + " and locale " + loc.getPrimaryName() + ".");
            strResource = "";
        } else {
            try {
                strResource = bundle.getString(strID, processValues(loc, obValues));
            } catch (Exception e) {
                System.err.println("Request for resource string '" + strID + "' from package " + m_strPackageName + " failed.");
                strResource = "";
            }
        }
        return strResource;
    }

    public String getResourceString(String strID, Object obValue1) {
        Object[] obValues = { obValue1 };
        return getResourceString(m_DefaultBundle, m_DefaultLocalizator, strID, obValues);
    }

    public String getResourceString(NLSLocalizator loc, String strID, Object obValue1) {
        Object[] obValues = { obValue1 };
        return getResourceString(loc, strID, obValues);
    }

    public String getResourceString(String strID, Object obValue1, Object obValue2) {
        Object[] obValues = { obValue1, obValue2 };
        return getResourceString(m_DefaultBundle, m_DefaultLocalizator, strID, obValues);
    }

    public String getResourceString(NLSLocalizator loc, String strID, Object obValue1, Object obValue2) {
        Object[] obValues = { obValue1, obValue2 };
        return getResourceString(loc, strID, obValues);
    }

    public String getResourceString(String strID, Object obValue1, Object obValue2, Object obValue3) {
        Object[] obValues = { obValue1, obValue2, obValue3 };
        return getResourceString(m_DefaultBundle, m_DefaultLocalizator, strID, obValues);
    }

    public String getResourceString(NLSLocalizator loc, String strID, Object obValue1, Object obValue2, Object obValue3) {
        Object[] obValues = { obValue1, obValue2, obValue3 };
        return getResourceString(loc, strID, obValues);
    }

    public String getResourceString(String strID, Object obValue1, Object obValue2, Object obValue3, Object obValue4) {
        Object[] obValues = { obValue1, obValue2, obValue3, obValue4 };
        return getResourceString(m_DefaultBundle, m_DefaultLocalizator, strID, obValues);
    }

    public String getResourceString(NLSLocalizator loc, String strID, Object obValue1, Object obValue2, Object obValue3, Object obValue4) {
        Object[] obValues = { obValue1, obValue2, obValue3, obValue4 };
        return getResourceString(loc, strID, obValues);
    }

    public boolean getResourcesLoaded() {
        return m_bResourcesLoaded;
    }

    public synchronized void setDefaultNlsLocalizator(NLSLocalizator loc) {
        m_DefaultLocalizator = loc;
        FormattableResourceBundle bundle = m_bundles[loc.getID()];
        if (bundle == null) {
            bundle = loadResources(loc);
        }
        m_DefaultBundle = bundle;
    }

    public NLSLocalizator getDefaultLocalzator() {
        return m_DefaultLocalizator;
    }

    /**
 * Performs special processing for non string values:<br>
 * - strings are kept in place
 * - for exceptions the original message is retrieved
 * - other values are kept unchanged
 */
    Object[] processValues(NLSLocalizator localizator, Object[] obValues) {
        if ((obValues == null) || (obValues.length == 0)) {
            return obValues;
        }
        Object[] result = new Object[obValues.length];
        for (int iIdx = 0; iIdx < obValues.length; iIdx++) {
            Object obj = obValues[iIdx];
            if (obj instanceof Throwable) {
                String strMsg = LocalizedExceptionAdapter.getLocalizedMessage((Throwable) obj, localizator);
                if ((strMsg == null) || (strMsg.length() == 0)) {
                    obj = obj.getClass().getName();
                } else {
                    obj = strMsg;
                }
            } else if (obj instanceof UnlocalizedMessage) {
                obj = ((UnlocalizedMessage) obj).getLocalizedString(localizator);
            }
            result[iIdx] = obj;
        }
        return result;
    }
}
