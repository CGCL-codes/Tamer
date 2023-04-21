package net.sf.btb;

import java.util.HashSet;

/**
 * A set used to contain and call QueryEventHandlers. Note that the call order
 * will not fit with the handler addition.
 * 
 * @author Jean-Philippe Gravel
 * @param <T>
 *            The type of the item handled.
 */
public class QueryEventHandlerSet<T> extends HashSet<QueryEventHandler<T>> implements QueryEventHandler<T> {

    private static final long serialVersionUID = 0x90FB56BB5379A47FL;

    public <E extends T> void handle(QueryEvent<E> action) {
        for (QueryEventHandler<T> handler : this) {
            handler.handle(action);
        }
    }
}
