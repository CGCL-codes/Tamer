package ru.whitesoft;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Thic class implements common listeners handling tasks.
 */
public class ListenersHandler<EventListener> extends AbstractCollection<EventListener> {

    public static interface Notifier<EventListener> {

        void notify(EventListener listener);
    }

    private Collection<EventListener> listeners;

    public ListenersHandler() {
        this.listeners = new HashSet<EventListener>();
    }

    ListenersHandler(Collection<? extends EventListener> listeners) {
        this.listeners = new HashSet<EventListener>(listeners);
    }

    public void fireEvent(Notifier<EventListener> notifier) {
        Collection<EventListener> clonedListeners = new LinkedList<EventListener>(listeners);
        for (EventListener listener : clonedListeners) {
            notifier.notify(listener);
        }
    }

    @Override
    public Iterator<EventListener> iterator() {
        return listeners.iterator();
    }

    @Override
    public int size() {
        return listeners.size();
    }

    @Override
    public boolean add(EventListener e) {
        return listeners.add(e);
    }
}
