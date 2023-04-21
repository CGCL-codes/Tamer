package org.argetr.resim.ui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.argetr.resim.ui.model.ModelElement;
import org.argetr.resim.ui.model.Shape;
import org.argetr.resim.ui.model.ShapesDiagram;
import org.argetr.resim.ui.model.commands.ShapeCreateCommand;
import org.argetr.resim.ui.model.commands.ShapeSetConstraintCommand;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * EditPart for the a ShapesDiagram instance.
 * <p>
 * This edit part server as the main diagram container, the white area where
 * everything else is in. Also responsible for the container's layout (the way
 * the container rearanges is contents) and the container's capabilities (edit
 * policies).
 * </p>
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so it can
 * be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
public class DiagramEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    /**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((ModelElement) getModel()).addPropertyChangeListener(this);
        }
    }

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ShapesXYLayoutEditPolicy());
    }

    protected IFigure createFigure() {
        Figure f = new FreeformLayer();
        f.setBorder(new MarginBorder(3));
        f.setLayoutManager(new FreeformLayout());
        return f;
    }

    /**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((ModelElement) getModel()).removePropertyChangeListener(this);
        }
    }

    public ShapesDiagram getCastedModel() {
        return (ShapesDiagram) getModel();
    }

    protected List<Shape> getModelChildren() {
        return getCastedModel().getChildren();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (ShapesDiagram.CHILD_ADDED_PROP.equals(prop) || ShapesDiagram.CHILD_REMOVED_PROP.equals(prop)) {
            refreshChildren();
        }
    }

    /**
	 * EditPolicy for the Figure used by this edit part. Children of
	 * XYLayoutEditPolicy can be used in Figures with XYLayout.
	 * 
	 * @author Elias Volanakis
	 */
    private class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy {

        protected Command createAddCommand(EditPart child, Object constraint) {
            return null;
        }

        protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
            Object type = request.getType();
            if (RequestConstants.REQ_MOVE.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type)) {
                if (child instanceof ShapeEditPart && constraint instanceof Rectangle) {
                    return new ShapeSetConstraintCommand((Shape) child.getModel(), request, (Rectangle) constraint);
                }
            }
            return super.createChangeConstraintCommand(request, child, constraint);
        }

        protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
            return null;
        }

        protected Command getCreateCommand(CreateRequest request) {
            Object childObject = request.getNewObject();
            if (null != Shape.class.cast(childObject)) {
                return new ShapeCreateCommand(DiagramEditPart.this.getCastedModel(), request);
            }
            return null;
        }

        protected Command getDeleteDependantCommand(Request request) {
            return null;
        }
    }
}
