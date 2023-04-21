package org.objectstyle.cayenne.modeler.util;

import org.objectstyle.cayenne.exp.Expression;
import org.objectstyle.cayenne.exp.ExpressionException;
import org.objectstyle.cayenne.exp.parser.ParseException;
import org.objectstyle.cayenne.util.Util;

/**
 * A Scope convertor that allows to display expressions in text fields.
 * 
 * @since 1.1
 * @author Andrei Adamchik
 */
public class ExpressionConvertor {

    public String valueAsString(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Expression)) {
            throw new IllegalArgumentException("Unsupported value class: " + value.getClass().getName());
        }
        return value.toString();
    }

    public Object stringAsValue(String string) throws IllegalArgumentException {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        try {
            return Expression.fromString(string);
        } catch (ExpressionException eex) {
            Throwable cause = Util.unwindException(eex);
            String message = (cause instanceof ParseException) ? cause.getMessage() : "Invalid expression: " + string;
            throw new IllegalArgumentException(message);
        }
    }

    public boolean supportsStringAsValue() {
        return true;
    }
}
