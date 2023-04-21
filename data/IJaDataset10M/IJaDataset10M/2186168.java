package net.sf.molae.pipe.trafo;

import java.util.Map.Entry;
import net.sf.molae.pipe.basic.AbstractMapEntry;

/**
 * A map entry with a transformed value.
 * @since 1.1
 * @version 2.0
 * @author Ralph Wagner
 */
public final class ValueTransformedMapEntry<K, S, V> extends AbstractMapEntry<K, V> {

    private final Entry<K, S> base;

    private final TwoWayTransformer<S, V> transformer;

    /**
     * Constructs a ValueTransformedMapEntry object.
     * @param base base map entry
     * @param transformer used transformer
     * @throws NullPointerException if any of the specified objects is
     * <code>null</code>.
     */
    public ValueTransformedMapEntry(Entry<K, S> base, TwoWayTransformer<S, V> transformer) {
        super(base.getKey());
        this.base = base;
        this.transformer = transformer;
    }

    public V getValue() {
        return transformer.transform(base.getValue());
    }

    /**
     * Replaces the value corresponding to this entry with the specified value.
     * @param value new value to be stored in this entry
     * @return old value corresponding to the entry.
     * @throws UnsupportedOperationException if the <code>setValue</code>
     *  operation is not supported by the backing map entry.
     * @throws ClassCastException depending on the base entry
     *  and the transformer
     * @throws IllegalArgumentException depending on the base entry
     *  and the transformer
     * @throws NullPointerException depending on the base entry
     *  and the transformer
     * @see #getValue()
     */
    public V setValue(V value) {
        return transformer.transform(base.setValue(transformer.retransform(value)));
    }
}
