package org.eaiframework.jmx;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eaiframework.Component;
import org.eaiframework.LifecycleEnum;

public class ComponentConfigMBean extends DynamicMBeanSupport implements Serializable {

    /**
	 * Generated Serial Version UID.
	 */
    private static final long serialVersionUID = -883096469865781121L;

    private static Log log = LogFactory.getLog(ComponentConfigMBean.class);

    private Component component;

    public ComponentConfigMBean(Component component) {
        this.component = component;
        this.buildDynamicMBeanInfo();
    }

    public void buildDynamicMBeanInfo() {
        dClassName = this.getClass().getName();
        dDescription = "Component configuration.";
        dAttributes = new MBeanAttributeInfo[2];
        dConstructors = new MBeanConstructorInfo[1];
        dOperations = new MBeanOperationInfo[2];
        Constructor<?>[] constructors = this.getClass().getConstructors();
        dConstructors[0] = new MBeanConstructorInfo("Constructor", constructors[0]);
        dAttributes[0] = new MBeanAttributeInfo("Id", "java.lang.String", "The id of the component.", true, false, false);
        dAttributes[1] = new MBeanAttributeInfo("State", "java.lang.String", "The state of the component.", true, false, false);
        MBeanParameterInfo[] params = new MBeanParameterInfo[0];
        dOperations[0] = new MBeanOperationInfo("start", "Starts the component.", params, "void", MBeanOperationInfo.ACTION);
        dOperations[1] = new MBeanOperationInfo("stop", "Stops the component.", params, "void", MBeanOperationInfo.ACTION);
        dMBeanInfo = new MBeanInfo(dClassName, dDescription, dAttributes, dConstructors, dOperations, new MBeanNotificationInfo[0]);
    }

    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + dClassName + " with null attribute name");
        }
        if (attributeName.equals("Id")) {
            return component.getComponentContext().getId();
        } else if (attributeName.equals("State")) {
            if (component.getComponentContext().getComponentController().getState() == LifecycleEnum.STARTED) {
                return "Started";
            } else if (component.getComponentContext().getComponentController().getState() == LifecycleEnum.STOPPED) {
                return "Stopped";
            } else {
                return "Idle";
            }
        }
        throw (new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + dClassName));
    }

    public Object invoke(String operationName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        if (operationName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), "Cannot invoke a null operation in " + dClassName);
        }
        if (operationName.equals("start")) {
            try {
                component.getComponentContext().getComponentController().start();
            } catch (Exception e) {
                log.error("Exception starting component: " + e.getMessage(), e);
            }
        } else if (operationName.equals("stop")) {
            try {
                component.getComponentContext().getComponentController().stop();
            } catch (Exception e) {
                log.error("Exception stopping component: " + e.getMessage(), e);
            }
        } else {
            throw new ReflectionException(new NoSuchMethodException(operationName), "Cannot find the operation " + operationName + " in " + dClassName);
        }
        return null;
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        throw new AttributeNotFoundException("Class " + dClassName + " has no writable attributes.");
    }

    /**
	 * @return the component
	 */
    public Component getComponent() {
        return component;
    }

    /**
	 * @param component the component to set
	 */
    public void setComponent(Component component) {
        this.component = component;
    }
}
