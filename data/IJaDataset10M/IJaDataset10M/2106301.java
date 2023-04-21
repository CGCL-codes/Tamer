package org.architecture.common.util;

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.architecture.common.web.multipart.MultipartRequestWrapper;

/**
 * Miscellaneous class utility methods. Mainly for internal use within the
 * framework; consider Jakarta's Commons Lang for a more comprehensive suite of
 * utilities.
 * 
 * @author Keith Donald
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.1
 */
public abstract class ClassUtils {

    private static final Log logger = LogFactory.getLog(MultipartRequestWrapper.class);

    /** Suffix for array class names */
    public static final String ARRAY_SUFFIX = "[]";

    /** The package separator character '.' */
    private static final char PACKAGE_SEPARATOR_CHAR = '.';

    /** The inner class separator character '$' */
    private static final char INNER_CLASS_SEPARATOR_CHAR = '$';

    /** The CGLIB class separator character "$$" */
    private static final String CGLIB_CLASS_SEPARATOR_CHAR = "$$";

    /**
     * Map with primitive wrapper type as key and corresponding primitive type
     * as value, for example: Integer.class -> int.class
     */
    private static final Map primitiveWrapperTypeMap = new HashMap(8);

    /**
     * Map with primitive type name as key and corresponding primitive type as
     * value, for example: "int" -> "int.class"
     */
    private static final Map primitiveTypeNameMap = new HashMap(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
        for (Iterator it = primitiveWrapperTypeMap.values().iterator(); it.hasNext(); ) {
            Class primitiveClass = (Class) it.next();
            primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
        }
    }

