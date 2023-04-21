package commons.utils;

import java.util.Iterator;
import java.util.Set;
import javolution.util.FastMap;
import javolution.util.FastCollection.Record;

@SuppressWarnings("unchecked")
public class AEFastSet<E> extends AEFastCollection<E> implements Set<E> {

    private static final Object NULL = new Object();

    private final FastMap<E, Object> map;

    public AEFastSet() {
        map = new FastMap<E, Object>();
    }

    public AEFastSet(int capacity) {
        map = new FastMap<E, Object>(capacity);
    }

    public AEFastSet(Set<? extends E> elements) {
        map = new FastMap<E, Object>(elements.size());
        addAll(elements);
    }

    public boolean isShared() {
        return map.isShared();
    }

    @Override
    public Record head() {
        return map.head();
    }

    @Override
    public Record tail() {
        return map.tail();
    }

    @Override
    public E valueOf(Record record) {
        return ((FastMap.Entry<E, Object>) record).getKey();
    }

    @Override
    public void delete(Record record) {
        map.remove(((FastMap.Entry<E, Object>) record).getKey());
    }

    @Override
    public void delete(Record record, E value) {
        map.remove(value);
    }

    public boolean add(E value) {
        return map.put(value, NULL) == null;
    }

    public void clear() {
        map.clear();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return super.toString() + "-" + map.keySet().toString();
    }
}
