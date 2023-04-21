package influx.dtc.event;

import influx.dtc.listener.IDTCListener;
import java.io.Serializable;
import java.util.List;

/**
 * Interface for a Data Transfer Container event delegate. An event delegate is used for maintaining what operations/processes are waiting to be performed on the DTC, the user that invoked it, and
 * whether or not the underlying events have been flagged for processing (in which case the actual list of events will not be processed when the flag is false). It is also responsible for delegating
 * events to corresponding listeners.
 * <p>
 * Internally a list is housed that contains all available operational events that can be performed on the DTC.
 * </p>
 * 
 * @author whoover
 */
public interface IEventDelegate extends Serializable {

    /**
	 * Gets the entity that invoked the event
	 * 
	 * @return the entity that invoked the event
	 */
    public String getInvokedBy();

    /**
	 * Sets the entity that invoked the event
	 * 
	 * @param invokedBy
	 *            entity that invoked the event
	 */
    public void setInvokedBy(String invokedBy);

    /**
	 * Queues the specified DTC event (which essentially adds the event to an internal list for later processing).
	 * 
	 * @param event
	 *            event
	 */
    public void queueDTCEvent(IDTCEvent<?, ?> event);

    /**
	 * @return list of current DTC events
	 */
    public List<IDTCEvent<?, ?>> getDTCEvents();

    /**
	 * Clears all queued DTC events
	 */
    public void clearDTCEvents();

    /**
	 * Clears all DTC listeners
	 */
    public void clearDTCListeners();

    /**
	 * Adds a DTC listener to the delegate
	 * 
	 * @param listener
	 *            listener
	 */
    public void addDTCListener(IDTCListener<?, ?> listener);

    /**
	 * Removes a DTC listener
	 * 
	 * @param listener
	 *            listener
	 */
    public void removeDTCListener(IDTCListener<?, ?> listener);

    /**
	 * Gets the list of DTC listeners
	 * 
	 * @return the list of DTC listeners
	 */
    public List<IDTCListener<?, ?>> getDTCListeners();

    /**
	 * Gets a list of DTC listeners by the specified class
	 * 
	 * @param <L>
	 *            listener
	 * @param listenerClass
	 *            listenerClass
	 * @return a list of DTC listeners by class
	 */
    public <L extends IDTCListener<?, ?>> List<L> getDTCListeners(Class<? super L> listenerClass);

    /**
	 * Sets the broadcast agent that is responsible for handling broadcast calls.
	 * 
	 * @param broadcastAgent
	 *            the broadcastAgent
	 */
    public void setBroadcastAgent(IBroadcastAgent broadcastAgent);

    /**
	 * @return the broadcast agent that is responsible for handling broadcast calls.
	 */
    public IBroadcastAgent getBroadcastAgent();

    /**
	 * Broadcasts all events in the current queue to the broadcast agent.
	 * 
	 * @param clearListenersOnAbort
	 *            clearListenersOnAbort
	 */
    public void broadcast(boolean clearListenersOnAbort);
}
