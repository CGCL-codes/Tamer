package org.gaea.common.command.event;

/**
 * Adapter class over CommandListener.
 * 
 * IMPLEMENTED PATTERNS: Adapter.
 * 
 * @author bdevost
 */
public class CommandAdapter implements CommandListener {

    /**
	 * Mask over the commandExecuted event handler.
	 * 
	 * @param e
	 *            CommandEvent created by the firing component
	 * @see org.gaea.common.command.event.CommandListener#commandExecuted(org.gaea.common.command.event.CommandEvent)
	 */
    public void commandExecuted(final CommandEvent e) {
    }

    /**
	 * Mask over the commandDisposed event handler.
	 * @param e CommandEvent created by the firing component
	 * @see org.gaea.common.command.event.CommandListener#commandDisposed(org.gaea.common.command.event.CommandEvent)
	 */
    public void commandDisposed(final CommandEvent e) {
    }

    /**
	 * Mask over the commandDisabled event handler.
	 * @param e CommandEvent created by the firing component
	 * @see org.gaea.common.command.event.CommandListener#commandDisabled(org.gaea.common.command.event.CommandEvent)
	 */
    public void commandDisabled(final CommandEvent e) {
    }
}
