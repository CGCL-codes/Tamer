package blomo.util;

import java.io.Serializable;

/**
 * @author Malte Schulze
 * 
 * Stores two objects as key and value. The value can be null.
 * Two pairs are equal if their keys and values are equal.  
 *
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> implements Serializable {

    private static final long serialVersionUID = 7104451333458317194L;

    protected K key;

    protected V value;

    /**
	 * @param key
	 * @param value
	 */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
	 * @return the key
	 */
    public K getKey() {
        return key;
    }

    /**
	 * @param key
	 */
    public void setKey(K key) {
        this.key = key;
    }

    /**
	 * @return the value
	 */
    public V getValue() {
        return value;
    }

    /**
	 * @param value
	 */
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair<?, ?> p = (Pair<?, ?>) obj;
            if (value == null) return p.getKey().equals(key) && p.getValue() == value;
            return p.getKey().equals(key) && p.getValue().equals(value);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (value == null) return key.hashCode();
        return key.hashCode() + value.hashCode();
    }
}
