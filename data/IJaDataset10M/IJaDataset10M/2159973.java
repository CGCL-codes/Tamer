package org.enerj.jga.util;

import java.util.NoSuchElementException;
import java.util.Vector;
import junit.framework.TestCase;
import org.enerj.jga.Samples;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.jga.fn.Visitor;
import org.enerj.jga.fn.comparison.EqualTo;
import org.enerj.jga.util.FilterIterator;

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
public class TestFilterIterator extends TestCase {

    public static final String FOO = "_foo_";

    public static final String BAR = "_bar_";

    public static final String BAZ = "_baz_";

    public static final String QLX = "_qlx_";

    public TestFilterIterator(String name) {
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

    public void testStandardUsage() {
        FilterIterator<String> iter = new FilterIterator<String>(list.iterator(), new EqualTo<String>().bind2nd(FOO));
        assertTrue(iter.hasNext());
        assertEquals(FOO, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(FOO, iter.next());
        assertTrue(!iter.hasNext());
    }

    public void testNoHasHextCalls() {
        FilterIterator<String> iter = new FilterIterator<String>(list.iterator(), new EqualTo<String>().bind2nd(FOO));
        assertEquals(FOO, iter.next());
        assertEquals(FOO, iter.next());
        assertTrue(!iter.hasNext());
    }

    public void testTooManyHasHextCalls() {
        FilterIterator<String> iter = new FilterIterator<String>(list.iterator(), new EqualTo<String>().bind2nd(FOO));
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertEquals(FOO, iter.next());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertEquals(FOO, iter.next());
        assertTrue(!iter.hasNext());
    }

    public void testNotFound() {
        FilterIterator<String> iter = new FilterIterator<String>(list.iterator(), new EqualTo<String>().bind2nd(QLX));
        assertTrue(!iter.hasNext());
        try {
            iter.next();
            fail("Expected NoSuchElementException when no element found, " + "but next() called anyway");
        } catch (NoSuchElementException x) {
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestFilterIterator.class);
    }
}
