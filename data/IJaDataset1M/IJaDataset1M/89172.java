package event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Takes care of dispatching events to multiple listeners.
 * 
 * @author Bernhard Bodenstorfer
 */
public class SourceAdapter<E extends Event> implements Source<E> {

    /** The list of registered listeners. */
    private final Collection<Listener<? super E>> listeners;

    /** Construct with empty listeners list. */
    protected SourceAdapter() {
        listeners = Collections.synchronizedCollection(new ArrayList<Listener<? super E>>());
    }

    @Override
    public void register(final Listener<? super E> listener) {
        listeners.add(listener);
    }

    /** Distribute an event to all current listeners. */
    protected void dispatch(final E event) {
        for (final Iterator<Listener<? super E>> iterator = listeners.iterator(); iterator.hasNext(); ) {
            final Listener<? super E> listener = iterator.next();
            listener.receive(event);
        }
    }
}
