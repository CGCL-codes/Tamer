package org.netbeans.core.startup;

import java.util.*;
import org.openide.util.*;
import org.openide.util.lookup.Lookups;

/**
 * A special filtering and lazy iterator used by our XML factories
 *
 * @author Petr Nejedly
 */
class LazyIterator implements Iterator {

    Object first;

    Object step;

    Class template;

    Object skip;

    Iterator delegate;

    LazyIterator(Object first, Class template, Object skip) {
        assert first != null;
        this.first = first;
        this.template = template;
        this.skip = skip;
    }

    public boolean hasNext() {
        if (first != null) return true;
        if (delegate == null) delegate = prepareDelegate();
        if (step != null) return true;
        while (delegate.hasNext() && step == null) {
            Object next = ((Lookup.Item) delegate.next()).getType();
            if (next != skip) step = next;
        }
        return step != null;
    }

    public java.lang.Object next() {
        if (first != null) {
            Object ret = first;
            first = null;
            return ret;
        }
        if (delegate == null) delegate = prepareDelegate();
        if (step != null) {
            Object ret = step;
            step = null;
            return ret;
        }
        while (delegate.hasNext()) {
            Object next = ((Lookup.Item) delegate.next()).getType();
            if (next != skip) return next;
        }
        throw new NoSuchElementException();
    }

    private Iterator prepareDelegate() {
        return Lookup.getDefault().lookup(new Lookup.Template(template)).allItems().iterator();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
