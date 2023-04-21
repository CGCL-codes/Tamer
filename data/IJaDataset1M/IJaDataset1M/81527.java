package com.google.code.donkirkby.events;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import com.google.code.donkirkby.events.dao.EventDao;

public class EventServiceTest extends MockObjectTestCase {

    public void testCreate() throws Exception {
        Event event = new Event();
        EventServiceImpl service = new EventServiceImpl();
        Mock mockDao = mock(EventDao.class);
        service.setEventDao((EventDao) mockDao.proxy());
        mockDao.expects(once()).method("store").with(eq(event));
        service.store(event);
    }
}
