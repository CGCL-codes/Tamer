package org.xorm.query;

public class AndCondition extends CompoundCondition {

    public AndCondition(Condition lhs, Condition rhs) {
        super(lhs, rhs);
    }

    public String toString() {
        return "(" + lhs + " && " + rhs + ")";
    }
}
