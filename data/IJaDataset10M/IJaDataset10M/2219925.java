package net.sourceforge.jfunctions.patterns;

import java.util.*;

public class MixinMap<Mixin> implements Set<Mixin> {

    private Map<Class<? extends Mixin>, Mixin> map = new HashMap<Class<? extends Mixin>, Mixin>();

    @SuppressWarnings("unchecked")
    public <T extends Mixin> T get(Class<T> type) {
        return (T) map.get(type);
    }

    public void put(Class<? extends Mixin> type, Mixin value) {
        map.put(type, value);
    }

    public Set<Map.Entry<Class<? extends Mixin>, Mixin>> entrySet() {
        return map.entrySet();
    }

    public Map<Class<? extends Mixin>, Mixin> map() {
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(Mixin e) {
        Class<? extends Mixin> clazz = (Class<? extends Mixin>) e.getClass();
        boolean willChange = map.get(clazz) != e;
        if (!willChange) return false;
        map.put(clazz, e);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Mixin> c) {
        boolean changed = false;
        for (Mixin m : c) {
            changed = changed || add(m);
        }
        return changed;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsValue(o);
    }

    public <T extends Mixin> boolean containsKey(Class<T> type) {
        return map.containsKey(type);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean contains = true;
        for (Object o : c) {
            contains = contains && contains(o);
        }
        return contains;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Iterator<Mixin> iterator() {
        return map.values().iterator();
    }

    @Override
    public boolean remove(Object o) {
        Mixin remove = map.remove(o.getClass());
        return remove != null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;
        for (Object o : c) {
            removed = removed || remove(o);
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Object[] toArray() {
        return map.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return map.values().toArray(a);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof MixinMap)) return false;
        MixinMap other = (MixinMap) obj;
        return map.equals(other.map);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
