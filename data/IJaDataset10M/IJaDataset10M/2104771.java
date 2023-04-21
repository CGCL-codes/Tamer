package org.enerj.jga.fn.adaptor;

import org.enerj.jga.fn.BinaryFunctor;
import org.enerj.jga.fn.UnaryFunctor;

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
public class ChainBinary<T1, T2, F, R> extends BinaryFunctor<T1, T2, R> {

    static final long serialVersionUID = -8161448545088932320L;

    private UnaryFunctor<F, R> _f;

    private BinaryFunctor<T1, T2, F> _g;

    /**
     * Builds a ChainBinary functor, given outer functor <b>f</b> and inner
     * functor <b>g</b>.
     * @throws IllegalArgumentException if any of the functors is missing
     */
    public ChainBinary(UnaryFunctor<F, R> f, BinaryFunctor<T1, T2, F> g) {
        if (f == null || g == null) {
            String msg = "Two functors are required";
            throw new IllegalArgumentException(msg);
        }
        _f = f;
        _g = g;
        ;
    }

    /**
     * Returns the outer functor
     * @return the outer functor
     */
    public UnaryFunctor<F, R> getOuterFunctor() {
        return _f;
    }

    /**
     * Returns the inner functor
     * @return the inner functor
     */
    public BinaryFunctor<T1, T2, F> getInnerFunctor() {
        return _g;
    }

    /**
     * Passes arguments <b>x</b> and <b>y</b> to the inner functor, and passes
     * the result to the outer functor.
     * 
     * @return f(g(x,y))
     */
    public R fn(T1 x, T2 y) {
        return _f.fn(_g.fn(x, y));
    }

    /**
     * Calls the Visitor's <code>visit(ChainBinary)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(org.enerj.jga.fn.Visitor v) {
        if (v instanceof ChainBinary.Visitor) ((ChainBinary.Visitor) v).visit(this);
    }

    public String toString() {
        return _f + ".compose(" + _g + ")";
    }

    /**
     * Interface for classes that may interpret a <b>ChainBinary</b> functor.
     */
    public interface Visitor extends org.enerj.jga.fn.Visitor {

        public void visit(ChainBinary host);
    }
}
