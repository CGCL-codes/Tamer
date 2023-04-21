package net.sourceforge.freejava.collection.scope;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections15.iterators.IteratorChain;

public abstract class CCollection<E> extends _ChainedScope<Collection<E>> implements Collection<E> {

    public CCollection() throws InstantiationException {
        super();
    }

    public CCollection(Collection<E> start) {
        super(start);
    }

    static class Link<El> extends SLink<Collection<El>> implements Set<El> {

        public Link(Collection<El> _this, Collection<El> next) {
            super(_this, next);
        }

        public boolean add(El e) {
            return _this.add(e);
        }

        public boolean addAll(Collection<? extends El> c) {
            return _this.addAll(c);
        }

        public void clear() {
            _this.clear();
        }

        public boolean remove(Object o) {
            return _this.remove(o);
        }

        public boolean removeAll(Collection<?> c) {
            return _this.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return _this.retainAll(c);
        }

        public int size() {
            return _this.size();
        }

        public Object[] toArray() {
            return _this.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return _this.toArray(a);
        }

        public boolean contains(Object o) {
            return _this.contains(o) || next.contains(o);
        }

        public boolean containsAll(Collection<?> c) {
            for (Object o : c) {
                if (!_this.contains(o) && !next.contains(o)) return false;
            }
            return true;
        }

        public boolean isEmpty() {
            return _this.isEmpty() && next.isEmpty();
        }

        public Iterator<El> iterator() {
            Iterator<El> it1 = _this.iterator();
            Iterator<El> it2 = next.iterator();
            return new IteratorChain<El>(it1, it2);
        }
    }

    public boolean add(E e) {
        return head.add(e);
    }

    public boolean addAll(Collection<? extends E> c) {
        return head.addAll(c);
    }

    public void clear() {
        head.clear();
    }

    public boolean contains(Object o) {
        return head.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return head.containsAll(c);
    }

    public boolean isEmpty() {
        return head.isEmpty();
    }

    public Iterator<E> iterator() {
        return head.iterator();
    }

    public boolean remove(Object o) {
        return head.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return head.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return head.retainAll(c);
    }

    public int size() {
        return head.size();
    }

    public Object[] toArray() {
        return head.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return head.toArray(a);
    }
}
