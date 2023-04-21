package org.scribble.projector;

import org.scribble.extensions.RegistryInfo;
import org.scribble.model.*;
import org.scribble.model.admin.ModelListener;

/**
 * This class provides the Namespace implementation of the
 * projector rule.
 */
@RegistryInfo(extension = ProjectorRule.class)
public class NamespaceProjectorRule implements ProjectorRule {

    /**
	 * This method determines whether the projection rule is
	 * appropriate for the supplied model object.
	 * 
	 * @param obj The model object to be projected
	 * @return Whether the rule is relevant for the
	 * 				model object
	 */
    public boolean isSupported(ModelObject obj) {
        return (obj.getClass() == Namespace.class);
    }

    /**
	 * This method projects the supplied model object based on the
	 * specified role.
	 * 
	 * @param model The model object
	 * @param role The role
	 * @param l The model listener
	 * @return The projected model object
	 */
    public ModelObject project(ProjectorContext context, ModelObject model, Role role, ModelListener l) {
        Namespace ret = new Namespace();
        Namespace source = (Namespace) model;
        ret.setName(source.getName());
        return (ret);
    }
}
