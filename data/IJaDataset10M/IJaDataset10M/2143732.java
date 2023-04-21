package org.apache.axis2.classloader;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * @version $Rev: 704201 $ $Date: 2008-10-13 14:52:25 -0400 (Mon, 13 Oct 2008) $
 */
public class ResourceEnumeration implements Enumeration {

    private Iterator iterator;

    private final String resourceName;

    private Object next;

    public ResourceEnumeration(Collection resourceLocations, String resourceName) {
        this.iterator = resourceLocations.iterator();
        this.resourceName = resourceName;
    }

    public boolean hasMoreElements() {
        fetchNext();
        return (next != null);
    }

    public Object nextElement() {
        fetchNext();
        Object next = this.next;
        this.next = null;
        if (next == null) {
            throw new NoSuchElementException();
        }
        return next;
    }

    private void fetchNext() {
        if (iterator == null) {
            return;
        }
        if (next != null) {
            return;
        }
        try {
            while (iterator.hasNext()) {
                ResourceLocation resourceLocation = (ResourceLocation) iterator.next();
                ResourceHandle resourceHandle = resourceLocation.getResourceHandle(resourceName);
                if (resourceHandle != null) {
                    next = resourceHandle.getUrl();
                    return;
                }
            }
            iterator = null;
        } catch (IllegalStateException e) {
            iterator = null;
            throw e;
        }
    }
}
