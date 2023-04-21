package mipt.aaf.command;

/**
 * Agent between controller and view-dependent operations (commands)
 * @author Evdokimov
 */
public class ControllerViewAgent {

    /**
	 * Returned command (if not null) should not make system exit
	 * It often asks user about system exit. It can also do some savings
	 */
    public ApplicationCommand initApplicationClosingCommand() {
        return null;
    }

    /**
	 * Returns false of closing was cancelled by the closingCommand
	 * @see initApplicationClosingCommand()
	 */
    public boolean isApplicationClosed(ApplicationCommand closingCommand) {
        return true;
    }
}
