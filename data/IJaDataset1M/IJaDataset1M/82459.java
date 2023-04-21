package com.safi.workshop.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import com.safi.workshop.edit.commands.CaseItemTargetToolstepCreateCommand;
import com.safi.workshop.edit.commands.CaseItemTargetToolstepReorientCommand;
import com.safi.workshop.edit.parts.CaseItemTargetToolstepEditPart;
import com.safi.workshop.providers.AsteriskElementTypes;

/**
 * @generated
 */
public class CaseItemItemSemanticEditPolicy extends AsteriskBaseItemSemanticEditPolicy {

    /**
   * @generated
   */
    @Override
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        addDestroyShortcutsCommand(cc);
        cc.add(getGEFWrapper(new DestroyElementCommand(req)));
        return cc.unwrap();
    }

    /**
   * @generated
   */
    @Override
    protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
        Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req) : getCompleteCreateRelationshipCommand(req);
        return command != null ? command : super.getCreateRelationshipCommand(req);
    }

    /**
   * @generated
   */
    protected Command getStartCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (AsteriskElementTypes.CaseItemTargetToolstep_3003 == req.getElementType()) {
            return getGEFWrapper(new CaseItemTargetToolstepCreateCommand(req, req.getSource(), req.getTarget()));
        }
        return null;
    }

    /**
   * @generated
   */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (AsteriskElementTypes.CaseItemTargetToolstep_3003 == req.getElementType()) {
            return null;
        }
        return null;
    }

    /**
   * Returns command to reorient EReference based link. New link target or source should
   * be the domain model element associated with this node.
   * 
   * @generated
   */
    @Override
    protected Command getReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest req) {
        switch(getVisualID(req)) {
            case CaseItemTargetToolstepEditPart.VISUAL_ID:
                return getGEFWrapper(new CaseItemTargetToolstepReorientCommand(req));
        }
        return super.getReorientReferenceRelationshipCommand(req);
    }
}
