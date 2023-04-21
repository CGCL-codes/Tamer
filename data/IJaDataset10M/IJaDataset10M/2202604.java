package gov.sandia.ccaffeine.dc.user_iface.MVC.event;

import java.util.EventObject;

/**
 * Used to notify components that an entity
 * wants to send a query.
 * A view entity might
 * respond by sending back a
 * ResultSetEvent.
 */
public class QueryEvent extends EventObject {

    static final long serialVersionUID = 1;

    protected Object query = null;

    /**
     * Retrieve the query.
     * @return The query.
     */
    public Object getQuery() {
        return (this.query);
    }

    protected ResultSetListener requester = null;

    /**
     * Retrieve the entity that sent the query.
     * The response to the query will be sent to the requester
     * of the query.
     * @return The entity that sent the query.
     */
    public ResultSetListener getRequester() {
        return (this.requester);
    }

    /**
     * Create a QueryEvent.
     * Used to notify components that an entity
     * wants to send a query.
     * A view entity might
     * respond by sending back a
     * ResultSetEvent.
     * @param source The entity that created this event.
     * @param query The query
     * @param requester The entity that sent the query.
     * The response to the query will be sent to the requester
     * of the query.
     */
    public QueryEvent(Object source, Object query, ResultSetListener requester) {
        super(source);
        this.query = query;
        this.requester = requester;
    }
}
