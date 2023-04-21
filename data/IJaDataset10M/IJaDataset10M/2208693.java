package bs;

import java.util.*;

/**
 *
 * @author  dw3
 */
public class EventDispatcher<T extends EventListener> {

    protected static DispatcherThread dpt;

    static {
        dpt = new DispatcherThread();
        dpt.setDaemon(true);
        dpt.start();
    }

    protected List<T> eventListenerQueue = new LinkedList<T>();

    protected List<EventObject> eventQueue = new LinkedList<EventObject>();

    private static class DispatcherThread extends Thread {

        protected List<EventDispatcher> eventDisp = new LinkedList<EventDispatcher>();

        public void run() {
            while (true) {
                int i = 0;
                while (i < eventDisp.size()) {
                    EventDispatcher dispatcher = null;
                    synchronized (eventDisp) {
                        dispatcher = eventDisp.get(i++);
                    }
                    dispatcher.dispatchEvents();
                }
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void addDispatcher(EventDispatcher e) {
            synchronized (eventDisp) {
                eventDisp.add(e);
            }
        }
    }

    public EventDispatcher() {
        dpt.addDispatcher(this);
    }

    public void addEventListener(T eventListener) {
        synchronized (eventListenerQueue) {
            eventListenerQueue.add(eventListener);
        }
    }

    public void removeEventListener(T eventListener) {
        synchronized (eventListenerQueue) {
            eventListenerQueue.remove(eventListener);
        }
    }

    public void dispatchEvent(EventObject evt) {
        synchronized (eventQueue) {
            eventQueue.add(evt);
        }
        synchronized (dpt) {
            dpt.notifyAll();
        }
    }

    private void dispatchEvents() {
        while (eventQueue.size() > 0) {
            EventObject e = null;
            synchronized (eventQueue) {
                e = eventQueue.remove(0);
            }
            synchronized (eventListenerQueue) {
                for (EventListener el : eventListenerQueue) el.handleEvent(e);
            }
        }
    }
}
