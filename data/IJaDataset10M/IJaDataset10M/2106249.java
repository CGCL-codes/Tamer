package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLTransitionSourceListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLStateMachineTopListModel.
     */
    public UMLTransitionSourceListModel() {
        super("source");
    }

    protected void buildModelList() {
        removeAllElements();
        addElement(Model.getFacade().getSource(getTarget()));
    }

    protected boolean isValidElement(Object element) {
        return element == Model.getFacade().getSource(getTarget());
    }
}
