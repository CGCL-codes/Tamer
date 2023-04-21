package jacky.lanlan.song.collection.ref;

import java.io.Serializable;
import java.lang.ref.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class ObservablePhantomHashMap<K, V> implements Map<K, V>, Serializable {

    private static final long serialVersionUID = 4025532675528367624L;

    private final Map<K, PhantomValue<K, V>> hash;

    private final ReferenceQueue<V> refs = new ReferenceQueue<V>();

    private final Cleaner<K, V> callback;

    public ObservablePhantomHashMap(Cleaner<K, V> handler) {
        this(32, handler);
    }

    public ObservablePhantomHashMap(int initSize, Cleaner<K, V> handler) {
        this.hash = new HashMap<K, PhantomValue<K, V>>(initSize);
        this.callback = handler;
    }

    public interface Cleaner<K, V> {

        public void cleanup(K key, V value);
    }

    private void processQueue() {
        while (true) {
            PhantomValue<K, V> ref = (PhantomValue<K, V>) refs.poll();
            if (ref == null) {
                return;
            }
            K key = ref.key;
            V value = ref.get();
            hash.remove(key);
            callback.cleanup(key, value);
            ref.clear();
        }
    }

    public void clear() {
        processQueue();
        hash.clear();
    }

    public boolean containsKey(Object key) {
        processQueue();
        return hash.containsKey(key);
    }

    public boolean containsValue(Object value) {
        processQueue();
        for (V v : values()) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public V get(Object key) {
        processQueue();
        PhantomValue<K, V> ref = hash.get(key);
        if (ref != null) {
            V value = ref.get();
            assert (value != null);
            return value;
        }
        return null;
    }

    public boolean isEmpty() {
        processQueue();
        return hash.isEmpty();
    }

    public Set<K> keySet() {
        processQueue();
        return hash.keySet();
    }

    public V put(K key, V value) {
        processQueue();
        hash.put(key, new PhantomValue<K, V>(key, value, refs));
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    public V remove(Object key) {
        processQueue();
        hash.remove(key);
        return null;
    }

    public int size() {
        processQueue();
        return hash.size();
    }

    public Collection<V> values() {
        processQueue();
        if (hash.isEmpty()) {
            return Collections.<V>emptyList();
        }
        final List<V> c = new LinkedList<V>();
        for (PhantomValue<K, V> value : hash.values()) {
            V v = value.get();
            if (v != null) {
                c.add(v);
            }
        }
        return c;
    }

    public static final class PhantomValue<K, V> extends PhantomReference<V> {

        private static final Field referentField;

        static {
            Class klass = Reference.class;
            try {
                referentField = klass.getDeclaredField("referent");
                referentField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        private final K key;

        public PhantomValue(K key, V referent, ReferenceQueue<? super V> q) {
            super(referent, q);
            this.key = key;
        }

        @Override
        public V get() {
            final Object value;
            try {
                value = referentField.get(this);
            } catch (IllegalArgumentException arge) {
                throw new IllegalStateException(arge);
            } catch (IllegalAccessException acce) {
                throw new IllegalStateException(acce);
            }
            return (V) value;
        }
    }
}
