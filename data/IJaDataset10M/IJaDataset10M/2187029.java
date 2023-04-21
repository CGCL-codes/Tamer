package net.sf.joafip.java.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import net.sf.joafip.AssertNotNull;
import net.sf.joafip.StorableClass;
import net.sf.joafip.StoreNotUseStandardSerialization;
import net.sf.joafip.store.service.proxy.IInstanceFactory;

/**
 * navigable key set of a tree map
 * 
 * @author luc peuvrier
 * 
 * @param <K>
 * @param <V>
 */
@StoreNotUseStandardSerialization
@StorableClass
public class PLinkedMapKeySet<K, V> extends PAbstractSet<K> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5452114901300895975L;

    @AssertNotNull
    private final Map<K, V> map;

    private final transient IInstanceFactory instanceFactory;

    private PLinkedMapKeySet(final IInstanceFactory instanceFactory, final Map<K, V> map) {
        super();
        this.instanceFactory = instanceFactory;
        this.map = map;
        if (map == null) {
            throw new IllegalArgumentException("map can not be null");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static PLinkedMapKeySet newInstance(final IInstanceFactory instanceFactory, final Map map) {
        PLinkedMapKeySet newInstance;
        if (instanceFactory == null) {
            newInstance = new PLinkedMapKeySet(instanceFactory, map);
        } else {
            newInstance = (PLinkedMapKeySet) instanceFactory.newInstance(PLinkedMapKeySet.class, new Class[] { IInstanceFactory.class, Map.class }, new Object[] { instanceFactory, map });
        }
        return newInstance;
    }

    private class KeyIterator implements Iterator<K> {

        @AssertNotNull
        private final Iterator<Map.Entry<K, V>> entrySetIterator;

        public KeyIterator(final Iterator<Entry<K, V>> entrySetIterator) {
            super();
            this.entrySetIterator = entrySetIterator;
            if (entrySetIterator == null) {
                throw new IllegalArgumentException("entrySetIterator can not be null");
            }
        }

        public boolean hasNext() {
            return entrySetIterator.hasNext();
        }

        public K next() {
            return entrySetIterator.next().getKey();
        }

        public void remove() {
            entrySetIterator.remove();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<K> iterator() {
        final KeyIterator keyIterator;
        if (instanceFactory == null) {
            keyIterator = new KeyIterator(map.entrySet().iterator());
        } else {
            keyIterator = (KeyIterator) instanceFactory.newInstance(KeyIterator.class, new Class[] { Iterator.class }, new Object[] { map.entrySet().iterator() });
        }
        return keyIterator;
    }

    @Override
    public int size() {
        return map.size();
    }

    private class DescendingKeyIterator implements Iterator<K> {

        @AssertNotNull
        private final Iterator<Map.Entry<K, V>> entrySetIterator;

        public DescendingKeyIterator(final Iterator<Entry<K, V>> entrySetIterator) {
            super();
            this.entrySetIterator = entrySetIterator;
            if (entrySetIterator == null) {
                throw new IllegalArgumentException("entrySetIterator can not be null");
            }
        }

        public boolean hasNext() {
            return entrySetIterator.hasNext();
        }

        public K next() {
            return entrySetIterator.next().getKey();
        }

        public void remove() {
            entrySetIterator.remove();
        }
    }

    @SuppressWarnings("unchecked")
    public Iterator<K> descendingIterator() {
        final DescendingKeyIterator descendingKeyIterator;
        if (instanceFactory == null) {
            descendingKeyIterator = new DescendingKeyIterator(((NavigableSet<Map.Entry<K, V>>) map.entrySet()).descendingIterator());
        } else {
            descendingKeyIterator = (DescendingKeyIterator) instanceFactory.newInstance(DescendingKeyIterator.class, new Class[] { Iterator.class }, new Object[] { ((NavigableSet<Map.Entry<K, V>>) map.entrySet()).descendingIterator() });
        }
        return descendingKeyIterator;
    }

    @Override
    public boolean add(final K element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        map.clear();
    }
}
