package com.peusoft.peucal.util.localization;

import java.util.*;

/**
 * Implements Localization based on ResourceBundle.
 * Implements a search of a localized message for the key from bottom to top<br>
 * in the package hierarchy.
 * @author Yauheni Prykhodzka
 * @version 1.0
 */
public class ResourceLocalization implements Localization {

    /** list of ResourceBundle (resource with localisations) */
    protected List<ResourceBundle> localisation = new ArrayList<ResourceBundle>();

    /**
     * Constructor for resources with the default locale.
     * @param resource the full name of recource including the package
     */
    public ResourceLocalization(String resource) {
        this(resource, Locale.getDefault());
    }

    /**
     * Constructor for resources with the specified locale.
     * @param resource the full name of recource including the package
     * @param locale the locale
     */
    public ResourceLocalization(String resource, Locale locale) {
        Locale l = locale != null ? locale : Locale.getDefault();
        String res = resource;
        while (true) {
            MissingResourceException ex = null;
            try {
                ResourceBundle rb = ResourceBundle.getBundle(res, l);
                localisation.add(rb);
            } catch (MissingResourceException e) {
                ex = e;
            }
            int i = res.lastIndexOf('.');
            if (i != -1) {
                String s = res.substring(i + 1);
                res = res.substring(0, i);
                i = res.lastIndexOf('.');
                if (i != -1) {
                    res = res.substring(0, i + 1) + s;
                    continue;
                }
            }
            if (ex != null && localisation.isEmpty()) {
                throw ex;
            } else {
                break;
            }
        }
    }

    /**
     * @see Localization#getLocalizedString(Object)
     */
    @Override
    public String getLocalizedString(Object key) {
        return getLocalizedString(key, null);
    }

    /**
     * @see Localization#getLocalizedString(Object, String)
     */
    @Override
    public String getLocalizedString(Object key, String value) {
        MissingResourceException ex = null;
        for (ResourceBundle rb : localisation) {
            ex = null;
            try {
                String val = rb.getString((String) key);
                if (val != null) {
                    return val;
                }
            } catch (MissingResourceException e) {
                ex = e;
            }
        }
        if (value != null) {
            return value;
        }
        throw ex;
    }
}
