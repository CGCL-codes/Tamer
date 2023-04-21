package org.briareo.persistence.service.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.briareo.persistence.dao.EventDao;
import org.briareo.persistence.model.Event;
import org.briareo.persistence.service.EventManager;

/**
 * 
 * 
 * @author Javier Aparicio
 *
 */
public class EventManagerImpl implements EventManager {

    /**
   * Class Logger
   */
    private final Log log = LogFactory.getLog(EventManagerImpl.class);

    private EventDao dao;

    public void setEventDao(EventDao dao) {
        this.dao = dao;
    }

    public Event getEvent(String id) {
        return dao.getEvent(Long.parseLong(id));
    }

    public List<Event> getEvents() {
        List<Event> list = dao.getEvents();
        return list;
    }

    public List<Event> getEvents(Event filter) {
        List<Event> list = dao.getEvents(filter);
        return list;
    }

    public void removeEvent(String id) {
        Event e = new Event();
        e.setId(Long.parseLong(id));
        dao.removeEvent(e);
    }

    public void saveEvent(Event e) {
        log.debug("Event to be saved: " + e);
        dao.saveEvent(e);
    }
}
