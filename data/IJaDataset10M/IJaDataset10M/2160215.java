package it.cnr.stlab.xd.plugin.editor.parts.policies;

import it.cnr.stlab.xd.plugin.editor.commands.ClassCreateCommand;
import it.cnr.stlab.xd.plugin.editor.commands.DataPropertyCreateCommand;
import it.cnr.stlab.xd.plugin.editor.commands.IndividualCreateCommand;
import it.cnr.stlab.xd.plugin.editor.commands.NodeSetConstraintCommand;
import it.cnr.stlab.xd.plugin.editor.commands.ObjectPropertyCreateCommand;
import it.cnr.stlab.xd.plugin.editor.model.WorkingOntology;
import it.cnr.stlab.xd.plugin.editor.model.Node;
import it.cnr.stlab.xd.plugin.editor.model.NodeClass;
import it.cnr.stlab.xd.plugin.editor.model.NodeDataProperty;
import it.cnr.stlab.xd.plugin.editor.model.NodeIndividual;
import it.cnr.stlab.xd.plugin.editor.model.NodeObjectProperty;
import it.cnr.stlab.xd.plugin.editor.parts.OntologyNodeEditPart;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public final class OntologyXYLayoutEditPolicy extends XYLayoutEditPolicy {

    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        if (child instanceof OntologyNodeEditPart && constraint instanceof Rectangle) {
            return new NodeSetConstraintCommand((Node) child.getModel(), request, (Rectangle) constraint);
        }
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        return null;
    }

    /**
	 * This command can be used to add any node to the ontology
	 */
    protected Command getCreateCommand(CreateRequest request) {
        Object childClass = request.getNewObjectType();
        WorkingOntology ontology = (WorkingOntology) getHost().getModel();
        Rectangle rect = (Rectangle) getConstraintFor(request);
        if (childClass == NodeClass.class) return new ClassCreateCommand((NodeClass) request.getNewObject(), ontology, rect); else if (childClass == NodeObjectProperty.class) return new ObjectPropertyCreateCommand((NodeObjectProperty) request.getNewObject(), ontology, rect); else if (childClass == NodeDataProperty.class) return new DataPropertyCreateCommand((NodeDataProperty) request.getNewObject(), ontology, rect); else if (childClass == NodeIndividual.class) return new IndividualCreateCommand((NodeIndividual) request.getNewObject(), ontology, rect);
        return null;
    }
}
