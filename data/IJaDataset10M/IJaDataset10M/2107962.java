package org.enerj.jga.fn.algorithm;

import java.util.Iterator;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.jga.fn.adaptor.Constant;
import org.enerj.jga.fn.adaptor.Identity;
import org.enerj.jga.fn.comparison.EqualTo;
import org.enerj.jga.fn.comparison.Equality;
import org.enerj.jga.fn.logical.UnaryNegate;
import org.enerj.jga.util.FilterIterator;
import org.enerj.jga.util.TransformIterator;

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
public class RemoveAll<T> extends UnaryFunctor<Iterator<? extends T>, FilterIterator<T>> {

    static final long serialVersionUID = 8231936204853616237L;

    private UnaryFunctor<T, Boolean> _fn;

    private UnaryFunctor<T, Boolean> _notFn;

    /**
     * Builds an RemoveAll functor that will remove instances of the given value
     * from an iteration.
     * @throws IllegalArgumentException if the test is null
     */
    public RemoveAll(T value) {
        this(new EqualTo<T>().bind2nd(value));
    }

    /**
     * Builds an RemoveAll functor that will remove instances of the given value
     * from an iteration, using the given Equality.
     * @throws IllegalArgumentException if the test is null
     */
    public RemoveAll(Equality<T> eq, T value) {
        this(eq.bind2nd(value));
    }

    /**
     * Builds an RemoveAll functor that will remove elements from an iteration
     * that pass the given test
     * @throws IllegalArgumentException if the test is null
     */
    public RemoveAll(UnaryFunctor<T, Boolean> test) {
        if (test == null) throw new IllegalArgumentException();
        _fn = test;
        _notFn = new UnaryNegate<T>(test);
    }

    /**
     * Returns the functor used to process elements in an iteration.
     */
    public UnaryFunctor<T, Boolean> getFunction() {
        return _fn;
    }

    /**
     * Apply the functor to each element in the iteration and return an iterator
     * over the results
     *
     * @return an iterator over the results of the transformation
     */
    public FilterIterator<T> fn(Iterator<? extends T> iterator) {
        return new FilterIterator<T>(iterator, _notFn);
    }

    /**
     * Calls the Visitor's <code>visit(RemoveAll)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(org.enerj.jga.fn.Visitor v) {
        if (v instanceof RemoveAll.Visitor) ((RemoveAll.Visitor) v).visit(this);
    }

    public String toString() {
        return "RemoveAll[" + _fn + "]";
    }

    /**
     * Interface for classes that may interpret an <b>RemoveAll</b> functor.
     */
    public interface Visitor extends org.enerj.jga.fn.Visitor {

        public void visit(RemoveAll host);
    }
}
