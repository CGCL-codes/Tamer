package net.syseventfw4j.test;

import net.syseventfw4j.context.IContext;
import net.syseventfw4j.event.IEvent;
import net.syseventfw4j.handler.ConsoleHandler;
import net.syseventfw4j.handler.HandlerException;

/**
 * Handler that always throws an exception
 * @author csiatgm
 */
public class IntermittentFailingConsoleHandler extends ConsoleHandler {

    private int i = 0;

    @Override
    public void handleEvent(IContext aContext, IEvent anEvent) throws HandlerException {
        if (i < 5) {
            i++;
            throw new HandlerException("Unable to handle event.");
        } else {
            super.handleEvent(aContext, anEvent);
        }
    }

    /**
	 * Default constructor
	 */
    public IntermittentFailingConsoleHandler() {
        super();
    }
}
