package org.ujac.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Name: BeanUtils<br>
 * Description: Class providing common utility methods for java bean access.
 * 
 * @author lauerc
 */
public class BeanUtils {

    /** The argument type list for a getter method. */
    public static final Class<?>[] GETTER_ARG_TYPES = new Class<?>[] {};

    /** The argument list for a getter method. */
    public static final Object[] GETTER_ARGS = new Object[] {};

    /**
   * Gets a property from the given bean. 
   * @param bean The bean to read property from.
   * @param propertyName The name of the property to read.
   * @return The determined value.
   * @exception BeanException In case the bean access failed.
   */
    public static final Object getProperty(Object bean, String propertyName) throws BeanException {
        return getProperty(bean, propertyName, false);
    }

    /**
   * Gets a property from the given bean. 
   * @param clazz The class to determine the property type for.
   * @param propertyName The name of the property to read.
   * @param lenient If true is passed for this attribute, null will returned for 
   *   in case no matching getter method is defined, else an Exception will be throw 
   *   in this case. 
   * @return The determined value.
   * @exception BeanException In case the bean access failed.
   */
    public static final Class getPropertyType(Class clazz, String propertyName, boolean lenient) throws BeanException {
        try {
            Method getterMethod = null;
            try {
                String getterName = new StringBuffer("get").append(Character.toUpperCase(propertyName.charAt(0))).append(propertyName.substring(1)).toString();
                getterMethod = clazz.getMethod(getterName, GETTER_ARG_TYPES);
            } catch (NoSuchMethodException ex) {
                String getterName = new StringBuffer("is").append(Character.toUpperCase(propertyName.charAt(0))).append(propertyName.substring(1)).toString();
                getterMethod = clazz.getMethod(getterName, GETTER_ARG_TYPES);
            }
            return getterMethod.getReturnType();
        } catch (NoSuchMethodError ex) {
            if (!lenient) {
                throw new BeanException("Property '" + propertyName + "' is undefined for given bean from class " + clazz.getName() + ".");
            }
        } catch (NoSuchMethodException ex) {
            if (!lenient) {
                throw new BeanException("Property '" + propertyName + "' is undefined for given bean from class " + clazz.getName() + ".");
            }
        }
        return null;
    }

