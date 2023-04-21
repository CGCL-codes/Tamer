package com.sh.architecture.util;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

public abstract class EventListenerSupport<ListenerClass extends EventListener, EventClass extends EventObject> {

    private Set<ListenerClass> listeners = new HashSet<ListenerClass>();

    public void addListener(ListenerClass listener) {
        listeners.add(listener);
    }

    public void removeListener(ListenerClass listener) {
        listeners.remove(listener);
    }

    public void fireStateChanged(EventClass event) {
        for (ListenerClass listener : listeners) {
            fireEvent(listener, event);
        }
    }

    protected abstract void fireEvent(ListenerClass listener, EventClass event);

    public Set<ListenerClass> getChangeListeners() {
        return listeners;
    }
}
