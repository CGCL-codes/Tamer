package org.dishevelled.observable;

import java.util.Map;

/**
 * Abstract map entry decorator.
 *
 * @param <K> map entry key type
 * @param <V> map entry value type
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
abstract class AbstractMapEntryDecorator<K, V> implements Map.Entry<K, V> {

    /** Map entry this decorator decorates. */
    private Map.Entry<K, V> entry;

    /**
     * Create a new abstract map entyr that decorates the
     * specified map entry.
     *
     * @param entry map entry to decorate, must not be null
     */
    protected AbstractMapEntryDecorator(final Map.Entry<K, V> entry) {
        if (entry == null) {
            throw new IllegalArgumentException("entry must not be null");
        }
        this.entry = entry;
    }

    /** {@inheritDoc} */
    public K getKey() {
        return entry.getKey();
    }

    /** {@inheritDoc} */
    public V getValue() {
        return entry.getValue();
    }

    /** {@inheritDoc} */
    public V setValue(final V value) {
        return entry.setValue(value);
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return entry.hashCode();
    }

    /** {@inheritDoc} */
    public boolean equals(final Object o) {
        return entry.equals(o);
    }
}
