package jopt.csp.spi.pool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Pool implementation that releases idle objects during garbage collection
 */
public class WeakObjectQueue {

    private WeakReference<ArrayList<Object>> ref = null;

    /**
     * Pops an object from the queue or returns null if no objects
     * are in the queue
     */
    public synchronized Object pop() {
        if (ref == null) return null;
        ArrayList<Object> list = (ArrayList<Object>) ref.get();
        if (list == null) return null;
        if (list.size() == 0) return null;
        return list.remove(0);
    }

    /**
     * Pushes an object onto the queue
     */
    public void push(Object obj) {
        try {
            ArrayList<Object> list = (ref != null) ? (ArrayList<Object>) ref.get() : null;
            if (list == null) {
                list = new ArrayList<Object>();
                ref = new WeakReference<ArrayList<Object>>(list);
            }
            list.add(obj);
        } catch (Exception e) {
            throw new RuntimeException("error adding object to queue", e);
        }
    }

    /**
     * Removes an object from the queue
     */
    public void remove(Object obj) {
        if (ref == null) return;
        ArrayList<Object> list = ref.get();
        if (list == null) return;
        if (list.size() == 0) return;
        list.remove(obj);
    }

    /**
     * Clears queue data
     */
    public void clear() {
        ref = null;
    }
}
