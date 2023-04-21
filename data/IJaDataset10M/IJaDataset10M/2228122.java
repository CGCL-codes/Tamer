package net.sf.eBus.util;

import java.io.Serializable;

/**
 * This {@link MultiKey} subclass uses generics to guarantee
 * key type and order. {@link MultiKey} is unable to provide
 * this type safety.
 * @param <K1> The first key.
 * @param <K2> The second key.
 * @param <K3> The third key.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public class MultiKey3<K1, K2, K3> extends MultiKey implements Serializable {

    /**
     * Creates a three value {@link MultiKey}. 
     * @param key1 the first key.
     * @param key2 the second key.
     * @param key3 the third key.
     */
    public MultiKey3(final K1 key1, final K2 key2, final K3 key3) {
        super(key1, key2, key3);
    }

    /**
     *  This is eBus version 2.1.0.
     */
    private static final long serialVersionUID = 0x020100L;
}
