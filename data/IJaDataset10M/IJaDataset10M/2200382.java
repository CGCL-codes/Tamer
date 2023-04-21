package edu.ucla.cs.typecast.event;

public class EventSinkImpl implements EventSink {

    private EventDispatcher dispatcher;

    private Class[] types;

    public EventSinkImpl(Class[] types, EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.types = types;
    }

    public void addEventListener(EventListener listener, Object handback) {
        dispatcher.addEventListener(types, listener, handback);
    }

    public void removeEventListener(EventListener listener, Object handback) {
        dispatcher.removeEventListener(listener);
    }
}
