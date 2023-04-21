package org.gerhardb.lib.util;

import java.util.*;
import java.lang.ref.*;

/**
 * From: http://www.java-forums.org/java-lang/7444-soft-hashmap.html
 */
@SuppressWarnings("unchecked")
public class SoftKeyHashMap extends AbstractMap<Object, Object> {

    private Set<?> entrySet = null;

    Map<SoftKey, Object> hash;

    private ReferenceQueue<?> queue = new ReferenceQueue<Object>();

    @SuppressWarnings("rawtypes")
    static class SoftKey extends SoftReference {

        private int hash;

        private SoftKey(Object k) {
            super(k);
            this.hash = k.hashCode();
        }

        static SoftKey create(Object k) {
            if (k == null) {
                return null;
            }
            return new SoftKey(k);
        }

        private SoftKey(Object k, ReferenceQueue<?> q) {
            super(k, q);
            this.hash = k.hashCode();
        }

        static SoftKey create(Object k, ReferenceQueue<?> q) {
            if (k == null) {
                return null;
            }
            return new SoftKey(k, q);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof SoftKey)) {
                return false;
            }
            Object t = this.get();
            Object u = ((SoftKey) o).get();
            if ((t == null) || (u == null)) {
                return false;
            } else if (t == u) {
                return true;
            } else {
                return t.equals(u);
            }
        }

        @Override
        public int hashCode() {
            return this.hash;
        }
    }

    void processQueue() {
        SoftKey sk;
        while ((sk = (SoftKey) this.queue.poll()) != null) {
            this.hash.remove(sk);
        }
    }

    public SoftKeyHashMap() {
        this.hash = new HashMap<SoftKey, Object>();
    }

    public SoftKeyHashMap(Map<?, ?> t) {
        this(Math.max(2 * t.size(), 11), 0.75f);
        putAll(t);
    }

    public SoftKeyHashMap(int initialCapacity) {
        this.hash = new HashMap<SoftKey, Object>(initialCapacity);
    }

    public SoftKeyHashMap(int initialCapacity, float loadFactor) {
        this.hash = new HashMap<SoftKey, Object>(initialCapacity, loadFactor);
    }

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.hash.containsKey(SoftKey.create(key));
    }

    @Override
    public Object get(Object key) {
        return this.hash.get(SoftKey.create(key));
    }

    @Override
    public Object put(Object key, Object value) {
        processQueue();
        return this.hash.put(SoftKey.create(key, this.queue), value);
    }

    @Override
    public Object remove(Object key) {
        processQueue();
        return this.hash.remove(SoftKey.create(key));
    }

    @Override
    public void clear() {
        processQueue();
        this.hash.clear();
    }

    @SuppressWarnings("rawtypes")
    private static class Entry implements Map.Entry {

        private Map.Entry myEnt;

        private Object myKey;

        Entry(Map.Entry ent, Object key) {
            this.myEnt = ent;
            this.myKey = key;
        }

        @Override
        public Object getKey() {
            return this.myKey;
        }

        @Override
        public Object getValue() {
            return this.myEnt.getValue();
        }

        @Override
        public Object setValue(Object value) {
            return this.myEnt.setValue(value);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            Object value = getValue();
            return (this.myKey == null ? e.getKey() == null : this.myKey.equals(e.getKey())) && (value == null ? e.getValue() == null : value.equals(e.getValue()));
        }

        @Override
        public int hashCode() {
            Object value = getValue();
            return (((this.myKey == null) ? 0 : this.myKey.hashCode()) ^ ((value == null) ? 0 : value.hashCode()));
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Set entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet();
        }
        return this.entrySet;
    }

    private class EntrySet extends AbstractSet<Object> {

        @SuppressWarnings("rawtypes")
        Set set = SoftKeyHashMap.this.hash.entrySet();

        protected EntrySet() {
        }

        @Override
        public Iterator<Object> iterator() {
            return new Iterator<Object>() {

                Iterator<Object> iter = EntrySet.this.set.iterator();

                Entry next = null;

                @Override
                public boolean hasNext() {
                    while (this.iter.hasNext()) {
                        @SuppressWarnings("rawtypes") Map.Entry ent = (Map.Entry) this.iter.next();
                        SoftKey sk = (SoftKey) ent.getKey();
                        Object k = null;
                        if ((sk != null) && ((k = sk.get()) == null)) {
                            continue;
                        }
                        this.next = new Entry(ent, k);
                        return true;
                    }
                    return false;
                }

                @Override
                public Object next() {
                    if ((this.next == null) && !hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Entry element = this.next;
                    this.next = null;
                    return element;
                }

                @Override
                public void remove() {
                    this.iter.remove();
                }
            };
        }

        @Override
        public boolean isEmpty() {
            return !(iterator().hasNext());
        }

        @Override
        public int size() {
            int size = 0;
            for (Iterator<Object> i = iterator(); i.hasNext(); i.next()) {
                size++;
            }
            return size;
        }

        @Override
        public boolean remove(Object o) {
            processQueue();
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            @SuppressWarnings("rawtypes") Map.Entry e = (Map.Entry) o;
            Object ev = e.getValue();
            SoftKey sk = SoftKey.create(e.getKey());
            Object hv = SoftKeyHashMap.this.hash.get(sk);
            if ((hv == null) ? ((ev == null) && SoftKeyHashMap.this.hash.containsKey(sk)) : hv.equals(ev)) {
                SoftKeyHashMap.this.hash.remove(sk);
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int h = 0;
            for (Iterator<?> i = this.set.iterator(); i.hasNext(); ) {
                @SuppressWarnings("rawtypes") Map.Entry ent = (Map.Entry) i.next();
                SoftKey sk = (SoftKey) ent.getKey();
                Object v;
                if (sk == null) {
                    continue;
                }
                h += (sk.hashCode() ^ (((v = ent.getValue()) == null) ? 0 : v.hashCode()));
            }
            return h;
        }
    }
}
