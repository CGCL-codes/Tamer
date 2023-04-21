package de.javagimmicks.collections.event;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Test;
import de.javagimmicks.collections.event.MapEvent.Type;

public class EventMapTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testPutAndRemove() {
        Map<String, String> map = new TreeMap<String, String>();
        ObservableEventMap<String, String> eventMap = new ObservableEventMap<String, String>(map);
        EventMapListener<String, String> mockListener = createStrictMock(EventMapListener.class);
        eventMap.addEventMapListener(mockListener);
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.ADDED, "1", "A"));
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.ADDED, "2", "B"));
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.ADDED, "3", "C"));
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.UPDATED, "1", "A", "B"));
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.UPDATED, "2", "B", "A"));
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.REMOVED, "1", "B"));
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.REMOVED, "2", "A"));
        mockListener.eventOccured(new MapEvent<String, String>(eventMap, Type.REMOVED, "3", "C"));
        replay(mockListener);
        assertNull(eventMap.put("1", "A"));
        assertEquals(eventMap, map);
        assertNull(eventMap.put("2", "B"));
        assertEquals(eventMap, map);
        assertNull(eventMap.put("3", "C"));
        assertEquals(eventMap, map);
        assertEquals("A", eventMap.entrySet().iterator().next().setValue("B"));
        assertEquals(eventMap, map);
        assertEquals("B", eventMap.put("2", "A"));
        assertEquals(eventMap, map);
        assertTrue(eventMap.keySet().remove("1"));
        assertEquals(eventMap, map);
        assertEquals("A", eventMap.remove("2"));
        assertEquals(eventMap, map);
        assertTrue(eventMap.values().remove("C"));
        assertEquals(eventMap, map);
        verify(mockListener);
    }
}
