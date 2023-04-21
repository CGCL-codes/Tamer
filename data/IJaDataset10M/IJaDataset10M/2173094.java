package org.rapla.framework;

import java.util.HashMap;

public class RaplaDefaultContext implements RaplaContext {

    private final HashMap contextObjects = new HashMap();

    private final RaplaContext m_parent;

    public RaplaDefaultContext() {
        this(null);
    }

    public RaplaDefaultContext(final RaplaContext parent) {
        m_parent = parent;
    }

    public Object lookup(final String key) throws RaplaContextException {
        final Object object = contextObjects.get(key);
        if (null != object) {
            return object;
        } else if (null != m_parent) {
            return m_parent.lookup(key);
        } else {
            final String message = "Unable to provide implementation for " + key;
            throw new RaplaContextException(key, message, null);
        }
    }

    public boolean has(final String key) {
        if (contextObjects.get(key) != null) return true;
        if (m_parent == null) return false;
        return m_parent.has(key);
    }

    public void put(final String key, final Object object) {
        contextObjects.put(key, object);
    }

    protected Object getUnsave(String key) {
        if (has(key)) {
            try {
                return lookup(key);
            } catch (RaplaContextException ex) {
            }
        }
        return null;
    }
}
