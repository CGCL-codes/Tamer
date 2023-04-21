package net.sf.msplice.property.impl;

import net.sf.msplice.property.IPropertySetter;
import net.sf.msplice.property.PropertySetException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class BeanutilsPropertySetter implements IPropertySetter {

    public void setProperty(String propertyName, Object rootObject, Object value) throws PropertySetException {
        boolean emptyPropertyName = StringUtils.isEmpty(propertyName);
        boolean nullRoot = (rootObject == null);
        StringBuffer messageBuffer = new StringBuffer();
        if (nullRoot || emptyPropertyName) {
            if (nullRoot) {
                messageBuffer.append("The root object must not be null! ");
            }
            if (emptyPropertyName) {
                messageBuffer.append("The property name must not be empty! ");
            }
            throw new PropertySetException(messageBuffer.toString());
        }
        try {
            PropertyUtils.setNestedProperty(rootObject, propertyName, value);
        } catch (Exception e) {
            throw new PropertySetException("Could not set the property with name: " + propertyName + ", on object: " + rootObject + ", with the value: " + value, e);
        }
    }
}
