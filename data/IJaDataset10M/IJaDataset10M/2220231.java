package org.enerj.jga.fn.algorithm;

import java.util.Iterator;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.jga.fn.adaptor.ConditionalUnary;
import org.enerj.jga.fn.adaptor.ConstantUnary;
import org.enerj.jga.util.TransformIterator;
import org.enerj.jga.fn.adaptor.Identity;

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
public class ReplaceAll<T> extends UnaryFunctor<Iterator<? extends T>, TransformIterator<T, T>> {

    static final long serialVersionUID = -6156225521195687370L;

    private UnaryFunctor<T, T> _fn;

    private T _value;

    /**
     * Builds an ReplaceAll functor that will apply the given test to
     * elements in an iteration, and replace those elements that pass with the
     * given value.
     * @throws IllegalArgumentException if the test is null
     */
    public ReplaceAll(UnaryFunctor<T, Boolean> test, T value) {
        if (test == null) throw new IllegalArgumentException();
        _value = value;
        _fn = new ConditionalUnary<T, T>(test, new ConstantUnary<T, T>(value), new Identity<T>());
    }

    /**
     * Returns the functor used to process elements in an iteration.
     */
    public UnaryFunctor<T, T> getFunction() {
        return _fn;
    }

    /**
     * Returns the replacement value
     */
    public T getValue() {
        return _value;
    }

    /**
     * Apply the functor to each element in the iteration and return an iterator
     * over the results
     *
     * @return an iterator over the results of the transformation
     */
    public TransformIterator<T, T> fn(Iterator<? extends T> iterator) {
        return new TransformIterator<T, T>(iterator, _fn);
    }

    /**
     * Calls the Visitor's <code>visit(ReplaceAll)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(org.enerj.jga.fn.Visitor v) {
        if (v instanceof ReplaceAll.Visitor) ((ReplaceAll.Visitor) v).visit(this);
    }

    public String toString() {
        return "ReplaceAll";
    }

    /**
     * Interface for classes that may interpret an <b>ReplaceAll</b> functor.
     */
    public interface Visitor extends org.enerj.jga.fn.Visitor {

        public void visit(ReplaceAll host);
    }
}
