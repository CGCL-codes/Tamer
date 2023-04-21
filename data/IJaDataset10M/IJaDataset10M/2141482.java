package sbpme.designer.policy;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import sbpme.designer.command.ActivityRenameCommand;
import sbpme.designer.model.Activity;

public class ActivityDirectEditPolicy extends DirectEditPolicy {

    protected Command getDirectEditCommand(DirectEditRequest request) {
        ActivityRenameCommand cmd = new ActivityRenameCommand();
        cmd.setSource((Activity) getHost().getModel());
        cmd.setOldName(((Activity) getHost().getModel()).getName());
        cmd.setName((String) request.getCellEditor().getValue());
        return cmd;
    }

    protected void showCurrentEditValue(DirectEditRequest request) {
        String value = (String) request.getCellEditor().getValue();
        ((Label) getHostFigure()).setText(value);
    }
}
