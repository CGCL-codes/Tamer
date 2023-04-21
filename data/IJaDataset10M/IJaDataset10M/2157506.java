package net.syseventfw4j.handler;

import java.util.Collection;
import net.syseventfw4j.context.IContext;
import net.syseventfw4j.event.IEvent;

/**
 * The handler is responsible of persisting the event.
 * This interface describes how the Receiver will interact
 * with the classes implementing it.
 * 
 * @author csiatgm
 */
public interface IHandler {

    /**
	 * @return
	 */
    Collection<Class<? extends IEvent>> getSupportedEventClasses();

    /**
     * Handle the event
	 * @param aContext
	 * @param anEvent
	 * @throws HandlerException
	 */
    void handleEvent(IContext aContext, IEvent anEvent) throws HandlerException;

    /**
	 * @return
	 */
    String getName();

    /**
     * Initialize the handler
	 * @throws HandlerException
	 */
    void initialize() throws HandlerException;

    /**
     * Reset the handler
     * @throws HandlerException
     */
    void reset() throws HandlerException;
}
