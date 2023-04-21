package nuts.core.orm.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaBeanHandler
 */
public class JavaBeanHandler extends AbstractBeanHandler {

    private Map<String, PropertyDescriptor> descriptors;

    /**
	 * Constructor
	 * @param type bean type
	 */
    public JavaBeanHandler(Class type) {
        super(type);
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        descriptors = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            descriptors.put(pd.getName(), pd);
            try {
                if (Character.isUpperCase(pd.getName().charAt(0))) {
                    String pn = pd.getName().substring(0, 1).toLowerCase() + pd.getName().substring(1);
                    PropertyDescriptor pd2 = new PropertyDescriptor(pn, pd.getReadMethod(), pd.getWriteMethod());
                    descriptors.put(pd.getName(), pd2);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
	 * get simple property type
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
    protected Class getSimplePropertyType(Object beanObject, String propertyName) {
        return getPropertyDescriptor(propertyName).getPropertyType();
    }

    /**
	 * get simple property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @return value
	 */
    protected Object getSimplePropertyValue(Object beanObject, String propertyName) {
        PropertyDescriptor descriptor = getPropertyDescriptor(propertyName);
        Method getter = getReadMethod(descriptor);
        try {
            return getter.invoke(beanObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * set simple property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @param value value
	 */
    protected void setSimplePropertyValue(Object beanObject, String propertyName, Object value) {
        PropertyDescriptor descriptor = getPropertyDescriptor(propertyName);
        Method setter = getWriteMethod(descriptor);
        try {
            setter.invoke(beanObject, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * getPropertyDescriptor
	 * 
	 * @param propertyName property name
	 * @return PropertyDescriptor
	 */
    private PropertyDescriptor getPropertyDescriptor(String propertyName) {
        PropertyDescriptor descriptor = descriptors.get(propertyName);
        if (descriptor == null) {
            throw new RuntimeException("No getter/setter method for property: " + propertyName + " [Class " + type.getName() + "].");
        }
        return descriptor;
    }

    /**
	 * getReadMethod
	 * @param propertyDescriptor PropertyDescriptor
	 * @return read method
	 * @throws RuntimeException if read method is null 
	 */
    private Method getReadMethod(PropertyDescriptor propertyDescriptor) {
        Method getter = propertyDescriptor.getReadMethod();
        if (getter == null) {
            throw new RuntimeException("No getter method for property: " + propertyDescriptor.getName() + " [Class " + type.getName() + "].");
        }
        return getter;
    }

    /**
	 * getWriteMethod
	 * @param propertyDescriptor PropertyDescriptor
	 * @return write method
	 * @throws RuntimeException if write method is null 
	 */
    private Method getWriteMethod(PropertyDescriptor propertyDescriptor) {
        Method setter = propertyDescriptor.getWriteMethod();
        if (setter == null) {
            throw new RuntimeException("No setter method for property: " + propertyDescriptor.getName() + " [Class " + type.getName() + "].");
        }
        return setter;
    }
}
