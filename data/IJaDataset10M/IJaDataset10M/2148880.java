package com.ixora.rms.agents.sqlserver.events;

/**
 * TracedEventColumn
 * Simple holder for a event/column association.
 */
public class TracedEventColumn {

    public int eventID;

    public int columnID;

    /**
     * @param eventID
     * @param columnID
     */
    public TracedEventColumn(int eventID, int columnID) {
        super();
        this.eventID = eventID;
        this.columnID = columnID;
    }
}
