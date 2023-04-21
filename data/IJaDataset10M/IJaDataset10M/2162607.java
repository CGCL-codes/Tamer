package com.bluemarsh.jswat.core.event;

import com.bluemarsh.jswat.core.util.Threads;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.EventRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class DefaultDispatcher is a concrete implementation of the Dispatcher
 * interface. Each event is delivered to the listener which was registered
 * with the event request which resulted in the event.
 *
 * @author  Nathan Fiedler
 */
public class DefaultDispatcher implements Dispatcher, Runnable {

    /** Logger for gracefully reporting unexpected errors. */
    private static final Logger logger = Logger.getLogger(DefaultDispatcher.class.getName());

    /** EventRequest property for the DispatcherListener. */
    private static final String PROP_LISTENER = "listener";

    /** VM event queue. */
    private EventQueue eventQueue;

    /** Invoked when VMStartEvent is received. */
    private DispatcherListener startedCallback;

    /** Invoked when debuggee dies or is disconnected. */
    private Runnable stoppedCallback;

    /** Invoked if the debuggee is suspended by an event. */
    private DispatcherListener suspendedCallback;

    /**
     * Constructs a new instance of DefaultDispatcher.
     */
    public DefaultDispatcher() {
    }

    @Override
    public void register(DispatcherListener listener, EventRequest request) {
        request.putProperty(PROP_LISTENER, listener);
    }

    @Override
    public void run() {
        boolean stop = false;
        while (!stop) {
            try {
                EventSet set = eventQueue.remove();
                EventIterator iter = set.eventIterator();
                boolean resume = true;
                Event suspendEvent = null;
                while (iter.hasNext()) {
                    Event event = iter.nextEvent();
                    if (event instanceof VMDisconnectEvent) {
                        stop = true;
                    } else if (event instanceof VMStartEvent) {
                        if (startedCallback != null) {
                            resume &= startedCallback.eventOccurred(event);
                        }
                    } else {
                        EventRequest request = event.request();
                        if (request != null) {
                            DispatcherListener listener = (DispatcherListener) request.getProperty(PROP_LISTENER);
                            if (listener != null) {
                                resume &= listener.eventOccurred(event);
                                if (!resume && suspendEvent == null) {
                                    suspendEvent = event;
                                }
                            }
                        }
                    }
                }
                if (resume) {
                    set.resume();
                } else if (suspendedCallback != null) {
                    suspendedCallback.eventOccurred(suspendEvent);
                }
            } catch (InterruptedException ie) {
                break;
            } catch (VMDisconnectedException vmde) {
                break;
            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
            }
        }
        if (stoppedCallback != null) {
            stoppedCallback.run();
        }
    }

    @Override
    public void start(EventQueue queue, DispatcherListener started, Runnable stopped, DispatcherListener suspended) {
        startedCallback = started;
        stoppedCallback = stopped;
        suspendedCallback = suspended;
        eventQueue = queue;
        Threads.getThreadPool().submit(this);
    }

    @Override
    public void unregister(EventRequest request) {
        request.putProperty(PROP_LISTENER, null);
    }
}
