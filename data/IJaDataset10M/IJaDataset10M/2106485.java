package jxmpp.com.code.google.core.events;

/**
 * Created by IntelliJ IDEA.
 * User: ternovykh
 * Date: 27.07.11
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public interface EventAggregator {

    void publish(CompositeEvent event);

    void subscribe(EventSubscriber subscriber, Class event);

    void unsubscribe(EventSubscriber subscriber, Class event);
}
