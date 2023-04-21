package net.sf.jctc.common.ldap.resolver;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 * <p>
 * Type: <strong><code>net.sf.jctc.common.ldap.resolver.ResolvingDictionary</code></strong>
 * </p>
 *
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public class ResolvingDictionary<K, V> extends Dictionary<K, V> {

    Dictionary<K, V> dictionary;

    private Resolver<V> resolver;

    /**
	 * 
	 */
    public ResolvingDictionary(Dictionary<K, V> dictionary, Resolver<V> resolver) {
        this.dictionary = dictionary;
        this.resolver = resolver;
    }

    public Enumeration<V> elements() {
        return dictionary.elements();
    }

    public boolean equals(Object obj) {
        return dictionary.equals(obj);
    }

    public V get(Object key) {
        return (V) resolver.resolve(dictionary.get(key));
    }

    public int hashCode() {
        return dictionary.hashCode();
    }

    public boolean isEmpty() {
        return dictionary.isEmpty();
    }

    public Enumeration<K> keys() {
        return dictionary.keys();
    }

    public V put(K key, V value) {
        return dictionary.put(key, value);
    }

    public V remove(Object key) {
        return dictionary.remove(key);
    }

    public int size() {
        return dictionary.size();
    }

    public String toString() {
        return dictionary.toString();
    }
}
