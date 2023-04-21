package se.mdh.mrtc.save.taEditor.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class CommittedLocationItemSemanticEditPolicy extends se.mdh.mrtc.save.taEditor.diagram.edit.policies.TaEditorBaseItemSemanticEditPolicy {

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
        if (se.mdh.mrtc.save.taEditor.diagram.providers.TaEditorElementTypes.StandardEdge_3001 == req.getElementType()) {
            return getGEFWrapper(new se.mdh.mrtc.save.taEditor.diagram.edit.commands.StandardEdgeCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (se.mdh.mrtc.save.taEditor.diagram.providers.TaEditorElementTypes.UrgentEdge_3002 == req.getElementType()) {
            return getGEFWrapper(new se.mdh.mrtc.save.taEditor.diagram.edit.commands.UrgentEdgeCreateCommand(req, req.getSource(), req.getTarget()));
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (se.mdh.mrtc.save.taEditor.diagram.providers.TaEditorElementTypes.StandardEdge_3001 == req.getElementType()) {
            return getGEFWrapper(new se.mdh.mrtc.save.taEditor.diagram.edit.commands.StandardEdgeCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (se.mdh.mrtc.save.taEditor.diagram.providers.TaEditorElementTypes.UrgentEdge_3002 == req.getElementType()) {
            return getGEFWrapper(new se.mdh.mrtc.save.taEditor.diagram.edit.commands.UrgentEdgeCreateCommand(req, req.getSource(), req.getTarget()));
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
            case se.mdh.mrtc.save.taEditor.diagram.edit.parts.StandardEdgeEditPart.VISUAL_ID:
                return getGEFWrapper(new se.mdh.mrtc.save.taEditor.diagram.edit.commands.StandardEdgeReorientCommand(req));
            case se.mdh.mrtc.save.taEditor.diagram.edit.parts.UrgentEdgeEditPart.VISUAL_ID:
                return getGEFWrapper(new se.mdh.mrtc.save.taEditor.diagram.edit.commands.UrgentEdgeReorientCommand(req));
        }
        return super.getReorientRelationshipCommand(req);
    }
}
