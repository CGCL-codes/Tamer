package test.javax.management.support;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * @version $Revision: 1.3 $
 */
public class ExceptionGeneratingDMB implements DynamicMBean, MBeanRegistration {

    private boolean invoked = false;

    private boolean register;

    public ExceptionGeneratingDMB(boolean register) {
        this.register = register;
    }

    public MBeanInfo getMBeanInfo() {
        if ((this.register == false) || (this.invoked == true)) {
            throw new RuntimeException();
        } else {
            invoked = true;
            return new MBeanInfo("test.javax.management.support.test.ExceptionGeneratingDMB", "Exception generating DynamicMBean", new MBeanAttributeInfo[0], new MBeanConstructorInfo[0], new MBeanOperationInfo[0], new MBeanNotificationInfo[0]);
        }
    }

    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        return null;
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
    }

    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    public Object invoke(String method, Object[] arguments, String[] params) throws MBeanException, ReflectionException {
        return null;
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        if (name.getKeyProperty("register").compareTo("no") == 0) {
            this.register = false;
        }
        return name;
    }

    public void postRegister(Boolean registrationDone) {
    }

    public void preDeregister() throws Exception {
    }

    public void postDeregister() {
    }
}
