package org.enerj.jga.fn.adaptor;

import java.io.IOException;
import org.enerj.jga.SampleBinaryFunctor;
import org.enerj.jga.fn.AbstractVisitor;
import org.enerj.jga.fn.BinaryFunctor;
import org.enerj.jga.fn.FunctorTest;

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
public class TestAndBinary extends FunctorTest<AndBinary> {

    public TestAndBinary(String name) {
        super(name);
    }

    public static final String FOO = "_foo_";

    public static final String BAR = "_bar_";

    SampleBinaryFunctor<String, String, Boolean> bfTrue = new SampleBinaryFunctor<String, String, Boolean>(FOO, BAR, Boolean.TRUE);

    SampleBinaryFunctor<String, String, Boolean> bfFalse = new SampleBinaryFunctor<String, String, Boolean>(FOO, BAR, Boolean.FALSE);

    SampleBinaryFunctor<String, String, Boolean> bfNull = new SampleBinaryFunctor<String, String, Boolean>(FOO, BAR, null);

    SampleBinaryFunctor<String, String, Boolean> bfStillTrue = new SampleBinaryFunctor<String, String, Boolean>(FOO, BAR, Boolean.TRUE);

    private AndBinary<String, String> makeFunctor(BinaryFunctor<String, String, Boolean> bf1, BinaryFunctor<String, String, Boolean> bf2) {
        return new AndBinary<String, String>(bf1, bf2);
    }

    public void testFunctorInterface0() {
        AndBinary<String, String> pred = makeFunctor(bfTrue, bfFalse);
        assertEquals(Boolean.FALSE, pred.fn(FOO, BAR));
        assertEquals(FOO, bfTrue._gotX);
        assertEquals(BAR, bfTrue._gotY);
        assertEquals(FOO, bfFalse._gotX);
        assertEquals(BAR, bfFalse._gotY);
    }

    public void testFunctorInterface1() {
        AndBinary<String, String> pred = makeFunctor(bfFalse, bfNull);
        assertEquals(Boolean.FALSE, pred.fn(FOO, BAR));
        assertNull(bfNull._gotX);
        assertNull(bfNull._gotY);
        assertEquals(FOO, bfFalse._gotX);
        assertEquals(BAR, bfFalse._gotY);
    }

    public void testFunctorInterface2() {
        AndBinary<String, String> pred = makeFunctor(bfTrue, bfStillTrue);
        assertEquals(Boolean.TRUE, pred.fn(FOO, BAR));
        assertEquals(FOO, bfTrue._gotX);
        assertEquals(BAR, bfTrue._gotY);
        assertEquals(FOO, bfStillTrue._gotX);
        assertEquals(BAR, bfStillTrue._gotY);
    }

    public void testPredicateInterface0() {
        AndBinary<String, String> pred = makeFunctor(bfTrue, bfFalse);
        assertTrue(!pred.p(FOO, BAR));
        assertEquals(FOO, bfTrue._gotX);
        assertEquals(BAR, bfTrue._gotY);
        assertEquals(FOO, bfFalse._gotX);
        assertEquals(BAR, bfFalse._gotY);
    }

    public void testPredicateInterface1() {
        AndBinary<String, String> pred = makeFunctor(bfFalse, bfNull);
        assertTrue(!pred.p(FOO, BAR));
        assertEquals(FOO, bfFalse._gotX);
        assertEquals(BAR, bfFalse._gotY);
        assertNull(bfNull._gotX);
        assertNull(bfNull._gotY);
    }

    public void testPredicateInterface2() {
        AndBinary<String, String> pred = makeFunctor(bfTrue, bfStillTrue);
        assertTrue(pred.p(FOO, BAR));
        assertEquals(FOO, bfTrue._gotX);
        assertEquals(BAR, bfTrue._gotY);
        assertEquals(FOO, bfStillTrue._gotX);
        assertEquals(BAR, bfStillTrue._gotY);
    }

    public void testSerializedFunctor() {
        AndBinary<String, String> pred = makeSerial(makeFunctor(bfTrue, bfFalse));
        assertEquals(Boolean.FALSE, pred.fn(FOO, BAR));
    }

    public void testVisitableInterface() {
        TestVisitor tv = new TestVisitor();
        AndBinary<String, String> pred = makeFunctor(bfTrue, bfFalse);
        pred.accept(tv);
        assertEquals(pred, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements AndBinary.Visitor {

        public Object host;

        public void visit(AndBinary host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestAndBinary.class);
    }
}
