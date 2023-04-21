package com.ibm.tuningfork.infra.stream.expression.literals;

import com.ibm.tuningfork.infra.event.AttributeType;
import com.ibm.tuningfork.infra.stream.expression.base.Expression;
import com.ibm.tuningfork.infra.stream.expression.base.MissingValueException;
import com.ibm.tuningfork.infra.stream.expression.base.NumericExpression;
import com.ibm.tuningfork.infra.stream.expression.base.StreamContext;
import com.ibm.tuningfork.infra.stream.expression.types.ExpressionType;
import com.ibm.tuningfork.infra.units.Unit;

/**
 * Represents a double literal
 */
public class DoubleLiteral extends NumericExpression {

    /** Special DoubleLiteral for missing double values */
    public static final DoubleLiteral MISSING = new DoubleLiteral(0) {

        public double getDoubleValue(StreamContext context) throws MissingValueException {
            throw new MissingValueException();
        }

        public String toString() {
            return "null";
        }
    };

    /** The value */
    private double value;

    /**
     * Make a new DoubleLiteral with Dimensionless Unit
     * @param value the value of the literal
     */
    public DoubleLiteral(double value) {
        super(ExpressionType.DOUBLE);
        this.value = value;
    }

    /**
     * Create a DoubleLiteral with a specified unit
     * @param value the value of the literal
     * @param unit the unit of the literal
     */
    public DoubleLiteral(double value, Unit unit) {
        super(ExpressionType.makeNumericType(AttributeType.DOUBLE, unit));
        this.value = value;
    }

    public double getDoubleValue(StreamContext context) throws MissingValueException {
        return value;
    }

    public Object getValue(StreamContext context) {
        return Double.valueOf(value);
    }

    public NumericExpression resolve(Expression[] arguments, int depth) {
        return this;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
