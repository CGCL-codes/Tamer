package org.elogistics.action.event;

import org.elogistics.event.AbstractEventSource;

/**
 * @author Jurkschat, Oliver
 *
 */
public class ActionEventSource extends AbstractEventSource<IActionEvent, IActionEventListener> implements IActionEventSource {

    /**
	 * @param listener
	 */
    public ActionEventSource(IActionEventListener listener) {
        super(listener);
    }
}
