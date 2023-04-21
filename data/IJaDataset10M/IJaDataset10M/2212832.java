package org.enerj.jga.fn.adaptor;

import org.enerj.jga.fn.Generator;
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
public class Generate<T, R> extends Generator<R> {

    static final long serialVersionUID = -7866951985301046565L;

    private UnaryFunctor<T, R> _fn;

    private Generator<T> _gen;

    public Generate(UnaryFunctor<T, R> fn, Generator<T> gen) {
        _fn = fn;
        _gen = gen;
    }

    /**
     * Returns the UnaryFunctor that is applied to values returned by the
     * nested Generator.
     */
    public UnaryFunctor<T, R> getFunctor() {
        return _fn;
    }

    /**
     * Returns the nested Generator;
     */
    public Generator<T> getGenerator() {
        return _gen;
    }

    /**
     * Returns the results of the nested generator as modified by the functor.
     */
    public R gen() {
        return _fn.fn(_gen.gen());
    }

    /**
     * Calls the Visitor's <code>visit(Generate)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(org.enerj.jga.fn.Visitor v) {
        if (v instanceof Generate.Visitor) ((Generate.Visitor) v).visit(this);
    }

    public String toString() {
        return _fn + ".generate(" + _gen + ")";
    }

    /**
     * Interface for classes that may interpret a <b>Generate</b>
     * functor.
     */
    public interface Visitor extends org.enerj.jga.fn.Visitor {

        public void visit(Generate host);
    }
}
