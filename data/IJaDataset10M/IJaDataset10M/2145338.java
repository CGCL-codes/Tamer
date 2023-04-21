package net.sf.gamine.util;

import java.util.*;

/**
 * An ObjectPool maintains a set of reusable objects that can be checked out when needed.
 * This is used to avoid unnecessary object creation in time critical routines.  This class
 * is not thread safe, so an ObjectPool should not be shared between threads.  Also, there
 * is no way to return a single object to the pool.  Once you are done with all objects that
 * have been taken from the pool, call reset() to allow all of them to be reused.
 */
public class ObjectPool<T> {

    private Class<T> objectClass;

    private ArrayList<T> pool;

    private int next;

    /**
   * Create a new ObjectPool.
   *
   * @param objectClass     the type of object this pool should contain.  The class must have
   *                        a constructor which takes no arguments.
   */
    public ObjectPool(Class<T> objectClass) {
        this.objectClass = objectClass;
        pool = new ArrayList<T>();
    }

    /**
   * Get an object from the pool.
   */
    public T getObject() {
        if (next == pool.size()) {
            try {
                pool.add(objectClass.newInstance());
            } catch (Exception ex) {
                throw new IllegalStateException("Exception creating new object for pool", ex);
            }
        }
        return pool.get(next++);
    }

    /**
   * Reset the pool.  This should be called when you are finished using all objects that have been
   * taken from the pool.
   */
    public void reset() {
        next = 0;
    }
}
