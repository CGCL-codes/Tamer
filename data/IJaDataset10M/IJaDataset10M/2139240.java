package com.taliasplayground.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Operates on classes without using reflection.
 * </p>
 * 
 * <p>
 * This class handles invalid <code>null</code> inputs as best it can. Each
 * method documents its behaviour in more detail.
 * </p>
 * 
 * <p>
 * The notion of a <code>canonical name</code> includes the human readable name
 * for the type, for example <code>int[]</code>. The non-canonical method
 * variants work with the JVM names, such as <code>[I</code>.
 * </p>
 * 
 * @author Apache Software Foundation
 * @author Gary Gregory
 * @author Norm Deane
 * @author Alban Peignier
 * @author Tomasz Blachowicz
 * @since 2.0
 * @version $Id$
 */
public abstract class ClassUtils {

    /**
     * <p>
     * The package separator character: <code>'&#x2e;' == {@value}</code>.
     * </p>
     */
    public static final char PACKAGE_SEPARATOR_CHAR = '.';

    /**
     * <p>
     * The package separator String: <code>"&#x2e;"</code>.
     * </p>
     */
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

    /**
     * <p>
     * The inner class separator character: <code>'$' == {@value}</code>.
     * </p>
     */
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

    /**
     * <p>
     * The inner class separator String: <code>"$"</code>.
     * </p>
     */
    public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

    /**
     * Maps primitive <code>Class</code>es to their corresponding wrapper
     * <code>Class</code>.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.class);
    }

    /**
     * Maps wrapper <code>Class</code>es to their corresponding primitive types.
     */
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();

    static {
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperMap.entrySet()) {
            wrapperPrimitiveMap.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Maps a primitive class name to its corresponding abbreviation used in
     * array class names.
     */
    private static final Map<String, String> abbreviationMap = new HashMap<String, String>();

    /**
     * Maps an abbreviation used in array class names to corresponding primitive
     * class name.
     */
    private static final Map<String, String> reverseAbbreviationMap = new HashMap<String, String>();

    /**
     * Add primitive type abbreviation to maps of abbreviations.
     * 
     * @param primitive
     *            Canonical name of primitive type
     * @param abbreviation
     *            Corresponding abbreviation of primitive type
     */
    private static void addAbbreviation(String primitive, String abbreviation) {
        abbreviationMap.put(primitive, abbreviation);
        reverseAbbreviationMap.put(abbreviation, primitive);
    }

    /**
     * Feed abbreviation maps
     */
    static {
        addAbbreviation("int", "I");
        addAbbreviation("boolean", "Z");
        addAbbreviation("float", "F");
        addAbbreviation("long", "J");
        addAbbreviation("short", "S");
        addAbbreviation("byte", "B");
        addAbbreviation("double", "D");
        addAbbreviation("char", "C");
    }

    /**
     * <p>
     * Gets the class name minus the package name for an <code>Object</code>.
     * </p>
     * 
     * @param object
     *            the class to get the short name for, may be null
     * @param valueIfNull
     *            the value to return if null
     * @return the class name of the object without the package name, or the
     *         null value
     */
    public static String getShortClassName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortClassName(object.getClass());
    }

    /**
     * <p>
     * Gets the class name minus the package name from a <code>Class</code>.
     * </p>
     * 
     * @param cls
     *            the class to get the short name for.
     * @return the class name without the package name or an empty string
     */
    public static String getShortClassName(Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getShortClassName(cls.getName());
    }

    /**
     * <p>
     * Gets the class name minus the package name from a String.
     * </p>
     * 
     * <p>
     * The string passed in is assumed to be a class name - it is not checked.
     * </p>
     * 
     * @param className
     *            the className to get the short name for
     * @return the class name of the class without the package name or an empty
     *         string
     */
    public static String getShortClassName(String className) {
        if (className == null) {
            return "";
        }
        if (className.length() == 0) {
            return "";
        }
        StringBuilder arrayPrefix = new StringBuilder();
        if (className.startsWith("[")) {
            while (className.charAt(0) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
                className = className.substring(1, className.length() - 1);
            }
        }
        if (reverseAbbreviationMap.containsKey(className)) {
            className = reverseAbbreviationMap.get(className);
        }
        int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
        String out = className.substring(lastDotIdx + 1);
        if (innerIdx != -1) {
            out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        }
        return out + arrayPrefix;
    }

    /**
     * <p>
     * Gets the package name of an <code>Object</code>.
     * </p>
     * 
     * @param object
     *            the class to get the package name for, may be null
     * @param valueIfNull
     *            the value to return if null
     * @return the package name of the object, or the null value
     */
    public static String getPackageName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageName(object.getClass());
    }

    /**
     * <p>
     * Gets the package name of a <code>Class</code>.
     * </p>
     * 
     * @param cls
     *            the class to get the package name for, may be
     *            <code>null</code>.
     * @return the package name or an empty string
     */
    public static String getPackageName(Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getPackageName(cls.getName());
    }

    /**
     * <p>
     * Gets the package name from a <code>String</code>.
     * </p>
     * 
     * <p>
     * The string passed in is assumed to be a class name - it is not checked.
     * </p>
     * <p>
     * If the class is unpackaged, return an empty string.
     * </p>
     * 
     * @param className
     *            the className to get the package name for, may be
     *            <code>null</code>
     * @return the package name or an empty string
     */
    public static String getPackageName(String className) {
        if (className == null || className.length() == 0) {
            return "";
        }
        while (className.charAt(0) == '[') {
            className = className.substring(1);
        }
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1);
        }
        int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (i == -1) {
            return "";
        }
        return className.substring(0, i);
    }

    /**
     * <p>
     * Gets a <code>List</code> of superclasses for the given class.
     * </p>
     * 
     * @param cls
     *            the class to look up, may be <code>null</code>
     * @return the <code>List</code> of superclasses in order going up from this
     *         one <code>null</code> if null input
     */
    public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        List<Class<?>> classes = new ArrayList<Class<?>>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    /**
     * <p>
     * Gets a <code>List</code> of all interfaces implemented by the given class
     * and its superclasses.
     * </p>
     * 
     * <p>
     * The order is determined by looking through each interface in turn as
     * declared in the source file and following its hierarchy up. Then each
     * superclass is considered in the same way. Later duplicates are ignored,
     * so the order is maintained.
     * </p>
     * 
     * @param cls
     *            the class to look up, may be <code>null</code>
     * @return the <code>List</code> of interfaces in order, <code>null</code>
     *         if null input
     */
    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        LinkedHashSet<Class<?>> intfsFound = new LinkedHashSet<Class<?>>();
        getAllInterfaces(cls, intfsFound);
        return new ArrayList<Class<?>>(intfsFound);
    }

    /**
     * Get the interfaces for the specified class.
     * 
     * @param cls
     *            the class to look up, may be <code>null</code>
     * @param intfsFound
     *            the <code>Set</code> of interfaces for the class
     */
    private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> intfsFound) {
        while (cls != null) {
            Class<?>[] intfs = cls.getInterfaces();
            for (Class<?> i : intfs) {
                if (intfsFound.add(i)) {
                    getAllInterfaces(i, intfsFound);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    /**
     * <p>
     * Given a <code>List</code> of class names, this method converts them into
     * classes.
     * </p>
     * 
     * <p>
     * A new <code>List</code> is returned. If the class name cannot be found,
     * <code>null</code> is stored in the <code>List</code>. If the class name
     * in the <code>List</code> is <code>null</code>, <code>null</code> is
     * stored in the output <code>List</code>.
     * </p>
     * 
     * @param classNames
     *            the classNames to change
     * @return a <code>List</code> of Class objects corresponding to the class
     *         names, <code>null</code> if null input
     * @throws ClassCastException
     *             if classNames contains a non String entry
     */
    public static List<Class<?>> convertClassNamesToClasses(Class<?> caller, List<String> classNames) {
        if (classNames == null) {
            return null;
        }
        List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
        for (String className : classNames) {
            try {
                classes.add(getClass(caller, className));
            } catch (Exception ex) {
                classes.add(null);
            }
        }
        return classes;
    }

    /**
     * <p>
     * Given a <code>List</code> of <code>Class</code> objects, this method
     * converts them into class names.
     * </p>
     * 
     * <p>
     * A new <code>List</code> is returned. <code>null</code> objects will be
     * copied into the returned list as <code>null</code>.
     * </p>
     * 
     * @param classes
     *            the classes to change
     * @return a <code>List</code> of class names corresponding to the Class
     *         objects, <code>null</code> if null input
     * @throws ClassCastException
     *             if <code>classes</code> contains a non-<code>Class</code>
     *             entry
     */
    public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
        if (classes == null) {
            return null;
        }
        List<String> classNames = new ArrayList<String>(classes.size());
        for (Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }

    /**
     * <p>
     * Checks if one <code>Class</code> can be assigned to a variable of another
     * <code>Class</code>.
     * </p>
     * 
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
     * method takes into account widenings of primitive classes and
     * <code>null</code>s.
     * </p>
     * 
     * <p>
     * Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.
     * </p>
     * 
     * <p>
     * <code>Null</code> may be assigned to any reference type. This method will
     * return <code>true</code> if <code>null</code> is passed in and the
     * toClass is non-primitive.
     * </p>
     * 
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>
     * , sections 5.1.1, 5.1.2 and 5.1.4 for details.
     * </p>
     * 
     * @param cls
     *            the Class to check, may be null
     * @param toClass
     *            the Class to try to assign into, returns false if null
     * @param autoboxing
     *            whether to use implicit autoboxing/unboxing between primitives
     *            and wrappers
     * @return <code>true</code> if assignment possible
     */
    public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
        if (toClass == null) {
            return false;
        }
        if (cls == null) {
            return !(toClass.isPrimitive());
        }
        if (cls.isPrimitive() && !toClass.isPrimitive()) {
            cls = primitiveToWrapper(cls);
            if (cls == null) {
                return false;
            }
        }
        if (toClass.isPrimitive() && !cls.isPrimitive()) {
            cls = wrapperToPrimitive(cls);
            if (cls == null) {
                return false;
            }
        }
        if (cls.equals(toClass)) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive() == false) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Boolean.TYPE.equals(cls)) {
                return false;
            }
            if (Double.TYPE.equals(cls)) {
                return false;
            }
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(toClass);
            }
            if (Character.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Short.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            if (Byte.TYPE.equals(cls)) {
                return Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
            }
            return false;
        }
        return toClass.isAssignableFrom(cls);
    }

    /**
     * <p>
     * Converts the specified primitive Class object to its corresponding
     * wrapper Class object.
     * </p>
     * 
     * <p>
     * NOTE: From v2.2, this method handles <code>Void.TYPE</code>, returning
     * <code>Void.TYPE</code>.
     * </p>
     * 
     * @param cls
     *            the class to convert, may be null
     * @return the wrapper class for <code>cls</code> or <code>cls</code> if
     *         <code>cls</code> is not a primitive. <code>null</code> if null
     *         input.
     * @since 2.1
     */
    public static Class<?> primitiveToWrapper(Class<?> cls) {
        return cls != null && cls.isPrimitive() ? primitiveWrapperMap.get(cls) : cls;
    }

    /**
     * <p>
     * Converts the specified wrapper class to its corresponding primitive
     * class.
     * </p>
     * 
     * <p>
     * This method is the counter part of <code>primitiveToWrapper()</code>. If
     * the passed in class is a wrapper class for a primitive type, this
     * primitive type will be returned (e.g. <code>Integer.TYPE</code> for
     * <code>Integer.class</code>). For other classes, or if the parameter is
     * <b>null</b>, the return value is <b>null</b>.
     * </p>
     * 
     * @param cls
     *            the class to convert, may be <b>null</b>
     * @return the corresponding primitive type if <code>cls</code> is a wrapper
     *         class, <b>null</b> otherwise
     * @see #primitiveToWrapper(Class)
     * @since 2.4
     */
    public static Class<?> wrapperToPrimitive(Class<?> cls) {
        return wrapperPrimitiveMap.get(cls);
    }

    /**
     * <p>
     * Is the specified class an inner class or static nested class.
     * </p>
     * 
     * @param cls
     *            the class to check, may be null
     * @return <code>true</code> if the class is an inner or static nested
     *         class, false if not or <code>null</code>
     */
    public static boolean isInnerClass(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        return cls.getEnclosingClass() != null;
    }

    /**
     * Returns the class represented by <code>className</code> using the
     * <code>classLoader</code>. This implementation supports the syntaxes "
     * <code>java.util.Map.Entry[]</code>", "<code>java.util.Map$Entry[]</code>
     * ", "<code>[Ljava.util.Map.Entry;</code>", and "
     * <code>[Ljava.util.Map$Entry;</code>".
     * 
     * @param classLoader
     *            the class loader to use to load the class
     * @param className
     *            the class name
     * @param initialize
     *            whether the class must be initialized
     * @return the class represented by <code>className</code> using the
     *         <code>classLoader</code>
     * @throws ClassNotFoundException
     *             if the class is not found
     */
    public static Class<?> getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
        Assert.notNullArg(classLoader, "'classLoader' may not be null");
        try {
            Class<?> cls;
            if (abbreviationMap.containsKey(className)) {
                String clsName = "[" + abbreviationMap.get(className);
                cls = Class.forName(clsName, initialize, classLoader).getComponentType();
            } else {
                cls = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return cls;
        } catch (ClassNotFoundException ex) {
            int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
            if (lastDotIndex != -1) {
                try {
                    return getClass(classLoader, className.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR_CHAR + className.substring(lastDotIndex + 1), initialize);
                } catch (ClassNotFoundException ex2) {
                }
            }
            throw ex;
        }
    }

    public static Class<?> getClass(Class<?> caller, String name, boolean init) throws ClassNotFoundException {
        return getClass(ClassLoaderUtils.DEFAULT_IMPL.getClassLoader(caller), name, init);
    }

    /**
     * Returns the (initialized) class represented by <code>className</code>
     * using the <code>classLoader</code>. This implementation supports the
     * syntaxes "<code>java.util.Map.Entry[]</code>", "
     * <code>java.util.Map$Entry[]</code>", "<code>[Ljava.util.Map.Entry;</code>
     * ", and "<code>[Ljava.util.Map$Entry;</code>".
     * 
     * @param classLoader
     *            the class loader to use to load the class
     * @param className
     *            the class name
     * @return the class represented by <code>className</code> using the
     *         <code>classLoader</code>
     * @throws ClassNotFoundException
     *             if the class is not found
     */
    public static Class<?> getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }

    public static Class<?> getClass(Class<?> caller, String name) throws ClassNotFoundException {
        return getClass(ClassLoaderUtils.DEFAULT_IMPL.getClassLoader(caller), name, true);
    }

    /**
     * Converts a class name to a JLS style class name.
     * 
     * @param className
     *            the class name
     * @return the converted name
     */
    private static String toCanonicalName(String className) {
        if (className == null) {
            throw new NullPointerException("className must not be null.");
        } else if (className.endsWith("[]")) {
            StringBuilder classNameBuffer = new StringBuilder();
            while (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
                classNameBuffer.append("[");
            }
            String abbreviation = abbreviationMap.get(className);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append("L").append(className).append(";");
            }
            className = classNameBuffer.toString();
        }
        return className;
    }

    /**
     * <p>
     * Converts an array of <code>Object</code> in to an array of
     * <code>Class</code> objects. If any of these objects is null, a null
     * element will be inserted into the array.
     * </p>
     * 
     * <p>
     * This method returns <code>null</code> for a <code>null</code> input
     * array.
     * </p>
     * 
     * @param array
     *            an <code>Object</code> array
     * @return a <code>Class</code> array, <code>null</code> if null array input
     * @since 2.4
     */
    public static Class<?>[] toClass(Object[] array) {
        if (array == null) {
            return null;
        }
        Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }

    /**
     * <p>
     * Gets the canonical name minus the package name for an <code>Object</code>
     * .
     * </p>
     * 
     * @param object
     *            the class to get the short name for, may be null
     * @param valueIfNull
     *            the value to return if null
     * @return the canonical name of the object without the package name, or the
     *         null value
     * @since 2.4
     */
    public static String getShortCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortCanonicalName(object.getClass().getName());
    }

    /**
     * <p>
     * Gets the canonical name minus the package name from a <code>Class</code>.
     * </p>
     * 
     * @param cls
     *            the class to get the short name for.
     * @return the canonical name without the package name or an empty string
     * @since 2.4
     */
    public static String getShortCanonicalName(Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getShortCanonicalName(cls.getName());
    }

    /**
     * <p>
     * Gets the canonical name minus the package name from a String.
     * </p>
     * 
     * <p>
     * The string passed in is assumed to be a canonical name - it is not
     * checked.
     * </p>
     * 
     * @param canonicalName
     *            the class name to get the short name for
     * @return the canonical name of the class without the package name or an
     *         empty string
     * @since 2.4
     */
    public static String getShortCanonicalName(String canonicalName) {
        return ClassUtils.getShortClassName(getCanonicalName(canonicalName));
    }

    /**
     * <p>
     * Gets the package name from the canonical name of an <code>Object</code>.
     * </p>
     * 
     * @param object
     *            the class to get the package name for, may be null
     * @param valueIfNull
     *            the value to return if null
     * @return the package name of the object, or the null value
     * @since 2.4
     */
    public static String getPackageCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageCanonicalName(object.getClass().getName());
    }

    /**
     * <p>
     * Gets the package name from the canonical name of a <code>Class</code>.
     * </p>
     * 
     * @param cls
     *            the class to get the package name for, may be
     *            <code>null</code>.
     * @return the package name or an empty string
     * @since 2.4
     */
    public static String getPackageCanonicalName(Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getPackageCanonicalName(cls.getName());
    }

    /**
     * <p>
     * Gets the package name from the canonical name.
     * </p>
     * 
     * <p>
     * The string passed in is assumed to be a canonical name - it is not
     * checked.
     * </p>
     * <p>
     * If the class is unpackaged, return an empty string.
     * </p>
     * 
     * @param canonicalName
     *            the canonical name to get the package name for, may be
     *            <code>null</code>
     * @return the package name or an empty string
     * @since 2.4
     */
    public static String getPackageCanonicalName(String canonicalName) {
        return ClassUtils.getPackageName(getCanonicalName(canonicalName));
    }

    /**
     * <p>
     * Converts a given name of class into canonical format. If name of class is
     * not a name of array class it returns unchanged name.
     * </p>
     * <p>
     * Example:
     * <ul>
     * <li><code>getCanonicalName("[I") = "int[]"</code></li>
     * <li>
     * <code>getCanonicalName("[Ljava.lang.String;") = "java.lang.String[]"</code>
     * </li>
     * <li>
     * <code>getCanonicalName("java.lang.String") = "java.lang.String"</code></li>
     * </ul>
     * </p>
     * 
     * @param className
     *            the name of class
     * @return canonical form of class name
     * @since 2.4
     */
    private static String getCanonicalName(String className) {
        if (className == null) {
            return null;
        } else {
            int dim = 0;
            while (className.startsWith("[")) {
                dim++;
                className = className.substring(1);
            }
            if (dim < 1) {
                return className;
            } else {
                if (className.startsWith("L")) {
                    className = className.substring(1, className.endsWith(";") ? className.length() - 1 : className.length());
                } else {
                    if (className.length() > 0) {
                        className = reverseAbbreviationMap.get(className.substring(0, 1));
                    }
                }
                StringBuilder canonicalClassNameBuffer = new StringBuilder(className);
                for (int i = 0; i < dim; i++) {
                    canonicalClassNameBuffer.append("[]");
                }
                return canonicalClassNameBuffer.toString();
            }
        }
    }
}
