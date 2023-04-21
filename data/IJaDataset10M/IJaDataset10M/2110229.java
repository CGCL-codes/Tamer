package org.autofetch.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import org.autofetch.CollectionTracker;
import org.autofetch.Statistics;
import org.autofetch.Trackable;
import org.hibernate.collection.PersistentSortedSet;
import org.hibernate.engine.SessionImplementor;

/**
 * Based on org.hibernate.collection.PersistentSet
 * 
 * @author Ali Ibrahim <aibrahim@cs.utexas.edu>
 * 
 */
@SuppressWarnings("unchecked")
public class AutofetchSortedSet extends PersistentSortedSet implements Trackable {

    private CollectionTracker collectionTracker = new CollectionTracker();

    public AutofetchSortedSet() {
        super();
    }

    public AutofetchSortedSet(SessionImplementor si, SortedSet s) {
        super(si, s);
    }

    public AutofetchSortedSet(SessionImplementor si) {
        super(si);
    }

    public void addTracker(Statistics tracker) {
        collectionTracker.addTracker(tracker);
    }

    public void addTrackers(Set<Statistics> trackers) {
        collectionTracker.addTrackers(trackers);
    }

    public boolean disableTracking() {
        boolean oldValue = collectionTracker.isTracking();
        collectionTracker.setTracking(false);
        return oldValue;
    }

    public boolean enableTracking() {
        boolean oldValue = collectionTracker.isTracking();
        collectionTracker.setTracking(true);
        return oldValue;
    }

    public void removeTracker(Statistics stats) {
        collectionTracker.removeTracker(stats);
    }

    public boolean isAccessed() {
        return collectionTracker.isAccessed();
    }

    private void accessed() {
        if (wasInitialized()) {
            collectionTracker.trackAccess(set);
        }
    }

    /**
     * @see java.util.Set#size()
     */
    @Override
    public int size() {
        int ret = super.size();
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see java.util.Set#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        boolean ret = super.isEmpty();
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see java.util.Set#contains(Object)
     */
    @Override
    public boolean contains(Object object) {
        boolean ret = super.contains(object);
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see java.util.Set#iterator()
     */
    @Override
    public Iterator iterator() {
        Iterator iter = super.iterator();
        if (wasInitialized()) {
            accessed();
        }
        return iter;
    }

    /**
     * @see java.util.Set#toArray()
     */
    @Override
    public Object[] toArray() {
        Object[] arr = super.toArray();
        if (wasInitialized()) {
            accessed();
        }
        return arr;
    }

    /**
     * @see java.util.Set#toArray(Object[])
     */
    @Override
    public Object[] toArray(Object[] array) {
        Object[] arr = super.toArray(array);
        if (wasInitialized()) {
            accessed();
        }
        return arr;
    }

    /**
     * @see java.util.Set#add(Object)
     */
    @Override
    public boolean add(Object value) {
        Boolean exists = isOperationQueueEnabled() ? readElementExistence(value) : null;
        if (exists == null) {
            initialize(true);
            accessed();
            if (set.add(value)) {
                dirty();
                return true;
            } else {
                return false;
            }
        } else {
            return super.add(value);
        }
    }

    /**
     * @see java.util.Set#remove(Object)
     */
    @Override
    public boolean remove(Object value) {
        boolean ret = super.remove(value);
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see java.util.Set#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection coll) {
        boolean ret = super.containsAll(coll);
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see java.util.Set#addAll(Collection)
     */
    @Override
    public boolean addAll(Collection coll) {
        if (coll.size() > 0) {
            initialize(true);
            accessed();
            if (set.addAll(coll)) {
                dirty();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @see java.util.Set#retainAll(Collection)
     */
    @Override
    public boolean retainAll(Collection coll) {
        boolean val = super.retainAll(coll);
        if (wasInitialized()) {
            accessed();
        }
        return val;
    }

    /**
     * @see java.util.Set#removeAll(Collection)
     */
    @Override
    public boolean removeAll(Collection coll) {
        boolean val = super.removeAll(coll);
        if (wasInitialized()) {
            accessed();
        }
        return val;
    }

    @Override
    public void clear() {
        super.clear();
        if (wasInitialized()) {
            accessed();
        }
    }

    @Override
    public String toString() {
        String ret = super.toString();
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    @Override
    public boolean equals(Object other) {
        boolean ret = super.equals(other);
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int ret = super.hashCode();
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see PersistentSortedSet#subSet(Object,Object)
     */
    public SortedSet subSet(Object fromElement, Object toElement) {
        SortedSet ret = super.subSet(fromElement, toElement);
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see PersistentSortedSet#headSet(Object)
     */
    public SortedSet headSet(Object toElement) {
        SortedSet ret = super.headSet(toElement);
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see PersistentSortedSet#tailSet(Object)
     */
    public SortedSet tailSet(Object fromElement) {
        SortedSet ret = super.tailSet(fromElement);
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see PersistentSortedSet#first()
     */
    public Object first() {
        Object ret = super.first();
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }

    /**
     * @see PersistentSortedSet#last()
     */
    public Object last() {
        Object ret = super.last();
        if (wasInitialized()) {
            accessed();
        }
        return ret;
    }
}
