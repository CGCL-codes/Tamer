package org.ikasan.systemevent.dao;

import java.util.Date;
import org.ikasan.spec.search.PagedSearchResult;
import org.ikasan.systemevent.model.SystemEvent;

/**
 * Data access interface for persistence of <code>SystemFlowEventDao</code>
 * 
 * @author Ikasan Development Team
 *
 */
public interface SystemEventDao {

    /**
	 * Persists a new system event
	 * 
	 * @param systemFlowEvent
	 */
    public void save(SystemEvent systemEvent);

    /**
	 * Performs a paged search for <code>SystemFlowEvent</code>s restricting by criteria fields as supplied
	 * 
	 * @param pageNo - page control field - page no of results to return
	 * @param pageSize - page control field - size of page
	 * @param orderBy - page control - field to order by
	 * @param orderAscending - page control field - true/false results in ascending order with respect to orderBy field
	 * @param subject - criteria field - filter for exact match on subject
	 * @param action - criteria field - filter for exact match on action
	 * @param timestampFrom - criteria field - filter for events with timestamp greater than this value
	 * @param timestampTo - criteria field - filter for events with timestamp less than this value
	 * @param actor - criteria field - filter for exact match on actor
	 * 
	 * @return PagedSearchResult<SystemFlowEvent> - page friendly search result subset
	 */
    public PagedSearchResult<SystemEvent> find(final int pageNo, final int pageSize, final String orderBy, final boolean orderAscending, String subject, String action, Date timestampFrom, Date timestampTo, String actor);

    /**
	 * Deletes all expired system events
	 */
    public void deleteExpired();
}
