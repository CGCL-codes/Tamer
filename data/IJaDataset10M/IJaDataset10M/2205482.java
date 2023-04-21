package com.byterefinery.rmbench.editpolicies;

import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FeedbackHelper;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import com.byterefinery.rmbench.editparts.TableEditPart;
import com.byterefinery.rmbench.operations.AddForeignKeyOperation;
import com.byterefinery.rmbench.operations.CommandAdapter;

/**
 * edit policy for creating foreign key connections
 * 
 * @author cse
 */
public class TableNodeEditPolicy extends GraphicalNodeEditPolicy {

    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        TableEditPart part = (TableEditPart) request.getTargetEditPart();
        CommandAdapter cmd = (CommandAdapter) request.getStartCommand();
        ((AddForeignKeyOperation) cmd.getOperation()).setTargetTable(part.getTable());
        return cmd;
    }

    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        TableEditPart part = (TableEditPart) getHost();
        Command cmd = new CommandAdapter(new AddForeignKeyOperation(part.getTable()));
        request.setStartCommand(cmd);
        return cmd;
    }

    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        return null;
    }

    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        return null;
    }

    protected FeedbackHelper getFeedbackHelper(CreateConnectionRequest request) {
        if (feedbackHelper == null) {
            feedbackHelper = new FeedbackHelper();
            Point p = request.getLocation();
            connectionFeedback = createDummyConnection(request);
            connectionFeedback.setConnectionRouter(new ManhattanConnectionRouter());
            connectionFeedback.setSourceAnchor(getSourceConnectionAnchor(request));
            feedbackHelper.setConnection(connectionFeedback);
            addFeedback(connectionFeedback);
            feedbackHelper.update(null, p);
        }
        return feedbackHelper;
    }
}
