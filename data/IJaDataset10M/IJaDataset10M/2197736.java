package org.enerj.jga.util;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/*******************************************************************************
 * Copyright 2000, 2006 Visual Systems Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License version 2
 * which accompanies this distribution in a file named "COPYING".
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *      
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *      
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *******************************************************************************/
public class ArrayIterator<T> implements ListIterator<T>, Iterable<T> {

    private T[] _array;

    private int _idx;

    private Boolean _goingForward;

    /**
     * Builds an ArrayIterator for the given array
     */
    public ArrayIterator(T[] array) {
        _array = array;
    }

    /**
     * 
     */
    public Iterator<T> iterator() {
        return this;
    }

    public boolean hasNext() {
        return _idx < _array.length;
    }

    public boolean hasPrevious() {
        return _idx > 0;
    }

    public int nextIndex() {
        return _idx;
    }

    public int previousIndex() {
        return _idx - 1;
    }

    public T next() {
        if (_idx >= _array.length) throw new NoSuchElementException();
        _goingForward = Boolean.TRUE;
        return _array[_idx++];
    }

    public T previous() {
        if (_idx <= 0) throw new NoSuchElementException();
        _goingForward = Boolean.FALSE;
        return _array[--_idx];
    }

    public void set(T value) {
        if (_goingForward == null) throw new IllegalStateException();
        if (_goingForward) _array[_idx - 1] = value; else _array[_idx] = value;
    }

    public void add(T value) {
        _goingForward = null;
        throw new UnsupportedOperationException();
    }

    public void remove() {
        _goingForward = null;
        throw new UnsupportedOperationException();
    }

    /**
     * Produce an ArrayIterator over the given array.
     */
    public static <E> ArrayIterator<E> iterate(E[] arr) {
        return new ArrayIterator<E>(arr);
    }
}
