package mipt.aaf.mdi.command;

import mipt.aaf.command.Command;
import mipt.aaf.command.CommandGeneralController;
import mipt.aaf.mdi.AbstractDocController;
import mipt.mvc.ActionHandler;
import mipt.mvc.GeneralEvent;

/**
 * Extracts Command from GeneralEvent.getEventData() and executes it
 * By default accepts all events with commands but subclasses can override isAccepted() method
 * Do not set doc to commands before execution; it is MDIController&DocBuilder resposibility (when docActivated)
 * @author Evdokimov
 */
class CommandDocController extends AbstractDocController {

    /**
	 * 
	 */
    public CommandDocController() {
        super();
    }

    /**
	 * @param nextHandler
	 */
    public CommandDocController(ActionHandler nextHandler) {
        super(nextHandler);
    }

    /**
	 * @see mipt.mvc.ActionHandler#performAction(mipt.mvc.GeneralEvent)
	 */
    public boolean performAction(GeneralEvent event) {
        if (!isAccepted(event)) return false;
        getCommand(event).execute(getSpecification(event));
        return true;
    }

    /**
	 * Can be overriden 
	 */
    public boolean isAccepted(GeneralEvent event) {
        return CommandGeneralController.isEventAccepted(event);
    }

    /**
	 * Can be overriden
	 */
    public Command getCommand(GeneralEvent event) {
        return CommandGeneralController.getCommandFromEvent(event);
    }

    /**
	 * Can be overriden
	 */
    public Object getSpecification(GeneralEvent event) {
        return CommandGeneralController.getSpecificationFromEvent(event);
    }
}
