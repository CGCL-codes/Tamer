package org.jikesrvm.util;

import org.jikesrvm.runtime.Magic;
import org.jikesrvm.util.HashMapRVM.Bucket;

/**
 * The same as {@link HashMapRVM} except object identities determine equality
 * not the equals method.
 */
public final class IdentityHashMapRVM<K, V> extends AbstractHashMapRVM<K, V> {

    @Override
    boolean same(K k1, K k2) {
        return k1 == k2;
    }

    @Override
    protected int hashTheKey(K key) {
        if (!org.jikesrvm.VM.runningVM) {
            return Magic.bootImageIdentityHashCode(key);
        } else {
            return System.identityHashCode(key);
        }
    }

    @Override
    AbstractBucket<K, V> createNewBucket(K key, V value, AbstractBucket<K, V> next) {
        return new Bucket<K, V>(key, value, next);
    }

    public IdentityHashMapRVM() {
        super(DEFAULT_SIZE);
    }

    public IdentityHashMapRVM(int size) {
        super(size);
    }
}
