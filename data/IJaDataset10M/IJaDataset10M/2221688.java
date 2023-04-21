package org.hironico.dbtool2.querymanager;

import java.io.File;

/**
 * Décrit un évennement qui se produit sur les operations du query manager.
 * @author hironico
 * @since 2.0.0
 */
public class QueryManagerEvent {

    public enum EventType {

        QUERY_ADDED, QUERY_REMOVED, QUERIES_LOADED
    }

    ;

    protected EventType eventType = EventType.QUERIES_LOADED;

    protected File query = null;

    public void setEventType(EventType type) {
        this.eventType = type;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setQuery(File query) {
        this.query = query;
    }

    public File getQuery() {
        return query;
    }

    @Override
    public String toString() {
        if (query != null) return eventType.toString() + " : " + query.getAbsolutePath(); else return eventType.toString();
    }
}
