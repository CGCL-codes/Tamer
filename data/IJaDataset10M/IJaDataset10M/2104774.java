package visualbiology.reactionEditor.editpolicies;

import myGEF.commands.DeleteCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class AppDeletePolicy extends ComponentEditPolicy {

    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        DeleteCommand command = new DeleteCommand();
        command.setModel(getHost().getModel());
        command.setParentModel(getHost().getParent().getModel());
        return command;
    }
}
