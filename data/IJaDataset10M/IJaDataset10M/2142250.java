package nl.utwente.ewi.hmi.deira.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Queue for RaceEvents, keeping them ordered based on their current importance.
 * MMM adds events; TGM, SAM, FAM peek to add information to the events; OM polls for events to generate output for.
 */
public class EventQueue {

    private Comparator<Object> comparator;

    /**< Comparator to keep the queue(s) sorted. */
    private ArrayList<ArrayList<Event>> queue = new ArrayList<ArrayList<Event>>();

    /**< The queues - the queue is internally represented by 4 queues to speed up enquiries. */
    private ArrayList<Event> history = new ArrayList<Event>();

    /**< The history of events polled by the output module */
    private HashMap<String, Integer> callers = new HashMap<String, Integer>();

    /**< Map for caller resolution */
    private HashMap<Integer, Condition> callerConditions = new HashMap<Integer, Condition>();

    private HashMap<Integer, Lock> callerLocks = new HashMap<Integer, Lock>();

    private boolean wakeAll = false;

    private int module_count = 0;

    /** Field to monitor the number of modules that have been added */
    private EventFilter eventfilter;

    private boolean do_lowerimportance_if_similar = true;

    /** < Logging instance */
    private static Logger log = Logger.getLogger("deira.eq");

    /**
	 * Constructor, sets comparator and creates the internal event queues.
	 * 
	 * @param comparator The comparator used to compare RaceEvents for sorting the queues.
	 */
    public EventQueue(Comparator<Object> comparator) {
        this.comparator = comparator;
    }

    public void addModule(String module) {
        queue.add(new ArrayList<Event>());
        Lock l = new ReentrantLock();
        callerLocks.put(module_count, l);
        callerConditions.put(module_count, l.newCondition());
        callers.put(module, module_count);
        module_count++;
    }

    /**
	 * Waits for an event to become available on the queue. Note that the working
	 * of this function is caller dependent. When this method returns it is _guaranteed_
	 * that at least one event is available, but keep in mind that between this method
	 * returning an invoking peek() or poll() the queue _can_ mutate. The event for
	 * which the wake up was triggered might have decayed in that time (also when context
	 * switches occur to other running threads). Hence you must _always_ check that you
	 * don't get 'null' back from peek() or poll(), if so, you can simply call waitForEvent
	 * again.
	 * 
	 * When wakeAllWaiters() is invoked all threads waiting in waitForEvent() are signaled and
	 * the function returns (w/o there being anything on the queue). One can not rely on the
	 * absence of an event in the queue (peek() or poll()) to determines if wakeAllWaiters()
	 * has been called however (invokers should handle wakeAllWaiters() tracking THEMSELVES).
	 * 
	 * Almer: This is an experimental new approach to see if we can avoid
	 * waking _all_ threads when something becomes available in the queue, instead
	 * only threads are woke for which a new event is _really_ available. The condition
	 * is also double checked here (to deal with deficiencies of java thread internals).
	 * 
	 * @throws Exception If something went wrong during the condition wait.
	 */
    public void waitForEvent() throws Exception {
        int q = getCaller();
        log.info("EQ: Waiting For Events On Queue " + q);
        while (queue.get(q).size() == 0 && !wakeAll) {
            Lock l = callerLocks.get(q);
            log.fine("EQ: Obtaining Lock On Queue " + q);
            l.lock();
            try {
                log.fine("EQ: Waiting For Condition On Queue " + q);
                callerConditions.get(q).await();
            } finally {
                l.unlock();
                log.fine("EQ: Releasing Lock On Queue " + q);
            }
        }
    }

    /**
	 * Wakes all threads waiting for the event queue and causes them to exit
	 * the waitForEvent() loop and return to the invoking function. This is mostly useful
	 * when exiting an EventQueue using application.
	 */
    public void wakeAllWaiters() {
        log.info("EQ: Waking all waiters");
        wakeAll = true;
        for (int i = 0; i < module_count; i++) {
            Lock l = callerLocks.get(i);
            l.lock();
            try {
                callerConditions.get(i).signal();
            } finally {
                l.unlock();
            }
        }
    }

    /**
	 * Translates the invoker of the function invoking this function to one
	 * of the modules using the callers map.
	 * 
	 * This uses the java run-time language facilities, which are cool :)
	 * Basically this obsoletes explicitely passing in identification of the calling
	 * module. It uses the stack to resolve the invoking module to the numbers used
	 * internally.
	 * 
	 * The stack will look something like this when in this function:
	 * - EventQueue.getCaller()
	 * - EventQueue.peek() <-- For example, but can be any EventQueue.* function that invokes getCaller()
	 * - *.* <-- Where the first * is resolved using getClassName() in the function below.
	 * 
	 * NOTE: This relies on the definition of proper mappings in setupCallers above.
	 * In fact multiple classes can be resolved to the same integer identification.
	 * 
	 * @return Numerical identification of the invoker's invoking module (caller).
	 */
    private int getCaller() {
        final Throwable t = new java.lang.Throwable();
        t.fillInStackTrace();
        String caller = t.getStackTrace()[2].getClassName();
        Integer callerName = callers.get(caller);
        if (callerName == null) {
            log.severe("EQ: Unknown caller: " + caller);
        }
        return callerName;
    }

