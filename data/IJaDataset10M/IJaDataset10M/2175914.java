package tchoukstats.events;

import java.util.EventListener;

/**
 * Interface that describes which elements must have the classes that want to listen
 * system action events.
 */
public interface SystemActionEventListener extends EventListener {

    /**
	 * The system has received a new action.
	 *
	 * @param e		The action received by the system.
	 */
    public void systemActionPerformed(SystemActionEvent e);

    /**
	 * The last action has been canceled.
	 *
	 * @param host	True if this is the host action that has been canceled. False otherwise.
	 */
    public void cancelLastAction(boolean host);
}
