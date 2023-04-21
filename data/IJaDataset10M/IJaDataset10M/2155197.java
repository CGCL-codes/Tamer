package org.progeeks.util;

import java.util.*;
import java.util.List;
import org.progeeks.util.beans.RecordingPropertyChangeListener;

/**
 * Tests of the constrained observable list class.
 *
 * @version		$Revision: 1.2 $
 * @author		Paul Wisneskey
 */
public class ConstrainedObservableListTests extends AbstractObservableListTests {

    protected ObservableList getObservableList() {
        return new TestConstrainedList();
    }

    protected ObservableList getObservableList(List list) {
        return new TestConstrainedList(list);
    }

    protected ObservableList getObservableList(String propertyName, List list) {
        return new TestConstrainedList(propertyName, list);
    }

    public void testConstructorPropertyName() {
        ConstrainedObservableList list = new TestConstrainedList("property");
        assertEquals("property", list.getPropertyName());
    }

    public void testSetList() {
        List wrappedList = new ArrayList(TEST_LIST);
        ConstrainedObservableList list = (ConstrainedObservableList) getObservableList((List) null);
        list.setList(wrappedList);
        assertTrue(list.containsAll(wrappedList));
        assertTrue(wrappedList.containsAll(list));
    }

    public void testSetListAlreadySet() {
        List wrappedList = new ArrayList(TEST_LIST);
        ConstrainedObservableList list = (ConstrainedObservableList) getObservableList(wrappedList);
        boolean caughtException = false;
        try {
            list.setList(wrappedList);
        } catch (Exception e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
    }

    public void testConstrainedAddOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject = new TestObject(true, true);
        boolean result = list.add(testObject);
        assertTrue(result);
        assertTrue(list.contains(testObject));
        assertTrue(testObject.addChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expected = new ArrayList(TEST_LIST);
        expected.add(testObject);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.INSERT, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(TEST_LIST, event.getOldValue());
        assertEquals(expected, event.getNewValue());
    }

    public void testConstrainedAddDenied() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject = new TestObject(false, true);
        boolean result = list.add(testObject);
        assertFalse(result);
        assertFalse(list.contains(testObject));
        assertTrue(testObject.addChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedAddIndexedOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject = new TestObject(true, true);
        list.add(1, testObject);
        assertTrue(list.contains(testObject));
        assertTrue(testObject.addChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expected = new ArrayList(TEST_LIST);
        expected.add(1, testObject);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.INSERT, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(TEST_LIST, event.getOldValue());
        assertEquals(expected, event.getNewValue());
    }

    public void testConstrainedAddIndexdDenied() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject = new TestObject(false, true);
        list.add(1, testObject);
        assertFalse(list.contains(testObject));
        assertTrue(testObject.addChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedRemoveOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, true);
        wrappedList.add(testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        boolean result = list.remove(testObject);
        assertTrue(result);
        assertFalse(list.contains(testObject));
        assertTrue(testObject.removeChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expectedOld = new ArrayList(TEST_LIST);
        expectedOld.add(testObject);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(TEST_LIST, event.getNewValue());
    }

    public void testConstrainedRemoveDenied() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, false);
        wrappedList.add(testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        boolean result = list.remove(testObject);
        assertFalse(result);
        assertTrue(list.contains(testObject));
        assertTrue(testObject.removeChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedRemoveIndexedOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, true);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        list.remove(1);
        assertFalse(list.contains(testObject));
        assertTrue(testObject.removeChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expectedOld = new ArrayList(TEST_LIST);
        expectedOld.add(1, testObject);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(TEST_LIST, event.getNewValue());
    }

    public void testConstrainedRemoveIndexedDenied() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, false);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        list.remove(1);
        assertTrue(list.contains(testObject));
        assertTrue(testObject.removeChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedAddAllOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, true);
        TestObject testObject3 = new TestObject(true, true);
        List addList = new ArrayList();
        addList.add(testObject1);
        addList.add(testObject2);
        addList.add(testObject3);
        boolean result = list.addAll(addList);
        assertTrue(result);
        assertTrue(list.contains(testObject1));
        assertTrue(list.contains(testObject2));
        assertTrue(list.contains(testObject3));
        assertTrue(testObject1.addChecked);
        assertTrue(testObject2.addChecked);
        assertTrue(testObject3.addChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expected = new ArrayList(TEST_LIST);
        expected.addAll(addList);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.INSERT, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(TEST_LIST, event.getOldValue());
        assertEquals(expected, event.getNewValue());
    }

    public void testConstrainedAddAllDeniedSome() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(false, true);
        TestObject testObject3 = new TestObject(true, true);
        List addList = new ArrayList();
        addList.add(testObject1);
        addList.add(testObject2);
        addList.add(testObject3);
        boolean result = list.addAll(addList);
        assertTrue(result);
        assertTrue(list.contains(testObject1));
        assertFalse(list.contains(testObject2));
        assertTrue(list.contains(testObject3));
        assertTrue(testObject1.addChecked);
        assertTrue(testObject2.addChecked);
        assertTrue(testObject3.addChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expected = new ArrayList(TEST_LIST);
        expected.add(testObject1);
        expected.add(testObject3);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.INSERT, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(TEST_LIST, event.getOldValue());
        assertEquals(expected, event.getNewValue());
    }

    public void testConstrainedAddAllDeniedAll() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject1 = new TestObject(false, true);
        TestObject testObject2 = new TestObject(false, true);
        TestObject testObject3 = new TestObject(false, true);
        List addList = new ArrayList();
        addList.add(testObject1);
        addList.add(testObject2);
        addList.add(testObject3);
        boolean result = list.addAll(addList);
        assertFalse(result);
        assertFalse(list.contains(testObject1));
        assertFalse(list.contains(testObject2));
        assertFalse(list.contains(testObject3));
        assertTrue(testObject1.addChecked);
        assertTrue(testObject2.addChecked);
        assertTrue(testObject3.addChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedAddAllIndexedOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, true);
        TestObject testObject3 = new TestObject(true, true);
        List addList = new ArrayList();
        addList.add(testObject1);
        addList.add(testObject2);
        addList.add(testObject3);
        boolean result = list.addAll(2, addList);
        assertTrue(result);
        assertTrue(list.contains(testObject1));
        assertTrue(list.contains(testObject2));
        assertTrue(list.contains(testObject3));
        assertTrue(testObject1.addChecked);
        assertTrue(testObject2.addChecked);
        assertTrue(testObject3.addChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expected = new ArrayList(TEST_LIST);
        expected.addAll(2, addList);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.INSERT, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(TEST_LIST, event.getOldValue());
        assertEquals(expected, event.getNewValue());
    }

    public void testConstrainedAddAllIndexedDeniedSome() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(false, true);
        TestObject testObject3 = new TestObject(true, true);
        List addList = new ArrayList();
        addList.add(testObject1);
        addList.add(testObject2);
        addList.add(testObject3);
        boolean result = list.addAll(2, addList);
        assertTrue(result);
        assertTrue(list.contains(testObject1));
        assertFalse(list.contains(testObject2));
        assertTrue(list.contains(testObject3));
        assertTrue(testObject1.addChecked);
        assertTrue(testObject2.addChecked);
        assertTrue(testObject3.addChecked);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expected = new ArrayList(TEST_LIST);
        expected.add(2, testObject1);
        expected.add(3, testObject3);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.INSERT, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(TEST_LIST, event.getOldValue());
        assertEquals(expected, event.getNewValue());
    }

    public void testConstrainedAddAllIndexedDeniedAll() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject testObject1 = new TestObject(false, true);
        TestObject testObject2 = new TestObject(false, true);
        TestObject testObject3 = new TestObject(false, true);
        List addList = new ArrayList();
        addList.add(testObject1);
        addList.add(testObject2);
        addList.add(testObject3);
        boolean result = list.addAll(2, addList);
        assertFalse(result);
        assertFalse(list.contains(testObject1));
        assertFalse(list.contains(testObject2));
        assertFalse(list.contains(testObject3));
        assertTrue(testObject1.addChecked);
        assertTrue(testObject2.addChecked);
        assertTrue(testObject3.addChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedRemoveAllOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, true);
        TestObject testObject3 = new TestObject(true, true);
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        List removeList = new ArrayList();
        removeList.add(testObject1);
        removeList.add(testObject2);
        removeList.add(testObject3);
        boolean result = list.removeAll(removeList);
        assertTrue(result);
        assertFalse(list.contains(testObject1));
        assertFalse(list.contains(testObject2));
        assertFalse(list.contains(testObject3));
        assertTrue(testObject1.removeChecked);
        assertTrue(testObject2.removeChecked);
        assertTrue(testObject3.removeChecked);
        assertEquals(3, listener.eventCount());
        List events = listener.getEvents(true);
        for (Iterator iterator = events.iterator(); iterator.hasNext(); ) {
            ListPropertyChangeEvent event = (ListPropertyChangeEvent) iterator.next();
            assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        }
    }

