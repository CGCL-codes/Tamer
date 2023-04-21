package org.gvt.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.gvt.command.DeleteConnectionCommand;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ChsConnectionEditPolicy extends ConnectionEditPolicy {

    protected Command getDeleteCommand(GroupRequest request) {
        DeleteConnectionCommand command = new DeleteConnectionCommand();
        command.setConnectionModel(getHost().getModel());
        return command;
    }
}
