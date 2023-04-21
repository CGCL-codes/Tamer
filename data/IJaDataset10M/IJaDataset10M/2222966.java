package org.xaware.ide.xadev.processview.controller;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.xaware.ide.xadev.processview.model.ComponentModel;
import org.xaware.ide.xadev.processview.model.ConnectionModel;
import org.xaware.ide.xadev.processview.model.DecisionMergeModel;
import org.xaware.ide.xadev.processview.model.ProcessViewModel;

/**
 * Factory that creates controllers for the model objects.
 */
public class ProcessViewControllerFactory implements EditPartFactory {

    /**
     * Create the controller for the given model object
     * 
     * @param context
     *            The context in which the EditPart is being created, such as its parent
     * @param modelElement
     * @return Controller for the given model object
     */
    public EditPart createEditPart(final EditPart context, final Object modelElement) {
        EditPart controller = null;
        if (modelElement instanceof ProcessViewModel) {
            controller = new ProcessViewController();
        } else if (modelElement instanceof DecisionMergeModel) {
            controller = new DecisionMergeController();
        } else if (modelElement instanceof ComponentModel) {
            controller = new ComponentController();
        } else if (modelElement instanceof ConnectionModel) {
            controller = new ConnectionController(((ConnectionModel) modelElement).getConnectionValue(), ((ConnectionModel) modelElement).getConnectionTitle());
        } else {
            if (modelElement != null) {
                final String modelClassName = modelElement.getClass().getName();
                throw new RuntimeException("Could not create controller for model : " + modelClassName + ".");
            } else {
                throw new RuntimeException("Could not create controller for null model object.");
            }
        }
        controller.setModel(modelElement);
        return controller;
    }
}
