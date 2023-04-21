package lazyj.cache;

import java.util.Collection;

/**
 * Similar to {@link ReversableUniqueCache}, a cache class implementing this interface can answer to request like
 * "give me the keys that have this value associated to them". It should be used when for example a key has a Collection
 * of associated values, one object can be part of several such entries. 
 * 
 * @author costing
 * @param <K> key type
 * @param <V> value type
 * @since 2007-03-11
 */
public interface ReversableMultivaluesCache<K, V> {

    /**
	 * @param value
	 * @return all the keys that have this value associated with them
	 */
    public Collection<K> getKeysForValue(V value);
}
