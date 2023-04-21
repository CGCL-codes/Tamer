package hub.sam.mof.simulator.editor.diagram.edit.policies;

import hub.sam.mof.simulator.editor.diagram.edit.commands.EAnnotationReferencesCreateCommand;
import hub.sam.mof.simulator.editor.diagram.edit.commands.EAnnotationReferencesReorientCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;

/**
 * @generated
 */
public class MOperation2ItemSemanticEditPolicy extends M3ActionsBaseItemSemanticEditPolicy {

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
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.EAnnotationReferences_3001 == req.getElementType()) {
            return null;
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.EAnnotationReferences_3001 == req.getElementType()) {
            return getGEFWrapper(new EAnnotationReferencesCreateCommand(req, req.getSource(), req.getTarget()));
        }
        return null;
    }

    /**
	 * Returns command to reorient EReference based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
    protected Command getReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest req) {
        switch(getVisualID(req)) {
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EAnnotationReferencesEditPart.VISUAL_ID:
                return getGEFWrapper(new EAnnotationReferencesReorientCommand(req));
        }
        return super.getReorientReferenceRelationshipCommand(req);
    }
}
