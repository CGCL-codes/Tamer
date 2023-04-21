package de.suse.swamp.util;

import java.util.*;

/**
 * Global I18nCache to avoid reloading of resource files. 
 *
 * @author Thomas Schmidt &lt;tschmidt@suse.de&gt;
 */
public class I18nCache {

    private Hashtable cache = new Hashtable();

    private static I18nCache i18nCache = null;

    private I18nCache() {
    }

    public static synchronized I18nCache getInstance() {
        if (i18nCache == null) {
            i18nCache = new I18nCache();
        }
        return i18nCache;
    }

    public org.xnap.commons.i18n.I18n getI18n(String id) {
        return (org.xnap.commons.i18n.I18n) cache.get(id);
    }

    public void add(org.xnap.commons.i18n.I18n i18n, String path) {
        cache.put(path, i18n);
    }

    public boolean contains(String id) {
        return cache.containsKey(id);
    }

    public void clearCache() {
        cache.clear();
    }
}
