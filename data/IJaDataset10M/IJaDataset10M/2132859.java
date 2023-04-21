package net.community.chest.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <P>Copyright as per GPLv2</P>
 * <P>Wraps an {@link Enumeration} into an {@link Iterator}</P> 
 * @param <V> Type of parameter being iterated
 * @author Lyor G.
 * @since Nov 14, 2010 2:00:01 PM
 */
public class EnumeratedIterator<V> implements Iterator<V> {

    private Enumeration<V> _enumerator;

    public Enumeration<V> getEnumerator() {
        return _enumerator;
    }

    public void setEnumerator(Enumeration<V> enumerator) {
        _enumerator = enumerator;
    }

    public EnumeratedIterator(Enumeration<V> enumerator) {
        _enumerator = enumerator;
    }

    public EnumeratedIterator() {
        this(null);
    }

    @Override
    public boolean hasNext() {
        final Enumeration<?> e = getEnumerator();
        if ((e != null) && e.hasMoreElements()) return true;
        return false;
    }

    @Override
    public V next() {
        final Enumeration<? extends V> e = getEnumerator();
        if (e == null) throw new NoSuchElementException("No enumerator");
        return e.nextElement();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("N/A");
    }
}
