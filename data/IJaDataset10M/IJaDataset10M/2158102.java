package com.philip.journal.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.philip.journal.core.Messages.Error;
import com.philip.journal.core.exception.JournalRuntimeException;

/**
 * Utility class for common bean/Class reflection functionalities. Helper bean class for bean manipulation.
 *
 * @author cry30
 */
public final class BeanUtils {

    /** Class logger. */
    private static final Logger LOGGER = Logger.getLogger(BeanUtils.class);

    /** property first letter start. */
    private static final int IDX_FL_START = 3;

    /** property first letter end. */
    private static final int IDX_FL_END = 4;

    /** Cache to store the array of properties for a given bean type. */
    private static Map<String, String[]> beanPropertyCache;

    /** Cache to store the property type of a bean property. */
    private static Map<String, Class<?>> beanPropTypeCache;

    /** Java wrapper to primitive type mapping. */
    private static Map<Class<?>, Class<?>> primitiveTypeMap;

    static {
        beanPropertyCache = new HashMap<String, String[]>();
        beanPropTypeCache = new HashMap<String, Class<?>>();
        primitiveTypeMap = new HashMap<Class<?>, Class<?>>();
        primitiveTypeMap.put(Integer.class, Integer.TYPE);
        primitiveTypeMap.put(Boolean.class, Boolean.TYPE);
        primitiveTypeMap.put(Long.class, Long.TYPE);
        primitiveTypeMap.put(Character.class, Character.TYPE);
        primitiveTypeMap.put(Short.class, Short.TYPE);
        primitiveTypeMap.put(Double.class, Double.TYPE);
        primitiveTypeMap.put(Float.class, Float.TYPE);
        primitiveTypeMap.put(Byte.class, Byte.TYPE);
    }

    /** Singleton class variable. */
    private static final BeanUtils INSTANCE = new BeanUtils();

    /** Prevent extension of this class. */
    private BeanUtils() {
    }

    /**
     * Singleton factory method.
     *
     * @return Singleton instance of this class.
     */
    public static BeanUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Produces the primitive type given a wrapper Class otherwise the original Class is returned.
     *
     * @param klass The Wrapper classes to derive the primitive type from.
     * @return primitive type equivalent for the given type. Will return the save Class if no primitive type is found.
     * @exception IllegalArgumentException when the parameter klass is null.
     */
    public static Class<?> getPrimitive(final Class<?> klass) {
        if (klass == null) {
            throw new IllegalArgumentException(Error.IAE_NULL);
        }
        final Class<?> retval = primitiveTypeMap.get(klass);
        return retval == null ? klass : retval;
    }

    /**
     * Returns the get accessor method name of the given property name.
     *
     * @param property property name to derive getter method from.
     * @return accessor method name. null if the property parameter is null or and empty String.
     * @exception IllegalArgumentException when the parameter property is null or is zero-length.
     */
    public static String getGetterMethodName(final String property) {
        if (property == null || property.length() < 1) {
            throw new IllegalArgumentException(Error.IAE_NULLOREMPTY);
        }
        final StringBuilder retval = new StringBuilder("get" + property.substring(0, 1).toUpperCase());
        if (property.length() > 1) {
            retval.append(property.substring(1));
        }
        return retval.toString();
    }

    /**
     * Returns the set accessor method name of the given property name.
     *
     * @param property property name to derive setter method from.
     * @return accessor method name. null if the property parameter is null or and empty String.
     * @exception IllegalArgumentException when the parameter property is null or is zero-length.
     */
    public static String getSetterMethodName(final String property) {
        if (property == null || property.length() < 1) {
            throw new IllegalArgumentException(Error.IAE_NULLOREMPTY);
        }
        final StringBuilder retval = new StringBuilder("set" + property.substring(0, 1).toUpperCase());
        if (property.length() > 1) {
            retval.append(property.substring(1));
        }
        return retval.toString();
    }

    /**
     * Returns all properties of a bean available via accessor methods.
     *
     * @param bean - the bean whose properties are to be derived.
     *
     * @return - array of bean fields/properties
     *
     * @throws IllegalArgumentException when the bean is null;
     */
    public static String[] getProperties(final Object bean) {
        return getProperties(bean, new String[0]);
    }