    /**
   * Gets a property from the given bean. 
   * @param bean The bean to read property from.
   * @param propertyName The name of the property to read.
   * @param lenient If true is passed for this attribute, null will returned for 
   *   in case no matching getter method is defined, else an Exception will be throw 
   *   in this case. 
   * @return The determined value.
   * @exception BeanException In case the bean access failed.
   */
    public static final Object getProperty(Object bean, String propertyName, boolean lenient) throws BeanException {
        try {
            Method getterMethod = null;
            try {
                String getterName = new StringBuffer("get").append(Character.toUpperCase(propertyName.charAt(0))).append(propertyName.substring(1)).toString();
                Class paramClass = bean.getClass();
                getterMethod = paramClass.getMethod(getterName, GETTER_ARG_TYPES);
            } catch (NoSuchMethodException ex) {
                String getterName = new StringBuffer("is").append(Character.toUpperCase(propertyName.charAt(0))).append(propertyName.substring(1)).toString();
                Class paramClass = bean.getClass();
                getterMethod = paramClass.getMethod(getterName, GETTER_ARG_TYPES);
            }
            return getterMethod.invoke(bean, GETTER_ARGS);
        } catch (NoSuchMethodError ex) {
            if (!lenient) {
                throw new BeanException("Property '" + propertyName + "' is undefined for given bean from class " + bean.getClass().getName() + ".", ex);
            }
        } catch (NoSuchMethodException ex) {
            if (!lenient) {
                throw new BeanException("Property '" + propertyName + "' is undefined for given bean from class " + bean.getClass().getName() + ".");
            }
        } catch (InvocationTargetException ex) {
            throw new BeanException("Property '" + propertyName + "' could not be evaluated for given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanException("Property '" + propertyName + "' could not be accessed for given bean from class " + bean.getClass().getName() + ".", ex);
        }
        return null;
    }

    /**
   * Invokes a getter method with the given value. 
   * @param bean The bean to set a property at.
   * @param getterMethod The setter method to invoke.
   * @return The determined value.
   * @exception BeanException In case the bean access failed.
   */
    public static final Object invokeGetter(Object bean, Method getterMethod) throws BeanException {
        if (bean == null) {
            return null;
        }
        try {
            return getterMethod.invoke(bean, GETTER_ARGS);
        } catch (InvocationTargetException ex) {
            throw new BeanException("Failed to call getter method '" + getterMethod.getName() + "' for given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (IllegalArgumentException ex) {
            throw new BeanException("Failed to call getter method '" + getterMethod.getName() + "' for given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanException("Failed to call getter method '" + getterMethod.getName() + "' for given bean from class " + bean.getClass().getName() + ".", ex);
        }
    }

    /**
   * Sets a property at the given bean. 
   * @param bean The bean to set a property at.
   * @param propertyName The name of the property to set.
   * @param value The value to set for the property. 
   * @exception BeanException In case the bean access failed.
   */
    public static final void setProperty(Object bean, String propertyName, Object value) throws BeanException {
        Class valueClass = null;
        try {
            Method setterMethod = null;
            String setterName = new StringBuffer("set").append(Character.toUpperCase(propertyName.charAt(0))).append(propertyName.substring(1)).toString();
            Class paramClass = bean.getClass();
            if (value != null) {
                valueClass = value.getClass();
                Class<?>[] setterArgTypes = new Class<?>[] { valueClass };
                setterMethod = paramClass.getMethod(setterName, setterArgTypes);
            } else {
                Method[] methods = paramClass.getMethods();
                for (int i = 0; i < methods.length; i++) {
                    Method m = methods[i];
                    if (m.getName().equals(setterName) && (m.getParameterTypes().length == 1)) {
                        setterMethod = m;
                        break;
                    }
                }
            }
            Object[] setterArgs = new Object[] { value };
            setterMethod.invoke(bean, setterArgs);
        } catch (NoSuchMethodError ex) {
            throw new BeanException("No setter method found for property '" + propertyName + "' and type " + valueClass + " at given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (NoSuchMethodException ex) {
            throw new BeanException("No setter method found for property '" + propertyName + "' and type " + valueClass + " at given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (InvocationTargetException ex) {
            throw new BeanException("Property '" + propertyName + "' could not be set for given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanException("Property '" + propertyName + "' could not be accessed for given bean from class " + bean.getClass().getName() + ".", ex);
        }
    }

    /**
   * Invokes a setter method with the given value. 
   * @param bean The bean to set a property at.
   * @param setterMethod The setter method to invoke.
   * @param value The value to set for the property.
   * @exception BeanException In case the bean access failed.
   */
    public static final void invokeSetter(Object bean, Method setterMethod, Object value) throws BeanException {
        TypeConverter tc = new DefaultTypeConverter();
        invokeSetter(bean, setterMethod, value, tc);
    }

    /**
   * Invokes a setter method with the given value. 
   * @param bean The bean to set a property at.
   * @param setterMethod The setter method to invoke.
   * @param value The value to set for the property.
   * @param typeConverter The TypeConverter to use.
   * @exception BeanException In case the bean access failed.
   */
    public static final void invokeSetter(Object bean, Method setterMethod, Object value, TypeConverter typeConverter) throws BeanException {
        if (bean == null) {
            return;
        }
        try {
            Class paramType = null;
            Class<?>[] paramTypes = setterMethod.getParameterTypes();
            if (paramTypes.length == 1) {
                paramType = paramTypes[0];
            }
            Object arg = typeConverter.convertObject(paramType, value);
            Object[] setterArgs = new Object[] { arg };
            setterMethod.invoke(bean, setterArgs);
        } catch (InvocationTargetException ex) {
            throw new BeanException("Failed to call setter method '" + setterMethod.getName() + "' for given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (IllegalArgumentException ex) {
            throw new BeanException("Failed to call setter method '" + setterMethod.getName() + "' for given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanException("Failed to call setter method '" + setterMethod.getName() + "' for given bean from class " + bean.getClass().getName() + ".", ex);
        } catch (TypeConverterException ex) {
            throw new BeanException("Type conversion failed for method '" + setterMethod.getName() + "' at given bean from class " + bean.getClass().getName() + ".", ex);
        }
    }

    /**
   * Checks if the given string is null or has zero length.
   * @param value The string to check.
   * @return true if the given string is null or has zero length.
   */
    public static final boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
   * Checks if the given objects are equal.
   * @param a The object to compare with object b.
   * @param b The object to compare with object a.
   * @return true if the objects are equal or are both null.
   */
    public static final boolean equals(Object a, Object b) {
        if ((a == null) || (b == null)) {
            return a == b;
        }
        return a.equals(b);
    }

    /**
   * Checks if the given objects are equal.
   * @param a The object to compare with object b.
   * @param b The object to compare with object a.
   * @param caseSensitive Tells whether or not to compare string values case sensitive.
   *    This parameter is only respected, in case a is a <code>java.lang.String</code> instance.
   * @return true if the objects are equal or are both null.
   */
    public static final boolean equals(Object a, Object b, boolean caseSensitive) {
        if ((a == null) || (b == null)) {
            return a == b;
        }
        if (a instanceof String) {
            return StringUtils.equals(a.toString(), b.toString(), caseSensitive);
        }
        return a.equals(b);
    }

    /**
   * Compares the given values.
   * @param a The object to compare with object b.
   * @param b The object to compare with object a.
   * @return true if the objects are equal or are both null.
   */
    public static final int compare(Comparable a, Comparable b) {
        if ((a == null) || (b == null)) {
            if (a == b) {
                return 0;
            }
            if (a == null) {
                return -1;
            }
            return 1;
        }
        return a.compareTo(b);
    }

    /**
   * Checks if the given object is between the last two parameter values.
   * @param obj The object to check.
   * @param from The lower value to compare.
   * @param to The upper value to compare.
   * @return true if <code>obj</code> is between <code>from</code> and <code>to</code>.
   */
    public static final boolean between(Comparable obj, Comparable from, Comparable to) {
        int compareObjFrom = compare(obj, from);
        int compareObjTo = compare(obj, to);
        return (compareObjFrom >= 0) && (compareObjTo <= 0);
    }

    /**
   * Gets the textual representation of the given object.
   * @param obj The object to get the extual representation of.
   * @return The textual representation of the given object or null.
   */
    public static final String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
