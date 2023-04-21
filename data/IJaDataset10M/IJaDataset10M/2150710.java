package com.xsm.lite.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.xsm.lite.event.RequestEvent.Processor;
import com.xsm.lite.util.Many2ManyMap;

/**
 * Manages Event.Listeners for the firing of Events.
 * 
 * Notes: 
 * Not Thread Safe.
 * Only holds Listeners, does NOT hold Events, only ability to fire them to interested listeners.
 * 
 * @author Sony Mathew
 */
public class EventManagerImpl implements EventManager {

    private Many2ManyMap<Class<? extends Event>, Event.Listener> eventClass2ListenerMap = new Many2ManyMap<Class<? extends Event>, Event.Listener>();

    private Map<Class<? extends Event>, RequestEvent.Processor> eventClass2ProcessorMap = new LinkedHashMap<Class<? extends Event>, RequestEvent.Processor>();

    private Many2ManyMap<Class, Event.Listener> eventCategory2ListenerMap = new Many2ManyMap<Class, Event.Listener>();

    /**
     * @see com.xsm.lite.event.EventManager#addListener(java.lang.Class, com.xsm.lite.event.Event.Listener)
     */
    public void addListener(Class<? extends Event> eventClass, Event.Listener l) {
        eventClass2ListenerMap.add(eventClass, l);
    }

    /**
     * @see com.xsm.lite.event.EventManager#addCategoryListener(java.lang.Class, com.xsm.lite.event.CategoryEvent.Listener)
     */
    public void addCategoryListener(Class<? extends Event> eventCategory, CategoryEvent.Listener l) {
        eventCategory2ListenerMap.add(eventCategory, l);
    }

    /**
     * @see com.xsm.lite.event.EventManager#removeListener(java.lang.Class, com.xsm.lite.event.Event.Listener)
     */
    public void removeListener(Class<? extends Event> eventClass, Event.Listener l) {
        eventClass2ListenerMap.remove(eventClass, l);
        eventCategory2ListenerMap.remove(eventClass, l);
    }

    /**
     * @see com.xsm.lite.event.EventManager#removeListener(com.xsm.lite.event.Event.Listener)
     */
    public void removeListener(Event.Listener l) {
        eventClass2ListenerMap.removeB(l);
        eventCategory2ListenerMap.removeB(l);
    }

    /**
     * @see com.xsm.lite.event.EventManager#removeListeners(java.lang.Class)
     */
    public void removeListeners(Class<? extends Event> eventClass) {
        eventClass2ListenerMap.removeA(eventClass);
        eventCategory2ListenerMap.removeA(eventClass);
    }

    /**
     * @see com.xsm.lite.event.EventManager#clear()
     */
    public void clear() {
        eventClass2ListenerMap.clear();
        eventClass2ProcessorMap.clear();
        eventCategory2ListenerMap.clear();
    }

    /**
     * Fires to all the CategoryEvent.Listeners that are registered for any of the given event's category heirarchy.
     * Categories are class types in the event's class heirarchy.
     * 
     * Note: Only Super classes form the class category heirarchy not interfaces.
     * Note: All Category Listnerers are notified regardles of whether the event was consumed.
     * Note: Order of Category Listeners notified are from narrowest to broadest super class.
     * 
     * author Sony Mathew
     */
    private void fireCategories(CategoryEvent ce, Class eventCategory) {
        Collection<Event.Listener> ls = eventCategory2ListenerMap.getB(eventCategory);
        if (ls != null && ls.size() > 0) {
            ls = new ArrayList<Event.Listener>(ls);
            for (Event.Listener l : ls) {
                ce.fire(l);
            }
        }
        if (eventCategory.equals(Event.class)) {
            return;
        }
        Class eventCategorySuper = eventCategory.getSuperclass();
        if (eventCategorySuper != null) {
            fireCategories(ce, eventCategorySuper);
        }
    }

