package com.acv.dao.catalog.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.acv.dao.common.Constants;

/**
 * The Class ContentWrapperMap. Prevent {@link NullPointerException} while trying to access
 * a missing i18n resource.
 */
public class ContentWrapperMap<T extends GenericI18n> implements Map<String, T> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The content. */
    private Map<String, T> content;

    /** The t class. */
    private Class tClass;

    /**
	 * Instantiates a new content wrapper map.
	 *
	 * @param map the map
	 * @param tClass the t class
	 */
    public ContentWrapperMap(Map<String, T> map, Class tClass) {
        this.content = map;
        this.tClass = tClass;
    }

    /**
	 * Gets the key.
	 *
	 * @return the key
	 */
    public Map<String, Boolean> getKey() {
        Map<String, Boolean> res = new HashMap<String, Boolean>();
        for (T collection : content.values()) {
            for (String key : collection.keySet()) {
                res.put(key, true);
            }
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public T get(Object key) {
        if (content.containsKey(key)) return content.get(key); else {
            if (content.containsKey(Constants.DEFAULT_CONTENT_LANGUAGE_CODE)) return content.get(Constants.DEFAULT_CONTENT_LANGUAGE_CODE); else {
                for (String myKey : content.keySet()) {
                    return content.get(myKey);
                }
                Constructor<T> cons = null;
                try {
                    cons = tClass.getConstructor(new Class[] {});
                } catch (SecurityException e) {
                    return null;
                } catch (NoSuchMethodException e) {
                    return null;
                }
                try {
                    return cons.newInstance(new Object[] {});
                } catch (IllegalArgumentException e) {
                    return null;
                } catch (InstantiationException e) {
                    return null;
                } catch (IllegalAccessException e) {
                    return null;
                } catch (InvocationTargetException e) {
                    return null;
                }
            }
        }
    }

    public Set<String> keySet() {
        return content.keySet();
    }

    public void clear() {
        content.clear();
    }

    public boolean containsKey(Object key) {
        return content.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return content.containsValue(value);
    }

    public Set<java.util.Map.Entry<String, T>> entrySet() {
        return content.entrySet();
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public T put(String key, T value) {
        return content.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends T> t) {
        content.putAll(t);
    }

    public T remove(Object key) {
        return content.remove(key);
    }

    public int size() {
        return content.size();
    }

    public Collection<T> values() {
        return content.values();
    }
}
