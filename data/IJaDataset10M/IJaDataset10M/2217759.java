package net.sourceforge.dynamicgmf.diagram.edit.policies;

import net.sourceforge.dynamicgmf.diagram.edit.commands.EContainmentReorientCommand;
import net.sourceforge.dynamicgmf.diagram.edit.commands.EReferenceCreateCommand;
import net.sourceforge.dynamicgmf.diagram.edit.commands.EReferenceReorientCommand;
import net.sourceforge.dynamicgmf.diagram.edit.parts.EContainmentEditPart;
import net.sourceforge.dynamicgmf.diagram.edit.parts.EReferenceEditPart;
import net.sourceforge.dynamicgmf.diagram.providers.EcoreModelElementTypes;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class EClassItemSemanticEditPolicy extends EcoreModelBaseItemSemanticEditPolicy {

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
	 * @generated NOT
	 */
    protected Command getStartCreateRelationshipCommand(CreateRelationshipRequest req) {
        View view = (View) getHost().getModel();
        EClass containerEClass = view.getElement().eClass();
        EReference eReference = (EReference) EcoreModelElementTypes.getElement(req.getElementType());
        if (containerEClass.getEAllReferences().contains(eReference)) {
            return getGEFWrapper(new EReferenceCreateCommand(req, req.getSource(), req.getTarget(), eReference));
        }
        return null;
    }

    /**
	 * @generated NOT
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        View view = (View) getHost().getModel();
        EClass targetEClass = view.getElement().eClass();
        EReference eReference = (EReference) EcoreModelElementTypes.getElement(req.getElementType());
        EClass eReferenceType = eReference.getEReferenceType();
        if (eReferenceType.isSuperTypeOf(targetEClass)) {
            return getGEFWrapper(new EReferenceCreateCommand(req, req.getSource(), req.getTarget(), eReference));
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
            case EContainmentEditPart.VISUAL_ID:
                return getGEFWrapper(new EContainmentReorientCommand(req));
            case EReferenceEditPart.VISUAL_ID:
                return getGEFWrapper(new EReferenceReorientCommand(req));
        }
        return super.getReorientReferenceRelationshipCommand(req);
    }
}
