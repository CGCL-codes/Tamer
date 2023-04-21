package net.sourceforge.freejava.collection.scope;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sourceforge.freejava.collection.iterator.ConcatIterator;
import net.sourceforge.freejava.collection.iterator.PrefetchedIterator;

/**
 * @test DerMapTest
 */
public abstract class DerMap<K, V> extends AbstractMap<K, V> implements Derivation<Map<K, V>>, Serializable {

    private static final long serialVersionUID = -4599316213416843857L;

    /** previous/parent, the original map */
    protected final Map<K, V> pMap;

    /** the override map */
    protected final Map<K, V> qMap;

    /**
     * if pDels.contains(x) and qMap.contains(x), x is exist in qMap.
     */
    protected Set<Object> pDels;

    public DerMap(Map<K, V> pMap) {
        this.pMap = pMap;
        this.qMap = createMap();
        this.pDels = null;
    }

    public DerMap(DerMap<K, V> pDerMap, boolean reduce) {
        this(reduce ? pDerMap.reduce() : pDerMap);
    }

    private Map<K, V> reduce() {
        if (qMap.isEmpty()) if (pDels != null && !pDels.isEmpty()) return pMap;
        return this;
    }

    /**
     * @return a new created map, null value must be allowed.
     */
    protected abstract Map<K, V> createMap();

    @Override
    public Map<K, V> getParent() {
        return pMap;
    }

    @Override
    public int size() {
        int n = pMap.size() + qMap.size();
        if (pDels != null) n -= pDels.size();
        return n;
    }

    @Override
    public V get(Object key) {
        V qVal = qMap.get(key);
        if (qVal == null) if (!qMap.containsKey(key)) if (pDels == null || !pDels.contains(key)) return pMap.get(key);
        return qVal;
    }

    void pOverride(Object key) {
        if (pMap.containsKey(key)) {
            if (pDels == null) pDels = new HashSet<Object>();
            pDels.add(key);
        }
    }

    @Override
    public V put(K key, V value) {
        V old = get(key);
        pOverride(key);
        qMap.put(key, value);
        return old;
    }

    @Override
    public V remove(Object key) {
        V old = get(key);
        pOverride(key);
        qMap.remove(key);
        return old;
    }

    @Override
    public void clear() {
        qMap.clear();
        Set<K> full = pMap.keySet();
        if (pDels == null) pDels = new HashSet<Object>(full); else {
            pDels.clear();
            pDels.addAll(full);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (qMap.containsKey(key)) return true;
        boolean p = pMap.containsKey(key);
        if (pDels == null) return p;
        if (!p) return false;
        return !pDels.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (qMap.containsValue(value)) return true;
        boolean p = pMap.containsValue(value);
        if (pDels == null) return p;
        if (!p) return false;
        for (Entry<K, V> pe : pMap.entrySet()) {
            K pk = pe.getKey();
            if (pDels.contains(pk)) continue;
            V pv = pe.getValue();
            if (pv == value) return true;
            if (pv != null && pv.equals(value)) return true;
        }
        return false;
    }

    class PIterator extends PrefetchedIterator<Entry<K, V>> {

        Iterator<Entry<K, V>> pItr = pMap.entrySet().iterator();

        @Override
        protected Entry<K, V> fetch() {
            while (pItr.hasNext()) {
                Entry<K, V> pNxt = pItr.next();
                K pNxtKey = pNxt.getKey();
                if (pDels != null && pDels.contains(pNxtKey)) continue;
                return pNxt;
            }
            return end();
        }
    }

    class EntrySet extends AbstractSet<Entry<K, V>> {

        @Override
        public int size() {
            return DerMap.this.size();
        }

        @Override
        public boolean add(Entry<K, V> e) {
            DerMap.this.put(e.getKey(), e.getValue());
            return super.add(e);
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Entry<?, ?>)) return false;
            Entry<?, ?> e = (Entry<?, ?>) o;
            Object key = e.getKey();
            if (DerMap.this.containsKey(key)) {
                DerMap.this.remove(key);
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            DerMap.this.clear();
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            Iterator<Entry<K, V>> pqItr = new ConcatIterator<Entry<K, V>>(new PIterator(), qMap.entrySet().iterator());
            return pqItr;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }
}
