package x10me.util;

/**
 * A hash map with entirely immutable buckets. It doesn't correctly support
 * remove, and its values cannot be mutated by a put with the same key - use
 * with care.
 */
public final class ImmutableEntryHashMapRVM<K, V> extends AbstractHashMapRVM<K, V> {

    static final class Bucket<K, V> extends AbstractBucket<K, V> {

        private final AbstractBucket<K, V> next;

        private final K key;

        private final V value;

        Bucket(K k, V v, AbstractBucket<K, V> n) {
            key = k;
            value = v;
            next = n;
        }

        AbstractBucket<K, V> getNext() {
            return next;
        }

        AbstractBucket<K, V> setNext(AbstractBucket<K, V> n) {
            if (next == n) {
                return this;
            } else {
                return new Bucket<K, V>(key, value, n);
            }
        }

        K getKey() {
            return key;
        }

        V getValue() {
            return value;
        }

        void setValue(V v) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    AbstractBucket<K, V> createNewBucket(K key, V value, AbstractBucket<K, V> next) {
        return new Bucket<K, V>(key, value, next);
    }

    public ImmutableEntryHashMapRVM() {
        super(DEFAULT_SIZE);
    }

    public ImmutableEntryHashMapRVM(int size) {
        super(size);
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean same(K k1, K k2) {
        return k1.equals(k2);
    }

    @Override
    protected int hashTheKey(K key) {
        return key.hashCode();
    }
}
