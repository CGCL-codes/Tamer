package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

public abstract class AbstractUnmodifiableMapDecorator<K, V> extends AbstractMap<K, V> implements Serializable {

    private static final long serialVersionUID = -760336385294119474L;

    protected final Map<K, V> _decorated;

    protected AbstractUnmodifiableMapDecorator(Map<K, V> decorated) {
        _decorated = decorated;
    }

    public Map<K, V> getDecorated() {
        return _decorated;
    }

    @Override
    public boolean containsKey(Object key) {
        return getDecorated().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return getDecorated().containsValue(value);
    }

    @Override
    public V get(Object key) {
        return getDecorated().get(key);
    }

    @Override
    public boolean isEmpty() {
        return getDecorated().isEmpty();
    }

    @Override
    public int size() {
        return getDecorated().size();
    }

    @Override
    public String toString() {
        return getDecorated().toString();
    }
}
