package skycastle.util.listenable.map.validator;

/**
 * Represents an entry that should be validated with a MapValidator. Has getter and setters for the key and value.
 *
 * @author Hans Häggström
 */
public interface ValidatedMapEntry<K, V> {

    void setEntry(K key, V value);

    K getKey();

    void setKey(K key);

    V getValue();

    void setValue(V value);
}
