package gpl.lonelysingleton.sleepwalker.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NoNullListSet extends AbstractList implements Set {

    public static final Object NULL_ELEMENT = null;

    protected List Elements;

    public NoNullListSet() {
        super();
        Elements = Collections.synchronizedList(new ArrayList());
    }

    public NoNullListSet(final int initialCapacity_P) {
        super();
        Elements = Collections.synchronizedList(new ArrayList(initialCapacity_P));
    }

    public NoNullListSet(final Collection Collection_P) {
        Object NextElement_L;
        Elements = Collections.synchronizedList(new ArrayList());
        synchronized (Collection_P) {
            for (Iterator Iter_L = Collection_P.iterator(); Iter_L.hasNext(); ) {
                NextElement_L = Iter_L.next();
                if (isPermittedElement(NextElement_L)) {
                    Elements.add(NextElement_L);
                }
            }
        }
    }

    public void add(final int index_P, final Object NewElement_P) {
        if (isPermittedElement(NewElement_P)) {
            Elements.add(index_P, NewElement_P);
        }
    }

    public boolean addAll(Collection Collection_P) {
        boolean wasModified_L;
        Object NextElement_L;
        wasModified_L = false;
        synchronized (Collection_P) {
            for (Iterator Iter_L = Collection_P.iterator(); Iter_L.hasNext(); ) {
                NextElement_L = Iter_L.next();
                if (isPermittedElement(NextElement_L)) {
                    Elements.add(NextElement_L);
                    wasModified_L = true;
                }
            }
        }
        return wasModified_L;
    }

    public Object remove(final int index_P) {
        return Elements.remove(index_P);
    }

    public Object get(final int index_P) {
        return Elements.get(index_P);
    }

    public Object set(final int index_P, final Object NewElement_P) {
        final Object OLD_ELEMENT;
        OLD_ELEMENT = Elements.get(index_P);
        Elements.remove(OLD_ELEMENT);
        if (isPermittedElement(NewElement_P)) {
            Elements.add(index_P, NewElement_P);
        }
        return OLD_ELEMENT;
    }

    public int size() {
        return Elements.size();
    }

    protected boolean isPermittedElement(final Object Element_P) {
        if ((Element_P == NULL_ELEMENT) || (Elements.contains(Element_P))) {
            return false;
        } else {
            return true;
        }
    }
}
