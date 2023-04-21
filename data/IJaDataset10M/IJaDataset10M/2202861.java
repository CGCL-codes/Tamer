package jp.ac.osaka_u.ist.sel.metricstool.main.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ConcurrentHashMap}��p��������Set
 * 
 * @author kou-tngt
 *
 * @param <T> �Z�b�g�̗v�f�̌^
 * @see Set
 */
public class ConcurrentHashSet<T> implements Set<T> {

    public boolean add(final T o) {
        return null == this.INNER_MAP.put(o, DUMMY_VALUE);
    }

    public boolean addAll(final Collection<? extends T> c) {
        boolean result = false;
        for (final T element : c) {
            if (this.add(element)) {
                result = true;
            }
        }
        return result;
    }

    public void clear() {
        this.INNER_MAP.clear();
    }

    public boolean contains(final Object o) {
        return this.INNER_MAP.containsKey(o);
    }

    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return this.INNER_MAP.isEmpty();
    }

    public Iterator<T> iterator() {
        return this.INNER_MAP.keySet().iterator();
    }

    public boolean remove(final Object o) {
        return null != this.INNER_MAP.remove(o);
    }

    public boolean removeAll(final Collection<?> c) {
        boolean result = false;
        for (final Object o : c) {
            if (this.remove(o)) {
                result = true;
            }
        }
        return result;
    }

    public boolean retainAll(final Collection<?> c) {
        boolean result = false;
        for (final Iterator<T> it = this.INNER_MAP.keySet().iterator(); it.hasNext(); ) {
            final T key = it.next();
            if (!c.contains(key)) {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    public int size() {
        return this.INNER_MAP.size();
    }

    public Object[] toArray() {
        return this.INNER_MAP.keySet().toArray();
    }

    public <K> K[] toArray(final K[] a) {
        return this.INNER_MAP.keySet().toArray(a);
    }

    /**
     * �}�b�v�̒l�ɓ���_�~�[�I�u�W�F�N�g.
     */
    private static final Object DUMMY_VALUE = new Object();

    /**
     * �����ŗ��p����n�b�V���}�b�v
     */
    private final Map<T, Object> INNER_MAP = new ConcurrentHashMap<T, Object>();
}
