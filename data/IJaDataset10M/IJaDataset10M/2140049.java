package org.ddsteps.step.ldap;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import junit.framework.AssertionFailedError;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.ldap.LdapOperations;

/**
 * Validates an entry against LDAP. The entry to validate is specified by the
 * <code>dn</code>. The actual Attribute validation takes place in
 * {@link #validateAttributes(Attributes)}, which gets automatically called
 * from the superclass if the requested DN was found.
 * 
 * The Attributes that will be validated are the Properties of the actual
 * implementing subclass - all Properties except 'class', 'dn' and
 * 'ldapOperations' that are not null are expected to have an attribute with the
 * same value(s). For Array values (attributes with multiple values) order is
 * ignored.
 * 
 * TODO: Perhaps need a hook method for subclasses to specify other properties
 * to ignore.
 * 
 * @author Mattias Arthursson
 */
public abstract class DefaultLdapValidator extends AbstractLdapValidator {

    private static String CLASS_PROPERTY = "class";

    private static String DN_PROPERTY = "dn";

    private static Set ignoreProperties;

    static {
        ignoreProperties = new HashSet();
        ignoreProperties.add(CLASS_PROPERTY);
        ignoreProperties.add(DN_PROPERTY);
    }

    public DefaultLdapValidator(LdapOperations ldapOperations) {
        super(ldapOperations);
    }

    /**
     * 
     * 
     * @see org.ddsteps.step.ldap.AbstractLdapValidator#validateAttributes(javax.naming.directory.Attributes)
     */
    protected void validateAttributes(Attributes attributes) throws NamingException {
        BeanWrapper beanWrapper = new BeanWrapperImpl(this);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (ignoreProperties.contains(propertyName)) {
                continue;
            }
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
            if (propertyValue != null) {
                Attribute attribute = attributes.get(propertyName);
                if (attribute == null) {
                    throw new AssertionFailedError("Attribute " + propertyName + " was not found, " + "expected " + propertyValue);
                }
                if (propertyValue instanceof Object[]) {
                    verifyArrayAttribute(propertyName, (Object[]) propertyValue, attribute);
                } else {
                    verifySingleValueAttribute(propertyName, propertyValue, attribute);
                }
            }
        }
    }

    private void verifySingleValueAttribute(String propertyName, Object propertyValue, Attribute attribute) throws AssertionFailedError, NamingException {
        if (attribute.size() > 1) {
            throw new AssertionFailedError("Attribute '" + propertyName + "' expected '" + propertyValue + "' but was '" + attribute);
        }
        Object attributeValue = attribute.get();
        if (!propertyValue.equals(attributeValue)) {
            throw new AssertionFailedError("Attribute '" + propertyName + "' expected '" + propertyValue + "' but was '" + attributeValue);
        }
    }

    void verifyArrayAttribute(String propertyName, Object[] propertyValue, Attribute attribute) throws NamingException {
        if (propertyValue.length != attribute.size()) {
            throw new AssertionFailedError("Attribute '" + propertyName + "' expected length " + propertyValue.length + ", but was " + attribute.size());
        }
        List list = new ArrayList(propertyValue.length);
        for (int i = 0; i < propertyValue.length; i++) {
            list.add(propertyValue[i]);
        }
        for (int i = 0; i < attribute.size(); i++) {
            Object entry = attribute.get(i);
            if (!list.contains(entry)) {
                StringBuffer buffer = buildArrayStringRepresentation(propertyValue, i);
                throw new AssertionFailedError("Attribute '" + propertyName + "' expected " + buffer + ", but was " + attribute);
            }
        }
    }

    private StringBuffer buildArrayStringRepresentation(Object[] propertyValue, int i) {
        StringBuffer buffer = new StringBuffer(200);
        buffer.append("[");
        for (int j = 0; j < propertyValue.length; j++) {
            buffer.append(propertyValue[i]);
            if (j < propertyValue.length - 1) {
                buffer.append(", ");
            }
        }
        buffer.append("]");
        return buffer;
    }
}
