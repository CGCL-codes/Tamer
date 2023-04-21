package com.bm.introspectors;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import com.bm.utils.Ejb3Utils;

/**
 * Every persistent property of a EJB3 entity bean can be defined as field
 * access or method access (using setter/getters). The property class abstracts
 * from the access method and represtets a field (AccessType.FIELD) OR
 * getter/setter (AccessType.PROPERTY)
 * 
 * @author Daniel Wiese
 * 
 */
public class Property {

    private static final Logger log = Logger.getLogger(Property.class);

    private final String propertyName;

    private final Field fieldName;

    private final PropertyDescriptor property;

    private final Class declaringClass;

    private final boolean accessTypeField;

    /**
	 * Contructor using getter/setter access.
	 * 
	 * @param declaringClass -
	 *            the class wich declares the property
	 * 
	 * @param property -
	 *            the property descriptor
	 */
    public Property(Class declaringClass, PropertyDescriptor property) {
        this.propertyName = property.getName();
        this.property = property;
        this.fieldName = null;
        this.accessTypeField = false;
        this.declaringClass = declaringClass;
    }

    /**
	 * Contructor uing field access.
	 * 
	 * @param fieldName -
	 *            der name des feldes
	 */
    public Property(Field fieldName) {
        this.propertyName = fieldName.getName();
        this.fieldName = fieldName;
        this.property = null;
        this.accessTypeField = true;
        this.declaringClass = fieldName.getDeclaringClass();
    }

    /**
	 * Sets a value of the field / getterMethod.
	 * 
	 * @author Daniel Wiese
	 * @param instance -
	 *            the instance (Typed)
	 * @param value -
	 *            the new value
	 * @throws IllegalAccessException -
	 *             in error case
	 */
    public void setField(Object instance, Object value) throws IllegalAccessException {
        try {
            if (this.accessTypeField) {
                if (!this.fieldName.isAccessible()) {
                    this.fieldName.setAccessible(true);
                    this.fieldName.set(instance, value);
                    this.fieldName.setAccessible(false);
                } else {
                    this.fieldName.set(instance, value);
                }
            } else {
                try {
                    PropertyUtils.setProperty(instance, this.propertyName, value);
                } catch (InvocationTargetException e) {
                    log.error("Can�t set the value (" + value + ") in property: " + this.propertyName);
                    throw new IllegalAccessException("Can�t invoke the setter method set" + this.propertyName);
                } catch (NoSuchMethodException e) {
                    log.error("Can�t set the value (" + value + ") in property: " + this.propertyName);
                    throw new IllegalAccessException("Can�t invoke the setter method set" + this.propertyName);
                }
            }
        } catch (IllegalAccessException ex) {
            log.error("Can�t set the value (" + value + ") in property: " + this.propertyName, ex);
            throw ex;
        } catch (RuntimeException ruex) {
            log.error("Can�t set the value (" + value + ") in property: " + this.propertyName, ruex);
            throw ruex;
        }
    }

    /**
	 * Returns a value of an field.
	 * 
	 * @author Daniel Wiese
	 * @param instance -
	 *            the instance
	 * @return - the value of the paremeter of the instance
	 * @throws IllegalAccessException -
	 *             in error case
	 */
    public Object getField(Object instance) throws IllegalAccessException {
        Object back = null;
        if (this.accessTypeField) {
            if (!this.fieldName.isAccessible()) {
                this.fieldName.setAccessible(true);
                back = this.fieldName.get(instance);
                this.fieldName.setAccessible(false);
            } else {
                back = this.fieldName.get(instance);
            }
        } else {
            try {
                back = PropertyUtils.getProperty(instance, this.propertyName);
            } catch (InvocationTargetException e) {
                throw new IllegalAccessException("Can�t invoke the getter method get" + this.propertyName);
            } catch (NoSuchMethodException e) {
                throw new IllegalAccessException("Can�t invoke the getter method get" + this.propertyName);
            }
        }
        return back;
    }

    /**
	 * Returns the type of the property.
	 * 
	 * @return - the type
	 */
    public Class getType() {
        if (this.accessTypeField) {
            return this.fieldName.getType();
        } else {
            final Method method = this.property.getReadMethod();
            return method.getReturnType();
        }
    }

    /**
	 * Returns the generic Type of this property. E.g. id the proprty is.
	 * Collection/Order/ then Order will be returned.
	 * 
	 * @author Daniel Wiese
	 * @since 28.10.2005
	 * @return the generic Type of this property
	 */
    public Type getGenericType() {
        if (this.accessTypeField) {
            return this.fieldName.getGenericType();
        } else {
            final Method methodToFind = this.property.getReadMethod();
            return methodToFind.getGenericReturnType();
        }
    }

    /**
	 * If the propety represents a genericType (e.g. Collection/Order/) ! Typed
	 * class thit ONE type) -> then the type is returned.
	 * 
	 * @return - the type of the typed class or null
	 */
    public Class<Object> getGenericTypeClass() {
        if (this.getGenericType() instanceof ParameterizedType) {
            final ParameterizedType type = (ParameterizedType) this.getGenericType();
            if (type.getActualTypeArguments().length == 1) {
                Class<Object> ty = (Class<Object>) type.getActualTypeArguments()[0];
                return ty;
            }
        }
        return null;
    }

    /**
	 * Return the name.
	 * 
	 * @return - the name
	 */
    public String getName() {
        return this.propertyName;
    }

    /**
	 * Returns the class where the property is declared.
	 * 
	 * @return Returns the declaringClass.
	 */
    public Class getDeclaringClass() {
        return this.declaringClass;
    }

    /**
	 * Equals.
	 * @param other the other to compare
	 * @return - true if equal
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Property) {
            final Property otherC = (Property) other;
            final EqualsBuilder eqBuilder = new EqualsBuilder();
            eqBuilder.append(this.accessTypeField, otherC.accessTypeField);
            eqBuilder.append(this.fieldName, otherC.fieldName);
            eqBuilder.append(this.property, otherC.property);
            return eqBuilder.isEquals();
        } else {
            return false;
        }
    }

    /**
	 * Hash Code.
	 * @return the hash code
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final HashCodeBuilder hcBuilder = new HashCodeBuilder(3, 17);
        hcBuilder.append(this.accessTypeField);
        hcBuilder.append(this.fieldName);
        hcBuilder.append(this.property);
        return hcBuilder.toHashCode();
    }

    /**
	 * Returns the property.
	 * 
	 * @return Returns the property.
	 */
    public PropertyDescriptor getProperty() {
        return property;
    }

    /**
	 * Returns null if the accestype is field.
	 * 
	 * @return Returns the propertyName.
	 */
    public String getPropertyName() {
        return propertyName;
    }

    /**
	 * To String.
	 * @return - formatted string
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Declared in Class: (").append(Ejb3Utils.getShortClassName(this.getDeclaringClass())).append(") Prop.Name: (").append(this.propertyName).append(") - Type (").append(Ejb3Utils.getShortClassName(this.getType())).append(")");
        return sb.toString();
    }
}
