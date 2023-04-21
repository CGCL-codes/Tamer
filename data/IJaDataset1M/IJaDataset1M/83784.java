package net.sf.signs.views.fsm.policies;

import net.sf.signs.views.fsm.model.FSMState;
import net.sf.signs.views.fsm.model.StructuredFSMState;
import net.sf.signs.views.fsm.model.commands.AddAndAssignSourceCommand;
import net.sf.signs.views.fsm.model.commands.CreateAndAssignSourceCommand;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author Daniel Lee
 */
public class ActivitySourceEditPolicy extends ContainerEditPolicy {

    /**
 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getAddCommand(org.eclipse.gef.requests.GroupRequest)
 */
    protected Command getAddCommand(GroupRequest request) {
        CompoundCommand cmd = new CompoundCommand();
        for (int i = 0; i < request.getEditParts().size(); i++) {
            AddAndAssignSourceCommand add = new AddAndAssignSourceCommand();
            add.setParent((StructuredFSMState) getHost().getParent().getModel());
            add.setSource((FSMState) getHost().getModel());
            add.setChild(((FSMState) ((EditPart) request.getEditParts().get(i)).getModel()));
            cmd.add(add);
        }
        return cmd;
    }

    /**
 * @see ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
 */
    protected Command getCreateCommand(CreateRequest request) {
        CreateAndAssignSourceCommand cmd = new CreateAndAssignSourceCommand();
        cmd.setParent((StructuredFSMState) getHost().getParent().getModel());
        cmd.setChild((FSMState) request.getNewObject());
        cmd.setSource((FSMState) getHost().getModel());
        return cmd;
    }

    /**
 * @see AbstractEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
 */
    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) return getHost();
        if (REQ_ADD.equals(request.getType())) return getHost();
        if (REQ_MOVE.equals(request.getType())) return getHost();
        return super.getTargetEditPart(request);
    }
}
