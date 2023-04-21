package com.loribel.commons.util;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import com.loribel.commons.abstraction.GB_IndexOwner;
import com.loribel.commons.util.impl.GB_IndexOwnerImpl;

/**
 * Test GB_IdOwnerTools.
 *
 * @author Gregory Borelli
 */
public class GB_IndexOwnerToolsTest extends TestCase {

    public GB_IndexOwnerToolsTest(String a_name) {
        super(a_name);
    }

    public void testAdditionDelta() {
        List l1 = new ArrayList();
        List l2 = new ArrayList();
        l1.add(new GB_IndexOwnerImpl(1));
        l1.add(new GB_IndexOwnerImpl(2));
        l1.add(new GB_IndexOwnerImpl(3));
        l1.add(new GB_IndexOwnerImpl(4));
        l2.add(new GB_IndexOwnerImpl(1));
        l2.add(new GB_IndexOwnerImpl(2));
        l2.add(new GB_IndexOwnerImpl(5));
        l2.add(new GB_IndexOwnerImpl(6));
        l2.add(new GB_IndexOwnerImpl(7));
        l2.add(new GB_IndexOwnerImpl(8));
        GB_IndexOwner[] l_delta = GB_IndexOwnerTools.additionDelta(l1, l2);
        assertEquals("length", l_delta.length, 4);
        assertEquals(l_delta[0].getIndex(), 5);
        assertEquals(l_delta[1].getIndex(), 6);
        assertEquals(l_delta[2].getIndex(), 7);
        assertEquals(l_delta[3].getIndex(), 8);
    }

    public void testSort_Array() {
        GB_IndexOwner[] l_items = new GB_IndexOwner[4];
        l_items[0] = new GB_IndexOwnerImpl(10);
        l_items[1] = new GB_IndexOwnerImpl(3);
        l_items[2] = new GB_IndexOwnerImpl(8);
        l_items[3] = new GB_IndexOwnerImpl(1);
        GB_IndexOwnerTools.sortByIndex(l_items);
        assertEquals(l_items[0].getIndex(), 1);
        assertEquals(l_items[1].getIndex(), 3);
        assertEquals(l_items[2].getIndex(), 8);
        assertEquals(l_items[3].getIndex(), 10);
    }

    public void testSort_List() {
        List l_items = new ArrayList();
        l_items.add(new GB_IndexOwnerImpl(10));
        l_items.add(new GB_IndexOwnerImpl(3));
        l_items.add(new GB_IndexOwnerImpl(8));
        l_items.add(new GB_IndexOwnerImpl(1));
        GB_IndexOwnerTools.sortByIndex(l_items);
        assertEquals(((GB_IndexOwner) l_items.get(0)).getIndex(), 1);
        assertEquals(((GB_IndexOwner) l_items.get(1)).getIndex(), 3);
        assertEquals(((GB_IndexOwner) l_items.get(2)).getIndex(), 8);
        assertEquals(((GB_IndexOwner) l_items.get(3)).getIndex(), 10);
    }

    public void testSubstractionDelta() {
        List l1 = new ArrayList();
        List l2 = new ArrayList();
        l1.add(new GB_IndexOwnerImpl(1));
        l1.add(new GB_IndexOwnerImpl(2));
        l1.add(new GB_IndexOwnerImpl(3));
        l1.add(new GB_IndexOwnerImpl(4));
        l2.add(new GB_IndexOwnerImpl(1));
        l2.add(new GB_IndexOwnerImpl(2));
        l2.add(new GB_IndexOwnerImpl(5));
        l2.add(new GB_IndexOwnerImpl(6));
        l2.add(new GB_IndexOwnerImpl(7));
        l2.add(new GB_IndexOwnerImpl(8));
        GB_IndexOwner[] l_delta = GB_IndexOwnerTools.substractionDelta(l1, l2);
        for (int i = 0; i < l_delta.length; i++) {
        }
        assertEquals("length", l_delta.length, 2);
        assertEquals(l_delta[0].getIndex(), 3);
        assertEquals(l_delta[1].getIndex(), 4);
    }
}
