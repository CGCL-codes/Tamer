package com.loribel.commons.util;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import com.loribel.commons.abstraction.GB_IdOwner;
import com.loribel.commons.abstraction.GB_ObjectFilter;
import com.loribel.commons.util.impl.GB_IdOwnerImpl;

/**
 * Test GB_IdOwnerTools.
 *
 * @author Gregory Borelli
 */
public class GB_IdOwnerToolsTest extends TestCase {

    public GB_IdOwnerToolsTest(String a_name) {
        super(a_name);
    }

    public void testAdditionDelta() {
        List l1 = new ArrayList();
        List l2 = new ArrayList();
        l1.add(new GB_IdOwnerImpl("1"));
        l1.add(new GB_IdOwnerImpl("2"));
        l1.add(new GB_IdOwnerImpl("3"));
        l1.add(new GB_IdOwnerImpl("4"));
        l2.add(new GB_IdOwnerImpl("1"));
        l2.add(new GB_IdOwnerImpl("2"));
        l2.add(new GB_IdOwnerImpl("5"));
        l2.add(new GB_IdOwnerImpl("6"));
        l2.add(new GB_IdOwnerImpl("7"));
        l2.add(new GB_IdOwnerImpl("8"));
        GB_IdOwner[] l_delta = GB_IdOwnerTools.additionDelta(l1, l2);
        assertEquals("length", l_delta.length, 4);
        assertEquals(l_delta[0].getId(), "5");
        assertEquals(l_delta[1].getId(), "6");
        assertEquals(l_delta[2].getId(), "7");
        assertEquals(l_delta[3].getId(), "8");
        l2.add(new GB_IdOwnerImpl(null));
        l_delta = GB_IdOwnerTools.additionDelta(l1, l2);
        assertEquals("length", l_delta.length, 5);
        assertEquals(l_delta[4].getId(), null);
        l1.add(new GB_IdOwnerImpl(null));
        l_delta = GB_IdOwnerTools.additionDelta(l1, l2);
        assertEquals("length", l_delta.length, 4);
    }

    public void testMyFilter() {
        List l1 = new ArrayList();
        l1.add(new GB_IdOwnerImpl("1"));
        l1.add(new GB_IdOwnerImpl("2"));
        l1.add(new GB_IdOwnerImpl("3"));
        l1.add(new GB_IdOwnerImpl("4"));
        l1.add(new GB_IdOwnerImpl("1"));
        l1.add(new GB_IdOwnerImpl("2"));
        l1.add(new GB_IdOwnerImpl("5"));
        l1.add(new GB_IdOwnerImpl("6"));
        l1.add(new GB_IdOwnerImpl("7"));
        l1.add(new GB_IdOwnerImpl("5"));
        l1.add(new GB_IdOwnerImpl("6"));
        l1.add(new GB_IdOwnerImpl("6"));
        l1.add(new GB_IdOwnerImpl("6"));
        l1.add(new GB_IdOwnerImpl("1"));
        GB_ObjectFilter l_filter;
        List l_result;
        l_filter = new GB_IdOwnerTools.MyFilterById("5");
        l_result = GB_ObjectFilterTools.getFilter(l1, l_filter);
        assertEquals("1", l_result.size(), 2);
        l_filter = new GB_IdOwnerTools.MyFilterById("1");
        l_result = GB_ObjectFilterTools.getFilter(l1, l_filter);
        assertEquals("2", l_result.size(), 3);
        l_filter = new GB_IdOwnerTools.MyFilterById("10");
        l_result = GB_ObjectFilterTools.getFilter(l1, l_filter);
        assertNull("3", l_result);
        l_filter = new GB_IdOwnerTools.MyFilterById("5");
        Object l_value = GB_ObjectFilterTools.getFirst(l1, l_filter);
        assertNotNull("4", l_value);
        l_filter = new GB_IdOwnerTools.MyFilterById("10");
        l_value = GB_ObjectFilterTools.getFirst(l1, l_filter);
        assertNull("5", l_value);
    }

    public void testSubstractionDelta() {
        List l1 = new ArrayList();
        List l2 = new ArrayList();
        l1.add(new GB_IdOwnerImpl("1"));
        l1.add(new GB_IdOwnerImpl("2"));
        l1.add(new GB_IdOwnerImpl("3"));
        l1.add(new GB_IdOwnerImpl("4"));
        l2.add(new GB_IdOwnerImpl("1"));
        l2.add(new GB_IdOwnerImpl("2"));
        l2.add(new GB_IdOwnerImpl("5"));
        l2.add(new GB_IdOwnerImpl("6"));
        l2.add(new GB_IdOwnerImpl("7"));
        l2.add(new GB_IdOwnerImpl("8"));
        GB_IdOwner[] l_delta = GB_IdOwnerTools.substractionDelta(l1, l2);
        for (int i = 0; i < l_delta.length; i++) {
            System.out.println(l_delta[i].getId());
        }
        assertEquals("length", l_delta.length, 2);
        assertEquals(l_delta[0].getId(), "3");
        assertEquals(l_delta[1].getId(), "4");
    }
}
