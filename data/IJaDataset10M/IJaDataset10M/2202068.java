package SensorDataWebGui.diagram.edit.policies;

import java.util.Iterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class SourceItemSemanticEditPolicy extends SensorDataWebGui.diagram.edit.policies.SensorDataWebGuiBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    public SourceItemSemanticEditPolicy() {
        super(SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.Source_2001);
    }

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        View view = (View) getHost().getModel();
        CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
        cmd.setTransactionNestingEnabled(false);
        for (Iterator it = view.getSourceEdges().iterator(); it.hasNext(); ) {
            Edge outgoingLink = (Edge) it.next();
            if (SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getVisualID(outgoingLink) == SensorDataWebGui.diagram.edit.parts.FixedWindowEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(), false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
            }
            if (SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getVisualID(outgoingLink) == SensorDataWebGui.diagram.edit.parts.TupleWindowEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(), false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                continue;
            }
            if (SensorDataWebGui.diagram.part.SensorDataWebGuiVisualIDRegistry.getVisualID(outgoingLink) == SensorDataWebGui.diagram.edit.parts.TimeWindowEditPart.VISUAL_ID) {
                DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(), false);
                cmd.add(new DestroyElementCommand(r));
                cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
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
        if (SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.FixedWindow_4001 == req.getElementType()) {
            return getGEFWrapper(new SensorDataWebGui.diagram.edit.commands.FixedWindowCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TupleWindow_4002 == req.getElementType()) {
            return getGEFWrapper(new SensorDataWebGui.diagram.edit.commands.TupleWindowCreateCommand(req, req.getSource(), req.getTarget()));
        }
        if (SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TimeWindow_4003 == req.getElementType()) {
            return getGEFWrapper(new SensorDataWebGui.diagram.edit.commands.TimeWindowCreateCommand(req, req.getSource(), req.getTarget()));
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.FixedWindow_4001 == req.getElementType()) {
            return null;
        }
        if (SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TupleWindow_4002 == req.getElementType()) {
            return null;
        }
        if (SensorDataWebGui.diagram.providers.SensorDataWebGuiElementTypes.TimeWindow_4003 == req.getElementType()) {
            return null;
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
            case SensorDataWebGui.diagram.edit.parts.FixedWindowEditPart.VISUAL_ID:
                return getGEFWrapper(new SensorDataWebGui.diagram.edit.commands.FixedWindowReorientCommand(req));
            case SensorDataWebGui.diagram.edit.parts.TupleWindowEditPart.VISUAL_ID:
                return getGEFWrapper(new SensorDataWebGui.diagram.edit.commands.TupleWindowReorientCommand(req));
            case SensorDataWebGui.diagram.edit.parts.TimeWindowEditPart.VISUAL_ID:
                return getGEFWrapper(new SensorDataWebGui.diagram.edit.commands.TimeWindowReorientCommand(req));
        }
        return super.getReorientRelationshipCommand(req);
    }
}
