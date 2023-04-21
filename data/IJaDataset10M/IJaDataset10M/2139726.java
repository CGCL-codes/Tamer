package com.ecmdeveloper.plugin.search.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import com.ecmdeveloper.plugin.search.dnd.NativeDropRequest;

/**
 * 
 * @author ricardo.belfor
 *
 */
public class QueryTextEditPolicy extends QueryElementEditPolicy {

    public Command getCommand(Request request) {
        if (NativeDropRequest.ID.equals(request.getType())) return getDropTextCommand((NativeDropRequest) request);
        return super.getCommand(request);
    }

    protected Command getDropTextCommand(NativeDropRequest request) {
        return null;
    }

    public EditPart getTargetEditPart(Request request) {
        if (NativeDropRequest.ID.equals(request.getType())) return getHost();
        return super.getTargetEditPart(request);
    }
}