    /**
     * Return a default ClassLoader to use (never <code>null</code>). Returns
     * the thread context ClassLoader, if available. The ClassLoader that loaded
     * the ClassUtils class will be used as fallback.
     * <p>
     * Call this method if you intend to use the thread context ClassLoader in a
     * scenario where you absolutely need a non-null ClassLoader reference: for
     * example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code>
     * ClassLoader reference as well).
     * 
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * Return whether the {@link Class}identified by the supplied name is
     * present and can be loaded. Will return <code>false</code> if either the
     * class or one of its dependencies is not present or cannot be loaded.
     */
    public static boolean isPresent(String className) {
        try {
            forName(className);
            return true;
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Class [" + className + "] or one of its dependencies is not present: " + ex);
            }
            return false;
        }
    }

    /**
     * Replacement for <code>Class.forName()</code> that also returns Class
     * instances for primitives (like "int") and array class names (like
     * "String[]").
     * <p>
     * Always uses the default class loader: that is, preferably the thread
     * context class loader, or the ClassLoader that loaded the ClassUtils class
     * as fallback.
     * 
     * @param name
     *                the name of the Class
     * @return Class instance for the supplied name
     * @see Class#forName(String, boolean, ClassLoader)
     * @see #getDefaultClassLoader()
     */
    public static Class forName(String name) throws ClassNotFoundException {
        return forName(name, getDefaultClassLoader());
    }

    /**
     * Replacement for <code>Class.forName()</code> that also returns Class
     * instances for primitives (like "int") and array class names (like
     * "String[]").
     * 
     * @param name
     *                the name of the Class
     * @param classLoader
     *                the class loader to use
     * @return Class instance for the supplied name
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static Class forName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        Class clazz = resolvePrimitiveClassName(name);
        if (clazz != null) {
            return clazz;
        }
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class elementClass = ClassUtils.forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }
        return Class.forName(name, true, classLoader);
    }

    /**
     * Resolve the given class name as primitive class, if appropriate.
     * 
     * @param name
     *                the name of the potentially primitive class
     * @return the primitive class, or <code>null</code> if the name does not
     *         denote a primitive class
     */
    public static Class resolvePrimitiveClassName(String name) {
        Class result = null;
        if (name != null && name.length() <= 8) {
            result = (Class) primitiveTypeNameMap.get(name);
        }
        return result;
    }

    /**
     * Get the class name without the qualified package name.
     * 
     * @param className
     *                the className to get the short name for
     * @return the class name of the class without the package name
     * @throws IllegalArgumentException
     *                 if the className is empty
     */
    public static String getShortName(String className) {
        Assert.hasLength(className, "Class name must not be empty");
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR_CHAR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        return shortName;
    }

    /**
     * Get the class name without the qualified package name.
     * 
     * @param clazz
     *                the class to get the short name for
     * @return the class name of the class without the package name
     */
    public static String getShortName(Class clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    /**
     * Return the short string name of a Java class in decapitalized JavaBeans
     * property format.
     * 
     * @param clazz
     *                the class
     * @return the short name rendered in a standard JavaBeans property format
     * @see java.beans.Introspector#decapitalize(String)
     */
    public static String getShortNameAsProperty(Class clazz) {
        return Introspector.decapitalize(getShortName(clazz));
    }

    /**
     * Return the qualified name of the given class: usually simply the class
     * name, but component type class name + "[]" for arrays.
     * 
     * @param clazz
     *                the class
     * @return the qualified name of the class
     */
    public static String getQualifiedName(Class clazz) {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isArray()) {
            return clazz.getComponentType().getName() + ARRAY_SUFFIX;
        } else {
            return clazz.getName();
        }
    }

    /**
     * Return the qualified name of the given method, consisting of fully
     * qualified interface/class name + "." + method name.
     * 
     * @param method
     *                the method
     * @return the qualified name of the method
     */
    public static String getQualifiedMethodName(Method method) {
        Assert.notNull(method, "Method must not be null");
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    /**
     * Determine whether the given class has a method with the given signature.
     * Essentially translates <code>NoSuchMethodException</code> to "false".
     * 
     * @param clazz
     *                the clazz to analyze
     * @param methodName
     *                the name of the method
     * @param paramTypes
     *                the parameter types of the method
     */
    public static boolean hasMethod(Class clazz, String methodName, Class[] paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        try {
            clazz.getMethod(methodName, paramTypes);
            return true;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    /**
     * Return the number of methods with a given name (with any argument types),
     * for the given class and/or its superclasses. Includes non-public methods.
     * 
     * @param clazz
     *                the clazz to check
     * @param methodName
     *                the name of the method
     * @return the number of methods with the given name
     */
    public static int getMethodCountForName(Class clazz, String methodName) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        int count = 0;
        do {
            for (int i = 0; i < clazz.getDeclaredMethods().length; i++) {
                Method method = clazz.getDeclaredMethods()[i];
                if (methodName.equals(method.getName())) {
                    count++;
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return count;
    }

    /**
     * Does the given class and/or its superclasses at least have one or more
     * methods (with any argument types)? Includes non-public methods.
     * 
     * @param clazz
     *                the clazz to check
     * @param methodName
     *                the name of the method
     * @return whether there is at least one method with the given name
     */
    public static boolean hasAtLeastOneMethodWithName(Class clazz, String methodName) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        do {
            for (int i = 0; i < clazz.getDeclaredMethods().length; i++) {
                Method method = clazz.getDeclaredMethods()[i];
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return false;
    }

    /**
     * Return a static method of a class.
     * 
     * @param methodName
     *                the static method name
     * @param clazz
     *                the class which defines the method
     * @param args
     *                the parameter types to the method
     * @return the static method, or <code>null</code> if no static method was
     *         found
     * @throws IllegalArgumentException
     *                 if the method name is blank or the clazz is null
     */
    public static Method getStaticMethod(Class clazz, String methodName, Class[] args) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        try {
            Method method = clazz.getDeclaredMethod(methodName, args);
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                return method;
            }
        } catch (NoSuchMethodException ex) {
        }
        return null;
    }

    /**
     * Check if the given class represents a primitive wrapper, i.e. Boolean,
     * Byte, Character, Short, Integer, Long, Float, or Double.
     */
    public static boolean isPrimitiveWrapper(Class clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

    /**
     * Check if the given class represents a primitive (i.e. boolean, byte,
     * char, short, int, long, float, or double) or a primitive wrapper (i.e.
     * Boolean, Byte, Character, Short, Integer, Long, Float, or Double).
     */
    public static boolean isPrimitiveOrWrapper(Class clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * Check if the given class represents an array of primitives, i.e. boolean,
     * byte, char, short, int, long, float, or double.
     */
    public static boolean isPrimitiveArray(Class clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isArray() && clazz.getComponentType().isPrimitive());
    }

    /**
     * Check if the given class represents an array of primitive wrappers, i.e.
     * Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
     */
    public static boolean isPrimitiveWrapperArray(Class clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
    }

    /**
     * Determine if the given target type is assignable from the given value
     * type, assuming setting by reflection. Considers primitive wrapper classes
     * as assignable to the corresponding primitive types.
     * 
     * @param targetType
     *                the target type
     * @param valueType
     *                the value type that should be assigned to the target type
     * @return if the target type is assignable from the value type
     */
    public static boolean isAssignable(Class targetType, Class valueType) {
        Assert.notNull(targetType, "Target type must not be null");
        Assert.notNull(valueType, "Value type must not be null");
        return (targetType.isAssignableFrom(valueType) || targetType.equals(primitiveWrapperTypeMap.get(valueType)));
    }

    /**
     * Determine if the given type is assignable from the given value, assuming
     * setting by reflection. Considers primitive wrapper classes as assignable
     * to the corresponding primitive types.
     * 
     * @param type
     *                the target type
     * @param value
     *                the value that should be assigned to the type
     * @return if the type is assignable from the value
     */
    public static boolean isAssignableValue(Class type, Object value) {
        Assert.notNull(type, "Type must not be null");
        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
    }

    /**
     * Return a path suitable for use with <code>ClassLoader.getResource</code>
     * (also suitable for use with <code>Class.getResource</code> by
     * prepending a slash ('/') to the return value. Built by taking the package
     * of the specified class file, converting all dots ('.') to slashes ('/'),
     * adding a trailing slash if necesssary, and concatenating the specified
     * resource name to this. <br/>As such, this function may be used to build a
     * path suitable for loading a resource file that is in the same package as
     * a class file, although
     * {@link org.springframework.core.io.ClassPathResource}is usually even
     * more convenient.
     * 
     * @param clazz
     *                the Class whose package will be used as the base
     * @param resourceName
     *                the resource name to append. A leading slash is optional.
     * @return the built-up resource path
     * @see ClassLoader#getResource
     * @see Class#getResource
     */
    public static String addResourcePathToPackagePath(Class clazz, String resourceName) {
        Assert.notNull(resourceName, "Resource name must not be null");
        if (!resourceName.startsWith("/")) {
            return classPackageAsResourcePath(clazz) + "/" + resourceName;
        }
        return classPackageAsResourcePath(clazz) + resourceName;
    }

    /**
     * Given an input class object, return a string which consists of the
     * class's package name as a pathname, i.e., all dots ('.') are replaced by
     * slashes ('/'). Neither a leading nor trailing slash is added. The result
     * could be concatenated with a slash and the name of a resource, and fed
     * directly to ClassLoader.getResource(). For it to be fed to
     * Class.getResource, a leading slash would also have to be prepended to the
     * return value.
     * 
     * @param clazz
     *                the input class. A null value or the default (empty)
     *                package will result in an empty string ("") being
     *                returned.
     * @return a path which represents the package name
     * @see ClassLoader#getResource
     * @see Class#getResource
     */
    public static String classPackageAsResourcePath(Class clazz) {
        if (clazz == null || clazz.getPackage() == null) {
            return "";
        }
        return clazz.getPackage().getName().replace('.', '/');
    }

    /**
     * Return all interfaces that the given object implements as array,
     * including ones implemented by superclasses.
     * 
     * @param object
     *                the object to analyse for interfaces
     * @return all interfaces that the given object implements as array
     */
    public static Class[] getAllInterfaces(Object object) {
        Set interfaces = getAllInterfacesAsSet(object);
        return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
    }

    /**
     * Return all interfaces that the given class implements as array, including
     * ones implemented by superclasses.
     * <p>
     * If the class itself is an interface, it gets returned as sole interface.
     * 
     * @param clazz
     *                the class to analyse for interfaces
     * @return all interfaces that the given object implements as array
     */
    public static Class[] getAllInterfacesForClass(Class clazz) {
        Set interfaces = getAllInterfacesForClassAsSet(clazz);
        return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
    }

    /**
     * Return all interfaces that the given object implements as List, including
     * ones implemented by superclasses.
     * 
     * @param object
     *                the object to analyse for interfaces
     * @return all interfaces that the given object implements as List
     */
    public static Set getAllInterfacesAsSet(Object object) {
        return getAllInterfacesForClassAsSet(object.getClass());
    }

    /**
     * Return all interfaces that the given class implements as Set, including
     * ones implemented by superclasses.
     * <p>
     * If the class itself is an interface, it gets returned as sole interface.
     * 
     * @param clazz
     *                the class to analyse for interfaces
     * @return all interfaces that the given object implements as Set
     */
    public static Set getAllInterfacesForClassAsSet(Class clazz) {
        if (clazz.isInterface()) {
            return Collections.singleton(clazz);
        }
        Set interfaces = new HashSet();
        while (clazz != null) {
            for (int i = 0; i < clazz.getInterfaces().length; i++) {
                Class ifc = clazz.getInterfaces()[i];
                interfaces.add(ifc);
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces;
    }

    public static Object getClassObject(String className) throws Exception {
        try {
            return (Class.forName(className)).newInstance();
        } catch (Exception ex) {
            logger.error("Get class object error : ", ex);
            throw new Exception(ex);
        }
    }
}
