package net.jtank.input;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>A KeyboardListener which records all events coming from the KeyboardDevice into a
 * First In First Out (FIFO) queue.</p>
 * 
 * <p>The advantage of this listener is that no key events are lost, yet it is still very fast.
 * Adhering to the garbage reduction policy of this package, no new objects are created by the
 * methods of this object.  The FIFO queue is initialized to a set limit on construction.</p>
 * 
 * @version 1.1, 2004-12-14
 * 
 * @author William Denniss
 */
public class KeyEventQueueListener implements KeyboardListener {

    private KeyboardEvent[] events;

    private int nextEventPut = 0;

    private int nextEventGet = 0;

    private Set<Integer> eventQueueFilter;

    private boolean invertFilter = false;

    private Timestamp timestamper;

    public static final int DEFAULT_QUEUE_SIZE = 512;

    /**
     * Constructs a new KeyEventQueueListener with a default queue size
     * and no filter.
     * 
     * @see #DEFAULT_QUEUE_SIZE
     */
    public KeyEventQueueListener() {
        this(DEFAULT_QUEUE_SIZE);
    }

    /**
     * Constructs a new KeyEventQueueListener with the given queue size
     * and no filters.
     * 
     * @param queueSize the queue size
     */
    public KeyEventQueueListener(int queueSize) {
        this(queueSize, null, false);
    }

    /**
     * Constructs a new KeyEventQueueListener using the given filter keys and
     * the default queue size.  When the filter collection is not null, only key
     * events for keys that are in the filter collection are recorded, the remainder
     * are ignored.
     * 
     * @param eventQueueFilter keys which events will be recorded for (or <code>null</code> for no filter)
     */
    public KeyEventQueueListener(Collection<Integer> eventQueueFilter) {
        this(eventQueueFilter, false);
    }

    /**
     * Constructs a new KeyEventQueueListener using the given sticky keys and
     * the default queue size.  When the filter collection is not null, only key
     * events for keys that are in the filter collection are recorded, the remainder
     * are ignored.  The behaviour of the filter can be inverted by setting the invertFilter parameter to true.
     * 
     * @param eventQueueFilter keys which events will be recorded for (or <code>null</code> for no filter)
     * @param invertFilter if true, the filter keys will be the ones NOT in the filter key collection
     *      else they will be the ones that are in the set.
     */
    public KeyEventQueueListener(Collection<Integer> eventQueueFilter, boolean invertFilter) {
        this(DEFAULT_QUEUE_SIZE, eventQueueFilter, invertFilter);
    }

    /**
     * Constructs a new KeyEventQueueListener using the given sticky keys and
     * the given queue size.  When the filter collection is not null, only key
     * events for keys that are in the filter collection are recorded, the remainder
     * are ignored.  The behaviour of the filter can be inverted by setting the invertFilter parameter to true.
     * 
     * @param queueSize the queue size
     * @param eventQueueFilter
     * @param invertFilter if true, the filter keys will be the ones NOT in the filter key collection
     *      else they will be the ones that are in the set.
     * 
     */
    public KeyEventQueueListener(int queueSize, Collection<Integer> eventQueueFilter, boolean invertFilter) {
        this(queueSize, eventQueueFilter, invertFilter, null);
    }

    /**
     * Constructs a new KeyEventQueueListener using the given sticky keys and
     * the given queue size.  When the filter collection is not null, only key
     * events for keys that are in the filter collection are recorded, the remainder
     * are ignored.  The behaviour of the filter can be inverted by setting the invertFilter parameter to true.
     * 
     * @param queueSize the queue size
     * @param eventQueueFilter
     * @param invertFilter if true, the filter keys will be the ones NOT in the filter key collection
     *      else they will be the ones that are in the set.
     * @param timestamper object used to timestamp events (if null, no timestamps are recorded)
     * 
     */
    public KeyEventQueueListener(int queueSize, Collection<Integer> eventQueueFilter, boolean invertFilter, Timestamp timestamper) {
        events = new KeyboardEvent[queueSize];
        for (int i = 0; i < events.length; i++) {
            events[i] = KeyboardEvent.create();
        }
        if (eventQueueFilter != null) {
            this.eventQueueFilter = new HashSet<Integer>(eventQueueFilter);
        }
        this.invertFilter = invertFilter;
        this.timestamper = timestamper;
    }

    /**
     * Adds an event to the queue.
     * 
     * @param key the keys whose state changed
     * @param pressed state of key (true if pressed)
     * @param timestamp the time of the event (optional)
     * 
     */
    private synchronized void addEvent(int key, boolean pressed, long timestamp) {
        if (nextEventPut + 1 == nextEventGet) {
            System.out.println("Keyboard event queue is full - input event ignored.");
        }
        events[nextEventPut].keyCode = key;
        events[nextEventPut].type = pressed ? KeyboardEvent.Type.PRESSED : KeyboardEvent.Type.RELEASED;
        events[nextEventPut].when = timestamp;
        nextEventPut++;
        if (nextEventPut == events.length) {
            nextEventPut = 0;
        }
    }

    /**
     * If there is an event in the queue, copies the data of the next event 
     * into the passed KeyboardEvent and returns true.  If there was no next
     * event, the passed KeyboardEvent is not altered, and the method returns
     * false.
     * 
     * @return true if there was an event in the queue, false if there wasn't.
     * 
     * @see #hasNextEvent()
     */
    public synchronized boolean next(KeyboardEvent event) {
        if (!hasNextEvent()) {
            return false;
        }
        event.keyCode = events[nextEventGet].getKeyCode();
        ;
        event.type = events[nextEventGet].getType();
        event.when = events[nextEventGet].getWhen();
        nextEventGet++;
        if (nextEventGet > events.length) {
            nextEventGet = 0;
        }
        return true;
    }

    /**
     * Returns true if there is an event in the event queue.  This event can be retrieved
     * from the queue using <code>nextEvent()</code>;
     * 
     * @return true if there is an event in the event queue
     * 
     * @throws IllegalArgumentException if the keyboard input is not set up for 
     *      event queue storage.
     * 
     * @see #next(KeyboardEvent)
     */
    public boolean hasNextEvent() {
        return nextEventGet != nextEventPut;
    }

    /**
     * Adds the event to the queue if it gets past the filters.
     * 
     * @param key the key
     * @param state state of key
     */
    private void filterAndAdd(int key, boolean state) {
        boolean add = true;
        if (!invertFilter) {
            if (eventQueueFilter != null) {
                add = eventQueueFilter.contains(new Integer(key));
            }
        } else {
            if (eventQueueFilter != null) {
                add = !eventQueueFilter.contains(new Integer(key));
            }
        }
        if (add) {
            long time = 0;
            if (timestamper != null) {
                time = timestamper.getTimestamp();
            }
            addEvent(key, state, time);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void onKeyPressed(KeyPressedEvent e) {
        filterAndAdd(e.getKeyCode(), true);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void onKeyReleased(KeyReleasedEvent e) {
        filterAndAdd(e.getKeyCode(), false);
    }

    /**
     * {@inheritDoc}
     */
    public void onKeyTyped(KeyTypedEvent e) {
    }
}
