package org.javalid.core.el;

import java.io.Serializable;

/**
 * @since 1.1
 */
public class ELString implements Serializable {

    private String value;

    private boolean expression;

    public ELString(String value, boolean expression) {
        this.value = value;
        this.expression = expression;
    }

    public String getValue() {
        return value;
    }

    public boolean isExpression() {
        return expression;
    }

    public String toString() {
        return "[ELString: value=" + value + ", expression=" + expression + "]\n";
    }
}
