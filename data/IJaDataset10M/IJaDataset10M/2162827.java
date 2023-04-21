package com.spicesoft.clientobjects.core.collections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import com.spicesoft.clientobjects.core.collections.VariableLengthIndexedPropertyChangeEvent.EventType;
import junit.framework.TestCase;

public class ObservableListTest extends TestCase {

    public void testAdd() {
        ObservableList<String> olist = new ObservableList<String>(new ArrayList<String>());
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add("hehe");
        assertEquals(1, l.count);
        assertEquals(0, l.lastEvent.getStartIndex());
        assertEquals(0, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertEquals("hehe", l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        olist.add("haha");
        assertEquals(2, l.count);
        assertEquals(1, l.lastEvent.getStartIndex());
        assertEquals(1, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertEquals("haha", l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
    }

    public void testIndexedAdd() {
        ObservableList<String> olist = new ObservableList<String>(new ArrayList<String>());
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add(0, "a");
        assertEquals(1, l.count);
        assertEquals(0, l.lastEvent.getStartIndex());
        assertEquals(0, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertEquals("a", l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        olist.add(0, "b");
        assertEquals(2, l.count);
        assertEquals(0, l.lastEvent.getStartIndex());
        assertEquals(0, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertEquals("b", l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        olist.add(2, "c");
        assertEquals(3, l.count);
        assertEquals(2, l.lastEvent.getStartIndex());
        assertEquals(2, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertEquals("c", l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        olist.add(1, "d");
        assertEquals(4, l.count);
        assertEquals(1, l.lastEvent.getStartIndex());
        assertEquals(1, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertEquals("d", l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals("b", olist.get(0));
        assertEquals("d", olist.get(1));
        assertEquals("a", olist.get(2));
        assertEquals("c", olist.get(3));
    }

    public void testAddAll() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("d");
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("e");
        list2.add("f");
        ObservableList<String> olist = new ObservableList<String>(list1);
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.addAll(list2);
        assertEquals(1, l.count);
        assertEquals(4, l.lastEvent.getStartIndex());
        assertEquals(5, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertNull(l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals("a", olist.get(0));
        assertEquals("b", olist.get(1));
        assertEquals("c", olist.get(2));
        assertEquals("d", olist.get(3));
        assertEquals("e", olist.get(4));
        assertEquals("f", olist.get(5));
    }

    public void testIndexedAddAll() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("d");
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("e");
        list2.add("f");
        ObservableList<String> olist = new ObservableList<String>(list1);
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.addAll(1, list2);
        assertEquals(1, l.count);
        assertEquals(1, l.lastEvent.getStartIndex());
        assertEquals(2, l.lastEvent.getEndIndex());
        assertEquals(EventType.INSERT, l.lastEvent.getEventType());
        assertNull(l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals("a", olist.get(0));
        assertEquals("e", olist.get(1));
        assertEquals("f", olist.get(2));
        assertEquals("b", olist.get(3));
        assertEquals("c", olist.get(4));
        assertEquals("d", olist.get(5));
    }

    public void testClear() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        ObservableList<String> olist = new ObservableList<String>(list1);
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add("c");
        olist.add("d");
        assertEquals(4, olist.size());
        assertEquals(4, list1.size());
        olist.clear();
        assertEquals(3, l.count);
        assertEquals(0, l.lastEvent.getStartIndex());
        assertEquals(3, l.lastEvent.getEndIndex());
        assertEquals(EventType.DELETE, l.lastEvent.getEventType());
        assertEquals(null, l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals(0, olist.size());
        assertEquals(0, list1.size());
    }

    public void testRemoveObject() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        ObservableList<String> olist = new ObservableList<String>(list1);
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add("c");
        olist.add("d");
        assertEquals(4, olist.size());
        assertEquals(4, list1.size());
        olist.remove("a");
        assertEquals(3, l.count);
        assertEquals(0, l.lastEvent.getStartIndex());
        assertEquals(0, l.lastEvent.getEndIndex());
        assertEquals(EventType.DELETE, l.lastEvent.getEventType());
        assertEquals(null, l.lastEvent.getNewValue());
        assertEquals("a", l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals(3, olist.size());
        assertEquals(3, list1.size());
    }

    public void testRemoveIndex() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        ObservableList<String> olist = new ObservableList<String>(list1);
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add("c");
        olist.add("d");
        assertEquals(4, olist.size());
        assertEquals(4, list1.size());
        String removedValue = olist.remove(2);
        assertEquals(3, l.count);
        assertEquals(2, l.lastEvent.getStartIndex());
        assertEquals(2, l.lastEvent.getEndIndex());
        assertEquals(EventType.DELETE, l.lastEvent.getEventType());
        assertEquals(null, l.lastEvent.getNewValue());
        assertEquals("c", l.lastEvent.getOldValue());
        assertEquals(removedValue, l.lastEvent.getOldValue());
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals(3, olist.size());
        assertEquals(3, list1.size());
    }

    public void testRemoveAll() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        ObservableList<String> olist = new ObservableList<String>(list1);
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add("c");
        olist.add("d");
        assertEquals(4, olist.size());
        assertEquals(4, list1.size());
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("a");
        list2.add("c");
        boolean result = olist.removeAll(list2);
        assertEquals(3, l.count);
        assertEquals(-1, l.lastEvent.getStartIndex());
        assertEquals(-1, l.lastEvent.getEndIndex());
        assertEquals(EventType.DELETE, l.lastEvent.getEventType());
        assertEquals(null, l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(result);
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals(2, olist.size());
        assertEquals(2, list1.size());
        assertEquals("b", olist.get(0));
        assertEquals("d", olist.get(1));
    }

    public void testRetainAll() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        ObservableList<String> olist = new ObservableList<String>(list1);
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add("c");
        olist.add("d");
        assertEquals(4, olist.size());
        assertEquals(4, list1.size());
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("b");
        list2.add("c");
        list2.add("z");
        boolean result = olist.retainAll(list2);
        assertEquals(3, l.count);
        assertEquals(-1, l.lastEvent.getStartIndex());
        assertEquals(-1, l.lastEvent.getEndIndex());
        assertEquals(EventType.DELETE, l.lastEvent.getEventType());
        assertEquals(null, l.lastEvent.getNewValue());
        assertEquals(null, l.lastEvent.getOldValue());
        assertTrue(result);
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals(2, olist.size());
        assertEquals(2, list1.size());
        assertEquals("b", olist.get(0));
        assertEquals("c", olist.get(1));
    }

    public void testSet() {
        ObservableList<String> olist = new ObservableList<String>(new ArrayList<String>());
        OLListener l = new OLListener();
        olist.addPropertyChangeListener(l);
        assertEquals(0, l.count);
        assertNull(l.lastEvent);
        olist.add("a");
        olist.add("b");
        olist.add("c");
        String oldVal = olist.set(1, "hehe");
        assertEquals(4, l.count);
        assertEquals(1, l.lastEvent.getStartIndex());
        assertEquals(1, l.lastEvent.getEndIndex());
        assertEquals(EventType.MODIFY, l.lastEvent.getEventType());
        assertEquals("hehe", l.lastEvent.getNewValue());
        assertEquals("b", l.lastEvent.getOldValue());
        assertEquals("b", oldVal);
        assertTrue(olist == l.lastEvent.getSource());
        assertEquals("a", olist.get(0));
        assertEquals("hehe", olist.get(1));
        assertEquals("c", olist.get(2));
    }

    private class OLListener implements PropertyChangeListener {

        int count = 0;

        VariableLengthIndexedPropertyChangeEvent lastEvent;

        public void propertyChange(PropertyChangeEvent evt) {
            count++;
            this.lastEvent = (VariableLengthIndexedPropertyChangeEvent) evt;
        }
    }
}
