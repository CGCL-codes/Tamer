package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.ResponseEvent;

/**
 * The EventGeneratingAction interface is implemented by ManagerActions that
 * return their result not in a ManagerResponse but by sending a series of
 * events.<p>
 * The event type that indicates that Asterisk is finished is returned by the
 * getActionCompleteEventClass() method.
 * 
 * @see org.asteriskjava.manager.event.ResponseEvent
 * @author srt
 * @version $Id: EventGeneratingAction.java 1121 2008-08-16 20:54:12Z srt $
 * @since 0.2
 */
public interface EventGeneratingAction extends ManagerAction {

    /**
     * Returns the event type that indicates that Asterisk is finished sending
     * response events for this action.
     * 
     * @return a Class that is an instance of ResponseEvent.
     * @see org.asteriskjava.manager.event.ResponseEvent
     */
    Class<? extends ResponseEvent> getActionCompleteEventClass();
}
