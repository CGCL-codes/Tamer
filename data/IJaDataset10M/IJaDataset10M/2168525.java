package org.fgraph.base;

import java.util.*;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.progeeks.util.ObjectUtils;
import org.progeeks.util.TypeTransformer;
import org.fgraph.Goid;
import org.fgraph.tstore.Triple;
import org.fgraph.tstore.TripleStore;
import org.fgraph.tstore.TripleStoreFactory;
import org.fgraph.steps.Reflection;
import org.fgraph.util.StringTypeTransformer;
import org.fgraph.util.TransformedSet;

/**
 *  Default implementation of the PropertyStore interface
 *  that internally uses a set of TripleStores to store
 *  different property values.
 *
 *  @version   $Revision: 589 $
 *  @author    Paul Speed
 */
public class DefaultPropertyStore implements PropertyStore {

    private TripleStoreFactory tStoreFactory;

    private TripleStore<Long, String, String> defaultStore;

    private Function<String, Object> toObject;

    private Function<Object, String> toString;

    public DefaultPropertyStore(TripleStoreFactory tStoreFactory) {
        this.tStoreFactory = tStoreFactory;
        this.defaultStore = tStoreFactory.createStore("def_props", Long.class, String.class, String.class);
        StringTypeTransformer typeTransform = new StringTypeTransformer();
        toObject = typeTransform.getToObject();
        toString = typeTransform.getToString();
    }

    public void close() {
        defaultStore.close();
    }

    public Object setProperty(Goid id, String name, Object value) {
        Triple<Long, String, String> existing = defaultStore.get(id.value(), name, null);
        Triple<Long, String, String> newValue = replaceRow(existing, id.value(), name, toString.apply(value));
        if (newValue == null) {
            throw new ConcurrentModificationException("Existing value modified during update, property:" + name);
        }
        if (existing == null) {
            return null;
        }
        return toObject.apply(existing.third());
    }

    public Object removeProperty(Goid id, String name) {
        Triple<Long, String, String> existing = defaultStore.get(id.value(), name, null);
        if (existing == null) return null;
        if (defaultStore.remove(existing)) return toObject.apply(existing.third());
        throw new ConcurrentModificationException("Existing value modified during remove, property:" + name);
    }

    public Object removeProperty(Goid id, String name, Object oldValue) {
        String sValue = toString.apply(oldValue);
        TripleStore<Long, String, String> subset = defaultStore.find(id.value(), name, sValue);
        long result = subset.removeAll();
        if (result == 0) return null;
        return oldValue;
    }

    public Object replaceProperty(Goid id, String name, Object oldValue, Object newValue) {
        Triple<Long, String, String> existing = defaultStore.get(id.value(), name, null);
        if (existing == null) return null;
        Object existingValue = toObject.apply(existing.third());
        if (!ObjectUtils.areEqual(existingValue, oldValue)) return null;
        String sValue = toString.apply(newValue);
        Triple<Long, String, String> replaced = defaultStore.replace(existing, id.value(), name, sValue);
        if (replaced == null) return null;
        return oldValue;
    }

    public void createProperty(Goid id, String name, Object value) {
        defaultStore.add(id.value(), name, toString.apply(value));
    }

    protected Triple<Long, String, String> replaceRow(Triple<Long, String, String> existing, Long id, String key, String value) {
        return defaultStore.replace(existing, id, key, value);
    }

    public Object getProperty(Goid id, String name) {
        Triple<Long, String, String> existing = defaultStore.get(id.value(), name, null);
        if (existing == null) return null;
        return toObject.apply(existing.third());
    }

    public boolean hasProperty(Goid id, String name) {
        Triple<Long, String, String> existing = defaultStore.get(id.value(), name, null);
        return existing != null;
    }

    public Set<String> getPropertyNames(Goid id) {
        Set<Map.Entry<String, Object>> entries = getPropertySet(id);
        return TransformedSet.create(entries, Reflection.<String>property("key"), null);
    }

    public Set<Map.Entry<String, Object>> getPropertySet(Goid id) {
        Collection<Triple<Long, String, String>> objectProps = defaultStore.find(id.value(), null, null);
        return new PropertyEntrySet(id, objectProps);
    }

    public Collection<Long> find(Map<String, Object> properties) {
        if (properties == null || properties.size() == 0) throw new IllegalArgumentException("No properties specified.");
        Set<Long> results = new HashSet<Long>();
        for (Map.Entry<String, Object> e : properties.entrySet()) {
            Collection<Long> sub = find(e.getKey(), e.getValue());
            if (results.size() == 0) {
                results.addAll(sub);
            } else {
                results.retainAll(sub);
            }
            if (results.size() == 0) break;
        }
        return Collections.unmodifiableSet(results);
    }

    public Collection<Long> find(String name, Object value) {
        if (name == null && value == null) throw new IllegalArgumentException("Both name and value are null.  Only one wild-card allowed.");
        String sVal = value != null ? toString.apply(value) : null;
        Collection<Triple<Long, String, String>> rows = defaultStore.find(null, name, sVal);
        Collection<Long> results = Collections2.transform(rows, new ElementFunction<Long>(0));
        return Collections.unmodifiableCollection(results);
    }

    public Long get(String name, Object value) {
        if (name == null && value == null) throw new IllegalArgumentException("Both name and value are null.  Only one wild-card allowed.");
        String sVal = value != null ? toString.apply(value) : null;
        Triple<Long, String, String> row = defaultStore.get(null, name, sVal);
        if (row == null) return null;
        return row.first();
    }

    /**
     *  Removes all properties associated with the object ID.
     */
    public void clearProperties(Goid id) {
        defaultStore.find(id.value(), null, null).clear();
    }

    protected static class ElementFunction<T> implements Function<Triple, T> {

        private int index;

        public ElementFunction(int index) {
            this.index = index;
        }

        @SuppressWarnings("unchecked")
        public T apply(Triple from) {
            return (T) from.get(index);
        }
    }

    protected class PropertyEntry implements Map.Entry<String, Object> {

        private Goid id;

        private Triple<Long, String, String> entry;

        protected PropertyEntry(Goid id, Triple<Long, String, String> entry) {
            this.id = id;
            this.entry = entry;
        }

        public String getKey() {
            return entry.second();
        }

        public Object getValue() {
            return toObject.apply(entry.third());
        }

        public Object setValue(Object value) {
            Object old = getValue();
            Triple<Long, String, String> newRow = replaceRow(entry, entry.first(), entry.second(), toString.apply(value));
            if (newRow == null) throw new ConcurrentModificationException("Existing value doesn't match entry, property:" + getKey());
            entry = newRow;
            return old;
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    protected class PropertyEntryIterator implements Iterator<Map.Entry<String, Object>> {

        private Goid id;

        private Iterator<Triple<Long, String, String>> iterator;

        protected PropertyEntryIterator(Goid id, Iterator<Triple<Long, String, String>> iterator) {
            this.id = id;
            this.iterator = iterator;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Map.Entry<String, Object> next() {
            return new PropertyEntry(id, iterator.next());
        }

        public void remove() {
            iterator.remove();
        }
    }

    protected class PropertyEntrySet extends AbstractSet<Map.Entry<String, Object>> {

        private Goid id;

        private Collection<Triple<Long, String, String>> set;

        protected PropertyEntrySet(Goid id, Collection<Triple<Long, String, String>> set) {
            this.id = id;
            this.set = set;
        }

        public int size() {
            return set.size();
        }

        public void clear() {
            set.clear();
        }

        public Iterator<Map.Entry<String, Object>> iterator() {
            return new PropertyEntryIterator(id, set.iterator());
        }
    }
}
