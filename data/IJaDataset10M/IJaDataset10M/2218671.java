package org.plazmaforge.studio.dbdesigner.policies;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.plazmaforge.studio.dbdesigner.commands.ERDConnectionCreateCommand;
import org.plazmaforge.studio.dbdesigner.commands.ERDLinkCreateCommand;
import org.plazmaforge.studio.dbdesigner.commands.ERDLinkReconnectCommand;
import org.plazmaforge.studio.dbdesigner.model.ERBasicModel;

public class ERNodeEditPolicy2 extends GraphicalNodeEditPolicy {

    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        ERDConnectionCreateCommand cmd = (ERDConnectionCreateCommand) request.getStartCommand();
        cmd.setTarget((ERBasicModel) getHost().getModel());
        return cmd;
    }

    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        try {
            ERBasicModel source = (ERBasicModel) getHost().getModel();
            Object type = request.getNewObjectType();
            ERDLinkCreateCommand cmd = new ERDLinkCreateCommand(source, type);
            request.setStartCommand(cmd);
            return cmd;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        ERDLinkReconnectCommand cmd = new ERDLinkReconnectCommand();
        return cmd;
    }

    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        ERDLinkReconnectCommand cmd = new ERDLinkReconnectCommand();
        return cmd;
    }

    protected ConnectionAnchor getSourceConnectionAnchor(CreateConnectionRequest request) {
        EditPart source = request.getSourceEditPart();
        ConnectionAnchor anchor = (source instanceof NodeEditPart) ? ((NodeEditPart) source).getSourceConnectionAnchor(request) : null;
        return anchor;
    }

    protected ConnectionAnchor getTargetConnectionAnchor(CreateConnectionRequest request) {
        EditPart target = request.getTargetEditPart();
        ConnectionAnchor anchor = (target instanceof NodeEditPart) ? ((NodeEditPart) target).getTargetConnectionAnchor(request) : null;
        return anchor;
    }
}
