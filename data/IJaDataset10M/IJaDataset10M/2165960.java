package net.sf.kerner.commons.collection.impl;

import java.util.Map.Entry;

/**
 * Simple Key-Value mapping.
 * <p>
 * {@code key} may not be {@code null}; {@code value} may be {@code null}.
 * </p>
 * 
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-09-13
 * @threadSave
 * 
 * @param <K>
 *            type of {@code key}
 * @param <V>
 *            type of {@code value}
 */
public class KeyValue<K, V> implements Entry<K, V> {

    /**
	 * 
	 */
    private final K key;

    /**
	 * 
	 */
    private volatile V value;

    /**
	 * 
	 * 
	 * Create a new {@code KeyValue} object, using given key and value.
	 * 
	 * @param key
	 *            key for this key-value-mapping
	 * @param value
	 *            value for this key-value-mapping
	 * @throws NullPointerException
	 *             if given {@code key} is {@code null}
	 */
    public KeyValue(K key, V value) {
        if (key == null) throw new NullPointerException("key must not be null");
        this.key = key;
        this.value = value;
    }

    /**
	 * 
	 * 
	 * Create a new {@code KeyValue} object, using given key.
	 * 
	 * @param key
	 *            key for this key-value-mapping
	 * @throws NullPointerException
	 *             if given {@code key} is {@code null}
	 */
    public KeyValue(K key) {
        if (key == null) throw new NullPointerException("key must not be null");
        this.key = key;
        this.value = null;
    }

    /**
	 * 
	 * 
	 * Create a new {@code KeyValue} object, using given {@code KeyValue} as a
	 * template.
	 * 
	 * @param template
	 *            {@code KeyValue} template
	 */
    public KeyValue(KeyValue<K, V> template) {
        this.key = template.getKey();
        this.value = template.getValue();
    }

    /**
	 * Return the key for this key-value-mapping
	 */
    public K getKey() {
        return key;
    }

    /**
	 * Return the value for this key-value-mapping
	 */
    public V getValue() {
        return value;
    }

    /**
	 * Set the value for this key-value-mapping, return the previous value
	 * mapped by this key-value-mapping
	 */
    public V setValue(V value) {
        final V result = this.value;
        this.value = value;
        return result;
    }
}
