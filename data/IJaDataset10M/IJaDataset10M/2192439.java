package openjmx.queries;

import javax.management.MBeanServer;
import javax.management.ValueExp;
import javax.management.ObjectName;
import javax.management.BadStringOperationException;
import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.InvalidApplicationException;

/**
 * Abstract parent of ValueExp classes 
 *
 * @author <a href="mailto:tibu@users.sourceforge.net">Carlos Quiroz</a>
 * @version $Revision: 1.2 $
 */
abstract class ValueExpImpl implements ValueExp {

    protected MBeanServer mBeanServer;

    public ValueExp apply(ObjectName name) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        return this;
    }

    public void setMBeanServer(MBeanServer server) {
        this.mBeanServer = server;
    }
}
