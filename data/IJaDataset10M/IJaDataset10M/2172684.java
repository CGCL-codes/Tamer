package tec.stan.ling.topo.graph.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class MapXYLayoutEditPolicy extends XYLayoutEditPolicy {

    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        return null;
    }

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }
}
