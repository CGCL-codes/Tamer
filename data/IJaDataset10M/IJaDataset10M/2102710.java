package net.sf.opentranquera.rules;

import net.sf.opentranquera.rules.AbstractBusinessRule;
import net.sf.opentranquera.rules.RuleResult;

public class WordLengthRule extends AbstractBusinessRule {

    private int length;

    public RuleResult evaluate() {
        if (this.getEvaluatedObject().toString().length() == this.getLength()) {
            return this.getSuccess();
        }
        return this.getError("Not a " + this.getLength() + " characters word");
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
