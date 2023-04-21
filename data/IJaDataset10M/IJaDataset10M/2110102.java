package org.scribble.conversation.validation;

import org.scribble.validation.*;
import org.scribble.extensions.*;
import org.scribble.model.*;
import org.scribble.model.admin.ModelIssue;
import org.scribble.model.admin.ModelListener;
import org.scribble.conversation.model.*;

/**
 * This class implements the validation rule for the
 * compose construct.
 */
@RegistryInfo(extension = ValidationRule.class, notation = ConversationNotation.NOTATION_CODE)
public class ComposeValidationRule extends AbstractValidationRule {

    /**
	 * This is the default constructor.
	 */
    public ComposeValidationRule() {
    }

    /**
	 * This method determines whether the rule is appropriate for
	 * the supplied model object.
	 * 
	 * @param obj The model object
	 * @return Whether the rule is appropriate
	 */
    public boolean isSupported(ModelObject obj) {
        return (obj instanceof Compose);
    }

    /**
	 * This method validates the supplied model object.
	 * 
	 * @param obj The model object
	 * @param context The context
	 * @param l The listener
	 */
    public void validate(ModelObject obj, ValidationContext context, ModelListener l) {
        Compose compose = (Compose) obj;
        Conversation conv = compose.getDefinition();
        java.util.Iterator<DeclarationBinding> iter = compose.getBindings().iterator();
        while (iter.hasNext()) {
            DeclarationBinding db = iter.next();
            Declaration other = conv.getDeclaration(db.getBoundName());
            if (other == null) {
                String name = compose.getReference().getAlias();
                if (compose.getReference().getLocatedRole() != null) {
                    name += ModelReference.LOCATED_REFERENCE_SEPARATOR + compose.getReference().getLocatedRole();
                }
                l.error(new ModelIssue(db, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.validation.Messages"), "_NOT_FOUND_BOUND_DECLARATION", new String[] { db.getBoundName(), name })));
            } else {
            }
        }
    }
}
