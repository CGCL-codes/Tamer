package org.dbe.composer.wfengine.bpeladmin.war.tags;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import org.dbe.composer.wfengine.bpeladmin.war.SdlMessages;

/**
 * Utility class for common reflection operations used by
 * JSP tags.
 */
public class SdlBeanUtils {

    /**
     * Extract the named accessor method.  The method should follow the
     * getXxx() naming convention.
     * For example, to locate the getName method, the property string should
     * be "name".
     * @param aBeanClass The bean class.
     * @param aProperty The name of the accessor method.
     * @param aReturnType The class of the return type.
     * @return Method The specified "getXXXX" method.
     * @throws IntrospectionException Thrown if the method is not found.
     */
    public static Method getAccessor(Class aBeanClass, String aProperty, Class aReturnType) throws IntrospectionException {
        Method m = findMethod(aBeanClass, aProperty, aReturnType, true);
        if (m == null) {
            throw new IntrospectionException(SdlMessages.getString("SdlBeanUtils.ERROR_0") + aProperty);
        }
        return m;
    }

    /**
     * Extract the named mutator method.  The method should follow the
     * setXxx() naming convention.
     * For example, to locate the setName(String) method, the property string should
     * be "name".
     *
     * @param aBeanClass The bean class
     * @param aProperty The name of the property
     * @param aType The type of the property argument
     * @throws IntrospectionException
     */
    public static Method getMutator(Class aBeanClass, String aProperty, Class aType) throws IntrospectionException {
        Method m = findMethod(aBeanClass, aProperty, aType, false);
        if (m == null) {
            throw new IntrospectionException(SdlMessages.getString("SdlBeanUtils.ERROR_1") + aProperty);
        }
        return m;
    }

    /**
     * Walks the property descriptors looking for the getter or setter.
     *
     * @param aBeanClass The bean class
     * @param aProperty Name of the property
     * @param aType Type of return value for a getter or type of arg for a setter
     * @param aGetterFlag True if you're looking for the getter
     * @throws IntrospectionException
     */
    private static Method findMethod(Class aBeanClass, String aProperty, Class aType, boolean aGetterFlag) throws IntrospectionException {
        Method m = null;
        PropertyDescriptor[] descriptors = getDescriptors(aBeanClass);
        for (int i = 0; m == null && i < descriptors.length; ++i) {
            PropertyDescriptor descriptor = descriptors[i];
            if (descriptor.getName().equals(aProperty) && descriptor.getPropertyType().isAssignableFrom(aType)) {
                if (aGetterFlag) {
                    m = descriptor.getReadMethod();
                } else {
                    m = descriptor.getWriteMethod();
                }
            }
        }
        return m;
    }

    /**
     * Returns an indexed accessor.
     * For example, to locate the getPoint(int aIndex) method, the string
     * property should be "point".
     * @param aBeanClass The bean class.
     * @param aProperty The indexed accessor property name.
     * @param aReturnType The return type of the method.
     * @return Method The method object.
     * @throws IntrospectionException Thrown if the method is not found.
     */
    public static Method getIndexedAccessor(Class aBeanClass, String aProperty, Class aReturnType) throws IntrospectionException {
        PropertyDescriptor[] descriptors = getDescriptors(aBeanClass);
        for (int i = 0; i < descriptors.length; ++i) {
            PropertyDescriptor descriptor = descriptors[i];
            if (descriptor instanceof IndexedPropertyDescriptor && descriptor.getName().equals(aProperty)) {
                IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) descriptor;
                if (ipd.getIndexedPropertyType().isAssignableFrom(aReturnType)) {
                    return ipd.getIndexedReadMethod();
                }
            }
        }
        throw new IntrospectionException(SdlMessages.getString("SdlBeanUtils.ERROR_2") + aProperty);
    }

    /**
     * Utility method for accessing <code>PropertyDescriptors</code> of
     * the given bean class.
     * @param aBeanClass
     * @throws IntrospectionException
     */
    protected static PropertyDescriptor[] getDescriptors(Class aBeanClass) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(aBeanClass);
        return info.getPropertyDescriptors();
    }
}