    /**
	 * Adds a race event to the queue. Only called by the MMM to add events for the TGM!
	 * 
	 * @param e The race event to be added.
	 * @return Whether the adding went well.
	 */
    public synchronized boolean add(Event e) {
        if (eventfilter != null) {
            eventfilter.doFilter(e);
        }
        if (do_lowerimportance_if_similar) {
            lowerImportanceIfSimilar(e);
        }
        boolean result = queue.get(0).add(e);
        if (result) {
            Lock l = callerLocks.get(0);
            l.lock();
            try {
                callerConditions.get(0).signal();
            } finally {
                l.unlock();
            }
        }
        return result;
    }

    private void lowerImportanceIfSimilar(Event e) {
        for (Event o : history) {
            if (o.isSimilar(e)) {
                float d = (System.currentTimeMillis() - o.getOutputSystemTime()) / 1000;
                if ((5 - d) > 0) e.lowerImportance(5 - d);
            }
        }
    }

    synchronized void deleteEvent(String type, String value) {
        for (ArrayList<Event> q : queue) {
            Event toRemove = null;
            for (Event e : q) {
                if (e.getType().equals(type)) {
                    if (e.getLabel().equals(value)) {
                        toRemove = e;
                    }
                }
            }
            if (toRemove != null) {
                q.remove(toRemove);
            }
        }
    }

    /**
	 * Removes all events which were ready for a certain module.
	 * Use publicly at own risk...
	 */
    public synchronized void clear() {
        int q = getCaller();
        if (q >= 0 && q < module_count) queue.get(q).clear();
    }

    /**
	 * Returns the most important event for the calling module, but leaves this event in the queue as well.
	 * 
	 * @return The event ready for the calling module with the highest importance at moment of request, or null if there are no such events. 
	 */
    public synchronized Event peek() {
        int q = getCaller();
        if (q >= 0 && q < module_count) {
            sort(q);
            if (size(q) > 0) {
                Event o = queue.get(q).get(size(q) - 1);
                return o;
            } else return null;
        } else return null;
    }

    /**
	 * Returns the nth (0 based!) most important event for the calling module.
	 * 
	 * @param q The calling module, aka for which module the event should be ready.
	 * @param n 0-based index of the event in the queue sorted on importance.
	 * @return The nth (0 based!) most important event in the queue for the calling module.
	 */
    public Event peekNbyModule(int q, int n) {
        if (q >= 0 && q < module_count) {
            sort(q);
            if (size(q) >= n + 1) return queue.get(q).get(size(q) - 1 - n); else return null;
        } else return null;
    }

    /**
	 * Returns the nth (0 based!) most important event for the calling module.
	 * 
	 * @param n 0-based index of the event in the queue sorted on importance.
	 * @return The nth (0 based!) most important event in the queue for the calling module.
	 */
    public Event peekN(int n) {
        int q = getCaller();
        return peekNbyModule(q, n);
    }

    /**
	 * Removes the most important event for calling module from the queue and returns it.
	 * Used only by the last module.
	 * 
	 * @return The event ready for last module with the highest importance at that moment, or null if there are no such events.
	 */
    public synchronized Event poll() {
        int exitQueue = module_count - 1;
        sort(exitQueue);
        int head = size(exitQueue) - 1;
        if (head >= 0) {
            Event o = queue.get(exitQueue).get(head);
            o.putOut();
            addToHistory(o);
            queue.get(exitQueue).remove(head);
            return o;
        } else return null;
    }

    private void addToHistory(Event o) {
        history.add(o);
        for (ArrayList<Event> q : queue) {
            for (Event e : q) {
                if ((o != e) && (o.isSimilar(e))) e.lowerImportance(6.0f);
            }
        }
    }

    /**
	 * Removes a single instance of o, ready for the calling module, if present.
	 * 
	 * @param o The event to be removed from the queue.
	 * @return Whether the removal of event o could be done.
	 */
    public synchronized boolean remove(Event o) {
        int q = getCaller();
        if (q >= 0 && q < module_count) return queue.get(q).remove(o); else return false;
    }

    /**
	 * Returns the number of events ready for the calling module.
	 * @return The number of events ready for the calling module.
	 */
    private int size(int q) {
        if (q >= 0 && q < module_count) return queue.get(q).size(); else return -1;
    }

    /**
	 * Used to notify the event queue that a certain event has been 'tagged' with information from the calling module.
	 * Used by TGM, SAM and FAM (to support the internal optimalization of this queue class).
	 * 
	 * @param o The race event that has been 'tagged' with information from the calling module.
	 * @return Whether the update of event o could be done for the calling module.
	 */
    public synchronized boolean update(Event o) {
        int q = getCaller();
        if (q >= 0 && q < module_count - 1) {
            boolean rm = queue.get(q).remove(o);
            if (!rm) return false;
            boolean ad = queue.get(q + 1).add(o);
            if (ad) {
                log.info("EQ: notifying next thread " + (q + 1));
                Lock l = callerLocks.get(q + 1);
                l.lock();
                try {
                    callerConditions.get(q + 1).signal();
                } finally {
                    l.unlock();
                }
            }
            return ad;
        } else return false;
    }

    /**
	 * Sorts the queue on priority, for module q. The higher the getPriority(), the higher the index of the element.
	 * 
	 * @param q The module requesting the action.
	 */
    private synchronized void sort(int q) {
        if (q >= 0 && q < module_count) {
            Object[] a = queue.get(q).toArray();
            Arrays.sort(a, comparator);
            queue.get(q).clear();
            for (int i = 0; i < a.length; i++) {
                if (((Event) a[i]).getImportance() > 0) {
                    queue.get(q).add((Event) a[i]);
                }
            }
        }
    }

    public void setFilter(EventFilter filter) {
        this.eventfilter = filter;
    }
}
