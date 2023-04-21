package org.activebpel.rt.bpel.def.validation.activity;

import org.activebpel.rt.bpel.def.activity.support.AeOnEventDef;

/**
 * Performs the same validation as the onMessage model 
 */
public class AeBPWSOnEventValidator extends AeOnMessageValidator {

    /**
    * Ctor accepts def
    * @param aDef
    */
    public AeBPWSOnEventValidator(AeOnEventDef aDef) {
        super(aDef);
    }
}
