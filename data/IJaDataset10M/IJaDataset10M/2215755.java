package joxtr.common.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * This class is used to listen for events in the event subsystem. The user does
 * not have to deal directly with the bus as a properly configured instance of
 * this class does all of the work for you. The event system will only accept
 * this or descendants of this class (instead of an interface). It provides
 * automatic thread decoupling that allows the event dispatcher to store down
 * and continue so that no work is done on it.
 * 
 * @author REP
 * 
 * @param <T>
 *            The type of I6xEvent this handler is interested in
 */
public class EventSubscriberProxy<T extends BaseEvent> {

    private static final String ENTER = "Enter";

    private static final String EXIT = "Exit";

    private static final Logger LOG = Logger.getLogger(EventSubscriberProxy.class.getName());

    private ArrayBlockingQueue<T> responseQueue;

    private Class<T> messageClass;

    /**
	 * 
	 * 
	 * @param messageClass
	 *            The event you are interested in for this handler.
	 * @param queueDepth
	 *            The number of events this will hold waiting to be handled.
	 */
    public EventSubscriberProxy(Class<T> messageClass, int queueDepth) {
        LOG.info(ENTER + varString(messageClass, queueDepth));
        responseQueue = new ArrayBlockingQueue<T>(queueDepth);
        this.messageClass = messageClass;
        EventBus.getInstance().subscribe(messageClass, this);
        LOG.info(EXIT);
    }

    public final void handleEvent(T event) {
        handleEventDelegate(event);
    }

    public final T getNextEvent() {
        return responseQueue.poll();
    }

    public final T getNextEvent(int seconds, TimeUnit unit) {
        LOG.info(ENTER + varString(seconds, unit));
        T t = null;
        try {
            t = responseQueue.poll(seconds, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.fine(EXIT + varString(t));
        return t;
    }

    protected void handleEventDelegate(T event) {
        responseQueue.offer(event);
    }

    public final void unsubscribe() {
        EventBus.getInstance().unsubscribe(messageClass, this);
    }

    public final Collection<T> clearEventCache() {
        LOG.info(ENTER);
        Collection<T> tc = new ArrayList<T>();
        responseQueue.drainTo(tc);
        LOG.info(EXIT + varString(tc));
        return tc;
    }

    protected static final String SPACE = " ";

    protected static String varString(Object... args) {
        if (LOG.getLevel() != null && (LOG.getLevel().intValue() > Level.FINER.intValue())) {
            return SPACE;
        }
        StringBuilder strb = new StringBuilder();
        for (Object object : args) {
            strb.append(object);
            strb.append(SPACE);
        }
        return strb.toString();
    }
}
