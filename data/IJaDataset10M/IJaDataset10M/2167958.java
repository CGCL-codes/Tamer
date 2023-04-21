package org.scribble.conversation.projector;

import org.scribble.extensions.RegistryInfo;
import org.scribble.model.*;
import org.scribble.model.admin.ModelListener;
import org.scribble.projector.*;
import org.scribble.conversation.model.*;

/**
 * This class provides the IdentityLocator implementation of the
 * projector rule.
 */
@RegistryInfo(extension = ProjectorRule.class, notation = ConversationNotation.NOTATION_CODE)
public class IdentityLocatorProjectorRule implements ProjectorRule {

    /**
	 * This method determines whether the projection rule is
	 * appropriate for the supplied model object.
	 * 
	 * @param obj The model object to be projected
	 * @return Whether the rule is relevant for the
	 * 				model object
	 */
    public boolean isSupported(ModelObject obj) {
        return (obj.getClass() == IdentityLocator.class);
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
        IdentityLocator ret = new IdentityLocator();
        IdentityLocator source = (IdentityLocator) model;
        ret.derivedFrom(source);
        ret.setType((TypeReference) context.project(source.getType(), role, l));
        for (int i = 0; i < source.getNumberOfLocators(); i++) {
            ret.setLocator(source.getIdentity(i), source.getLocator(i));
        }
        return (ret);
    }
}
