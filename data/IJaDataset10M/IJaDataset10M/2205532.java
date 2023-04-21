package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 26, 2003
 */
public class UMLGeneralizableElementGeneralizationListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLGeneralizableElementGeneralizationListModel.
     */
    public UMLGeneralizableElementGeneralizationListModel() {
        super("generalization", Model.getMetaTypes().getGeneralization());
    }

    protected void buildModelList() {
        if (getTarget() != null && Model.getFacade().isAGeneralizableElement(getTarget())) {
            setAllElements(Model.getFacade().getGeneralizations(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAGeneralization(element) && Model.getFacade().getGeneralizations(getTarget()).contains(element);
    }
}
