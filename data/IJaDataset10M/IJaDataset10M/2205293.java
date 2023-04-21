package pl.olek.textmash.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * 
 * @author anaszko
 *
 */
public class EventDispatcher {

    private static EventDispatcher singleton;

    public static synchronized EventDispatcher getInstance() {
        if (singleton == null) {
            singleton = new EventDispatcher();
        }
        return singleton;
    }

    public static final String WORKSPACE_IS_DIRTY = "WORKSPACE_IS_DIRTY";

    public static final String WORKSPACE_SAVED = "WORKSPACE_SAVED";

    public static final String REMOTE_CHANGED = "REMOTE_CHANGED";

    HashMap<Object, TreeMap<String, HashSet<EventListener>>> registry = new HashMap<Object, TreeMap<String, HashSet<EventListener>>>();

    public synchronized boolean fire(Object group, String name) {
        TreeMap<String, HashSet<EventListener>> listeners = registry.get(group);
        if (listeners == null) {
            if (group != null) {
                return fire(null, name);
            }
            return true;
        }
        HashSet<EventListener> events = listeners.get(name);
        if (events == null) {
            if (group != null) {
                return fire(null, name);
            }
            return true;
        }
        Event event = new Event(name);
        for (EventListener e : events) {
            e.action(event);
            if (event.isAborted()) {
                return false;
            }
        }
        if (group != null) {
            return fire(null, name);
        }
        return true;
    }

    public synchronized void register(Object group, EventListener listener, String... names) {
        for (String name : names) {
            register(group, name, listener);
        }
    }

    public synchronized void register(EventListener listener, String... names) {
        for (String name : names) {
            register(null, name, listener);
        }
    }

    public synchronized void register(Object group, String name, EventListener listener) {
        TreeMap<String, HashSet<EventListener>> listeners = registry.get(group);
        if (listeners == null) {
            listeners = new TreeMap<String, HashSet<EventListener>>();
            registry.put(group, listeners);
        }
        HashSet<EventListener> events = listeners.get(name);
        if (events == null) {
            events = new HashSet<EventListener>();
            listeners.put(name, events);
        }
        events.add(listener);
    }

    public synchronized void unregister(EventListener listener) {
        for (TreeMap<String, HashSet<EventListener>> entry : registry.values()) {
            for (HashSet<EventListener> events : entry.values()) {
                events.remove(listener);
            }
        }
    }
}
