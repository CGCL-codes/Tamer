package ieditor.parts;

import ieditor.model.Element;
import ieditor.model.PageDiagram;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

/**
 * TreeEditPart for a ShapesDiagram instance.
 * This is used in the Outline View of the ShapesEditor.
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
class DiagramTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

    /** 
 * Create a new instance of this edit part using the given model element.
 * @param model a non-null ShapesDiagram instance
 */
    DiagramTreeEditPart(PageDiagram model) {
        super(model);
    }

    /**
 * Upon activation, attach to the model element as a property change listener.
 */
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((Element) getModel()).addPropertyChangeListener(this);
        }
    }

    protected void createEditPolicies() {
        if (getParent() instanceof RootEditPart) {
            installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
        }
    }

    /**
 * Upon deactivation, detach from the model element as a property change listener.
 */
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((PageDiagram) getModel()).removePropertyChangeListener(this);
        }
    }

    private PageDiagram getCastedModel() {
        return (PageDiagram) getModel();
    }

    /**
 * Convenience method that returns the EditPart corresponding to a given child.
 * @param child a model element instance
 * @return the corresponding EditPart or null
 */
    private EditPart getEditPartForChild(Object child) {
        return (EditPart) getViewer().getEditPartRegistry().get(child);
    }

    protected List getModelChildren() {
        return getCastedModel().getChildren();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (PageDiagram.CHILD_ADDED_PROP.equals(prop)) {
            addChild(createChild(evt.getNewValue()), -1);
        } else if (PageDiagram.CHILD_REMOVED_PROP.equals(prop)) {
            removeChild(getEditPartForChild(evt.getNewValue()));
        } else {
            refreshVisuals();
        }
    }
}
