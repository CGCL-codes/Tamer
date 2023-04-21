package indiji.struct;

/**
 * A Tuple.
 * @author Pascal Lehwark
 *
 * @param <K> The Key type
 * @param <V> The value type
 */
public class Tuple<K, V> {

    private K key;

    private V value;

    /**
	 * Create empty tuple.
	 */
    public Tuple() {
        key = null;
        value = null;
    }

    /**
	 * Create tuple of given key and value.
	 * @param k
	 * @param v
	 */
    public Tuple(K k, V v) {
        key = k;
        value = v;
    }

    /**
	 * Get the key.
	 * @return
	 */
    public K getKey() {
        return key;
    }

    /**
	 * Get the value.
	 * @return
	 */
    public V getValue() {
        return value;
    }

    /**
	 * Set the value.
	 * @param val
	 */
    public void setValue(V val) {
        value = (V) val;
    }

    /**
	 * Set the key.
	 * @param k
	 */
    public void setKey(K k) {
        key = k;
    }
}
