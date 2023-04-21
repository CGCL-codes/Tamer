package core;

/**
 * <p>
 * Title: EventQueue class
 * </p>
 * <p>
 * Description: EventQueue instance keep all the event, you can use postEvent method to put a event
 * to the queue and use getEvent method to get a event from the queue
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Zealot
 * @version 0.1
 */
public final class EventQueue {

    /** The first event index */
    private byte firstEventIndex;

    /** The last event index */
    private byte lastEventIndex;

    /** Store all of the events */
    private Event[] eventQueue;

    /** Unique instance */
    private static EventQueue instance;

    /** Indicate a new event in the queue */
    private static volatile boolean newEventFlag;

    /** Event queue length */
    private static final byte EVENT_QUEUE_LENGTH = 16;

    private EventQueue() {
        this.eventQueue = new Event[EVENT_QUEUE_LENGTH];
        for (byte i = 0; i < EVENT_QUEUE_LENGTH; i++) {
            this.eventQueue[i] = new Event((short) 0, (short) 0, (short) 0, null);
        }
        this.firstEventIndex = 0;
        this.lastEventIndex = -1;
    }

    /**
     * Obtain the unique instance
     * 
     * @return Unique event queue instance
     */
    public static EventQueue getInstance() {
        if (EventQueue.instance == null) {
            EventQueue.instance = new EventQueue();
        }
        return EventQueue.instance;
    }

    /**
     * Post a event to the queue
     * 
     * @param eventType
     *            Specified event
     * @param keyCode
     *            Specified keyCode
     * @param keyAction
     *            Specified keyAction
     * @param accessory
     *            Specified accessory
     * @return Error code
     */
    public byte sendEvent(short eventType, short keyCode, short keyAction, Object accessory) {
        byte temp = 0;
        synchronized (this.eventQueue) {
            EventQueue.newEventFlag = true;
            if (this.lastEventIndex != -1) {
                temp = (byte) (this.lastEventIndex + 1);
                if (temp >= EventQueue.EVENT_QUEUE_LENGTH) {
                    temp = 0;
                }
                if (temp == this.firstEventIndex) {
                    return -1;
                }
            } else {
                temp = this.firstEventIndex;
            }
            this.eventQueue[temp].eventType = eventType;
            this.eventQueue[temp].keyCode = keyCode;
            this.eventQueue[temp].keyAction = keyAction;
            this.eventQueue[temp].accessory = accessory;
            this.lastEventIndex = temp;
        }
        return 0;
    }

    /**
     * Process the specified event immediately
     * 
     * @param event
     *            Specified event instance
     * @return Error code
     */
    private byte sendEventImmediately(Event event) {
        return 0;
    }

    /**
     * Check if the event queue has at least a event
     * 
     * @return Whether the event queue has events or not
     */
    public boolean hasNewEvent() {
        return EventQueue.newEventFlag;
    }

    /**
     * Get a event form the queue
     * 
     * @return Event instance
     */
    public Event getEvent() {
        if (EventQueue.newEventFlag == false) {
            return null;
        }
        Event event;
        synchronized (this.eventQueue) {
            if (this.lastEventIndex == -1) {
                EventQueue.newEventFlag = false;
                return null;
            }
            event = this.eventQueue[firstEventIndex];
            if (this.firstEventIndex == this.lastEventIndex) {
                this.firstEventIndex = 0;
                this.lastEventIndex = -1;
                EventQueue.newEventFlag = false;
            } else {
                this.firstEventIndex++;
                if (this.firstEventIndex >= EventQueue.EVENT_QUEUE_LENGTH) {
                    this.firstEventIndex = 0;
                }
            }
        }
        return event;
    }

    /**
     * Empty the event queue
     */
    public void cleanEventQueue() {
        synchronized (this.eventQueue) {
            for (int i = 0; i < EventQueue.EVENT_QUEUE_LENGTH; i++) {
                this.eventQueue[i] = null;
            }
            this.firstEventIndex = 0;
            this.lastEventIndex = -1;
        }
    }

    /**
     * Get the event number in the queue
     * 
     * @return Event number
     */
    public byte getEventNum() {
        byte number = 0;
        synchronized (this.eventQueue) {
            if (this.lastEventIndex == -1) {
                return 0;
            }
            number = (byte) (this.lastEventIndex - this.firstEventIndex + 1);
            if (number <= 0) {
                number += EventQueue.EVENT_QUEUE_LENGTH;
            }
        }
        return number;
    }
}
