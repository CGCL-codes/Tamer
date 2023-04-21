package org.liris.schemerger.core.dataset;

import java.util.LinkedList;
import org.liris.schemerger.core.event.EDate;
import org.liris.schemerger.core.event.IEvent;

/**
 * A stream of events. Meant for algorithms that count the pattern frequency
 * over a database of sequence, not in a single sequence of events. (SPADE,
 * PrefixSpan, Apriori, etc.)
 * 
 * @author Damien Cram
 * 
 * @param <E>
 *            the type of event
 */
public abstract class SequenceStream<E extends IEvent> {

    private LinkedList<SequenceStreamListener<E>> listeners = new LinkedList<SequenceStreamListener<E>>();

    public void addEventListener(SequenceStreamListener<E> listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeEventListener(SequenceStreamListener<E> listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners(E event) {
        for (SequenceStreamListener<E> l : listeners) l.newEvent(event);
    }

    public abstract EDate getCurrentDate();

    public abstract void start();
}
