package org.enerj.jga.util;

import java.util.NoSuchElementException;
import java.util.Vector;
import junit.framework.TestCase;
import org.enerj.jga.Samples;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.jga.fn.Visitor;
import org.enerj.jga.util.CachingIterator;

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
public class TestCachingIterator extends TestCase {

    public TestCachingIterator(String name) {
        super(name);
    }

    public static final String FOO = "_foo_";

    public static final String BAR = "_bar_";

    public static final String BAZ = "_baz_";

    public static final String QLX = "_qlx_";

    Vector<String> list = new Vector<String>();

    public void setUp() {
        list.add(FOO);
        list.add(BAR);
        list.add(BAZ);
        list.add(QLX);
    }

    public void tearDown() {
    }

    public void testCaching1() {
        CachingIterator<String> iter = new CachingIterator<String>(list.iterator(), 1);
        assertTrue(iter.hasNext());
        assertFalse(iter.hasCached(0));
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(1));
        try {
            iter.cached(-1);
            fail("Expected NoSuchElementException when cached(1) called " + "at start");
        } catch (NoSuchElementException x) {
        }
        try {
            iter.cached(-1);
            fail("Expected NoSuchElementException when cached(2) called " + "(2 > size)");
        } catch (NoSuchElementException x) {
        }
        assertEquals(FOO, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertEquals(FOO, iter.cached(1));
        assertEquals(BAR, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertEquals(BAR, iter.cached(1));
        assertEquals(BAZ, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertEquals(BAZ, iter.cached(1));
        assertEquals(QLX, iter.next());
        assertFalse(iter.hasNext());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertEquals(QLX, iter.cached(1));
        try {
            iter.next();
            fail("Expected NoSuchElementException when next is called when " + "iterator off the end");
        } catch (NoSuchElementException x) {
        }
        try {
            iter.cached(-1);
            fail("Expected NoSuchElementException when cached(-1) called");
        } catch (NoSuchElementException x) {
        }
    }

    public void testCachingN() {
        CachingIterator<String> iter = new CachingIterator<String>(list.iterator(), 3);
        assertTrue(iter.hasNext());
        assertFalse(iter.hasCached(0));
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertFalse(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        try {
            iter.cached(-1);
            fail("Expected NoSuchElementException when cached(1) called " + "at start");
        } catch (NoSuchElementException x) {
        }
        try {
            iter.cached(-1);
            fail("Expected NoSuchElementException when cached(4) called " + "(4 > size)");
        } catch (NoSuchElementException x) {
        }
        assertEquals(FOO, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertFalse(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertEquals(FOO, iter.cached(1));
        try {
            iter.cached(-1);
            fail("Expected NoSuchElementException when cached(2) called " + "(2 > count)");
        } catch (NoSuchElementException x) {
        }
        assertEquals(BAR, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertTrue(iter.hasCached(2));
        assertFalse(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertEquals(FOO, iter.cached(2));
        assertEquals(BAR, iter.cached(1));
        assertEquals(BAZ, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertTrue(iter.hasCached(2));
        assertTrue(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertEquals(FOO, iter.cached(3));
        assertEquals(BAR, iter.cached(2));
        assertEquals(BAZ, iter.cached(1));
        assertEquals(QLX, iter.next());
        assertFalse(iter.hasNext());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertTrue(iter.hasCached(2));
        assertTrue(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertEquals(BAR, iter.cached(3));
        assertEquals(BAZ, iter.cached(2));
        assertEquals(QLX, iter.cached(1));
    }

    public void testCachingMoreThanSize() {
        CachingIterator<String> iter = new CachingIterator<String>(list.iterator(), 5);
        assertTrue(iter.hasNext());
        assertFalse(iter.hasCached(0));
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertFalse(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertFalse(iter.hasCached(5));
        assertFalse(iter.hasCached(6));
        assertEquals(FOO, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertFalse(iter.hasCached(2));
        assertFalse(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertFalse(iter.hasCached(5));
        assertFalse(iter.hasCached(6));
        assertEquals(FOO, iter.cached(1));
        assertEquals(BAR, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertTrue(iter.hasCached(2));
        assertFalse(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertFalse(iter.hasCached(5));
        assertFalse(iter.hasCached(6));
        assertEquals(FOO, iter.cached(2));
        assertEquals(BAR, iter.cached(1));
        assertEquals(BAZ, iter.next());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertTrue(iter.hasCached(2));
        assertTrue(iter.hasCached(3));
        assertFalse(iter.hasCached(4));
        assertFalse(iter.hasCached(5));
        assertFalse(iter.hasCached(6));
        assertEquals(FOO, iter.cached(3));
        assertEquals(BAR, iter.cached(2));
        assertEquals(BAZ, iter.cached(1));
        assertEquals(QLX, iter.next());
        assertFalse(iter.hasNext());
        assertFalse(iter.hasCached(-1));
        assertFalse(iter.hasCached(0));
        assertTrue(iter.hasCached(1));
        assertTrue(iter.hasCached(2));
        assertTrue(iter.hasCached(3));
        assertTrue(iter.hasCached(4));
        assertFalse(iter.hasCached(5));
        assertFalse(iter.hasCached(6));
        assertEquals(FOO, iter.cached(4));
        assertEquals(BAR, iter.cached(3));
        assertEquals(BAZ, iter.cached(2));
        assertEquals(QLX, iter.cached(1));
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestCachingIterator.class);
    }
}
