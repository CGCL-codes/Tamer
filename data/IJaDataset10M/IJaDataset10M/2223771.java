package ti.plato.ui.views.console;

/**
 * A console listener is notified when consoles are added or removed from
 * the console manager.
 * <p>
 * Clients may implement this interface.
 * </p>
 * @since 3.0
 */
public interface IConsoleListener {

    /**
	 * Notification the given consoles have been added to the console
	 * manager.
	 * 
	 * @param consoles added consoles
	 */
    public void consolesAdded(IConsole[] consoles);

    /**
	 * Notification the given consoles have been removed from the
	 * console manager.
	 * 
	 * @param consoles removed consoles
	 */
    public void consolesRemoved(IConsole[] consoles);
}
