package org.ist.contract.edit;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.ist.contract.commands.ConnectionRoleenactmentCommand;
import org.ist.contract.impl.RoleenactmentImpl;

/**
 * Edit policy for an Role.
 */
public class RoleEnactmentEditPolicy extends ConnectionEditPolicy {

    /**
	 * 
	 */
    public RoleEnactmentEditPolicy() {
        super();
    }

    protected Command getDeleteCommand(GroupRequest request) {
        ConnectionRoleenactmentCommand c = new ConnectionRoleenactmentCommand();
        c.setRole((RoleenactmentImpl) getHost().getModel());
        return c;
    }
}
