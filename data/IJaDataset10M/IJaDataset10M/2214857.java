package edu.vt.middleware.ldap.props;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides methods common to property invokers.
 *
 * @author  Middleware Services
 * @version  $Revision: 2197 $ $Date: 2012-01-01 22:40:30 -0500 (Sun, 01 Jan 2012) $
 */
public abstract class AbstractPropertyInvoker implements PropertyInvoker {

    /** Cache of properties. */
    protected static final Map<String, Map<String, Method[]>> PROPERTIES_CACHE = new HashMap<String, Map<String, Method[]>>();

    /** Class to invoke methods on. */
    private Class<?> clazz;

    /** Map of all properties to their getter and setter methods. */
    private Map<String, Method[]> properties;

    /**
   * Initializes the properties cache with the supplied class. The cache
   * contains a map of properties to an array of the setter and getter methods.
   * If a method named 'initialize' is found, it is also cached.
   *
   * @param  c  to read methods from
   */
    protected void initialize(final Class<?> c) {
        final String cacheKey = c.getName();
        if (PROPERTIES_CACHE.containsKey(cacheKey)) {
            properties = PROPERTIES_CACHE.get(cacheKey);
        } else {
            properties = new HashMap<String, Method[]>();
            PROPERTIES_CACHE.put(cacheKey, properties);
            for (Method method : c.getMethods()) {
                if (!method.isBridge()) {
                    if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
                        final String mName = method.getName().substring(3);
                        final String pName = new StringBuilder(mName.substring(0, 1).toLowerCase()).append(mName.substring(1, mName.length())).toString();
                        if (properties.containsKey(pName)) {
                            final Method[] m = properties.get(pName);
                            m[1] = method;
                            properties.put(pName, m);
                        } else {
                            properties.put(pName, new Method[] { null, method });
                        }
                    } else if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                        final String mName = method.getName().substring(3);
                        final String pName = new StringBuilder(mName.substring(0, 1).toLowerCase()).append(mName.substring(1, mName.length())).toString();
                        if (properties.containsKey(pName)) {
                            final Method[] m = properties.get(pName);
                            m[0] = method;
                            properties.put(pName, m);
                        } else {
                            properties.put(pName, new Method[] { method, null });
                        }
                    } else if ("initialize".equals(method.getName()) && method.getParameterTypes().length == 0) {
                        final String pName = new StringBuilder(method.getName()).toString();
                        properties.put(pName, new Method[] { method, method });
                    }
                }
            }
            final Iterator<Method[]> i = properties.values().iterator();
            while (i.hasNext()) {
                final Method[] m = i.next();
                if (m[0] == null || m[1] == null) {
                    i.remove();
                }
            }
        }
        clazz = c;
    }

    /**
   * This invokes the setter method for the supplied property name with the
   * supplied value.
   *
   * @param  object  to invoke method on
   * @param  name  of the property
   * @param  value  of the property
   *
   * @throws  IllegalArgumentException  if an invocation exception occurs
   */
    public void setProperty(final Object object, final String name, final String value) {
        if (!clazz.isInstance(object)) {
            throw new IllegalArgumentException("Illegal attempt to set property for class " + clazz.getName() + " on object of type " + object.getClass().getName());
        }
        final Method getter = properties.get(name) != null ? properties.get(name)[0] : null;
        if (getter == null) {
            throw new IllegalArgumentException("No getter method found for " + name + " on object " + clazz.getName());
        }
        final Method setter = properties.get(name) != null ? properties.get(name)[1] : null;
        if (setter == null) {
            throw new IllegalArgumentException("No setter method found for " + name + " on object " + clazz.getName());
        }
        invokeMethod(setter, object, convertValue(getter.getReturnType(), value));
    }

    /**
   * Converts the supplied string value into an Object of the supplied type. If
   * value cannot be converted it is returned as is.
   *
   * @param  type  of object to convert value into
   * @param  value  to parse
   *
   * @return  object of the supplied type
   */
    protected abstract Object convertValue(final Class<?> type, final String value);

    /**
   * Returns whether the supplied property exists for this invoker.
   *
   * @param  name  to check
   *
   * @return  whether the supplied property exists
   */
    public boolean hasProperty(final String name) {
        return properties.containsKey(name);
    }

    /**
   * Returns the property keys for this invoker.
   *
   * @return  set of property names
   */
    public Set<String> getProperties() {
        return Collections.unmodifiableSet(properties.keySet());
    }

    /**
   * Creates an instance of the supplied type.
   *
   * @param  <T>  type of class returned
   * @param  type  of class to create
   * @param  className  to create
   *
   * @return  class of type T
   *
   * @throws  IllegalArgumentException  if the supplied class name cannot create
   * a new instance of T
   */
    @SuppressWarnings("unchecked")
    public static <T> T instantiateType(final T type, final String className) {
        try {
            try {
                return (T) createClass(className).newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        } catch (RuntimeException e) {
            final Logger l = LoggerFactory.getLogger(AbstractPropertyInvoker.class);
            l.error("Error instantiating type {}, with {}", type, className);
            throw e;
        }
    }

    /**
   * Creates the class with the supplied name.
   *
   * @param  className  to create
   *
   * @return  class
   *
   * @throws  IllegalArgumentException  if the supplied class name cannot be
   * created
   */
    public static Class<?> createClass(final String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("Could not find class '%s'", className), e);
        }
    }

    /**
   * Converts simple types that are common to all property invokers. If value
   * cannot be converted it is returned as is.
   *
   * @param  type  of object to convert value into
   * @param  value  to parse
   *
   * @return  object of the supplied type
   */
    protected Object convertSimpleType(final Class<?> type, final String value) {
        Object newValue = value;
        if (Class.class.isAssignableFrom(type)) {
            newValue = createTypeFromPropertyValue(Class.class, value);
        } else if (Class[].class.isAssignableFrom(type)) {
            newValue = createArrayTypeFromPropertyValue(Class.class, value);
        } else if (type.isEnum()) {
            newValue = getEnum(type, value);
        } else if (String[].class == type) {
            newValue = value.split(",");
        } else if (Object[].class == type) {
            newValue = value.split(",");
        } else if (float.class == type) {
            newValue = Float.parseFloat(value);
        } else if (int.class == type) {
            newValue = Integer.parseInt(value);
        } else if (long.class == type) {
            newValue = Long.parseLong(value);
        } else if (short.class == type) {
            newValue = Short.parseShort(value);
        } else if (double.class == type) {
            newValue = Double.parseDouble(value);
        } else if (boolean.class == type) {
            newValue = Boolean.valueOf(value);
        }
        return newValue;
    }

    /**
   * Returns the enum for the supplied type and value.
   *
   * @param  clazz  of the enum
   * @param  value  of the enum
   *
   * @return  enum that matches the supplied value
   */
    protected static Enum<?> getEnum(final Class<?> clazz, final String value) {
        for (Object o : clazz.getEnumConstants()) {
            final Enum<?> e = (Enum<?>) o;
            if (e.name().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown enum value %s for %s", value, clazz));
    }

    /**
   * Returns the object which represents the supplied class given the supplied
   * string representation.
   *
   * @param  c  type to instantiate
   * @param  s  property value to parse
   *
   * @return  the supplied type or null
   */
    protected Object createTypeFromPropertyValue(final Class<?> c, final String s) {
        Object newObject = null;
        if ("null".equals(s)) {
            newObject = null;
        } else {
            if (PropertyValueParser.isConfig(s)) {
                final PropertyValueParser configParser = new PropertyValueParser(s);
                newObject = configParser.initializeType();
            } else {
                if (Class.class == c) {
                    newObject = createClass(s);
                } else {
                    newObject = instantiateType(c, s);
                }
            }
        }
        return newObject;
    }

    /**
   * Returns the object which represents an array of the supplied class given
   * the supplied string representation.
   *
   * @param  c  type to instantiate
   * @param  s  property value to parse
   *
   * @return  an array or null
   */
    protected Object createArrayTypeFromPropertyValue(final Class<?> c, final String s) {
        Object newObject = null;
        if ("null".equals(s)) {
            newObject = null;
        } else {
            if (s.indexOf("},") != -1) {
                final String[] classes = s.split("\\},");
                newObject = Array.newInstance(c, classes.length);
                for (int i = 0; i < classes.length; i++) {
                    classes[i] = classes[i] + "}";
                    if (PropertyValueParser.isConfig(classes[i])) {
                        final PropertyValueParser configParser = new PropertyValueParser(classes[i]);
                        Array.set(newObject, i, configParser.initializeType());
                    } else {
                        throw new IllegalArgumentException(String.format("Could not parse property string: %s", classes[i]));
                    }
                }
            } else {
                final String[] classes = s.split(",");
                newObject = Array.newInstance(c, classes.length);
                for (int i = 0; i < classes.length; i++) {
                    if (PropertyValueParser.isConfig(classes[i])) {
                        final PropertyValueParser configParser = new PropertyValueParser(classes[i]);
                        Array.set(newObject, i, configParser.initializeType());
                    } else {
                        if (Class.class == c) {
                            Array.set(newObject, i, createClass(classes[i]));
                        } else {
                            Array.set(newObject, i, instantiateType(c, classes[i]));
                        }
                    }
                }
            }
        }
        return newObject;
    }

    /**
   * Returns the enum array which represents the supplied class given the
   * supplied string representation.
   *
   * @param  c  type to instantiate
   * @param  s  property value to parse
   *
   * @return  Enum[] of the supplied type or null
   */
    protected Object createArrayEnumFromPropertyValue(final Class<?> c, final String s) {
        Object newObject = null;
        if ("null".equals(s)) {
            newObject = null;
        } else {
            final String[] values = s.split(",");
            newObject = Array.newInstance(c, values.length);
            for (int i = 0; i < values.length; i++) {
                Array.set(newObject, i, getEnum(c, values[i]));
            }
        }
        return newObject;
    }

    /**
   * Invokes the supplied method on the supplied object with the supplied
   * argument.
   *
   * @param  method  to invoke
   * @param  object  to invoke method on
   * @param  arg  to invoke method with
   *
   * @return  object produced by the invocation
   *
   * @throws  IllegalArgumentException  if an error occurs invoking the method
   */
    public static Object invokeMethod(final Method method, final Object object, final Object arg) {
        try {
            try {
                Object[] params = new Object[] { arg };
                if (arg == null && method.getParameterTypes().length == 0) {
                    params = (Object[]) null;
                }
                return method.invoke(object, params);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        } catch (RuntimeException e) {
            final Logger l = LoggerFactory.getLogger(AbstractPropertyInvoker.class);
            l.error("Error invoking {}, on {}, with params {}", new Object[] { method, object, arg });
            throw e;
        }
    }
}
