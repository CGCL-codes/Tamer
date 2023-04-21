package se.mdh.mrtc.saveccm.assembly.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.ConnectionComplexComplexFromCreateCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.ConnectionComplexComplexFromReorientCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.ConnectionComplexComplexToCreateCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.ConnectionComplexComplexToReorientCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.ConnectionCreateCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.ConnectionReorientCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.DelegationCreateCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.commands.DelegationReorientCommand;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.parts.ConnectionComplexComplexFromEditPart;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.parts.ConnectionComplexComplexToEditPart;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.parts.ConnectionEditPart;
import se.mdh.mrtc.saveccm.assembly.diagram.edit.parts.DelegationEditPart;
import se.mdh.mrtc.saveccm.assembly.diagram.providers.SaveccmElementTypes;

/**
 * @generated
 */
public class TriggerIn6ItemSemanticEditPolicy extends SaveccmBaseItemSemanticEditPolicy {

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
        if (SaveccmElementTypes.Connection_3001 == req.getElementType()) {
            return getGEFWrapper(new ConnectionCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (SaveccmElementTypes.Delegation_3002 == req.getElementType()) {
            return getGEFWrapper(new DelegationCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (SaveccmElementTypes.ConnectionComplexComplexFrom_3003 == req.getElementType()) {
            return null;
        }
        if (SaveccmElementTypes.ConnectionComplexComplexTo_3004 == req.getElementType()) {
            return null;
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (SaveccmElementTypes.Connection_3001 == req.getElementType()) {
            return getGEFWrapper(new ConnectionCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (SaveccmElementTypes.Delegation_3002 == req.getElementType()) {
            return getGEFWrapper(new DelegationCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (SaveccmElementTypes.ConnectionComplexComplexFrom_3003 == req.getElementType()) {
            return getGEFWrapper(new ConnectionComplexComplexFromCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (SaveccmElementTypes.ConnectionComplexComplexTo_3004 == req.getElementType()) {
            return getGEFWrapper(new ConnectionComplexComplexToCreateCommand(req, req.getSource(), req.getTarget()));
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
            case ConnectionEditPart.VISUAL_ID:
                return getGEFWrapper(new ConnectionReorientCommand(req));
            case DelegationEditPart.VISUAL_ID:
                return getGEFWrapper(new DelegationReorientCommand(req));
        }
        return super.getReorientRelationshipCommand(req);
    }

    /**
	 * Returns command to reorient EReference based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
    protected Command getReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest req) {
        switch(getVisualID(req)) {
            case ConnectionComplexComplexFromEditPart.VISUAL_ID:
                return getGEFWrapper(new ConnectionComplexComplexFromReorientCommand(req));
            case ConnectionComplexComplexToEditPart.VISUAL_ID:
                return getGEFWrapper(new ConnectionComplexComplexToReorientCommand(req));
        }
        return super.getReorientReferenceRelationshipCommand(req);
    }
}
