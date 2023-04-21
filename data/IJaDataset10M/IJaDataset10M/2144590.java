package org.scribble.protocol.validation;

import org.scribble.validation.*;
import org.scribble.extensions.*;
import org.scribble.model.*;
import org.scribble.model.admin.ModelIssue;
import org.scribble.model.admin.ModelListener;
import org.scribble.protocol.model.*;

/**
 * This class implements the validation rule for the
 * Raise construct.
 */
@RegistryInfo(extension = ValidationRule.class, notation = ProtocolNotation.NOTATION_CODE)
public class RaiseValidationRule extends AbstractValidationRule {

    /**
	 * This is the default constructor.
	 */
    public RaiseValidationRule() {
    }

    /**
	 * This method determines whether the rule is appropriate for
	 * the supplied model object.
	 * 
	 * @param obj The model object
	 * @return Whether the rule is appropriate
	 */
    public boolean isSupported(ModelObject obj) {
        return (obj instanceof Raise);
    }

    /**
	 * This method validates the supplied model object.
	 * 
	 * @param obj The model object
	 * @param context The context
	 * @param l The listener
	 */
    public void validate(ModelObject obj, ValidationContext context, ModelListener l) {
        Raise elem = (Raise) obj;
        if (elem.getType() != null) {
            boolean f_found = false;
            ModelObject act = elem.getParent();
            while (f_found == false && act != null && (act instanceof Definition) == false) {
                if (act instanceof TryEscape) {
                    TryEscape te = (TryEscape) act;
                    for (int i = 0; f_found == false && i < te.getEscapeBlocks().size(); i++) {
                        if (te.getEscapeBlocks().get(i) instanceof CatchBlock && elem.getType().equals(((CatchBlock) te.getEscapeBlocks().get(i)).getType())) {
                            f_found = true;
                        }
                    }
                }
                act = act.getParent();
            }
            if (f_found == false) {
                l.error(new ModelIssue(elem, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.validation.Messages"), "_RAISED_TYPE_NOT_CAUGHT", new String[] { elem.getType().toString() })));
            }
        }
    }
}
