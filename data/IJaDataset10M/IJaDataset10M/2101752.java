package org.apache.commons.chain.web.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.chain.web.MapEntry;

/**
 * <p>Private implementation of <code>Map</code> for servlet context
 * init parameters.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 480477 $ $Date: 2006-11-29 08:34:52 +0000 (Wed, 29 Nov 2006) $
 */
final class ServletInitParamMap implements Map {

    public ServletInitParamMap(ServletContext context) {
        this.context = context;
    }

    private ServletContext context = null;

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return (context.getInitParameter(key(key)) != null);
    }

    public boolean containsValue(Object value) {
        Iterator values = values().iterator();
        while (values.hasNext()) {
            if (value.equals(values.next())) {
                return (true);
            }
        }
        return (false);
    }

    public Set entrySet() {
        Set set = new HashSet();
        Enumeration keys = context.getInitParameterNames();
        String key;
        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            set.add(new MapEntry(key, context.getInitParameter(key), false));
        }
        return (set);
    }

    public boolean equals(Object o) {
        return (context.equals(o));
    }

    public Object get(Object key) {
        return (context.getInitParameter(key(key)));
    }

    public int hashCode() {
        return (context.hashCode());
    }

    public boolean isEmpty() {
        return (size() < 1);
    }

    public Set keySet() {
        Set set = new HashSet();
        Enumeration keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        int n = 0;
        Enumeration keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }

    public Collection values() {
        List list = new ArrayList();
        Enumeration keys = context.getInitParameterNames();
        while (keys.hasMoreElements()) {
            list.add(context.getInitParameter((String) keys.nextElement()));
        }
        return (list);
    }

    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }
    }
}
