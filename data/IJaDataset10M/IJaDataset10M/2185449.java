package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.ActionSetMultiplicity;

/**
 * Action to set the multiplicity of a structural feature.
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 6, 2003
 */
public class ActionSetStructuralFeatureMultiplicity extends ActionSetMultiplicity {

    private static final ActionSetStructuralFeatureMultiplicity SINGLETON = new ActionSetStructuralFeatureMultiplicity();

    /**
     * Constructor for ActionSetStructuralFeatureMultiplicity.
     */
    protected ActionSetStructuralFeatureMultiplicity() {
        super();
    }

    public void setSelectedItem(Object item, Object target) {
        if (target != null && Model.getFacade().isAStructuralFeature(target)) {
            if (Model.getFacade().isAMultiplicity(item)) {
                if (!item.equals(Model.getFacade().getMultiplicity(target))) {
                    Model.getCoreHelper().setMultiplicity(target, item);
                }
            } else if (item instanceof String) {
                if (!item.equals(Model.getFacade().toString(Model.getFacade().getMultiplicity(target)))) {
                    Model.getCoreHelper().setMultiplicity(target, Model.getDataTypesFactory().createMultiplicity((String) item));
                }
            } else {
                Model.getCoreHelper().setMultiplicity(target, null);
            }
        }
    }

    /**
     * @return Returns the sINGLETON.
     */
    public static ActionSetStructuralFeatureMultiplicity getInstance() {
        return SINGLETON;
    }
}
