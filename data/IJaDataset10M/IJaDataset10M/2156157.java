package com.orientechnologies.jdo;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 *
 * @author
 * @version 1.0
 */
public class oIterator implements Iterator {

    public oIterator(oResultSet iResultSet) {
        result = iResultSet;
        cursor = -1;
    }

    public boolean hasNext() {
        if (cursor >= result.size() - 1) return false;
        return true;
    }

    public Object next() {
        if (cursor >= result.size() - 1) throw new NoSuchElementException();
        return result.get(++cursor);
    }

    public boolean hasPrevious() {
        if (cursor <= 0) return false;
        return true;
    }

    public Object previous() {
        if (cursor <= 0) throw new NoSuchElementException();
        return result.get(--cursor);
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException("Method remove() not yet implemented.");
    }

    private oResultSet result;

    private int cursor;
}
