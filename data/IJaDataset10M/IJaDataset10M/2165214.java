package org.sdf4j.model.psdf.parameters;

import jscl.math.Expression;
import jscl.text.ParseException;
import org.sdf4j.model.parameters.Argument;
import org.sdf4j.model.parameters.InvalidExpressionException;
import org.sdf4j.model.parameters.NoIntegerValueException;

public class PSDFDynamicArgument extends Argument implements DynamicValue {

    DynamicValue value;

    public PSDFDynamicArgument(String name, DynamicValue value) {
        super(name, "0");
        this.value = value;
    }

    public DynamicValue getDynamicValue() {
        return value;
    }

    public String getValue() {
        return value.getName();
    }

    public int intValue() throws InvalidExpressionException, NoIntegerValueException {
        throw (new NoIntegerValueException(this.getName() + " is a dynamic value"));
    }

    public Expression getExpression() {
        try {
            return Expression.valueOf(value.toString());
        } catch (ParseException e) {
            try {
                return Expression.valueOf(getName());
            } catch (ParseException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }
}
