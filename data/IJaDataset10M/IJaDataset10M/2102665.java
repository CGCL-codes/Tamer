package org.argetr.resim.ui.parts;

import org.argetr.resim.ui.model.Shape;
import org.argetr.resim.ui.model.ShapesDiagram;
import org.argetr.resim.ui.model.commands.ShapeDeleteCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * This edit policy enables the removal of a Shapes instance from its container.
 * 
 * @see ShapeEditPart#createEditPolicies()
 * @see ShapeTreeEditPart#createEditPolicies()
 * @author Elias Volanakis
 */
class ShapeComponentEditPolicy extends ComponentEditPolicy {

    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        Object parent = getHost().getParent().getModel();
        Object child = getHost().getModel();
        if (parent instanceof ShapesDiagram && (null != Shape.class.cast(child))) {
            return new ShapeDeleteCommand((ShapesDiagram) parent, (Shape) child);
        }
        return super.createDeleteCommand(deleteRequest);
    }
}
