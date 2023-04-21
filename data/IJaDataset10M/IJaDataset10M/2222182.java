package dataflowScheme.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

/**
 * @generated
 */
public class DataPort2ItemSemanticEditPolicy extends dataflowScheme.diagram.edit.policies.ModelBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        addDestroyShortcutsCommand(cc);
        cc.add(getGEFWrapper(new DestroyElementCommand(req)));
        return cc.unwrap();
    }

    /**
	 * @generated
	 */
    protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
        Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req) : getCompleteCreateRelationshipCommand(req);
        return command != null ? command : super.getCreateRelationshipCommand(req);
    }

    /**
	 * @generated
	 */
    protected Command getStartCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (dataflowScheme.diagram.providers.ModelElementTypes.DataConnection_3002 == req.getElementType()) {
            return getGEFWrapper(new dataflowScheme.diagram.edit.commands.DataConnectionCreateCommand(req, req.getSource(), req.getTarget()));
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (dataflowScheme.diagram.providers.ModelElementTypes.DataConnection_3002 == req.getElementType()) {
            return getGEFWrapper(new dataflowScheme.diagram.edit.commands.DataConnectionCreateCommand(req, req.getSource(), req.getTarget()));
        }
        return null;
    }

    /**
	 * Returns command to reorient EClass based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
    protected Command getReorientRelationshipCommand(ReorientRelationshipRequest req) {
        switch(getVisualID(req)) {
            case dataflowScheme.diagram.edit.parts.DataConnectionEditPart.VISUAL_ID:
                return getGEFWrapper(new dataflowScheme.diagram.edit.commands.DataConnectionReorientCommand(req));
        }
        return super.getReorientRelationshipCommand(req);
    }
}
