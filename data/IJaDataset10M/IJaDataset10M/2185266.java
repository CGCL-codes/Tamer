package mirrormonkey.util.netevent.queue;

import mirrormonkey.util.netevent.NetworkEvent;

/**
 * Defines a policy for event storage and processing.
 * 
 * @author Philipp Christian Loewner
 * 
 */
public interface EventQueue {

    /**
	 * Adds a new <tt>NetworkEvent</tt> to the queue to be processed later
	 * on.
	 * 
	 * @param event
	 *            the event that should be added to the queue
	 */
    public void add(NetworkEvent event);

    /**
	 * Returns the current number of unprocessed events.
	 * 
	 * @return the number of events currently in the queue
	 */
    public int getSize();

    /**
	 * Processes a number of enqueued events.
	 * 
	 * Standard implementations must guarantee that:
	 * <ul>
	 * <li>Events are processed in order of their receipt.</li>
	 * <li>Events are consumed after they have been processed.</li>
	 * </ul>
	 * 
	 * In most cases, implementations will simply notify a number of listeners
	 * and then exit. Any behavior additional to that would normally have to be
	 * implemented within its own listener class.
	 */
    public void processEvents();
}
