package org.activebpel.rt.bpel.def.validation.expressions;

import org.activebpel.rt.bpel.def.activity.support.AeUntilDef;

/**
 * model provides validation for the &lt;until&gt; def
 */
public class AeUntilValidator extends AeDeadlineExpressionValidator {

    /**
    * ctor
    * @param aDef
    */
    public AeUntilValidator(AeUntilDef aDef) {
        super(aDef);
    }
}
