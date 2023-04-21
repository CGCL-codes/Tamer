package thinwire.apps.formcreator;

import java.util.*;

/**
 * @author Joshua J. Gertzen
 */
class MessageBus {

    enum Id {

        ADD_PROJECT_OBJECT, DELETE_PROJECT_OBJECT, SET_ACTIVE_PROJECT, SET_ACTIVE_PROPERTY_OBJECT, OPEN_PROJECT, SAVE_PROJECT, IMPORT_XOD, OPEN_DESIGN_SHEET, CLOSE_DESIGN_SHEET, SET_PROPERTY_SHEET_VALUE, ADD_TOOLBOX_DRAG_DROP, REMOVE_TOOLBOX_DRAG_DROP
    }

    static interface Listener {

        public void eventOccured(Event e);
    }

    static class Event {

        private Id id;

        private Object data;

        public Event(Id id) {
            this(id, null);
        }

        public Event(Id id, Object data) {
            if (id == null) throw new IllegalArgumentException("id == null");
            this.id = id;
            this.data = data;
        }

        public Id getId() {
            return id;
        }

        public Object getData() {
            return data;
        }
    }

    private Map<Id, Set<Listener>> listeners = new HashMap<Id, Set<Listener>>();

    public void addListener(Id id, Listener listener) {
        if (id == null) throw new IllegalArgumentException("id == null");
        if (listener == null) throw new IllegalArgumentException("listener == null");
        Set<Listener> set = listeners.get(id);
        if (set == null) listeners.put(id, set = new HashSet<Listener>());
        set.add(listener);
    }

    public void removeListener(Id id, Listener listener) {
        if (id == null) throw new IllegalArgumentException("id == null");
        if (listener == null) throw new IllegalArgumentException("listener == null");
        Set<Listener> set = listeners.get(id);
        if (set == null) return;
        set.remove(listener);
    }

    public void fireEvent(Id id, Object data) {
        fireEvent(new Event(id, data));
    }

    public void fireEvent(Event event) {
        if (event == null) throw new IllegalArgumentException("event == null");
        Set<Listener> set = listeners.get(event.getId());
        if (set != null) {
            for (Listener l : set.toArray(new Listener[set.size()])) {
                l.eventOccured(event);
            }
        }
    }
}
