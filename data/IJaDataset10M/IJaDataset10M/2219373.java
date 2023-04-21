package net.sf.doolin.bus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import net.sf.doolin.bus.support.DefaultBusLog;
import net.sf.doolin.bus.support.SubscriberAdapter;
import net.sf.doolin.bus.support.SubscriberValidator;

/**
 * Default implementation for a bus.
 * 
 * @author Damien Coraboeuf
 */
public class DefaultBus extends Bus implements BusLog {

    /**
	 * The bus log
	 */
    private BusLog busLog = new DefaultBusLog();

    /**
	 * Registered processors
	 */
    private Set<Subscription<?>> subscriptions = Collections.synchronizedSet(new TreeSet<Subscription<?>>());

    /**
	 * Queue of messages
	 */
    private LinkedList<Object> messageQueue = new LinkedList<Object>();

    /**
	 * Loops over all subscriptions and removes those which are no longer valid.
	 * 
	 * @see Subscriber#isValid()
	 */
    @Override
    public synchronized void cleanUp() {
        Iterator<Subscription<?>> iterator = this.subscriptions.iterator();
        while (iterator.hasNext()) {
            Subscription<?> subscription = iterator.next();
            Subscriber<?> subscriber = subscription.getSubscriber();
            if (!subscriber.isValid()) {
                unregistration(subscriber);
                iterator.remove();
            }
        }
    }

    @Override
    public synchronized void clear() {
        this.subscriptions.clear();
    }

    @Override
    public BusLog getBusLog() {
        return this.busLog;
    }

    @Override
    public Collection<SubscriberInfo> getSubscribers() {
        cleanUp();
        ArrayList<SubscriberInfo> subscribers = new ArrayList<SubscriberInfo>();
        for (Subscription<?> subscription : this.subscriptions) {
            Subscriber<?> subscriber = subscription.getSubscriber();
            long time = subscription.getRegistrationTime();
            subscribers.add(new SubscriberInfo(subscriber, time));
        }
        return subscribers;
    }

    @Override
    public <M> Collection<Subscriber<M>> getSubscribers(M message) {
        ArrayList<Subscriber<M>> subscribers = new ArrayList<Subscriber<M>>();
        for (Subscription<?> subscription : this.subscriptions) {
            if (subscription.accept(message)) {
                @SuppressWarnings("unchecked") Subscriber<M> subscriber = (Subscriber<M>) subscription.getSubscriber();
                if (subscriber.isValid()) {
                    subscribers.add(subscriber);
                }
            }
        }
        return subscribers;
    }

    /**
	 * Get a string representation of a message
	 * 
	 * @param message
	 *            Message to display
	 * @return String representation
	 */
    public String getTrace(Object message) {
        return message.getClass().getSimpleName() + " (" + message + ")";
    }

    public <M> void messageAccepted(M message, Subscriber<M> subscriber) {
        if (this.busLog != null) {
            this.busLog.messageAccepted(message, subscriber);
        }
    }

    public void messageNoSubscription(Object message) {
        if (this.busLog != null) {
            this.busLog.messageNoSubscription(message);
        }
    }

    public void messageReceived(Object message) {
        if (this.busLog != null) {
            this.busLog.messageReceived(message);
        }
    }

    public <M> void messageWithError(M message, Subscriber<M> subscriber, Throwable th) {
        if (this.busLog != null) {
            this.busLog.messageWithError(message, subscriber, th);
        }
    }

    /**
	 * Process a message
	 * 
	 * @param message
	 *            Message to process
	 * @param <M>
	 *            Message type
	 */
    protected <M> void process(M message) {
        messageReceived(message);
        cleanUp();
        Object[] processors;
        synchronized (this.subscriptions) {
            processors = this.subscriptions.toArray();
        }
        if (processors.length == 0) {
            messageNoSubscription(message);
        } else {
            boolean ok = true;
            for (int i = 0; ok && i < processors.length; i++) {
                Subscription<?> subscription = (Subscription<?>) processors[i];
                if (subscription.accept(message)) {
                    @SuppressWarnings("unchecked") Subscriber<M> subscriber = (Subscriber<M>) subscription.getSubscriber();
                    messageAccepted(message, subscriber);
                    try {
                        subscriber.receive(message);
                    } catch (Exception ex) {
                        messageWithError(message, subscriber, ex);
                        ok = false;
                    }
                }
            }
        }
        processContinue();
    }

    /**
	 * Continue the processing
	 */
    protected void processContinue() {
        Object message = null;
        synchronized (this.messageQueue) {
            if (!this.messageQueue.isEmpty()) {
                message = this.messageQueue.removeFirst();
            }
        }
        if (message != null) {
            process(message);
        }
    }

    @Override
    public <Message> void publish(Message message) {
        boolean empty;
        synchronized (this.messageQueue) {
            empty = this.messageQueue.isEmpty();
        }
        if (empty) {
            process(message);
        } else {
            synchronized (this.messageQueue) {
                this.messageQueue.addLast(message);
            }
            processContinue();
        }
    }

    public void registrationFailed(Subscriber<?> subscriber, Priority priority) {
        if (this.busLog != null) {
            this.busLog.registrationFailed(subscriber, priority);
        }
    }

    public void registrationOk(Subscriber<?> subscriber, Priority priority) {
        if (this.busLog != null) {
            this.busLog.registrationOk(subscriber, priority);
        }
    }

    @Override
    public void setBusLog(BusLog busLog) {
        this.busLog = busLog;
    }

    @Override
    public synchronized <M> void subscribe(Subscriber<M> subscriber) {
        subscribe(subscriber, Priority.NORMAL);
    }

    @Override
    public synchronized <M> void subscribe(Subscriber<M> subscriber, Priority priority) {
        Subscription<M> subscription = new Subscription<M>(subscriber, priority);
        boolean ok = this.subscriptions.add(subscription);
        if (ok) {
            registrationOk(subscriber, priority);
        } else {
            registrationFailed(subscriber, priority);
        }
    }

    @Override
    public <M> void subscribe(SubscriberValidator validator, SubscriberTrigger trigger, SubscriberExecution<M> execution) {
        subscribe(new SubscriberAdapter<M>(validator, trigger, execution));
    }

    public void unregistration(Subscriber<?> subscriber) {
        if (this.busLog != null) {
            this.busLog.unregistration(subscriber);
        }
    }

    @Override
    public synchronized void unsubscribe(Subscriber<?> subscriber) {
        Iterator<Subscription<?>> iterator = this.subscriptions.iterator();
        while (iterator.hasNext()) {
            Subscription<?> subscription = iterator.next();
            Subscriber<?> s = subscription.getSubscriber();
            if (s == subscriber) {
                iterator.remove();
            }
        }
    }
}
