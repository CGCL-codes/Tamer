package org.enerj.jga.fn.adaptor;

import junit.framework.AssertionFailedError;
import org.enerj.jga.SampleUnaryFunctor;
import org.enerj.jga.Samples;
import org.enerj.jga.fn.AbstractVisitor;
import org.enerj.jga.fn.FunctorTest;
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
public class TestConditionalUnary extends FunctorTest<ConditionalUnary<String, String>> {

    public static final String FOO = "_foo_";

    public static final String BAR = "_bar_";

    public static final String BAZ = "_baz_";

    public TestConditionalUnary(String name) {
        super(name);
    }

    private SampleUnaryFunctor<String, Boolean> testPasses = new SampleUnaryFunctor<String, Boolean>(FOO, Boolean.TRUE);

    private SampleUnaryFunctor<String, Boolean> testFails = new SampleUnaryFunctor<String, Boolean>(FOO, Boolean.FALSE);

    private SampleUnaryFunctor<String, String> trueFn = new SampleUnaryFunctor<String, String>(FOO, BAR);

    private SampleUnaryFunctor<String, String> falseFn = new SampleUnaryFunctor<String, String>(FOO, BAZ);

    public void testTruePath() {
        ConditionalUnary<String, String> c = new ConditionalUnary<String, String>(testPasses, trueFn, falseFn);
        assertEquals(BAR, c.fn(FOO));
        assertEquals(FOO, testPasses._got);
        assertEquals(FOO, trueFn._got);
        assertNull(falseFn._got);
    }

    public void testFalsePath() {
        ConditionalUnary<String, String> c = new ConditionalUnary<String, String>(testFails, trueFn, falseFn);
        assertEquals(BAZ, c.fn(FOO));
        assertEquals(FOO, testFails._got);
        assertNull(trueFn._got);
        assertEquals(FOO, falseFn._got);
    }

    public void testSerialization() {
        ConditionalUnary<String, String> c = makeSerial(new ConditionalUnary<String, String>(testPasses, trueFn, falseFn));
        assertEquals(BAR, c.fn(FOO));
        try {
            assertEquals(BAR, c.fn(BAZ));
            String msg = "Expeced AssertionFailed when given BAZ expecting FOO";
            fail(msg);
        } catch (AssertionFailedError x) {
        }
    }

    public void testVisitableInterface() {
        ConditionalUnary<String, String> c = new ConditionalUnary<String, String>(testPasses, trueFn, falseFn);
        TestVisitor tv = new TestVisitor();
        c.accept(tv);
        assertEquals(c, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements ConditionalUnary.Visitor {

        public Object host;

        public void visit(ConditionalUnary host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestConditionalUnary.class);
    }
}
