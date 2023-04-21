package se.mdh.mrtc.saveccm.swi.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.swi.diagram.edit.commands.SwitchConnectionCreateCommand;
import se.mdh.mrtc.saveccm.swi.diagram.edit.commands.SwitchConnectionReorientCommand;
import se.mdh.mrtc.saveccm.swi.diagram.edit.parts.SwitchConnectionEditPart;
import se.mdh.mrtc.saveccm.swi.diagram.providers.SaveccmElementTypes;

/**
 * @generated
 */
public class DataOutItemSemanticEditPolicy extends SaveccmBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        addDestroyShortcutsCommand(cc);
        View view = (View) getHost().getModel();
        if (view.getEAnnotation("Shortcut") != null) {
            req.setElementToDestroy(view);
        }
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
        if (SaveccmElementTypes.SwitchConnection_3002 == req.getElementType()) {
            return getGEFWrapper(new SwitchConnectionCreateCommand(req, req.getSource(), req.getTarget()));
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (SaveccmElementTypes.SwitchConnection_3002 == req.getElementType()) {
            return getGEFWrapper(new SwitchConnectionCreateCommand(req, req.getSource(), req.getTarget()));
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
            case SwitchConnectionEditPart.VISUAL_ID:
                return getGEFWrapper(new SwitchConnectionReorientCommand(req));
        }
        return super.getReorientRelationshipCommand(req);
    }
}
