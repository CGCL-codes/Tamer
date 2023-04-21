package org.simplerrd.impl.util.SLPH;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.simplerrd.impl.util.stream.Streamable;

/**
 * SLPH = SimpleLockablePersistantHashMap
 * @author ddeucher
 *
 */
public class MappedFile implements Map<String, Streamable> {

    private final short processID;

    private final short SLPHVersion;

    private MappedFile() {
        processID = 1;
        SLPHVersion = 100;
    }

    public static MappedFile create(File file) {
        return null;
    }

    public static MappedFile open(File file) {
        return null;
    }

    short getProcessID() {
        return processID;
    }

    short getSLPHVersion() {
        return SLPHVersion;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Set<java.util.Map.Entry<String, Streamable>> entrySet() {
        return null;
    }

    @Override
    public Streamable get(Object key) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Streamable put(String key, Streamable value) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Streamable> m) {
    }

    @Override
    public Streamable remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<Streamable> values() {
        return null;
    }
}
