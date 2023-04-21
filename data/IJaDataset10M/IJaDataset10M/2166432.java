package com.rubixinfotech.SKJava;

/**
 * This interface must be implemented by any class wishing to listen to channelgroup
 * specifc events on the SKJava framework.
 * 
 * @author Renny Koshy
 *
 */
public interface SKJChanGroupEventListener extends SKJEventListener {

    /**
	 * Processes an event that has been received by SKJava on a specific channelgroup.
	 * This may be a switch initiated event, or an application level event that was 
	 * created by conversion filters setup by the application (based on a switch-
	 * initiated event).
	 * 
	 * @see #onEvent(SKJMessage)
	 * 
	 * @param chanGroup The channelgroup on which the message was received
	 * @param msg	The event to process
	 * @return		Return true to indicate that the event has been processed.  Return
	 * 				false to indicate that the event has not been processed and may be
	 * 				passed down the stack of listeners
	 */
    public boolean onEvent(String chanGroup, SKJMessage msg);
}
