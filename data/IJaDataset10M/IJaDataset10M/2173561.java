package net.sf.btb;

/**
 * Interface used to handle Query events.
 * 
 * @author Jean-Philippe Gravel
 * @param <T>
 *            The type of the object on witch the query is operating on.
 */
public interface QueryEventHandler<T> {

    /**
     * Handles a Query event.
     * 
     * @param action
     *            Information about the event that occured.
     */
    public <E extends T> void handle(QueryEvent<E> action);
}
