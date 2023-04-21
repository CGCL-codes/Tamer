package net.sf.gilead.loading.proxy.wrapper;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import net.sf.gilead.loading.proxy.LoadingProxyManager;

/**
 * Wrapper for Set collection
 * @author bruno.marchesson
 *
 */
public class LoadingSet<PROXY, PERSISTENT> {

    /**
	 * The wrapped set
	 */
    private Set<PERSISTENT> _wrapped;

    /**
	 * The proxy class
	 */
    private Class<?> _proxyClass;

    /**
	 * Constructor
	 */
    public LoadingSet(Set<PERSISTENT> wrapped) {
        _wrapped = wrapped;
        _proxyClass = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public boolean add(PROXY arg0) {
        return _wrapped.add(unwrapLoadingInterface(arg0));
    }

    public boolean addAll(Collection<? extends PROXY> arg0) {
        boolean changed = false;
        for (PROXY proxy : arg0) {
            if (_wrapped.add(unwrapLoadingInterface(proxy))) {
                changed = true;
            }
        }
        return changed;
    }

    public void clear() {
        _wrapped.clear();
    }

    public boolean contains(Object arg0) {
        return _wrapped.contains(unwrap(arg0));
    }

    public boolean containsAll(Collection<?> arg0) {
        return _wrapped.containsAll(arg0);
    }

    public boolean equals(Object arg0) {
        return _wrapped.equals(arg0);
    }

    public int hashCode() {
        return _wrapped.hashCode();
    }

    public boolean isEmpty() {
        return _wrapped.isEmpty();
    }

    public Iterator<PROXY> iterator() {
        return new LoadingIterator<PROXY, PERSISTENT>(_wrapped.iterator());
    }

    public boolean remove(Object arg0) {
        return _wrapped.remove(unwrap(arg0));
    }

    public boolean removeAll(Collection<?> arg0) {
        return _wrapped.removeAll(arg0);
    }

    public boolean retainAll(Collection<?> arg0) {
        return _wrapped.retainAll(arg0);
    }

    public int size() {
        return _wrapped.size();
    }

    public Object[] toArray() {
        Object[] copy = (Object[]) java.lang.reflect.Array.newInstance(_proxyClass, _wrapped.size());
        int index = 0;
        for (PERSISTENT persistent : _wrapped) {
            copy[index++] = wrap(persistent);
        }
        return copy;
    }

    public <T> T[] toArray(T[] arg0) {
        return _wrapped.toArray(arg0);
    }

    /**
	 * Wrap persistent item in a proxy
	 */
    @SuppressWarnings("unchecked")
    private PROXY wrap(PERSISTENT item) {
        return (PROXY) LoadingProxyManager.getInstance().wrapAs(item, _proxyClass);
    }

    /**
	 * Get the underlying persistent item from a proxy
	 */
    @SuppressWarnings("unchecked")
    private PERSISTENT unwrapLoadingInterface(Object item) {
        if (item instanceof LoadingWrapper) {
            return (PERSISTENT) ((LoadingWrapper) item).getData();
        } else {
            return (PERSISTENT) item;
        }
    }

    /**
	 * Get the underlying persistent item from a proxy
	 */
    @SuppressWarnings("unchecked")
    private Object unwrap(Object item) {
        if (item instanceof LoadingWrapper) {
            return ((LoadingWrapper) item).getData();
        } else {
            return item;
        }
    }
}
