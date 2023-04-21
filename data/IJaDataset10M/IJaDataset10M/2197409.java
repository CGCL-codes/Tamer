package com.hack23.cia.service.impl.common;

import java.util.List;
import com.hack23.cia.service.dao.EventDAO;

/**
 * The Class ActivityServiceImpl.
 */
public class ActivityServiceImpl implements ActivityService {

    /** The event dao. */
    private final EventDAO eventDAO;

    /**
	 * Instantiates a new activity service impl.
	 * 
	 * @param eventDAO
	 *            the event dao
	 */
    public ActivityServiceImpl(final EventDAO eventDAO) {
        super();
        this.eventDAO = eventDAO;
    }

    @SuppressWarnings("unchecked")
    public final List getActionEventHistory() {
        return eventDAO.getActionEventHistory();
    }

    @SuppressWarnings("unchecked")
    public final List getResponseTimeHistory() {
        return eventDAO.getResponseTimeHistory();
    }
}