    /**
     * Returns all properties of a bean available via accessor methods.
     *
     * @param bean - the bean whose properties are to be derived.
     * @param exemptParam - array of properties not will be excluded from list.
     *
     * @return - array of bean fields/properties
     *
     * @throws IllegalArgumentException when the bean parameter is null;
     */
    public static String[] getProperties(final Object bean, final String[] exemptParam) {
        if (bean == null) {
            throw new IllegalArgumentException(Error.IAE_NULL);
        }
        final String[] exemption = exemptParam == null ? new String[0] : exemptParam;
        String[] retval;
        final String exemptionId = Arrays.asList(exemption).toString();
        retval = beanPropertyCache.get(bean.getClass() + exemptionId);
        if (retval == null) {
            final List<String> exemptList = new ArrayList<String>(Arrays.asList(exemption));
            exemptList.add("class");
            final List<String> fields = new ArrayList<String>();
            for (final Field nextField : bean.getClass().getDeclaredFields()) {
                final String property = nextField.getName();
                if (exemptList.contains(property)) {
                    continue;
                }
                fields.add(property);
            }
            retval = fields.toArray(new String[fields.size()]);
            beanPropertyCache.put(bean.getClass() + exemptionId, retval);
        }
        return retval;
    }

    /**
     * Extracts property name from a given getter Method.
     *
     * @param accessorMethod accessor method name.
     *
     * @return derived property name.
     * @exception IllegalArgumentException when the accessorMethod parameter is null.
     */
    public static String getPropertyName(final Method accessorMethod) {
        if (accessorMethod == null) {
            throw new IllegalArgumentException(Error.IAE_NULL);
        }
        return BeanUtils.getPropertyName(accessorMethod.getName());
    }

    /**
     * Converts the accessor method name into bean property name.
     *
     * @param accessorMethName accessor method name.
     * @return property bean property name.
     *
     * @exception IllegalArgumentException when the passed parameter is null or when the passed parameter is
     *                not a valid accessor method name.
     */
    public static String getPropertyName(final String accessorMethName) {
        if (accessorMethName == null) {
            throw new IllegalArgumentException(Error.IAE_GENERIC);
        }
        final int length = accessorMethName.trim().length();
        if (length < IDX_FL_END) {
            throw new IllegalArgumentException(Error.IAE_GENERIC);
        }
        if (!(accessorMethName.startsWith("get") || accessorMethName.startsWith("set"))) {
            throw new IllegalArgumentException(Error.IAE_GENERIC + " " + accessorMethName);
        }
        final StringBuilder propName = new StringBuilder(accessorMethName.substring(IDX_FL_START, IDX_FL_END).toLowerCase());
        if (length > IDX_FL_END) {
            propName.append(accessorMethName.substring(IDX_FL_END));
        }
        return propName.toString();
    }

