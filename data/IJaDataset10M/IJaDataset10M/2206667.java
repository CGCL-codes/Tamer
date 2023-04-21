package de.hu_berlin.sam.mmunit.diagram.edit.policies;

import java.util.Iterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class AttributeItemSemanticEditPolicy extends de.hu_berlin.sam.mmunit.diagram.edit.policies.MMUnitBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    public AttributeItemSemanticEditPolicy() {
        super(de.hu_berlin.sam.mmunit.diagram.providers.MMUnitElementTypes.Attribute_3001);
    }

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        View view = (View) getHost().getModel();
        CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
        cmd.setTransactionNestingEnabled(false);
        for (Iterator it = view.getTargetEdges().iterator(); it.hasNext(); ) {
            Edge incomingLink = (Edge) it.next();
            if (de.hu_berlin.sam.mmunit.diagram.part.MMUnitVisualIDRegistry.getVisualID(incomingLink) == de.hu_berlin.sam.mmunit.diagram.edit.parts.ConjunctionElementsEditPart.VISUAL_ID) {
                DestroyReferenceRequest r = new DestroyReferenceRequest(incomingLink.getSource().getElement(), null, incomingLink.getTarget().getElement(), false);
                cmd.add(new DestroyReferenceCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                continue;
            }
        }
        EAnnotation annotation = view.getEAnnotation("Shortcut");
        if (annotation == null) {
            addDestroyShortcutsCommand(cmd, view);
            cmd.add(new DestroyElementCommand(req));
        } else {
            cmd.add(new DeleteCommand(getEditingDomain(), view));
        }
        return getGEFWrapper(cmd.reduce());
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
        if (de.hu_berlin.sam.mmunit.diagram.providers.MMUnitElementTypes.ConjunctionElements_4002 == req.getElementType()) {
            return null;
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (de.hu_berlin.sam.mmunit.diagram.providers.MMUnitElementTypes.ConjunctionElements_4002 == req.getElementType()) {
            return getGEFWrapper(new de.hu_berlin.sam.mmunit.diagram.edit.commands.ConjunctionElementsCreateCommand(req, req.getSource(), req.getTarget()));
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
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.ConjunctionElementsEditPart.VISUAL_ID:
                return getGEFWrapper(new de.hu_berlin.sam.mmunit.diagram.edit.commands.ConjunctionElementsReorientCommand(req));
        }
        return super.getReorientReferenceRelationshipCommand(req);
    }
}
