package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * @since Oct 11, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLModelElementNamespaceListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLModelElementNamespaceListModel.
     */
    public UMLModelElementNamespaceListModel() {
        super("namespace");
    }

    protected void buildModelList() {
        removeAllElements();
        if (getTarget() != null) {
            addElement(Model.getFacade().getNamespace(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().getNamespace(getTarget()) == element;
    }
}
