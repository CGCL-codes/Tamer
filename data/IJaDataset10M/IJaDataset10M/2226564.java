package system;

import java.io.Serializable;

public abstract class RuleIMP implements RuleIF, Serializable {

    public RuleIMP() {
    }

    public abstract void applyRule();

    public abstract String displayRule();
}
