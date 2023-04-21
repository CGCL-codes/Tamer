package org.incava.util;

import java.awt.Point;
import java.io.*;
import java.util.*;
import junit.framework.TestCase;

public class TestCollect extends TestCase {

    public TestCollect(String name) {
        super(name);
    }

    public void test() {
        List list = new ArrayList();
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        List collected = new Collect(list) {

            public boolean where(Object obj) {
                return ((String) obj).indexOf('o') != -1;
            }
        };
        assertEquals(3, collected.size());
        assertEquals("one", (String) collected.get(0));
        assertEquals("two", (String) collected.get(1));
        assertEquals("four", (String) collected.get(2));
    }

    public void testBlock() {
        List list = new ArrayList();
        list.add(new Point(4, 5));
        list.add(new Point(2, 15));
        list.add(new Point(9, 11));
        list.add(new Point(24, 7));
        list.add(new Point(3, 12));
        List collected = new Collect(list) {

            public boolean where(Object obj) {
                return ((Point) obj).x % 2 == 0;
            }

            public Object block(Object obj) {
                Point pt = (Point) obj;
                return new Integer(pt.y);
            }
        };
        assertEquals(3, collected.size());
        assertEquals(new Integer(5), (Integer) collected.get(0));
        assertEquals(new Integer(15), (Integer) collected.get(1));
        assertEquals(new Integer(7), (Integer) collected.get(2));
    }
}
