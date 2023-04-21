package net.sf.doolin.bus.jmx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import net.sf.doolin.bus.Bus;
import net.sf.doolin.bus.Subscriber;
import net.sf.doolin.bus.SubscriberInfo;
import org.apache.commons.lang.StringUtils;

public class JmxBusBean implements IJmxBusBean {

    private final Bus bus;

    public JmxBusBean() {
        this(Bus.get());
    }

    public JmxBusBean(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void clean() {
        this.bus.cleanUp();
    }

    protected IBusSubscription extractInformation(SubscriberInfo subscriberInfo) {
        Subscriber<?> subscriber = subscriberInfo.getSubscriber();
        String name = subscriber.toString();
        String type = subscriber.getClass().getName();
        type = StringUtils.substringBefore(StringUtils.substringAfterLast(type, "."), "$");
        return new BusSubscription(name, type, new Date(subscriberInfo.getRegistrationTime()));
    }

    @Override
    public int getSubscriptionCount() {
        return this.bus.getSubscribers().size();
    }

    @Override
    public List<IBusSubscription> getSubscriptionList() {
        Collection<SubscriberInfo> subscribers = this.bus.getSubscribers();
        List<IBusSubscription> subscriptions = new ArrayList<IBusSubscription>(subscribers.size());
        for (SubscriberInfo subscriber : subscribers) {
            IBusSubscription subscription = extractInformation(subscriber);
            subscriptions.add(subscription);
        }
        return subscriptions;
    }
}
