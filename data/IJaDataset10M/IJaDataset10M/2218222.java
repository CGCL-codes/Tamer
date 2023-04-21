package org.argouml.core.propertypanels.ui;

import java.util.ArrayList;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * Listmodel for the incoming transitions of a Statevertex.
 *
 * @since Dec 14, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLStateVertexIncomingListModel extends UMLModelElementListModel {

    /**
     * Constructor for UMLStateVertexIncomingListModel.
     */
    public UMLStateVertexIncomingListModel(final Object modelElement) {
        super("incoming");
        setTarget(modelElement);
    }

    protected void buildModelList() {
        ArrayList c = new ArrayList(Model.getFacade().getIncomings(getTarget()));
        if (Model.getFacade().isAState(getTarget())) {
            ArrayList i = new ArrayList(Model.getFacade().getInternalTransitions(getTarget()));
            c.removeAll(i);
        }
        setAllElements(c);
    }

    protected boolean isValidElement(Object element) {
        ArrayList c = new ArrayList(Model.getFacade().getIncomings(getTarget()));
        if (Model.getFacade().isAState(getTarget())) {
            ArrayList i = new ArrayList(Model.getFacade().getInternalTransitions(getTarget()));
            c.removeAll(i);
        }
        return c.contains(element);
    }
}
