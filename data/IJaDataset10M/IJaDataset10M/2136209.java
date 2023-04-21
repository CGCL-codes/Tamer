package com.hp.hpl.jena.util.iterator;

import java.util.Iterator;

/**
    A subclass of FilterIterator which keeps the elements that pass the
    test.
    @author kers
*/
public class FilterKeepIterator extends FilterIterator implements Iterator {

    public FilterKeepIterator(Filter f, Iterator it) {
        super(f, it);
    }

    protected boolean accept(Object x) {
        return f.accept(x);
    }
}
