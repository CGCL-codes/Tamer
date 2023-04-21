package net.sf.opentranquera.rules;

import net.sf.opentranquera.rules.AbstractBusinessRule;
import net.sf.opentranquera.rules.RuleResult;

public class OKRule extends AbstractBusinessRule {

    public RuleResult evaluate() {
        if ("OK".equals(this.getEvaluatedObject().toString())) {
            return this.getSuccess();
        } else {
            return this.getError("Error en la rule");
        }
    }
}
