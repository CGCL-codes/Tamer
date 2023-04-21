package org.activebpel.rt.bpel.def.validation.process;

import org.activebpel.rt.bpel.AeMessages;
import org.activebpel.rt.bpel.def.AeProcessDef;
import org.activebpel.rt.bpel.def.validation.IAeValidationContext;
import org.activebpel.rt.util.AeUtil;

/**
 * Process def validator for ws-bpel 2.x abstract processes.
 *
 */
public class AeWSBPELAbstractProcessProcessValidator extends AeWSBPELProcessValidator {

    /**
    * Default ctor.
    * @param aContext
    * @param aDef
    */
    public AeWSBPELAbstractProcessProcessValidator(IAeValidationContext aContext, AeProcessDef aDef) {
        super(aContext, aDef);
    }

    /**
    * Overrides method to check for abstractProcessProfile attribute.
    * @see org.activebpel.rt.bpel.def.validation.process.AeWSBPELProcessValidator#validate()
    */
    public void validate() {
        super.validate();
        if (AeUtil.isNullOrEmpty(getProcessDef().getAbstractProcessProfile())) {
            getReporter().reportProblem(WSBPEL_PROFILE_REQUIRED_CODE, AeMessages.getString("AeWSBPELAbstractProcessProcessValidator.ProfileRequired"), null, getDefinition());
        }
    }
}
