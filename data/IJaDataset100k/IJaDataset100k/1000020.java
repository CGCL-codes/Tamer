package org.asteriskjava.manager.event;

/**
 * A ParkedCallsCompleteEvent is triggered after all parked calls have been reported in response to
 * a ParkedCallsAction.
 * 
 * @see org.asteriskjava.manager.action.ParkedCallsAction
 * @see org.asteriskjava.manager.event.ParkedCallEvent
 * 
 * @author srt
 * @version $Id: ParkedCallsCompleteEvent.java 938 2007-12-31 03:23:38Z srt $
 */
public class ParkedCallsCompleteEvent extends ResponseEvent {

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -2736388036245367249L;

    /**
     * @param source
     */
    public ParkedCallsCompleteEvent(Object source) {
        super(source);
    }
}
