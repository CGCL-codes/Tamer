package org.enerj.jga.fn.comparison;

import java.util.Comparator;
import org.enerj.jga.fn.BinaryFunctor;
import org.enerj.jga.util.ComparableComparator;

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
public class Max<T> extends BinaryFunctor<T, T, T> {

    static final long serialVersionUID = -8414037855280091672L;

    private Comparator<? super T> _comp;

    /**
     * Builds a Max functor using the given Comparator
     * @throws IllegalArgumentException if the argument is null
     */
    public Max(Comparator<? super T> comp) {
        if (comp == null) {
            throw new IllegalArgumentException("Comparator may not be null");
        }
        _comp = comp;
    }

    /**
     * Returns the comparator in use by this functor
     * @return the comparator in use by this functor
     */
    public Comparator<? super T> getComparator() {
        return _comp;
    }

    /**
     * Returns the greater of two arguments, or the first if they are equal.
     * @return the greater of two arguments, or the first if they are equal.
     */
    public T fn(T x, T y) {
        return _comp.compare(x, y) >= 0 ? x : y;
    }

    /**
     * Calls the Visitor's <code>visit(Max)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(org.enerj.jga.fn.Visitor v) {
        if (v instanceof Max.Visitor) ((Max.Visitor) v).visit(this);
    }

    public String toString() {
        return "Max";
    }

    /**
     * Interface for classes that may interpret a <b>Max</b> predicate.
     */
    public interface Visitor extends org.enerj.jga.fn.Visitor {

        public void visit(Max host);
    }

    /**
     * Max functor for use with Comparable arguments.  This class exists
     * as an implementation detail that works around a limit in the javac
     * inferencer -- in all substantive ways, this is simply a Max functor.
     */
    public static class Comparable<T extends java.lang.Comparable<? super T>> extends Max<T> {

        static final long serialVersionUID = 2442624792379403532L;

        public Comparable() {
            super(new ComparableComparator<T>());
        }
    }
}
