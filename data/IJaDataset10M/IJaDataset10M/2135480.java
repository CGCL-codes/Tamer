package org.argouml.uml.ui.behavior.use_cases;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * @since Oct 7, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLUseCaseIncludeListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLUseCaseIncludeListModel.
     */
    public UMLUseCaseIncludeListModel() {
        super("include");
    }

    protected void buildModelList() {
        setAllElements(Model.getFacade().getIncludes(getTarget()));
    }

    protected boolean isValidElement(Object o) {
        return Model.getFacade().getIncludes(getTarget()).contains(o);
    }
}
