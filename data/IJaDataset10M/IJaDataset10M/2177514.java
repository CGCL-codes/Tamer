package org.progeeks.util;

import java.util.*;
import junit.framework.TestCase;

/**
 * Tests of the ListPropertyChangeEvent class.
 *
 * @version		$Revision: 1.1 $
 * @author		Paul Wisneskey
 */
public class ListPropertyChangeEventTests extends TestCase {

    public void testConstructorDetailedParams() {
        List originalList = Arrays.asList(new String[] { "The", "orginal", "list" });
        List newList = Arrays.asList(new String[] { "The", "new", "list" });
        ListPropertyChangeEvent event = new ListPropertyChangeEvent(this, "PropertyName", originalList, newList, 1, 3, ListPropertyChangeEvent.UPDATE);
        assertEquals(this, event.getSource());
        assertEquals("PropertyName", event.getPropertyName());
        assertEquals(originalList, event.getOldValue());
        assertEquals(newList, event.getNewValue());
        assertEquals(1, event.getFirstIndex());
        assertEquals(3, event.getLastIndex());
        assertEquals(ListPropertyChangeEvent.UPDATE, event.getType());
    }

    public void testConstructorResourced() {
        List originalList = Arrays.asList(new String[] { "The", "orginal", "list" });
        List newList = Arrays.asList(new String[] { "The", "new", "list" });
        ListPropertyChangeEvent sourceEvent = new ListPropertyChangeEvent(this, "PropertyName", originalList, newList, 1, 3, ListPropertyChangeEvent.UPDATE);
        Object newSource = new Object();
        ListPropertyChangeEvent event = new ListPropertyChangeEvent(newSource, sourceEvent);
        assertEquals(newSource, event.getSource());
        assertEquals("PropertyName", event.getPropertyName());
        assertEquals(originalList, event.getOldValue());
        assertEquals(newList, event.getNewValue());
        assertEquals(1, event.getFirstIndex());
        assertEquals(3, event.getLastIndex());
        assertEquals(ListPropertyChangeEvent.UPDATE, event.getType());
    }

    public void testConstructorResourcedPropertyRenamed() {
        List originalList = Arrays.asList(new String[] { "The", "orginal", "list" });
        List newList = Arrays.asList(new String[] { "The", "new", "list" });
        ListPropertyChangeEvent sourceEvent = new ListPropertyChangeEvent(this, "PropertyName", originalList, newList, 1, 3, ListPropertyChangeEvent.UPDATE);
        Object newSource = new Object();
        ListPropertyChangeEvent event = new ListPropertyChangeEvent(newSource, "NewProperty", sourceEvent);
        assertEquals(newSource, event.getSource());
        assertEquals("NewProperty", event.getPropertyName());
        assertEquals(originalList, event.getOldValue());
        assertEquals(newList, event.getNewValue());
        assertEquals(1, event.getFirstIndex());
        assertEquals(3, event.getLastIndex());
        assertEquals(ListPropertyChangeEvent.UPDATE, event.getType());
    }
}
