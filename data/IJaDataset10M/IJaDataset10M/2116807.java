package org.peertrust.event;

import org.peertrust.net.Query;

/**
 * <p>
 * Event that represents a query message.
 * </p><p>
 * $Id: QueryEvent.java,v 1.5 2005/08/09 13:47:55 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/09 13:47:55 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class QueryEvent extends NewMessageEvent {

    /**
	 * 
	 */
    Query _query;

    public QueryEvent(Object source, Query query) {
        super(source, query);
        _query = query;
    }

    public Query getQuery() {
        return _query;
    }
}
