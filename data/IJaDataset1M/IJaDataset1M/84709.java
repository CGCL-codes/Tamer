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
public class BooleanValueExp extends ValueExpImpl {

    /** Attribute's name */
    protected Boolean boolValue = null;

    public BooleanValueExp(Boolean value) {
        this.boolValue = value;
    }

    public BooleanValueExp(boolean value) {
        if (value) {
            boolValue = Boolean.TRUE;
        } else {
            boolValue = Boolean.FALSE;
        }
    }

    public boolean booleanValue() {
        return boolValue.booleanValue();
    }

    public String toString() {
        return boolValue.toString();
    }
}
