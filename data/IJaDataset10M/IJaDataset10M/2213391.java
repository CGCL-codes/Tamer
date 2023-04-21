package org.pagger.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Franz Wilhelmstötter
 */
public class EnumerationAdapter<T> implements Enumeration<T> {

    private final Iterator<T> _iterator;

    public EnumerationAdapter(final Iterator<T> iterator) {
        Validator.notNull(iterator, "Iterator");
        _iterator = iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return _iterator.hasNext();
    }

    @Override
    public T nextElement() {
        return _iterator.next();
    }
}
