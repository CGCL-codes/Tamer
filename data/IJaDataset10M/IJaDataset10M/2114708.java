package org.columba.calendar.store;

import java.io.File;
import junit.framework.TestCase;
import org.columba.calendar.base.UUIDGenerator;
import org.columba.calendar.model.Event;
import org.columba.calendar.model.api.IComponent;

public class LocalCalendarStoreTest extends TestCase {

    private File file;

    private LocalCalendarStore storage;

    protected void setUp() throws Exception {
        file = new File("test_calendar");
        storage = new LocalCalendarStore(file);
    }

    public void testAddGet() throws Exception {
        Event model = new Event();
        model.setSummary("summary");
        model.setDescription("description");
        String uuid = new UUIDGenerator().newUUID();
        storage.add(model);
        boolean exists = storage.exists(uuid);
        assertTrue(exists);
        IComponent result = storage.get(uuid);
        assertNotNull(result);
    }

    public void testRemove() throws Exception {
        Event model = new Event();
        model.setSummary("summary");
        model.setDescription("description");
        String uuid = new UUIDGenerator().newUUID();
        storage.add(model);
        storage.remove(uuid);
        IComponent result = storage.get(uuid);
        assertNull(result);
    }

    protected void tearDown() throws Exception {
    }
}
