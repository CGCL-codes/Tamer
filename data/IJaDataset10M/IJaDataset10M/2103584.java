package org.enerj.jga;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.enerj.jga.fn.BinaryFunctor;
import org.enerj.jga.fn.AbstractVisitor;
import org.enerj.jga.fn.Visitable;
import org.enerj.jga.fn.Visitor;

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
public class SampleBinaryFunctor<Arg1Type, Arg2Type, ReturnType> extends BinaryFunctor<Arg1Type, Arg2Type, ReturnType> {

    public Arg1Type _x, _gotX;

    public Arg2Type _y, _gotY;

    public ReturnType _ret;

    public SampleBinaryFunctor(Arg1Type expX, Arg2Type expY, ReturnType ret) {
        _x = expX;
        _y = expY;
        _ret = ret;
    }

    public ReturnType fn(Arg1Type arg1, Arg2Type arg2) {
        Assert.assertEquals(_x, arg1);
        Assert.assertEquals(_y, arg2);
        _gotX = arg1;
        _gotY = arg2;
        return _ret;
    }

    public String toString() {
        return "SampleBinary(" + _x + "," + _y + "," + _ret + ")";
    }
}
