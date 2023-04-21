package com.psychosally.i18n.hibernate;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.psychosally.i18n.util.HibernateUtil;
import com.psychosally.i18n.util.ListEnumeration;

/**
 * @author <a href="mailto:spaddo@users.sourceforge.net">Aleksandar Vidakovic </a>
 * 
 * @since 1.0
 *  
 */
public class HibernateResourceBundle extends ResourceBundle {

    public static final String DEFAULT_BUNDLE = "default";

    public static final String DEFAULT_LOCALE = "default";

    private static Log logger = LogFactory.getLog(HibernateResourceBundle.class);

    private String bundle;

    private String locale;

    public HibernateResourceBundle() {
        this.bundle = HibernateResourceBundle.DEFAULT_BUNDLE;
        this.locale = HibernateResourceBundle.DEFAULT_LOCALE;
    }

    public HibernateResourceBundle(String bundle) {
        this.bundle = bundle;
        this.locale = HibernateResourceBundle.DEFAULT_LOCALE;
    }

    public HibernateResourceBundle(String bundle, String language) {
        this.bundle = bundle;
        this.locale = language;
    }

    public Enumeration getKeys() {
        List keys = new LinkedList();
        String q = "from com.psychosally.i18n.hibernate.Resource r where r.locale='" + locale + "' and r.bundle='" + bundle + "'";
        Iterator result = HibernateUtil.query(q).iterator();
        while (result.hasNext()) {
            Resource r = (Resource) result.next();
            keys.add(r.getKey());
        }
        return new ListEnumeration(keys);
    }

    protected Object handleGetObject(String key) {
        String q = "from com.psychosally.i18n.hibernate.Resource r where r.locale='" + locale + "' and r.bundle='" + bundle + "' and r.key='" + key + "'";
        List result = HibernateUtil.query(q);
        if (result.size() == 0) {
            throw new MissingResourceException("Could not get value for key '" + key + "'", getClass().getName(), key);
        } else if (result.size() > 1) {
            throw new MissingResourceException("There are too many entries for key '" + key + "' (" + result.size() + ")", getClass().getName(), key);
        } else {
            return ((Resource) result.get(0)).getValue();
        }
    }
}
