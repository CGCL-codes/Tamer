package org.deri.iris.utils;

import java.util.Iterator;
import junit.framework.TestCase;

public class UniqueListTest extends TestCase {

    UniqueList<Integer> mList;

    @Override
    protected void setUp() throws Exception {
        mList = new UniqueList<Integer>();
    }

    public void testAdd() {
        mList.add(0);
        mList.add(1);
        mList.add(2);
        assertEquals(mList.size(), 3);
        assertEquals(mList.get(0), new Integer(0));
        assertEquals(mList.get(1), new Integer(1));
        assertEquals(mList.get(2), new Integer(2));
        mList.add(1);
        assertEquals(mList.size(), 3);
        mList.add(1, 3);
        assertEquals(mList.size(), 4);
        assertEquals(mList.get(0), new Integer(0));
        assertEquals(mList.get(1), new Integer(3));
        assertEquals(mList.get(2), new Integer(1));
        assertEquals(mList.get(3), new Integer(2));
    }

    public void testIterator() {
        mList.add(0);
        mList.add(1);
        Iterator<Integer> it = mList.iterator();
        assertTrue(it.hasNext());
        assertEquals(it.next().intValue(), 0);
        assertTrue(it.hasNext());
        assertEquals(it.next().intValue(), 1);
        assertFalse(it.hasNext());
    }
}
