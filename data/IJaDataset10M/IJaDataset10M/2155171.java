package org.openproxy;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class Session extends Proxy implements ISession {

    public Session() {
        super(null);
    }

    private Map<Long, Proxy> proxies = new HashMap<Long, Proxy>();

    private Map<Long, Map<String, Object>> dirtyProxies = new HashMap<Long, Map<String, Object>>();

    protected Session getSession() {
        return this;
    }

    public void activate() {
    }

    ;

    public void passivate() {
    }

    ;

    public void processRequest() {
    }

    public void propertyChange(Proxy proxy, String property, boolean isCollection, Object newValue) {
        if (!proxies.containsKey(proxy.getID())) {
            proxies.put(proxy.getID(), proxy);
        }
        Map<String, Object> attributes = dirtyProxies.get(proxy.getID());
        if (attributes == null) {
            attributes = new HashMap<String, Object>();
            dirtyProxies.put(proxy.getID(), attributes);
        }
        Object attribute = attributes.get(property);
        if (isCollection) {
            if (attribute == null) {
                attribute = new HashSet();
            }
            ((Collection) attribute).add(newValue);
        } else {
            attribute = newValue;
        }
        attributes.put(property, attribute);
    }

    public void flush(ResponseHandler handler) {
        handler.serialize(dirtyProxies);
        dirtyProxies.clear();
    }
}