    /**
     * @see com.xsm.lite.event.EventManager#fire(com.xsm.lite.event.Event)
     * 
     * First notifies all CategoryEvent.Listerers for the various categories (see fireCategories(CategoryEvent,Class))
     * If a CategoryEvent.Listener consumes the event it is not propogated to the Event's specific Listeners.
     * 
     * Note: All Category Listeners get a crack at the Event regardless of whether it was consumed, it the actual
     *  event itself that can be consumed and NOT propogated to its listeners. 
     *  
     *  @see fireCategories(CategoryEvent,Class)
     */
    public void fire(Event e) {
        CategoryEvent ce = new CategoryEvent(e);
        fireCategories(ce, e.getClass());
        if (ce.isConsumed()) {
            return;
        }
        Collection<Event.Listener> ls = eventClass2ListenerMap.getB(e.getClass());
        if (ls != null && ls.size() > 0) {
            ls = new ArrayList<Event.Listener>(ls);
            for (Event.Listener l : ls) {
                e.fire(l);
            }
        }
    }

    /**
     * @see com.xsm.lite.event.RequestEvent.Mediator#addProcessor(java.lang.Class, com.xsm.lite.event.RequestEvent.Processor)
     */
    public void addProcessor(Class<? extends RequestEvent> eventClass, RequestEvent.Processor p) {
        RequestEvent.Processor pcurr = eventClass2ProcessorMap.get(eventClass);
        if (pcurr != null) {
            throw new IllegalStateException("There is already a RequestEvent.Processor [" + pcurr.getClass() + "] registered for event type [" + eventClass + "]");
        }
        eventClass2ProcessorMap.put(eventClass, p);
    }

    /**
     * @see com.xsm.lite.event.RequestEvent.Mediator#removeProcessor(java.lang.Class, com.xsm.lite.event.RequestEvent.Processor)
     */
    public void removeProcessor(Class<? extends RequestEvent> eventClass, Processor p) {
        eventClass2ProcessorMap.remove(eventClass);
    }

    /**
     * @see com.xsm.lite.event.RequestEvent.Mediator#getProcessor(com.xsm.lite.event.RequestEvent)
     */
    public RequestEvent.Processor getProcessor(RequestEvent e) {
        RequestEvent.Processor p = eventClass2ProcessorMap.get(e.getClass());
        if (p == null) {
            throw new IllegalStateException("No RequestEvent.Processor registered for event type [" + e.getClass() + "]");
        }
        return p;
    }

    /**
     * @see com.xsm.lite.event.EventManager#snapshot()
     */
    public List<Object> snapshot() {
        List<Object> all = new ArrayList<Object>();
        all.addAll(eventClass2ListenerMap.getMapA2B().keySet());
        Collection<Set<Event.Listener>> lsCol = eventClass2ListenerMap.getMapA2B().values();
        for (Set<Event.Listener> ls : lsCol) {
            all.addAll(ls);
        }
        all.addAll(eventClass2ListenerMap.getMapB2A().keySet());
        Collection<Set<Class<? extends Event>>> evcCol = eventClass2ListenerMap.getMapB2A().values();
        for (Set<Class<? extends Event>> evc : evcCol) {
            all.addAll(evc);
        }
        all.addAll(eventCategory2ListenerMap.getMapA2B().keySet());
        Collection<Set<Event.Listener>> lsColCat = eventCategory2ListenerMap.getMapA2B().values();
        for (Set<Event.Listener> ls : lsColCat) {
            all.addAll(ls);
        }
        all.addAll(eventCategory2ListenerMap.getMapB2A().keySet());
        Collection<Set<Class>> evcColCat = eventCategory2ListenerMap.getMapB2A().values();
        for (Set<Class> evc : evcColCat) {
            all.addAll(evc);
        }
        all.addAll(eventClass2ProcessorMap.keySet());
        all.addAll(eventClass2ProcessorMap.values());
        return all;
    }
}
