package org.sourceforge.jemm.client;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Transverses a Class and its parent classes that are either
 * abstract or concrete, but skips Object as the final parent.
 * 
 * The iterator does not iterate the interfaces.
 * 
 * @author Paul Keeble
 *
 */
public class ClassHierarchyIterator implements Iterator<Class<?>>, Iterable<Class<?>> {

    Class<?> current;

    public ClassHierarchyIterator(Class<?> clazz) {
        this.current = clazz;
    }

    @Override
    public boolean hasNext() {
        if (current.isInterface() || current.isPrimitive()) return false;
        return !current.equals(Object.class);
    }

    @Override
    public Class<?> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No further classes found");
        }
        Class<?> result = current;
        current = current.getSuperclass();
        return result;
    }

    @Override
    public void remove() {
        throw new RuntimeException("Remove not supported for classHierarchyIterator");
    }

    @Override
    public Iterator<Class<?>> iterator() {
        return this;
    }
}
