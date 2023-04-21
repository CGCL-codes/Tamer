package net.sf.jncu.protocol;

/**
 * Docking command listener interface.
 * 
 * @author moshew
 */
public interface DockCommandListener {

    /**
	 * Notification that a command was received.
	 * 
	 * @param command
	 *            the command.
	 */
    public void commandReceived(IDockCommandFromNewton command);

    /**
	 * Notification that a command is being received.<br>
	 * Used mainly to show a progress bar.
	 * <p>
	 * <em>If the command is small then this method might never be called.</em>
	 * 
	 * @param command
	 *            the command.
	 * @param progress
	 *            the number of bytes received.
	 * @param total
	 *            the total number of bytes to receive.
	 */
    public void commandReceiving(IDockCommandFromNewton command, int progress, int total);

    /**
	 * Notification that a command was sent.
	 * 
	 * @param command
	 *            the command.
	 */
    public void commandSent(IDockCommandToNewton command);

    /**
	 * Notification that a command is being received.<br>
	 * Used mainly to show a progress bar.
	 * <p>
	 * <em>If the command is small then this method might never be called.</em>
	 * 
	 * @param command
	 *            the command.
	 * @param progress
	 *            the number of bytes sent.
	 * @param total
	 *            the total number of bytes to send.
	 */
    public void commandSending(IDockCommandToNewton command, int progress, int total);

    /**
	 * Notification that no more commands will be available.
	 */
    public void commandEOF();
}
