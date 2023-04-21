package lt.ktu.scheduler.base.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import lt.ktu.scheduler.base.CollectionPart;

public class BaseCollection<S extends CollectionPart> implements Collection<S>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4747779262941153094L;

    protected List<S> data = new ArrayList<S>();

    public S get(int index) {
        return data.get(index);
    }

    public boolean add(S o) {
        boolean ret = data.add(o);
        o.setCollection(this);
        return ret;
    }

    public boolean addAll(Collection<? extends S> c) {
        return data.addAll(c);
    }

    public void clear() {
        data.clear();
    }

    public boolean contains(Object o) {
        return data.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public Iterator<S> iterator() {
        return data.iterator();
    }

    public boolean remove(Object o) {
        return data.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return data.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return data.retainAll(c);
    }

    public int size() {
        return data.size();
    }

    public Object[] toArray() {
        return data.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return data.toArray(a);
    }
}
