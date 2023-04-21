package jfb.examples.gmf.math.diagram.edit.policies;

import java.util.Iterator;
import jfb.examples.gmf.math.diagram.edit.parts.NumberOperatorInputsEditPart;
import jfb.examples.gmf.math.diagram.edit.parts.OperatorInputEditPart;
import jfb.examples.gmf.math.diagram.edit.parts.OperatorOutputEditPart;
import jfb.examples.gmf.math.diagram.edit.parts.OperatorOutputResultEditPart;
import jfb.examples.gmf.math.diagram.edit.parts.PlusOperatorPlusOperatorFigureCompartmentEditPart;
import jfb.examples.gmf.math.diagram.part.MathVisualIDRegistry;
import jfb.examples.gmf.math.diagram.providers.MathElementTypes;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class PlusOperatorItemSemanticEditPolicy extends MathBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    public PlusOperatorItemSemanticEditPolicy() {
        super(MathElementTypes.PlusOperator_2004);
    }

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        View view = (View) getHost().getModel();
        CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
        cmd.setTransactionNestingEnabled(false);
        EAnnotation annotation = view.getEAnnotation("Shortcut");
        if (annotation == null) {
            addDestroyChildNodesCommand(cmd);
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
    private void addDestroyChildNodesCommand(ICompositeCommand cmd) {
        View view = (View) getHost().getModel();
        for (Iterator nit = view.getChildren().iterator(); nit.hasNext(); ) {
            Node node = (Node) nit.next();
            switch(MathVisualIDRegistry.getVisualID(node)) {
                case PlusOperatorPlusOperatorFigureCompartmentEditPart.VISUAL_ID:
                    for (Iterator cit = node.getChildren().iterator(); cit.hasNext(); ) {
                        Node cnode = (Node) cit.next();
                        switch(MathVisualIDRegistry.getVisualID(cnode)) {
                            case OperatorInputEditPart.VISUAL_ID:
                                for (Iterator it = cnode.getTargetEdges().iterator(); it.hasNext(); ) {
                                    Edge incomingLink = (Edge) it.next();
                                    if (MathVisualIDRegistry.getVisualID(incomingLink) == NumberOperatorInputsEditPart.VISUAL_ID) {
                                        DestroyReferenceRequest r = new DestroyReferenceRequest(incomingLink.getSource().getElement(), null, incomingLink.getTarget().getElement(), false);
                                        cmd.add(new DestroyReferenceCommand(r));
                                        cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                                        continue;
                                    }
                                }
                                cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode.getElement(), false)));
                                break;
                            case OperatorOutputEditPart.VISUAL_ID:
                                for (Iterator it = cnode.getSourceEdges().iterator(); it.hasNext(); ) {
                                    Edge outgoingLink = (Edge) it.next();
                                    if (MathVisualIDRegistry.getVisualID(outgoingLink) == OperatorOutputResultEditPart.VISUAL_ID) {
                                        DestroyReferenceRequest r = new DestroyReferenceRequest(outgoingLink.getSource().getElement(), null, outgoingLink.getTarget().getElement(), false);
                                        cmd.add(new DestroyReferenceCommand(r));
                                        cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                                        continue;
                                    }
                                }
                                cmd.add(new DestroyElementCommand(new DestroyElementRequest(getEditingDomain(), cnode.getElement(), false)));
                                break;
                        }
                    }
                    break;
            }
        }
    }
}
