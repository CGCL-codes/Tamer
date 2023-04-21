package org.activebpel.rt.bpel.def.validation.extensions;

import org.activebpel.rt.bpel.def.AeActivityDef;
import org.activebpel.rt.bpel.def.AeExtensionActivityDef;
import org.activebpel.rt.bpel.def.activity.IAeExtensionActivityDef;

/**
 * WSBPEL version of an extension activity validator.
 */
public class AeWSBPELExtensionActivityValidator extends AeExtensionActivityValidator {

    /**
    * Constructor.
    * 
    * @param aDef
    */
    public AeWSBPELExtensionActivityValidator(AeExtensionActivityDef aDef) {
        super(aDef);
    }

    /**
    * @see org.activebpel.rt.bpel.def.validation.activity.AeActivityValidator#validate()
    */
    public void validate() {
        AeActivityDef activityDef = getDef().getActivityDef();
        if (activityDef != null) {
            IAeExtensionActivityDef extActivity = (IAeExtensionActivityDef) activityDef;
            AeExtensionValidator extensionValidator = findExtensionValidator(extActivity.getNamespace());
            processExtensionValidator(extensionValidator, extActivity.isUnderstood(), extActivity.getNamespace());
        }
        super.validate();
    }
}
