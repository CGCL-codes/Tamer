package com.prolix.editor.graph.editparts.polices;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import com.prolix.editor.graph.commands.connection.BendPointCommand;
import com.prolix.editor.graph.commands.connection.CreateBendpointCommand;
import com.prolix.editor.graph.commands.connection.DeleteBendpointCommand;
import com.prolix.editor.graph.commands.connection.MoveBendpointCommand;
import com.prolix.editor.graph.editparts.connections.ConnectionEditPart;

public class ModelConnectionBendPointEditPolicy extends BendpointEditPolicy {

    protected Command getCreateBendpointCommand(BendpointRequest request) {
        CreateBendpointCommand com = new CreateBendpointCommand();
        setupCommand(com, request);
        return com;
    }

    protected Command getMoveBendpointCommand(BendpointRequest request) {
        MoveBendpointCommand com = new MoveBendpointCommand();
        setupCommand(com, request);
        return com;
    }

    protected Command getDeleteBendpointCommand(BendpointRequest request) {
        BendPointCommand com = new DeleteBendpointCommand();
        setupCommand(com, request);
        return com;
    }

    private void setupCommand(BendPointCommand com, BendpointRequest request) {
        com.setEditPart((ConnectionEditPart) request.getSource());
        com.setLocation(request.getLocation());
        com.setIndex(request.getIndex());
    }
}
