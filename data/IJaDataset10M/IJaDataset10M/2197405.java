package adlez.logic.events;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

public class EventManager {

    protected HashMap<Class, LinkedList<EventListener>> listeners;

    protected static EventManager instance;

    private EventManager() {
        this.listeners = new HashMap<Class, LinkedList<EventListener>>();
    }

    public static EventManager getInstance() {
        if (EventManager.instance == null) {
            EventManager.instance = new EventManager();
        }
        return EventManager.instance;
    }

    public void registerFor(Class eventType, EventListener listener) {
        if (this.findMethod(listener, eventType) == null) {
            throw new RuntimeException("listener does not implement handle*() method for " + eventType);
        }
        if (!this.listeners.containsKey(eventType)) {
            this.listeners.put(eventType, new LinkedList<EventListener>());
        }
        this.listeners.get(eventType).add(listener);
    }

    public void unregisterFor(Class eventType, EventListener listener) {
        if (this.listeners.containsKey(eventType)) {
            this.listeners.get(eventType).remove(listener);
        }
    }

    public void fireEvent(Event e) {
        LinkedList<EventListener> currentListeners = this.listeners.get(e.getClass());
        if (currentListeners != null) {
            for (EventListener l : currentListeners) {
                Method m = findMethod(l, e.getClass());
                try {
                    m.invoke(l, e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private Method findMethod(EventListener listener, Class<?> eventType) {
        Class<?> clazz = listener.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("handle")) {
                Class<?>[] params = m.getParameterTypes();
                if (params.length == 1 && params[0] == eventType) {
                    return m;
                }
            }
        }
        return null;
    }
}
