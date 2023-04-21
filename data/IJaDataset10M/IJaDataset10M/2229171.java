package net.sf.jfpl.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author <a href="mailto:sunguilin@users.sourceforge.net">Guile</a>
 */
public class SingletonIterator<T> implements Iterator<T> {

    private Iterator<T> proxy;

    public SingletonIterator(final T obj) {
        final List<T> list = new ArrayList<T>(1);
        list.add(obj);
        proxy = list.iterator();
    }

    public boolean hasNext() {
        return proxy.hasNext();
    }

    public T next() {
        return proxy.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
