package net.sf.urlchecker.events;

import java.util.EventListener;

/**
 * This interface allows Listeners on events during the life of a chain of
 * commands. By implementing this interface a class can listen to events
 * occurring during the processing of the context by any command that fires
 * these events. The listener then needs to be added to the command by the
 * relevant interface method.
 * 
 * <p>
 * <b> $Id: ChainListener.java 182 2010-12-13 22:51:53Z georgosn $</b>
 * </p>
 * 
 * @author $LastChangedBy: georgosn $
 * @version $LastChangedRevision: 182 $
 */
public interface ChainListener extends EventListener {

    /**
     * Fire event.
     * 
     * @param event
     *            the event
     */
    void fireEvent(ChainEvent event);
}
