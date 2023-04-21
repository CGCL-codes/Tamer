package org.scribble.conversation.projector;

import org.scribble.extensions.RegistryInfo;
import org.scribble.model.*;
import org.scribble.model.admin.ModelListener;
import org.scribble.projector.*;
import org.scribble.conversation.model.*;

/**
 * This class provides the InterruptBlock implementation of the
 * projector rule.
 */
@RegistryInfo(extension = ProjectorRule.class, notation = ConversationNotation.NOTATION_CODE)
public class InterruptBlockProjectorRule extends AbstractBlockProjectorRule {

    /**
	 * This method determines whether the projection rule is
	 * appropriate for the supplied model object.
	 * 
	 * @param obj The model object to be projected
	 * @return Whether the rule is relevant for the
	 * 				model object
	 */
    public boolean isSupported(ModelObject obj) {
        return (obj.getClass() == InterruptBlock.class);
    }

    /**
	 * This method creates a new block of the appropriate
	 * type.
	 * 
	 * @return The block
	 */
    protected Block createBlock() {
        return (new InterruptBlock());
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
        InterruptBlock ret = (InterruptBlock) super.project(context, model, role, l);
        InterruptBlock source = (InterruptBlock) model;
        if (ret == null && source.getRoles().contains(role)) {
            ret = new InterruptBlock();
            ret.derivedFrom(source);
        }
        if (ret != null) {
            for (int i = 0; i < source.getRoles().size(); i++) {
                ret.getRoles().add(new Role(source.getRoles().get(i)));
            }
            if (source.getExpression() != null) {
                ret.setExpression((Expression) context.project(source.getExpression(), role, l));
            }
        }
        return (ret);
    }
}
