package org.gwtcmis.service.discovery.event;

import org.gwtcmis.model.restatom.EntryCollection;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event is fired when search results received.
 * 
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Anna Zhuleva</a>
 * @version $Id:
 */
public class QueryResultReceivedEvent extends GwtEvent<QueryResultReceivedHandler> {

    /**
    * Type.
    */
    public static final GwtEvent.Type<QueryResultReceivedHandler> TYPE = new GwtEvent.Type<QueryResultReceivedHandler>();

    /**
    * Results of the query.
    */
    private EntryCollection queryResults;

    /**
    * @param entryCollection entryCollection
    */
    public QueryResultReceivedEvent(EntryCollection entryCollection) {
        this.queryResults = entryCollection;
    }

    /**
    * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
    * 
    * @param handler handler
    * 
    */
    @Override
    protected void dispatch(QueryResultReceivedHandler handler) {
        handler.onQueryResultReceived(this);
    }

    /**
    * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    * 
    * @return Type {@link QueryResultReceivedHandler}
    */
    @Override
    public Type<QueryResultReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
    * @return the queryResults
    */
    public EntryCollection getQueryResults() {
        return queryResults;
    }
}
