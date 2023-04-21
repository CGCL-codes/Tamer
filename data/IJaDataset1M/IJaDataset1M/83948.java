package mx4j.queries;

import javax.management.AttributeValueExp;
import javax.management.ObjectName;
import javax.management.ValueExp;
import javax.management.MBeanServer;
import javax.management.BadStringOperationException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadAttributeValueExpException;
import javax.management.InvalidApplicationException;

/**
 *
 *
 * @author <a href="mailto:tibu@users.sourceforge.net">Carlos Quiroz</a>
 * @version $Revision: 3 $
 */
public class NumericalValueExp extends ValueExpImpl {

    /** Numerical value */
    private Number value = null;

    private boolean isLong;

    public NumericalValueExp(Number value) {
        if (value == null) {
            throw new IllegalArgumentException("Tried to build a null Numerical value");
        }
        this.value = value;
        if (value instanceof Double || value instanceof Float) {
            isLong = false;
        } else {
            isLong = true;
        }
    }

    public Number getNumber() {
        return value;
    }

    public boolean isLong() {
        return isLong;
    }

    public long longValue() {
        if (isLong) {
            return value.intValue();
        } else {
            return (long) value.doubleValue();
        }
    }

    public double doubleValue() {
        if (isLong) {
            return (double) value.intValue();
        } else {
            return value.doubleValue();
        }
    }

    public String toString() {
        return value.toString();
    }
}
