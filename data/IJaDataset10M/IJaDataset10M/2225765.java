package org.dmpotter.mapper;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Manages multiple object factories.
 * <p>
 * This class is not inherently threadsafe.
 * <p>
 * (And yes, manager is currently a misnomer - "collection" would work
 * just as well.)
 */
public class MapObjectManager {

    private MapObjectFactory factories[];

    private int size;

    private int modcount;

    public MapObjectManager() {
        factories = new MapObjectFactory[5];
        size = 0;
        modcount = 0;
    }

    /**
     * Gets an array of all the current MapObjectFactory objects in
     * this manager.
     * <p>
     * This method <em>copies</em> the array, and therefore should not
     * be called repeatedly if it can be avoided.
     */
    public MapObjectFactory[] getMapObjectFactories() {
        MapObjectFactory rval[] = new MapObjectFactory[size];
        System.arraycopy(factories, 0, rval, 0, size);
        return rval;
    }

    /**
     * Gets a MapObjectFactory object from the given index.
     * @param index the index of the object to get, in the range of
     * <code>[0..</code>{@link #size()}<code>]</code>
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public MapObjectFactory getMapObjectFactoryAt(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException(Integer.toString(index) + " out of bounds of [0.." + size + "]");
        return factories[index];
    }

    /**
     * Gets a MapObjectFactory object from a class name.
     * @param className the class name to look up
     * @return <code>null</code> if no matching class is found
     */
    public MapObjectFactory getFactoryFromClassName(String className) {
        for (int i = 0; i < size; i++) if (factories[i].getBuiltObjectClassName().equals(className)) return factories[i];
        return null;
    }

    /**
     * Gets the number of MapObjectFactory objects currently in this manager.
     * @return the number of MapObjectFactory objects in this manager
     */
    public int size() {
        return size;
    }

    /**
     * Remove any additional storage space and make the manager use an array
     * that is exactly the size of the number of objects.  Use this after
     * having added all the objects to save a pathetic amount of memory when
     * the next garbage collection runs.
     */
    public void compact() {
        MapObjectFactory b[] = new MapObjectFactory[size];
        System.arraycopy(factories, 0, b, 0, size);
        factories = b;
    }

    /**
     * Adds a map object factory to this object manager.
     * @param factory the factory to add
     */
    public void addMapObjectFactory(MapObjectFactory factory) {
        if (size >= factories.length) {
            MapObjectFactory b[] = new MapObjectFactory[factories.length << 1];
            System.arraycopy(factories, 0, b, 0, factories.length);
            factories = b;
        }
        factories[size++] = factory;
        modcount++;
    }

    /**
     * Gets an iterator of the objects in this collection.
     * <p>
     * The iterator returned is fail-fast, meaning that a
     * ConcurrentModificationException will be thrown if the structure is
     * changed during iteration.  Maybe - there are scenarios when a change
     * will not be detected immediately: this feature should be used only to
     * find bugs and not to attempt to prevent them.
     * <p>
     * If you wish to iterate safely, use {@link #getMapObjectFactories()} and
     * iterate over the array.
     */
    public Iterator iterator() {
        return new FactoryIterator();
    }

    private class FactoryIterator implements Iterator {

        private int mymodcount;

        private int index;

        public FactoryIterator() {
            mymodcount = modcount;
            index = 0;
        }

        public boolean hasNext() {
            if (mymodcount != modcount) throw new ConcurrentModificationException();
            return index < size;
        }

        public Object next() {
            if (mymodcount != modcount) throw new ConcurrentModificationException();
            if (index >= size) throw new NoSuchElementException();
            return factories[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
