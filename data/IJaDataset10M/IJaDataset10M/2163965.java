package org.xfeep.asura.core.index;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UniqueFieldIndex<T> implements FieldIndex<T> {

    String field;

    ObjectAccessor<T> fieldAccessor;

    ConcurrentHashMap<Object, T> index = new ConcurrentHashMap<Object, T>();

    CopyOnWriteArrayList<IndexEventListener> listeners = new CopyOnWriteArrayList<IndexEventListener>();

    public UniqueFieldIndex(String field, ObjectAccessor<T> fieldAccessor) {
        this.field = field;
        this.fieldAccessor = fieldAccessor;
    }

    public T insert(T o) {
        Object key = fieldAccessor.resolveField(field, o);
        return insert(key, o);
    }

    public boolean isUniqueIndex() {
        return true;
    }

    public List<T> match(Object val) {
        T t = matchSingle(val);
        if (t == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(t);
    }

    public T matchSingle(Object val) {
        return index.get(val);
    }

    public List<T> remove(Object val) {
        T t = index.remove(val);
        if (t == null) {
            return Collections.EMPTY_LIST;
        }
        if (!listeners.isEmpty()) {
            IndexEvent event = new IndexEvent(IndexEvent.REMOVE, t);
            for (IndexEventListener listener : listeners) {
                listener.onIndexChanged(event);
            }
        }
        return Arrays.asList(t);
    }

    public void removeSilent(Object val) {
        index.remove(val);
    }

    public T removeSingle(T o) {
        Object val = fieldAccessor.resolveField(field, o);
        return removeSingle(val, o);
    }

    public void visitAll(IndexedObjectVisitor<T> visitor) {
        for (T t : index.values()) {
            visitor.visit(t);
        }
    }

    public IndexEventListener addEventListener(IndexEventListener listener) {
        listeners.add(listener);
        return listener;
    }

    public IndexEventListener removeEventListener(IndexEventListener listener) {
        return listeners.remove(listener) ? listener : null;
    }

    public boolean isEmpty() {
        return index.isEmpty();
    }

    public T insert(Object val, T o) {
        if (val == null) {
            return null;
        }
        T rt = index.put(val, o);
        if (!listeners.isEmpty()) {
            IndexEvent event = new IndexEvent(IndexEvent.INSERT, o);
            for (IndexEventListener listener : listeners) {
                listener.onIndexChanged(event);
            }
        }
        return rt;
    }

    public T removeSingle(Object val, T o) {
        if (val == null) {
            return null;
        }
        T rt = index.remove(val);
        if (!listeners.isEmpty()) {
            IndexEvent event = new IndexEvent(IndexEvent.REMOVE, rt);
            for (IndexEventListener listener : listeners) {
                listener.onIndexChanged(event);
            }
        }
        return rt;
    }
}
