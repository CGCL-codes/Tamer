package org.activebpel.rt.bpel.def.validation.activity.wsio;

import org.activebpel.rt.bpel.def.activity.support.AeToPartsDef;
import org.activebpel.rt.bpel.def.validation.AeBaseValidator;

/**
 * Model that provides validation of toParts.
 */
public class AeToPartsValidator extends AeBaseValidator {

    /**
    * Constructor.
    */
    public AeToPartsValidator(AeToPartsDef aDef) {
        super(aDef);
    }

    /**
    * @see org.activebpel.rt.bpel.def.validation.AeBaseValidator#validate()
    */
    public void validate() {
        super.validate();
        AeToPartsDef toPartsDef = (AeToPartsDef) getDefinition();
        if (!toPartsDef.getToPartDefs().hasNext()) getReporter().reportProblem(BPEL_TO_EMPTY_CONTAINER_CODE, ERROR_EMPTY_CONTAINER, new String[] { toPartsDef.getLocationPath() }, getDefinition());
    }
}
