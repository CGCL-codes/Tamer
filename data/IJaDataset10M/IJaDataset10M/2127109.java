package org.argouml.core.propertypanels.ui;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLModelElementElementResidenceListModel extends UMLModelElementListModel {

    /**
     * Constructor for UMLModelElementElementResidenceListModel.
     */
    public UMLModelElementElementResidenceListModel() {
        super("elementResidence");
    }

    protected void buildModelList() {
        setAllElements(Model.getFacade().getElementResidences(getTarget()));
    }

    protected boolean isValidElement(Object o) {
        return Model.getFacade().isAElementResidence(o) && Model.getFacade().getElementResidences(getTarget()).contains(o);
    }
}
