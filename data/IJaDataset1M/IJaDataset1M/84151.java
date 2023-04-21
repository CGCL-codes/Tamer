package javax.microedition.sensor;

import com.sun.javame.sensor.*;

public final class LimitCondition implements Condition {

    private double limit;

    private String operator;

    /** Creates a new instance of LimitCondition */
    public LimitCondition(double limit, java.lang.String operator) {
        if (operator == null) {
            throw new NullPointerException();
        }
        if (!ConditionHelpers.checkOperator(operator)) {
            throw new IllegalArgumentException();
        }
        this.limit = limit;
        this.operator = operator;
    }

    public final double getLimit() {
        return limit;
    }

    public final String getOperator() {
        return operator;
    }

    public boolean isMet(double value) {
        return ConditionHelpers.checkValue(operator, limit, value);
    }

    public boolean isMet(java.lang.Object value) {
        return false;
    }
}
