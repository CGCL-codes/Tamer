package info.absu.snow.pattern;

/**
 * An abstract pair of two values.
 * @author Denys Rtveliashvili
 *
 * @param <K> type of the first value
 * @param <V> type of the second value
 */
public class Pair<K, V> {

    private final K key;

    private final V value;

    /**
	 * Creates a new pair of values
	 * @param key first value / key
	 * @param value second value / value
	 */
    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    /**
	 * @return the first value
	 */
    public K getKey() {
        return key;
    }

    /**
	 * @return the second value
	 */
    public V getValue() {
        return value;
    }
}
