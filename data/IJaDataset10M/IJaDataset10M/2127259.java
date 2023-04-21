package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPopupMenu;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLTransitionTriggerListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLTransitionTriggerListModel.
     */
    public UMLTransitionTriggerListModel() {
        super("trigger");
    }

    protected void buildModelList() {
        removeAllElements();
        addElement(Model.getFacade().getTrigger(getTarget()));
    }

    protected boolean isValidElement(Object element) {
        return element == Model.getFacade().getTrigger(getTarget());
    }

    @Override
    public boolean buildPopup(JPopupMenu popup, int index) {
        PopupMenuNewEvent.buildMenu(popup, ActionNewEvent.Roles.TRIGGER, getTarget());
        return true;
    }

    @Override
    protected boolean hasPopup() {
        return true;
    }
}
