package org.nvframe.event;

/**
 * 
 * @author Nik Van Looy
 */
public interface Event {

    /**
	 * used to determine if a listener can listen to the event instance
	 * 
	 * @param listener The listener that wants to listen for this event
	 * @return true When listener can listen to this event
	 */
    public boolean hasListener(EventListener listener);

    public void dispatchToListener(EventListener listener);
}
