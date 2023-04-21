package hudson.zipscript.parser.template.element.comparator.math;

import java.math.BigDecimal;

public class AddExpression extends AbstractMathExpression {

    protected Object performOperation(Number lhs, Number rhs) {
        Class clazz = getCommonDenominatorClass(lhs, rhs);
        if (clazz.getName().equals(Double.class.getName())) {
            return new Double(lhs.doubleValue() + rhs.doubleValue());
        } else if (clazz.getName().equals(Float.class.getName())) {
            return new Float(lhs.floatValue() + rhs.floatValue());
        } else if (clazz.getName().equals(BigDecimal.class.getName())) {
            return new BigDecimal(lhs.doubleValue() + rhs.doubleValue());
        } else if (clazz.getName().equals(Long.class.getName())) {
            return new Long(lhs.longValue() + rhs.longValue());
        } else if (clazz.getName().equals(Short.class.getName())) {
            return new Long(lhs.shortValue() + rhs.shortValue());
        } else return new Integer(lhs.intValue() + rhs.intValue());
    }

    protected String getComparatorString() {
        return "+";
    }
}
