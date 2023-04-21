package com.genia.toolbox.projects.csv.bean.impl;

import java.beans.PropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;
import com.genia.toolbox.projects.csv.bean.EntityDescriptor;
import com.genia.toolbox.projects.csv.bean.MutableBinder;

/**
 * base implementation for {@link MutableBinder}s.
 */
public abstract class AbstractBinder implements MutableBinder {

    /**
   * the name of the property to associate with the field.
   */
    private String propertyName;

    /**
   * the name of the mapped property for the persistence API.
   */
    private String mappedPropertyName = null;

    /**
   * if <code>true</code>, this binder will force the returned entity value
   * to be null if the associated value is <code>null</code>.
   */
    private Boolean nullable = false;

    /**
   * the {@link EntityDescriptor} containing this binder.
   */
    private EntityDescriptor entityDescriptor;

    /**
   * returns the {@link Class} of the object to bind with.
   * 
   * @return the {@link Class} of the object to bind with
   */
    public Class<?> getBindedObjectClass() {
        final EntityDescriptor entityDescriptor = getEntityDescriptor();
        for (PropertyDescriptor propertyDescriptor : PropertyUtils.getPropertyDescriptors(entityDescriptor.getEntityClass())) {
            if (propertyDescriptor.getName().equals(getMappedPropertyName())) {
                return propertyDescriptor.getPropertyType();
            }
        }
        return null;
    }

    /**
   * getter for the mappedPropertyName property.
   * 
   * @return the mappedPropertyName
   */
    public String getMappedPropertyName() {
        if (mappedPropertyName == null) {
            return getPropertyName();
        }
        return mappedPropertyName;
    }

    /**
   * setter for the mappedPropertyName property.
   * 
   * @param mappedPropertyName
   *          the mappedPropertyName to set
   */
    public void setMappedPropertyName(String mappedPropertyName) {
        this.mappedPropertyName = StringUtils.trimWhitespace(mappedPropertyName);
    }

    /**
   * getter for the propertyName property.
   * 
   * @return the propertyName
   */
    public String getPropertyName() {
        return propertyName;
    }

    /**
   * setter for the propertyName property.
   * 
   * @param propertyName
   *          the propertyName to set
   */
    public void setPropertyName(String propertyName) {
        this.propertyName = StringUtils.trimWhitespace(propertyName);
    }

    /**
   * getter for the nullable property.
   * 
   * @return the nullable
   */
    public Boolean getNullable() {
        return nullable;
    }

    /**
   * setter for the nullable property.
   * 
   * @param nullable
   *          the nullable to set
   */
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    /**
   * getter for the entityDescriptor property.
   * 
   * @return the entityDescriptor
   */
    public EntityDescriptor getEntityDescriptor() {
        return entityDescriptor;
    }

    /**
   * setter for the entityDescriptor property.
   * 
   * @param entityDescriptor
   *          the entityDescriptor to set
   */
    public void setEntityDescriptor(EntityDescriptor entityDescriptor) {
        this.entityDescriptor = entityDescriptor;
    }
}
