package org.enerj.jga.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.enerj.jga.fn.BinaryFunctor;

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
public class TransformBinaryIterator<T1, T2, R> implements Iterator<R>, Iterable<R> {

    private Iterator<? extends T1> _i1;

    private Iterator<? extends T2> _i2;

    private BinaryFunctor<T1, T2, R> _bf;

    /**
     * Builds a TransformBinaryIterator that applies the given functor to
     * corresponding elements of the given base iterators.
     */
    public TransformBinaryIterator(Iterator<? extends T1> i1, Iterator<? extends T2> i2, BinaryFunctor<T1, T2, R> bf) {
        _i1 = i1;
        _i2 = i2;
        _bf = bf;
    }

    public Iterator<R> iterator() {
        return this;
    }

    public boolean hasNext() {
        return _i1.hasNext() && _i2.hasNext();
    }

    public R next() {
        return _bf.fn(_i1.next(), _i2.next());
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
