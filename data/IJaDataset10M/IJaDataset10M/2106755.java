package org.objectstyle.cayenne.property;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * A property accessor that uses set/get methods following JavaBean naming conventions.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public class BeanAccessor implements PropertyAccessor {

    protected String propertyName;

    protected Method readMethod;

    protected Method writeMethod;

    protected Object nullValue;

    public BeanAccessor(Class objectClass, String propertyName, Class propertyType) {
        if (objectClass == null) {
            throw new IllegalArgumentException("Null objectClass");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("Null propertyName");
        }
        this.propertyName = propertyName;
        this.nullValue = PropertyUtils.defaultNullValueForType(propertyType);
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, objectClass);
            this.readMethod = descriptor.getReadMethod();
            this.writeMethod = descriptor.getWriteMethod();
        } catch (IntrospectionException e) {
            throw new PropertyAccessException("Invalid bean property: " + propertyName, null, e);
        }
    }

    public String getName() {
        return propertyName;
    }

    public Object readPropertyDirectly(Object object) throws PropertyAccessException {
        if (readMethod == null) {
            throw new PropertyAccessException("Property '" + propertyName + "' is not readable", this, object);
        }
        try {
            return readMethod.invoke(object, null);
        } catch (Throwable th) {
            throw new PropertyAccessException("Error reading property: " + propertyName, this, object, th);
        }
    }

    public void writePropertyDirectly(Object object, Object oldValue, Object newValue) throws PropertyAccessException {
        if (writeMethod == null) {
            throw new PropertyAccessException("Property '" + propertyName + "' is not writable", this, object);
        }
        if (newValue == null) {
            newValue = this.nullValue;
        }
        try {
            writeMethod.invoke(object, new Object[] { newValue });
        } catch (Throwable th) {
            throw new PropertyAccessException("Error reading property: " + propertyName, this, object, th);
        }
    }
}
