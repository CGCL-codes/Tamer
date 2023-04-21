package org.ist.contract.edit;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * EditPolicy for a Agent.
 */
public class ContractObjectEditPolicy extends GraphicalNodeEditPolicy {

    public ContractObjectEditPolicy() {
        super();
    }

    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        Command command = request.getStartCommand();
        return command;
    }

    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        return null;
    }

    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        return null;
    }

    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        return null;
    }
}
