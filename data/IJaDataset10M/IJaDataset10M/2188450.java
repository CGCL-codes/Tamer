package com.ibm.tuningfork.infra.stream.expression.operations;

import com.ibm.tuningfork.infra.data.ITuple;
import com.ibm.tuningfork.infra.stream.expression.base.MissingValueException;
import com.ibm.tuningfork.infra.stream.expression.base.StreamContext;
import com.ibm.tuningfork.infra.stream.expression.base.TupleExpression;
import com.ibm.tuningfork.infra.stream.expression.types.ExpressionType;

/**
 * Select a double field from a tuple
 */
public class DoubleFieldSelect extends NumericFieldSelect {

    /**
     * Create a new double field select operation
     * @param tuple expression denoting the tuple being selected from
     * @param fieldName the name of the field
     */
    public DoubleFieldSelect(TupleExpression tuple, String fieldName) {
        super(tuple, fieldName);
    }

    public double getDoubleValue(StreamContext context) throws MissingValueException {
        ITuple tuple = this.tuple.getTupleValue(context);
        if (tuple == null) {
            throw new MissingValueException();
        }
        return tuple.getDouble(fieldIndex);
    }

    public Object getValue(StreamContext context) {
        try {
            return getDoubleValue(context);
        } catch (MissingValueException e) {
            return null;
        }
    }

    protected void setType(ExpressionType type) {
        checkAssertion(type.isDouble(), "Wrong type for double expression");
        super.setType(type);
    }
}
