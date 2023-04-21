package com.windsor.node.common.service.admin;

import java.util.List;
import com.windsor.node.common.domain.NodeVisit;
import com.windsor.node.common.domain.ScheduledItem;

/**
 * 
 * @author mchmarny
 * 
 */
public interface ScheduleService {

    /**
     * Save a Schedule to the database.
     * 
     * @param instance
     *            the ScheduledItem to save
     * @param visit
     *            authenticated NodeVisit
     * @return the saved ScheduledItem
     */
    ScheduledItem save(ScheduledItem instance, NodeVisit visit);

    /**
     * Get a Schedule from the database by id.
     * 
     * @param scheduleId
     * @param visit
     *            authenticated NodeVisit
     * @return the saved ScheduledItem
     */
    ScheduledItem get(String scheduleId, NodeVisit visit);

    /**
     * Delete a Schedule from the database by id.
     * 
     * @param scheduleId
     * @param visit
     *            authenticated NodeVisit
     */
    void delete(String scheduleId, NodeVisit visit);

    /**
     * Get the list of all Schedules in the database.
     * 
     * @param visit
     *            authenticated NodeVisit
     * @return a List of ScheduledItems
     */
    List<ScheduledItem> get(NodeVisit visit);

    /**
     * Save a Schedule and flag it for running the next time the scheduler polls
     * the database.
     * 
     * @param instance
     *            the ScheduledItem to save
     * @param visit
     *            authenticated NodeVisit
     */
    void saveAndRunNow(ScheduledItem instance, NodeVisit visit);
}
