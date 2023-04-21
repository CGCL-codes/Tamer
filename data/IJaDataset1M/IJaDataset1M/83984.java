package org.progeeks.filament.util;

import java.util.*;
import org.progeeks.util.ObjectUtils;
import org.progeeks.filament.StoreException;
import org.progeeks.filament.Triple;
import org.progeeks.filament.TripleStore;
import org.progeeks.filament.TripleStoreFactory;
import org.progeeks.filament.core.AbstractTripleStore;
import org.progeeks.filament.core.AbstractFilteredTripleStore;

/**
 *  An in-memory HashMap based TripleStore reference implementation.
 *
 *  @version   $Revision: 4028 $
 *  @author    Paul Speed
 */
public class HashTripleStore<U, V, W> extends AbstractTripleStore<U, V, W> {

    public static final TripleStoreFactory FACTORY = new Factory();

    private long sequence = 0;

    private Map<Object, Triple<U, V, W>> records = new HashMap<Object, Triple<U, V, W>>();

    protected HashTripleStore(String name, Class<U> u, Class<V> v, Class<W> w) {
        super(name, u, v, w);
    }

    public static <U, V, W> HashTripleStore<U, V, W> create(String name, Class<U> u, Class<V> v, Class<W> w) {
        return new HashTripleStore<U, V, W>(name, u, v, w);
    }

    /**
     *  Default implementation does nothing.
     */
    public void close() {
    }

    public int size() {
        return records.size();
    }

    public Iterator<Triple<U, V, W>> iterator() {
        return records.values().iterator();
    }

    protected Object getNewId(Object id) {
        if (id == null) return sequence++;
        return id;
    }

    public Triple<U, V, W> create(U first, V second, W third) {
        Object id = null;
        if (first == null && Long.class.isAssignableFrom(getFirstType())) {
            id = getNewId(id);
            first = getFirstType().cast(id);
        }
        if (second == null && Long.class.isAssignableFrom(getSecondType())) {
            id = getNewId(id);
            second = getSecondType().cast(id);
        }
        if (third == null && Long.class.isAssignableFrom(getThirdType())) {
            id = getNewId(id);
            third = getThirdType().cast(id);
        }
        Triple<U, V, W> result = new Triple<U, V, W>(id, first, second, third);
        records.put(id, result);
        return result;
    }

    public boolean add(Triple<U, V, W> t) {
        if (contains(t)) return false;
        if (t.getId() != null) throw new StoreException("Triple instance has an existing but invalid ID, triple:" + t);
        long id = sequence++;
        t.setId(id);
        records.put(id, t);
        return true;
    }

    public boolean remove(Object o) {
        if (!(o instanceof Triple)) return false;
        Triple<U, V, W> t = cast(o);
        if (t.getId() == null) return false;
        Triple<U, V, W> existing = records.get(t.getId());
        if (existing != t) return false;
        records.remove(t.getId());
        return true;
    }

    public Triple<U, V, W> get(Object id) {
        return records.get(id);
    }

    public Triple<U, V, W> get(U first, V second, W third) {
        TripleStore<U, V, W> subset = find(first, second, third);
        if (subset.size() == 0) return null;
        return subset.iterator().next();
    }

    public TripleStore<U, V, W> find(U first, V second, W third) {
        return new FilteredStore<U, V, W>(this, first, second, third);
    }

    public Triple<U, V, W> replace(Triple<U, V, W> oldValue, Triple<U, V, W> newValue) {
        if (oldValue == newValue || (oldValue != null && oldValue.equalValues(newValue))) return oldValue;
        if (contains(newValue)) return null;
        if (newValue.getId() != null) throw new StoreException("Triple instance has an existing but invalid ID, triple:" + newValue);
        if (oldValue == null) {
            if (add(newValue)) return newValue;
            return null;
        }
        Triple<U, V, W> existing = records.get(oldValue.getId());
        if (existing != oldValue) return null;
        Object id = oldValue.getId();
        newValue.setId(id);
        records.put(id, newValue);
        return newValue;
    }

    protected static class FilteredIterator<U, V, W> implements Iterator<Triple<U, V, W>> {

        private U first;

        private V second;

        private W third;

        private TripleStore<U, V, W> store;

        private Iterator<Triple<U, V, W>> delegate;

        private Triple<U, V, W> nextValue;

        private Triple<U, V, W> lastValue;

        protected FilteredIterator(TripleStore<U, V, W> store, U first, V second, W third) {
            this.store = store;
            this.delegate = store.iterator();
            this.first = first;
            this.second = second;
            this.third = third;
            fetch();
        }

        protected void fetch() {
            lastValue = nextValue;
            while (delegate.hasNext()) {
                nextValue = delegate.next();
                if (first != null && !ObjectUtils.areEqual(first, nextValue.first())) continue;
                if (second != null && !ObjectUtils.areEqual(second, nextValue.second())) continue;
                if (third != null && !ObjectUtils.areEqual(third, nextValue.third())) continue;
                return;
            }
            nextValue = null;
        }

        public boolean hasNext() {
            return nextValue != null;
        }

        public Triple<U, V, W> next() {
            Triple<U, V, W> result = nextValue;
            if (result == null) throw new NoSuchElementException("No more items in iterator.");
            fetch();
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove not supported for HashTripleStore iterators.");
        }
    }

    protected static class FilteredStore<U, V, W> extends AbstractFilteredTripleStore<U, V, W> {

        private U first;

        private V second;

        private W third;

        protected FilteredStore(HashTripleStore<U, V, W> parent, U first, V second, W third) {
            super(parent);
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public void clear() {
            Collection<Triple<U, V, W>> all = new ArrayList<Triple<U, V, W>>(this);
            getParent().removeAll(all);
        }

        public Iterator<Triple<U, V, W>> iterator() {
            return new FilteredIterator<U, V, W>(getParent(), first, second, third);
        }

        public Triple<U, V, W> get(U first, V second, W third) {
            if (first == null) first = this.first; else if (this.first != null) throw new IllegalArgumentException("Cannot override first element filter.");
            if (second == null) second = this.second; else if (this.second != null) throw new IllegalArgumentException("Cannot override second element filter.");
            if (third == null) third = this.third; else if (this.third != null) throw new IllegalArgumentException("Cannot override third element filter.");
            return getParent().get(first, second, third);
        }

        public TripleStore<U, V, W> find(U first, V second, W third) {
            if (first == null) first = this.first; else if (this.first != null) throw new IllegalArgumentException("Cannot override first element filter.");
            if (second == null) second = this.second; else if (this.second != null) throw new IllegalArgumentException("Cannot override second element filter.");
            if (third == null) third = this.third; else if (this.third != null) throw new IllegalArgumentException("Cannot override third element filter.");
            return getParent().find(first, second, third);
        }
    }

    protected static class Factory implements TripleStoreFactory {

        public <U, V, W> TripleStore<U, V, W> createStore(String name, Class<U> u, Class<V> v, Class<W> w) {
            return HashTripleStore.create(name, u, v, w);
        }
    }
}
