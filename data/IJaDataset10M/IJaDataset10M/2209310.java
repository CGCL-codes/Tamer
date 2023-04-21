package ch.qos.logback.eclipse.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ui.IMemento;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * This class manages the logging events that are recieved by the plugin.
 * 
 * @author S&eacute;bastien Pennec
 */
public class LoggingEventManager {

    private static final String TAG_MGR = "manager";

    private static final String TAG_MAXSIZE = "maxSize";

    private static final double REMOVE_RATIO = 0.33;

    static LoggingEvent[] NONE = new LoggingEvent[] {};

    private static LoggingEventManager manager;

    private int maxSize = 20000;

    private int removeQuantity = 14000;

    private final List<LoggingEvent> loggingEventList = Collections.synchronizedList(new ArrayList<LoggingEvent>());

    private final List<LoggingEventManagerListener> listeners = new ArrayList<LoggingEventManagerListener>();

    private LoggingEventManager() {
    }

    public static LoggingEventManager getManager() {
        if (manager == null) {
            manager = new LoggingEventManager();
        }
        return manager;
    }

    public void addLoggingEvent(final LoggingEvent event) {
        if (!EventFilter.filter(event)) {
            return;
        }
        loggingEventList.add(event);
        listSizeCheck();
        sendEvent(event);
    }

    private void sendEvent(final LoggingEvent event) {
        fireLoggingEventAdded(new LoggingEvent[] { event }, NONE);
    }

    public void addLoggingEventManagerListener(final LoggingEventManagerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeLoggingEventManagerListener(final LoggingEventManagerListener listener) {
        listeners.remove(listener);
    }

    private void fireLoggingEventAdded(final LoggingEvent[] itemsAdded, final LoggingEvent[] itemsRemoved) {
        final LoggingEventManagerEvent event = new LoggingEventManagerEvent(this, itemsAdded, itemsRemoved);
        final Iterator<LoggingEventManagerListener> it = listeners.iterator();
        while (it.hasNext()) {
            (it.next()).loggingEventsChanged(event);
        }
    }

    private void listSizeCheck() {
        synchronized (loggingEventList) {
            if (loggingEventList.size() > maxSize) {
                final List<LoggingEvent> sub = loggingEventList.subList(0, removeQuantity);
                sub.clear();
            }
        }
    }

    public void clearEventList() {
        loggingEventList.clear();
    }

    public LoggingEvent getEvent(final int index) {
        synchronized (loggingEventList) {
            if (index > loggingEventList.size() - 1) {
                return null;
            }
            return loggingEventList.get(index);
        }
    }

    public int getEventCount() {
        return loggingEventList.size();
    }

    public int getIndex(final LoggingEvent event) {
        return loggingEventList.indexOf(event);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
        final Double dbl = maxSize * REMOVE_RATIO;
        this.removeQuantity = dbl.intValue();
        listSizeCheck();
    }

    public void saveState(final IMemento memento) {
        final IMemento mem = memento.createChild(TAG_MGR);
        mem.putInteger(TAG_MAXSIZE, maxSize);
    }

    public void init(final IMemento memento) {
        final IMemento mem = memento.getChild(TAG_MGR);
        if (mem == null) {
            return;
        }
        final Integer max = mem.getInteger(TAG_MAXSIZE);
        if (max != null) {
            this.maxSize = max;
        }
    }
}