    /**
     * Retrieves a property value of the bean. Note: This will work only when a public getter method is available for
     * the property.
     *
     * @param bean instance where we want to get the property value from.
     * @param property property name of the bean we want to get the property value from.
     * @return null when the property could not be retrieved.
     * @throws IllegalArgumentException when the bean is null or the property is not valid;
     */
    public static Object getProperty(final Object bean, final String property) {
        if (bean == null) {
            throw new IllegalArgumentException(Error.IAE_NULL);
        }
        final String getterMethod = getGetterMethodName(property);
        try {
            return getDeclaredOrInheritedMethod(bean, getterMethod, new Class[0]).invoke(bean, new Object[0]);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(Error.IAE_INVALID_PROP + " " + property);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (final Exception e) {
            throw new JournalRuntimeException(Error.GEN_ERROR, e);
        }
    }

    /**
     * Retrieves the type of the bean property.
     *
     * @param bean bean instance whose property type we are getting.
     * @param property bean property whose type we are getting.
     * @return null when the property cannot be retrieved.
     * @exception IllegalArgumentException when the bean or the property parameter is null; If any of the following is
     *                true:
     *                <ul>
     *                <li>when the object passed is null.
     *                <li>when the property passed is null.
     *                <li>when the property passed is not found.
     *                </ul>
     */
    public static Class<?> getPropertyType(final Object bean, final String property) {
        if (bean == null || property == null) {
            throw new IllegalArgumentException(Error.IAE_NULLOREMPTY);
        }
        final String key = bean.getClass().getName() + "." + property;
        Class<?> type = beanPropTypeCache.get(key);
        if (type == null) {
            try {
                type = bean.getClass().getDeclaredField(property).getType();
            } catch (final NoSuchFieldException e) {
                throw new IllegalArgumentException(Error.IAE_INVALID_PROP + property);
            }
        }
        beanPropTypeCache.put(key, type);
        return type;
    }

    /**
     * Helper method for calling method on a bean. All Exceptions are ignored except for NoSuchMethodException wherein
     * IllegalArgumentException is thrown in its place. For primitive typed return, the corresponding wrapper method
     * should be called on return Object.
     *
     * Note: Use this method with extreme caution.
     *
     * @param object object instance to invoke the method from.
     * @param method method name.
     * @param types method parameter types.
     * @param args argument passed to the bean via reflection
     * @return null if any exception is encountered.
     * @exception IllegalArgumentException If any of the following is true:
     *                <ul>
     *                <li>when the object passed is null.
     *                <li>when the method parameter passed is null.
     *                <li>when the method parameter passed is not found.
     *                </ul>
     */
    public static Object invokeMethodSilent(final Object object, final String method, final Class<?>[] types, final Object[] args) {
        if (object == null || method == null) {
            throw new IllegalArgumentException(Error.IAE_GENERIC);
        }
        Object retval = null;
        try {
            retval = invokeMethod(object, method, types, args);
        } catch (final InvocationTargetException ignore) {
            LOGGER.warn(ignore.getMessage(), ignore);
        }
        return retval;
    }

    /**
     * Helper method for calling method on a bean. Exception during target invocation is thrown back to the client code.
     * For primitive typed return, the corresponding wrapper method should be called on return Object.
     *
     * @param object object instance to invoke the method from.
     * @param methodName method name.
     * @param types method parameter types.
     * @param args argument passed to the bean via reflection
     * @return null if any exception is encountered other than the InvocationTargetException
     * @exception IllegalArgumentException If any of the following is true:
     *                <ul>
     *                <li>when the object passed is null.
     *                <li>when the method parameter passed is null.
     *                <li>when the method parameter passed is not found.
     *                </ul>
     * @throws InvocationTargetException - when an exception is thrown inside the target object.
     */
    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] types, final Object[] args) throws InvocationTargetException {
        if (object == null || methodName == null || "".equals(methodName.trim())) {
            throw new IllegalArgumentException(Error.IAE_NULLOREMPTY);
        }
        Object retval = null;
        try {
            final Method method = getDeclaredOrInheritedMethod(object, methodName, types);
            retval = method.invoke(object, args);
        } catch (final IllegalArgumentException e) {
            LOGGER.debug(e.getMessage(), e);
        } catch (final SecurityException e) {
            LOGGER.debug(e.getMessage(), e);
        } catch (final IllegalAccessException e) {
            LOGGER.debug(e.getMessage(), e);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(Error.IAE_INV_METHOD + methodName);
        }
        return retval;
    }

    /**
     * Will get the accessible method from the class either declared or inherited.
     *
     * @param object - object instance containing the method.
     * @param methName - method name.
     * @param types - argument type for the method.
     * @return the accessible method. null if no accessible method with the correct signature is found.
     * @throws SecurityException When the object exist but the calling class do not have access to the method.
     * @throws NoSuchMethodException when the method was not found in the passed object instance.
     */
    static Method getDeclaredOrInheritedMethod(final Object object, final String methName, final Class<?>[] types) throws NoSuchMethodException {
        Method retval = null;
        try {
            retval = object.getClass().getMethod(methName, types);
        } catch (final NoSuchMethodException nsm) {
            retval = object.getClass().getDeclaredMethod(methName, types);
        }
        return retval;
    }

    /**
     * Converts a java bean object to a name value pair Map.
     *
     * @param javaBean the java bean object to convert to map.
     * @param excludedProps optional array of properties not to be included in the resulting Map.
     * @return null when the javaBean passed is null.
     */
    public static Map<String, Object> convertToMap(final Object javaBean, final String[] excludedProps) {
        Map<String, Object> retval = null;
        if (javaBean != null) {
            retval = new HashMap<String, Object>();
            final String[] properties = getProperties(javaBean, excludedProps);
            for (final String property : properties) {
                retval.put(property, getProperty(javaBean, property));
            }
        }
        return retval;
    }
}
