package com.safi.workshop.edit.policies;

import java.util.Iterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import com.safi.core.actionstep.ActionStepPackage;
import com.safi.workshop.edit.commands.CaseItemTargetToolstepCreateCommand;
import com.safi.workshop.edit.commands.CaseItemTargetToolstepReorientCommand;
import com.safi.workshop.edit.commands.OutputCreateCommand;
import com.safi.workshop.edit.commands.OutputTargetCreateCommand;
import com.safi.workshop.edit.commands.OutputTargetReorientCommand;
import com.safi.workshop.edit.parts.CaseItemTargetToolstepEditPart;
import com.safi.workshop.edit.parts.OutputEditPart;
import com.safi.workshop.edit.parts.OutputTargetEditPart;
import com.safi.workshop.part.AsteriskVisualIDRegistry;
import com.safi.workshop.providers.AsteriskElementTypes;

/**
 * @generated
 */
public class SetColValueItemSemanticEditPolicy extends AsteriskBaseItemSemanticEditPolicy {

    /**
   * @generated
   */
    @Override
    protected Command getCreateCommand(CreateElementRequest req) {
        if (AsteriskElementTypes.Output_2001 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(ActionStepPackage.eINSTANCE.getActionStep_Outputs());
            }
            return getGEFWrapper(new OutputCreateCommand(req));
        }
        return super.getCreateCommand(req);
    }

    /**
   * @generated
   */
    @Override
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        addDestroyChildNodesCommand(cc);
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
    protected void addDestroyChildNodesCommand(CompoundCommand cmd) {
        View view = (View) getHost().getModel();
        EAnnotation annotation = view.getEAnnotation("Shortcut");
        if (annotation != null) {
            return;
        }
        for (Iterator it = view.getChildren().iterator(); it.hasNext(); ) {
            Node node = (Node) it.next();
            switch(AsteriskVisualIDRegistry.getVisualID(node)) {
                case OutputEditPart.VISUAL_ID:
                    cmd.add(getDestroyElementCommand(node));
                    break;
            }
        }
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
        if (AsteriskElementTypes.OutputTarget_3001 == req.getElementType()) {
            return null;
        }
        if (AsteriskElementTypes.CaseItemTargetToolstep_3003 == req.getElementType()) {
            return null;
        }
        return null;
    }

    /**
   * @generated
   */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (AsteriskElementTypes.OutputTarget_3001 == req.getElementType()) {
            return getGEFWrapper(new OutputTargetCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (AsteriskElementTypes.CaseItemTargetToolstep_3003 == req.getElementType()) {
            return getGEFWrapper(new CaseItemTargetToolstepCreateCommand(req, req.getSource(), req.getTarget()));
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
            case OutputTargetEditPart.VISUAL_ID:
                return getGEFWrapper(new OutputTargetReorientCommand(req));
            case CaseItemTargetToolstepEditPart.VISUAL_ID:
                return getGEFWrapper(new CaseItemTargetToolstepReorientCommand(req));
        }
        return super.getReorientReferenceRelationshipCommand(req);
    }
}
