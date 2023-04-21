package influx.dtc;

import influx.dtc.event.IEventDelegate;
import influx.model.Entity;

/**
 * This provides a generic approach to encompass any transfer object that needs to be identified as part of event processing scheme. In general this scheme can apply to any Data Transfer Container
 * entity.
 * 
 * @author whoover
 * @see influx.dtc.event.IEventDelegate
 * @param <EVENTDELEGATE>
 *            the event delegate type
 */
@Entity
public interface IDTC<EVENTDELEGATE extends IEventDelegate> {

    /**
	 * Gets the event delegate
	 * 
	 * @return the delegation event
	 */
    public EVENTDELEGATE getEventDelegate();

    /**
	 * Sets the event delegate
	 * 
	 * @param eventDelegate
	 *            the eventDelegate
	 */
    public void setEventDelegate(EVENTDELEGATE eventDelegate);
}
