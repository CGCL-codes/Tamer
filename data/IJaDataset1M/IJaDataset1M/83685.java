package name.huzhenbo.hibernate.service;

import name.huzhenbo.hibernate.domain.Event;
import name.huzhenbo.hibernate.service.EventManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;

public class EventManagerTest {

    private EventManager eventManager;

    @Before
    public void setup() {
        eventManager = new EventManager();
    }

    @Test
    public void should_store_event() {
        assertNotNull(eventManager.createAndStoreEvent("FirstEvent", new Date()));
    }

    @Test
    public void should_store_person() {
        assertNotNull(eventManager.createAndStorePerson("Jeccia", "Xia", 32));
    }

    @Test
    public void should_list() {
        List events = eventManager.listEvents();
        assertEquals(1, events.size());
        for (int i = 0; i < events.size(); i++) {
            Event theEvent = (Event) events.get(i);
            assertEquals("FirstEvent", theEvent.getTitle());
        }
    }

    @Test
    public void should_add_person_to_event() {
        Long eventId = eventManager.createAndStoreEvent("My Event", new Date());
        Long personId = eventManager.createAndStorePerson("Foo", "Bar", 0);
        assertNotNull(eventId);
        assertNotNull(personId);
        eventManager.addPersonToEvent(personId, eventId);
    }
}
