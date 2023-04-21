package org.colombbus.tangara.ide.model;

import java.util.EventListener;

/**
 * A {@link ScriptModel} listener.
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
public interface ScriptModelListener extends EventListener {

    /**
	 * When a script has been executed.
	 *
	 * @param scriptEvent
	 *            The associated {@link ScriptEvent}
	 */
    public void scriptExecuted(ScriptEvent scriptEvent);
}