    public void testConstrainedRemoveAllDeniedSome() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, false);
        TestObject testObject3 = new TestObject(true, true);
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        List removeList = new ArrayList();
        removeList.add(testObject1);
        removeList.add(testObject2);
        removeList.add(testObject3);
        boolean result = list.removeAll(removeList);
        assertTrue(result);
        assertFalse(list.contains(testObject1));
        assertTrue(list.contains(testObject2));
        assertFalse(list.contains(testObject3));
        assertTrue(testObject1.removeChecked);
        assertTrue(testObject2.removeChecked);
        assertTrue(testObject3.removeChecked);
        assertEquals(2, listener.eventCount());
        List events = listener.getEvents(true);
        for (Iterator iterator = events.iterator(); iterator.hasNext(); ) {
            ListPropertyChangeEvent event = (ListPropertyChangeEvent) iterator.next();
            assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        }
    }

    public void testConstrainedRemoveAllDeniedAll() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject1 = new TestObject(true, false);
        TestObject testObject2 = new TestObject(true, false);
        TestObject testObject3 = new TestObject(true, false);
        List removeList = new ArrayList();
        removeList.add(testObject1);
        removeList.add(testObject2);
        removeList.add(testObject3);
        wrappedList.addAll(removeList);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        boolean result = list.removeAll(removeList);
        assertFalse(result);
        assertTrue(list.contains(testObject1));
        assertTrue(list.contains(testObject2));
        assertTrue(list.contains(testObject3));
        assertTrue(testObject1.removeChecked);
        assertTrue(testObject2.removeChecked);
        assertTrue(testObject3.removeChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedRetainAllOk() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, true);
        TestObject testObject3 = new TestObject(true, true);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ArrayList retainList = new ArrayList();
        retainList.add(testObject1);
        retainList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        boolean result = list.retainAll(retainList);
        assertTrue(result);
        assertTrue(list.contains(testObject1));
        assertFalse(list.contains(testObject2));
        assertTrue(list.contains(testObject3));
        assertTrue(testObject2.removeChecked);
        List expectedOld = new ArrayList();
        expectedOld.add(testObject1);
        expectedOld.add(testObject2);
        expectedOld.add(testObject3);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(retainList, event.getNewValue());
    }

    public void testConstrainedRetainAllDeniedSome() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, false);
        TestObject testObject3 = new TestObject(true, true);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ArrayList retainList = new ArrayList();
        retainList.add(testObject1);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        boolean result = list.retainAll(retainList);
        assertTrue(result);
        assertTrue(list.contains(testObject1));
        assertTrue(list.contains(testObject2));
        assertFalse(list.contains(testObject3));
        assertTrue(testObject2.removeChecked);
        assertTrue(testObject3.removeChecked);
        List expectedOld = new ArrayList();
        expectedOld.add(testObject1);
        expectedOld.add(testObject2);
        expectedOld.add(testObject3);
        List expectedNew = new ArrayList();
        expectedNew.add(testObject1);
        expectedNew.add(testObject2);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(expectedNew, event.getNewValue());
    }

    public void testConstrainedRetainAllDeniedAll() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, false);
        TestObject testObject3 = new TestObject(true, false);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ArrayList retainList = new ArrayList();
        retainList.add(testObject1);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        boolean result = list.retainAll(retainList);
        assertFalse(result);
        assertTrue(list.contains(testObject1));
        assertTrue(list.contains(testObject2));
        assertTrue(list.contains(testObject3));
        assertTrue(testObject2.removeChecked);
        assertTrue(testObject3.removeChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedClearOk() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, true);
        TestObject testObject3 = new TestObject(true, true);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        list.clear();
        assertTrue(list.isEmpty());
        assertTrue(testObject1.removeChecked);
        assertTrue(testObject2.removeChecked);
        assertTrue(testObject3.removeChecked);
        List expectedOld = new ArrayList();
        expectedOld.add(testObject1);
        expectedOld.add(testObject2);
        expectedOld.add(testObject3);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(Collections.EMPTY_LIST, event.getNewValue());
    }

    public void testConstrainedClearDenied() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, false);
        TestObject testObject3 = new TestObject(true, true);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        boolean caughtException = false;
        try {
            list.clear();
        } catch (RuntimeException e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
    }

    public void testConstrainedSetOk() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, true);
        TestObject testObject3 = new TestObject(true, true);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(true, true);
        Object result = list.set(1, newObject);
        assertEquals(result, testObject2);
        assertTrue(testObject2.removeChecked);
        assertTrue(newObject.addChecked);
        ArrayList expectedOld = new ArrayList();
        expectedOld.add(testObject1);
        expectedOld.add(testObject2);
        expectedOld.add(testObject3);
        ArrayList expectedNew = new ArrayList();
        expectedNew.add(testObject1);
        expectedNew.add(newObject);
        expectedNew.add(testObject3);
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.UPDATE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(expectedNew, event.getNewValue());
    }

    public void testConstrainedSetDeniedRemove() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, false);
        TestObject testObject3 = new TestObject(true, true);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(true, true);
        boolean caughtException = false;
        try {
            list.set(1, newObject);
        } catch (RuntimeException e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
    }

    public void testConstrainedSetDeniedAdd() {
        TestObject testObject1 = new TestObject(true, true);
        TestObject testObject2 = new TestObject(true, true);
        TestObject testObject3 = new TestObject(true, true);
        ArrayList wrappedList = new ArrayList();
        wrappedList.add(testObject1);
        wrappedList.add(testObject2);
        wrappedList.add(testObject3);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(false, true);
        boolean caughtException = false;
        try {
            list.set(1, newObject);
        } catch (RuntimeException e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
    }

    public void testConstrainedIteratorRemoveOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, true);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (testObject.equals(object)) iterator.remove();
        }
        assertFalse(list.contains(testObject));
        assertTrue(testObject.removeChecked);
        assertEquals(1, listener.eventCount());
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expectedOld = new ArrayList(TEST_LIST);
        expectedOld.add(1, testObject);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.DELETE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(TEST_LIST, event.getNewValue());
    }

    public void testConstrainedIteratorRemoveDenied() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, false);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (testObject.equals(object)) iterator.remove();
        }
        assertTrue(list.contains(testObject));
        assertTrue(testObject.removeChecked);
        assertFalse(listener.hasEvents());
    }

    public void testConstrainedListIteratorSetOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, true);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(true, true);
        for (ListIterator iterator = list.listIterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (testObject.equals(object)) iterator.set(newObject);
        }
        assertFalse(list.contains(testObject));
        assertTrue(list.contains(newObject));
        assertTrue(testObject.removeChecked);
        assertTrue(newObject.addChecked);
        assertEquals(1, listener.eventCount());
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expectedOld = new ArrayList(TEST_LIST);
        expectedOld.add(1, testObject);
        List expectedNew = new ArrayList(TEST_LIST);
        expectedNew.add(1, newObject);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.UPDATE, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(expectedNew, event.getNewValue());
    }

    public void testConstrainedListIteratorSetDeniedRemove() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, false);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(true, true);
        boolean caughtException = false;
        try {
            for (ListIterator iterator = list.listIterator(); iterator.hasNext(); ) {
                Object object = iterator.next();
                if (testObject.equals(object)) iterator.set(newObject);
            }
        } catch (RuntimeException e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
        assertTrue(testObject.removeChecked);
    }

    public void testConstrainedListIteratorSetDeniedAdd() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, true);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(false, true);
        boolean caughtException = false;
        try {
            for (ListIterator iterator = list.listIterator(); iterator.hasNext(); ) {
                Object object = iterator.next();
                if (testObject.equals(object)) iterator.set(newObject);
            }
        } catch (RuntimeException e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
        assertTrue(newObject.addChecked);
    }

    public void testConstrainedListIteratorAddOk() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, true);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(true, true);
        for (ListIterator iterator = list.listIterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (testObject.equals(object)) iterator.add(newObject);
        }
        assertTrue(list.contains(testObject));
        assertTrue(list.contains(newObject));
        assertTrue(newObject.addChecked);
        assertEquals(1, listener.eventCount());
        ListPropertyChangeEvent event = (ListPropertyChangeEvent) listener.getLastEvent(true);
        List expectedOld = new ArrayList(TEST_LIST);
        expectedOld.add(1, testObject);
        List expectedNew = new ArrayList(TEST_LIST);
        expectedNew.add(1, testObject);
        expectedNew.add(2, newObject);
        assertNotNull(event);
        assertEquals(ListPropertyChangeEvent.INSERT, event.getType());
        assertEquals(DEFAULT_PROPERTY, event.getPropertyName());
        assertEquals(expectedOld, event.getOldValue());
        assertEquals(expectedNew, event.getNewValue());
    }

    public void testConstrainedListIteratorAddDenied() {
        ArrayList wrappedList = new ArrayList(TEST_LIST);
        TestObject testObject = new TestObject(true, true);
        wrappedList.add(1, testObject);
        ObservableList list = getObservableList(wrappedList);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        list.addPropertyChangeListener(listener);
        TestObject newObject = new TestObject(false, true);
        for (ListIterator iterator = list.listIterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (testObject.equals(object)) iterator.add(newObject);
        }
        ArrayList expected = new ArrayList(TEST_LIST);
        expected.add(1, testObject);
        assertEquals(expected, list);
        assertFalse(listener.hasEvents());
        assertTrue(newObject.addChecked);
    }

    protected static class TestConstrainedList extends ConstrainedObservableList {

        public TestConstrainedList() {
            setList(new ArrayList());
        }

        public TestConstrainedList(String propertyName) {
            super(propertyName);
        }

        public TestConstrainedList(List list) {
            super(list);
        }

        public TestConstrainedList(String propertyName, List list) {
            super(propertyName, list);
        }

        protected String getPropertyName() {
            String propertyName = super.getPropertyName();
            if (propertyName == null) propertyName = "list";
            return propertyName;
        }

        protected boolean canAdd(Object object) {
            if (object instanceof TestObject) return (((TestObject) object).canAdd());
            return super.canAdd(object);
        }

        protected boolean canRemove(Object object) {
            if (object instanceof TestObject) return (((TestObject) object).canRemove());
            return super.canRemove(object);
        }
    }

    protected static class TestObject {

        protected boolean canAdd = true;

        protected boolean canRemove = true;

        protected boolean addChecked = false;

        protected boolean removeChecked = false;

        public TestObject(boolean canAdd, boolean canRemove) {
            this.canAdd = canAdd;
            this.canRemove = canRemove;
        }

        public boolean canAdd() {
            this.addChecked = true;
            return canAdd;
        }

        public boolean canRemove() {
            this.removeChecked = true;
            return canRemove;
        }
    }
}
