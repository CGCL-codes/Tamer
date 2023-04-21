package org.apache.tapestry5.ioc.internal.services;

import org.apache.tapestry5.ioc.ObjectCreator;

/**
 * An {@link org.apache.tapestry5.ioc.ObjectCreator} that delegates to another {@link
 * org.apache.tapestry5.ioc.ObjectCreator} and caches the result.
 */
public class CachingObjectCreator implements ObjectCreator {

    private boolean cached;

    private Object cachedValue;

    private ObjectCreator delegate;

    public CachingObjectCreator(ObjectCreator delegate) {
        this.delegate = delegate;
    }

    public synchronized Object createObject() {
        if (!cached) {
            cachedValue = delegate.createObject();
            cached = true;
            delegate = null;
        }
        return cachedValue;
    }
}
