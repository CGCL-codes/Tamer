package org.enerj.jga.fn.algorithm;

import java.util.Iterator;
import java.util.Vector;
import org.enerj.jga.Samples;
import org.enerj.jga.fn.AbstractVisitor;
import org.enerj.jga.fn.FunctorTest;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.jga.fn.Visitor;
import org.enerj.jga.fn.comparison.EqualEqual;
import org.enerj.jga.fn.comparison.EqualTo;

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
public class TestFind extends FunctorTest<Find<String>> {

    public static final String FOO = "_foo_";

    public static final String BAR = "_bar_";

    public static final String BAZ = "_baz_";

    public static final String QLX = "_qlx_";

    public static UnaryFunctor<String, Boolean> eqBar = new EqualTo<String>().bind2nd(BAR);

    public TestFind(String name) {
        super(name);
    }

    Vector<String> list = new Vector<String>();

    public void setUp() {
        list.add(FOO);
        list.add(BAR);
        list.add(BAZ);
        list.add(FOO);
        list.add(BAR);
        list.add(BAZ);
    }

    public void tearDown() {
    }

    /**
     * Ensures that searching an empty iteration doesn't fail due to an error
     */
    public void testFindEmptyList() {
        Find<String> finder = new Find<String>(BAR);
        Iterator<? extends String> iter = finder.fn(new Vector<String>().iterator());
        assertTrue(!iter.hasNext());
    }

    /**
     * Ensures that the first instance is found by walking the remainder of the
     * list.
     */
    public void testFindListFirst() {
        Find<String> finder = new Find<String>(BAR);
        Iterator<? extends String> iter = finder.fn(list.iterator());
        assertEquals(BAR, iter.next());
        assertEquals(BAZ, iter.next());
        assertEquals(FOO, iter.next());
        assertEquals(BAR, iter.next());
        assertEquals(BAZ, iter.next());
        assertTrue(!iter.hasNext());
    }

    /**
     * Ensures that the mulitple instances are found, and that the same instance
     * isn't returned multiple times, by walking the remainder of the list.
     */
    public void testFindListTwice() {
        Find<String> finder = new Find<String>(new EqualEqual<String>(), BAR);
        Iterator<? extends String> iter = finder.fn(list.iterator());
        assertEquals(BAR, iter.next());
        iter = finder.fn(iter);
        assertEquals(BAR, iter.next());
        assertEquals(BAZ, iter.next());
        assertTrue(!iter.hasNext());
    }

    /**
     * Ensures no false positives
     */
    public void testFindNotFound() {
        Find<String> finder = new Find<String>(QLX);
        Iterator<? extends String> iter = finder.fn(list.iterator());
        assertTrue(!iter.hasNext());
    }

    /**
     * Ensures that the mulitple instances are found, and that no subsequent
     * false positives occur     
     */
    public void testFindIterTwice() {
        Find<String> finder = new Find<String>(eqBar);
        Iterator<? extends String> iter = finder.fn(list.iterator());
        assertEquals(BAR, iter.next());
        iter = finder.fn(iter);
        assertEquals(BAR, iter.next());
        iter = finder.fn(iter);
        assertTrue(!iter.hasNext());
    }

    public void testSerialization() {
        Find<String> finder = makeSerial(new Find<String>(BAR));
        Iterator<? extends String> iter = finder.fn(new Vector<String>().iterator());
        assertTrue(!iter.hasNext());
    }

    public void testVisitableInterface() {
        Find<String> finder = new Find<String>(BAR);
        TestVisitor tv = new TestVisitor();
        finder.accept(tv);
        assertEquals(finder, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements Find.Visitor {

        public Object host;

        public void visit(Find host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestFind.class);
    }
}
