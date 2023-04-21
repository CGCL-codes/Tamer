package org.metawb.lib;

import com.google.common.collect.MapMaker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.WeakSet;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * EventBus implementation based on Lookup. This class is threadsafe.
 *
 * @author Ernie Rael {@literal <metawb at raelity.com>}
 */
public class EventBus {

    private static final Logger LOG = Logger.getLogger(EventBus.class.getName());

    private final ConcurrentMap<Queue, Consumer> consumers = new MapMaker().weakKeys().makeMap();

    private final WeakSet<Driver> producers = new WeakSet<Driver>();

    private class Consumer {

        DataListener listener;

        Collection<Class<?>> clazzes;

        private Consumer(DataListener listener, Collection<Class<?>> clazzes) {
            this.listener = listener;
            this.clazzes = clazzes;
        }
    }

    /**
     * Use this to produce events on an EventBus. It may be used on
     * multiple busses.
     * @param <T>
     */
    public static class Driver<T> {

        private final Lookup lookup;

        private final InstanceContent ic;

        private final Lookup.Result<T> lookupResult;

        /**
         * Create an event bus driver.
         * @param <E> The type of events
         * @param clazz the class/superclass of events that are sent
         * @return the driver used to transmit events
         */
        public static <E> Driver<E> newDriver(Class<?> clazz) {
            @SuppressWarnings("unchecked") Driver<E> p = new Driver(clazz);
            return p;
        }

        private Driver(Class<T> clazz) {
            ic = new InstanceContent();
            lookup = new AbstractLookup(ic);
            lookupResult = lookup.lookupResult(clazz);
        }

        /**
         * Send an event.
         * @param o the event
         */
        public void xmit(T o) {
            synchronized (Driver.class) {
                ic.add(o);
                ic.remove(o);
            }
        }

        private Lookup.Result<T> getLookupResult() {
            return lookupResult;
        }
    }

    public static interface DataListener<T> {

        public void dataAvailable(Queue<T> q);
    }

    /**
     * Add a driver/source of events for this EventBus.
     * @param <T> type of events involved
     * @param p events from the driver are delivered through here
     */
    public <T> void addDriver(Driver p) {
        synchronized (producers) {
            boolean itsNew = producers.add(p);
            if (itsNew) p.getLookupResult().addLookupListener(lkListen);
        }
    }

    public <T> void removeDriver(Driver p) {
        synchronized (producers) {
            p.getLookupResult().removeLookupListener(lkListen);
            producers.remove(p);
        }
    }

    /**
     * Register a {@link Queue} to recieve events that are subclasses of
     * the events supplied in the collection. {@link Queue#offer(java.lang.Object)}
     * is used to add events to the Q; if the event is not added then
     * it is dropped.
     * The optional listener
     * is invoked whether the data is actually added or not.
     * <p/>The q is referenced weakly.
     * @param q events added here.
     * @param listener optional
     * @param c the event types of interest
     */
    public void addReceiver(Queue q, DataListener listener, Collection<Class<?>> c) {
        consumers.put(q, new Consumer(listener, new ArrayList<Class<?>>(c)));
    }

    public void removeReceiver(Queue q) {
        consumers.remove(q);
    }

    @SuppressWarnings("unchecked")
    LookupListener lkListen = new LookupListener() {

        public void resultChanged(LookupEvent ev) {
            Lookup.Result res = (Result) ev.getSource();
            List l = (List) res.allInstances();
            assert l.size() < 2;
            if (l.size() > 0) {
                Object o = l.get(0);
                boolean didIt = false;
                for (Map.Entry<Queue, Consumer> entry : consumers.entrySet()) {
                    Queue q = entry.getKey();
                    Consumer consumer = entry.getValue();
                    for (Class<?> clazz : consumer.clazzes) {
                        if (clazz.isAssignableFrom(o.getClass())) {
                            if (LOG.isLoggable(Level.FINEST)) LOG.finest(EventBus.this.getClass().getSimpleName() + "@" + System.identityHashCode(EventBus.this) + ":" + q.getClass().getSimpleName() + "@" + System.identityHashCode(q) + ":" + o);
                            if (!q.offer(o)) LOG.warning("dropping event: " + o);
                            if (consumer.listener != null) consumer.listener.dataAvailable(q);
                            didIt = true;
                            break;
                        }
                    }
                }
                if (!didIt) {
                    if (LOG.isLoggable(Level.FINEST)) LOG.finest("NoListener: " + EventBus.this.getClass().getSimpleName() + "@" + System.identityHashCode(EventBus.this) + ":" + o);
                }
            }
        }
    };
}
