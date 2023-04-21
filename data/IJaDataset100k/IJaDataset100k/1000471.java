package org.bionote.page.type;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.discovery.tools.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Returns singleton instances of the page type classes... (they are immutable, so this makes sense to me)
 * @author mbreese
 *
 */
public class PageTypeUtil {

    protected static final Log log = LogFactory.getLog(PageTypeUtil.class);

    private static HashMap types = null;

    public static PageType getInstance(String className) {
        try {
            return getInstance(Class.forName(className));
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public static PageType getInstance(Class clazz) {
        if (types == null) {
            loadTypes();
        }
        if (clazz == null) {
            return null;
        }
        if (types.get(clazz) == null) {
            try {
                addInstance(clazz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (PageType) types.get(clazz);
    }

    protected static void addInstance(Object o) {
        if (o != null) {
            types.put(o.getClass(), o);
        }
    }

    public static Collection getPageTypes() {
        return getPageTypes(false);
    }

    public static Collection getAllPageTypes() {
        return getPageTypes(true);
    }

    public static Collection getPageTypes(boolean all) {
        if (types == null) {
            types = new HashMap();
            loadTypes();
        }
        Vector pageTypes = new Vector();
        Iterator it = types.values().iterator();
        while (it.hasNext()) {
            PageType type = (PageType) it.next();
            if (all) {
                pageTypes.add(type);
            } else {
                if (type.isPublic()) {
                    pageTypes.add(type);
                }
            }
        }
        Collections.sort(pageTypes, new Comparator() {

            public int compare(Object arg0, Object arg1) {
                PageType one = (PageType) arg0;
                PageType two = (PageType) arg1;
                return (one.getTypeName().compareTo(two.getTypeName()));
            }
        });
        return pageTypes;
    }

    public static void loadTypes() {
        types = new HashMap();
        log.debug("Loading Page types");
        Enumeration en = Service.providers(PageType.class);
        while (en.hasMoreElements()) {
            PageType t = (PageType) en.nextElement();
            log.debug("loading: " + t.getClassName());
            addInstance(t);
        }
    }

    /**
	 * @return
	 */
    public static PageType getDefaultPageType() {
        return getInstance(GeneralType.class);
    }
}
