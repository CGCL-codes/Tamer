package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPopupMenu;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewAction;
import org.argouml.uml.ui.behavior.common_behavior.PopupMenuNewAction;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLStateEntryList extends UMLMutableLinkedList {

    /**
     * Constructor for UMLStateEntryList.
     * @param dataModel the model
     */
    public UMLStateEntryList(UMLModelElementListModel2 dataModel) {
        super(dataModel);
    }

    public JPopupMenu getPopupMenu() {
        return new PopupMenuNewAction(ActionNewAction.Roles.ENTRY, this);
    }
}
