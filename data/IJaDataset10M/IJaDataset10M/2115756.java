package com.versant.core.util;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.ref.Reference;

/**
 * This maintains a bag of weakly referenced objects. The clean method
 * must be called from time to time to get rid of the objects that the
 * garbage collector wants to nuke. This class is not synchronized.
 */
public final class WeakBag {

    private HashSet set = new HashSet();

    private ReferenceQueue refQueue = new ReferenceQueue();

    public WeakBag() {
    }

    /**
     * Add o to the bag and return the WeakReference that can be used to
     * remove it.
     */
    public WeakReference add(Object o) {
        WeakReference ans = new WeakReference(o, refQueue);
        set.add(ans);
        return ans;
    }

    /**
     * Remove o from the bag.
     */
    public void remove(Reference o) {
        set.remove(o);
    }

    /**
     * Get the approximate number of objects in the bag.
     */
    public int size() {
        return set.size();
    }

    /**
     * Get all the objects still in the bag.
     */
    public List values() {
        ArrayList a = new ArrayList(set.size());
        for (Iterator i = set.iterator(); i.hasNext(); ) {
            WeakReference r = (WeakReference) i.next();
            Object o = r.get();
            if (o != null) a.add(o);
        }
        return a;
    }

    /**
     * Get rid of objects in the bag that the garbage collector wants to
     * nuke. This does not block.
     */
    public void clean() {
        for (; ; ) {
            Object r = refQueue.poll();
            if (r == null) return;
            set.remove(r);
        }
    }
}
