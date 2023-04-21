package org.jsmtpd.tools.cache;

/**
 * Cache things
 * @author jean-francois POUX
 *
 * @param <Key>
 * @param <Value>
 */
public interface ICache<Key, Value> {

    /**
     * get something from cache
     * @param k key
     * @return
     * @throws CacheFaultException when not found
     */
    public Value get(Key k);

    /**
     * Cache something
     * @param key
     * @param value
     */
    public void cache(Key key, Value value);

    /**
     * Clears the entire cache
     *
     */
    public void clear();

    /**
     * Expires a cached item
     * @param k
     */
    public void destroy(Key k);
}
