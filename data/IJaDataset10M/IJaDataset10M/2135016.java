package org.mariella.persistence.runtime;

import org.mariella.persistence.schema.PropertyDescription;

public class ModifiableAccessor {

    public static ModifiableAccessor Singleton = new ModifiableAccessor();

    public Object getValue(Object receiver, PropertyDescription propertyDescription) {
        try {
            return propertyDescription.getPropertyDescriptor().getReadMethod().invoke(receiver);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public void setValue(Object receiver, PropertyDescription propertyDescription, Object value) {
        try {
            propertyDescription.getPropertyDescriptor().getWriteMethod().invoke(receiver, new Object[] { value });
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
