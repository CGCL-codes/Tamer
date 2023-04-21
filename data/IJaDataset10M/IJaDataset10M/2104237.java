package jxmpp.com.code.google.core.events;

import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ternovykh
 * Date: 27.07.11
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public class CompositeEventAggregator implements EventAggregator {

    private static Logger log = Logger.getLogger(CompositeEventAggregator.class.getName());

    public CompositeEventAggregator() {
        log.info("Creating composite event aggegator");
        subscriberHashMap = new ConcurrentHashMap<String, ArrayList<EventSubscriber>>();
    }

    public void publish(CompositeEvent event) {
        if (event == null) return;
        String eventTypeName = getEventTypeName(event.getClass());
        ArrayList<EventSubscriber> subscribers = subscriberHashMap.get(eventTypeName);
        if (subscribers != null) {
            for (EventSubscriber s : subscribers) {
                try {
                    s.processEvent(event);
                } catch (Exception ex) {
                    log.error("Error while publishing event", ex);
                }
            }
        }
    }

    public void subscribe(EventSubscriber subscriber, Class event) {
        if (subscriber == null || event == null) return;
        String eventTypeName = getEventTypeName(event);
        ArrayList<EventSubscriber> subscribers = getSubscibers(event);
        if (subscribers == null) {
            subscribers = new ArrayList<EventSubscriber>();
            subscribers.add(subscriber);
            subscriberHashMap.put(eventTypeName, subscribers);
        } else {
            if (!subscribers.contains(subscriber)) {
                subscribers.add(subscriber);
            }
        }
    }

    public void unsubscribe(EventSubscriber subscriber, Class event) {
        if (subscriber == null || event == null) return;
        ArrayList<EventSubscriber> subscribers = getSubscibers(event);
        if (subscribers != null && subscribers.contains(subscriber)) {
            subscribers.remove(subscriber);
        }
    }

    private String getEventTypeName(Class event) {
        return event.getName();
    }

    private ArrayList<EventSubscriber> getSubscibers(Class event) {
        return subscriberHashMap.get(getEventTypeName(event));
    }

    private ConcurrentHashMap<String, ArrayList<EventSubscriber>> subscriberHashMap;
}
