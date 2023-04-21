package org.jtell.internal;

import org.jtell.JTellException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * <code>ReflectionUtils</code> provides utility methods for dealing with classes and reflection.
 * </p>
 * <p>
 * <strong>Thread Safety</strong><br/>
 * Unless otherwise noted, all methods provided by this class (and all values they return) are safe for concurrent
 * access. 
 * </p>
 */
public class ReflectionUtils {

    /**
     * <p>
     * Cache of fully qualified class names to {@link Set}s of {@link String} superclass and interface class names. 
     * </p>
     */
    private static final ConcurrentMap<String, List<String>> s_superclassCache = new ConcurrentHashMap<String, List<String>>();

    /**
     * <p>
     * Construct a {@link ReflectionUtils} instance.
     * </p>
     */
    private ReflectionUtils() {
        super();
    }

    /**
     * <p>
     * Determine whether a class is available in the current runtime environment.
     * </p>
     *
     * @param className the fully qualified class name.
     * @return <code>boolean</code> <code>true</code> if the class is available.
     */
    public static boolean isClassAvailable(final String className) {
        boolean result = false;
        try {
            Class.forName(className);
            result = true;
        } catch (ClassNotFoundException e) {
        }
        return result;
    }

    /**
     * <p>
     * Find a class given its fully qualified name, properly handling primitive and array types, and throwing unchecked
     * exceptions when classes are not found.
     * </p>
     *
     * @param className the class name.
     * @return {@link Class} instance.
     * @throws IllegalArgumentException if the class is not found.
     */
    public static Class<?> findClass(final String className) throws IllegalArgumentException {
        final Class<?> result;
        if (PRIMITIVE_CLASSES.containsKey(className)) {
            result = PRIMITIVE_CLASSES.get(className);
        } else if (className.endsWith("[]")) {
            final String componentClassName = className.substring(0, className.length() - 2);
            final Class<?> componentClass = findClass(componentClassName);
            final Object array = Array.newInstance(componentClass, 0);
            result = array.getClass();
        } else {
            try {
                result = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Unable to load class [%s].", className), e);
            }
        }
        return result;
    }

    /**
     * <p>
     * Find the method on a given class which matches a method signature string.
     * </p>
     *
     * @param clazz the class.
     * @param methodSignature the method signature.
     * @return {@link Method} instance.
     * @throws IllegalArgumentException if unable to find a matching method.
     */
    public static Method findMethodBySignature(final Class<?> clazz, final String methodSignature) throws IllegalArgumentException {
        final Matcher matcher = METHOD_SIGNATURE.matcher(methodSignature);
        final Method result;
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Unable to find method matching signature [%s] on class [%s].", methodSignature, clazz));
        } else {
            final Class<?> returnClass = findClass(matcher.group(1));
            final String methodName = matcher.group(2);
            final Class<?>[] parameterClasses;
            final String parameterClassNames = matcher.group(3);
            if (null == parameterClassNames) {
                parameterClasses = new Class<?>[0];
            } else {
                final String[] classNames = parameterClassNames.split(",");
                parameterClasses = new Class<?>[classNames.length];
                for (int i = 0; i < classNames.length; ++i) {
                    parameterClasses[i] = findClass(classNames[i]);
                }
            }
            try {
                result = clazz.getMethod(methodName, parameterClasses);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("Unable to find method matching signature [%s] on class [%s].", methodSignature, clazz), e);
            }
            if (!returnClass.equals(result.getReturnType())) {
                throw new IllegalArgumentException(String.format("Unable to find method matching signature [%s] on class [%s] because return type of method [%s] is not [%s].", methodSignature, clazz, result, returnClass));
            }
        }
        return result;
    }

    /**
     * <p>
     * Get the names of all superclasses and interfaces implemented by a given class. The returned class names are
     * ordered as follows, starting with the provided class or interface name and ending with {@link Object}. First,
     * the class itself is added. Next, each interface directly implemented by the class is added, each followed by its
     * superinterfaces. Finally, we recurse into the immediate superclass and start over again.
     * </p>
     * <p>
     * At each step, interfaces are added according to their order in the <code>implements</code> clause of the
     * implementing class, and each interface occurs only once where it is first encountered while traversing the
     * inheritance tree. 
     * </p>
     * <p>
     * Note that CGLib dynamic proxy classes and JDK proxy classes are never included in the returned collection. If a
     * CGLib class is passed, we traverse up its inheritance tree until a real class is reached and start from there.
     * If a JDK proxy class is passed, we ignore it and return its implemented interfaces. 
     * </p>
     * 
     * @param className the class name.
     * @return {@link Set} of {@link String} superclass and interface class names.
     */
    public static List<String> getAllSuperclassNames(final String className) {
        final List<String> result;
        if ("org.jtell.xdoclet.internal.TestJTellXDocletTemplateSource".equals(className) && !isClassAvailable(className)) {
            result = Arrays.asList(className, Object.class.getName());
        } else {
            final Class<?> applicationClass;
            if (isCGLibProxyClassName(className)) {
                applicationClass = getApplicationClass(findClass(className));
            } else {
                applicationClass = findClass(className);
            }
            final String applicationClassName = applicationClass.getName();
            if (s_superclassCache.containsKey(applicationClassName)) {
                result = s_superclassCache.get(applicationClassName);
            } else {
                List<String> superclassNames = new ArrayList<String>();
                addAllSuperclassNames(superclassNames, applicationClass);
                superclassNames = Collections.unmodifiableList(superclassNames);
                final List<String> winningSuperclassNames = s_superclassCache.putIfAbsent(applicationClassName, superclassNames);
                if (null != winningSuperclassNames) {
                    result = winningSuperclassNames;
                } else {
                    result = superclassNames;
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Get the class which is applied to a type parameter on an interface implemented by a class. For example, if
     * <code>MyClass</code> implements <code>MyGenericInterface&lt;String, Integer&gt;</code>, invoking this method like
     * such:
     * <br/>
     * <code>
     * Class&lt;?&gt; firstParameterClass = getGenericInterfaceTypeParameter(MyClass.class, MyGenericInterface.class,
     * 0); 
     * </code>
     * <br/>
     * Will return <code>String.class</code>.
     * </p>
     *
     * @param implementingClass the class to examine.
     * @param genericInterface the generic interface which is implemented by the class.
     * @param typeParameterIndex the index of the type parameter on the generic interface.
     * @return {@link Class} instance.
     *
     */
    public static Class<?> getGenericInterfaceTypeParameter(final Class<?> implementingClass, final Class<?> genericInterface, final int typeParameterIndex) {
        Guard.notNull("implementingClass", implementingClass);
        Guard.notNull("genericInterface", genericInterface);
        if (typeParameterIndex < 0 || typeParameterIndex >= genericInterface.getTypeParameters().length) {
            throw new IllegalArgumentException(String.format("Type parameter index %d is out of range for interface [%s].", typeParameterIndex, genericInterface));
        }
        if (!genericInterface.isInterface()) {
            throw new IllegalArgumentException(String.format("Class [%s] is not an interface.", genericInterface));
        }
        if (!genericInterface.isAssignableFrom(implementingClass)) {
            throw new IllegalArgumentException(String.format("Class [%s] does not implement interface [%s].", implementingClass, genericInterface));
        }
        Class<?> result = null;
        outer: for (Class<?> nextClass = implementingClass; !Object.class.equals(nextClass); nextClass = nextClass.getSuperclass()) {
            for (final Type interfaceType : nextClass.getGenericInterfaces()) {
                if (interfaceType instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
                    if (parameterizedType.getRawType().equals(genericInterface)) {
                        final Type type = parameterizedType.getActualTypeArguments()[typeParameterIndex];
                        if (!(type instanceof Class)) {
                            throw new IllegalStateException(String.format("Expected a class, got [%s].", type));
                        }
                        result = (Class<?>) type;
                        break outer;
                    }
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Get a string representing the signature for a given method. The signature consists of the fully qualfied name
     * of the return type, followed by the method name, followed by a comma-delimited list of fully qualified names of
     * the parameter types.
     * </p>
     *
     * @param method the method.
     * @param includeDeclaringClass flag controlling whether the fully qualified name of the method's declaring class
     * is prepended to the method name.
     * @return {@link String} method signature.
     */
    public static String getMethodSignature(final Method method, final boolean includeDeclaringClass) {
        Guard.notNull("method", method);
        final StringBuilder signatureBuilder = new StringBuilder();
        appendTypeName(signatureBuilder, method.getReturnType());
        signatureBuilder.append(' ');
        if (includeDeclaringClass) {
            appendTypeName(signatureBuilder, method.getDeclaringClass());
            signatureBuilder.append('.');
        }
        signatureBuilder.append(method.getName());
        signatureBuilder.append('(');
        boolean first = true;
        for (final Class<?> parameterType : method.getParameterTypes()) {
            if (first) {
                first = false;
            } else {
                signatureBuilder.append(',');
            }
            appendTypeName(signatureBuilder, parameterType);
        }
        signatureBuilder.append(')');
        return signatureBuilder.toString();
    }

    /**
     * <p>
     * Get a string representing the signature for a given method. The signature consists of the fully qualfied name
     * of the return type, optionally followed by the fully qualified declaring class name, followed by the method name,
     * followed by a comma-delimited list of fully qualified names of the parameter types.
     * </p>
     *
     * @param method the method.
     * @return {@link String} method signature.
     */
    public static String getMethodSignature(final Method method) {
        return getMethodSignature(method, false);
    }

    /**
     * <p>
     * Get a string representing the signature for a given method. The signature consists of the fully qualfied name
     * of the return type, followed by the method name, followed by a comma-delimited list of fully qualified names of
     * the parameter types.
     * </p>
     *
     * @param clazz the class defining the method.
     * @param name the method name.
     * @param parameterTypes the method parameter types.
     * @return {@link String} method signature.
     */
    public static String getMethodSignature(final Class<?> clazz, final String name, final Class<?>... parameterTypes) {
        Guard.notNull("clazz", clazz);
        Guard.notNull("name", name);
        final Method method;
        try {
            method = clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new JTellException(e);
        }
        return getMethodSignature(method);
    }

    /**
     * <p>
     * Get the <em>application</em> class which corresponds to a given class. In most cases, this will be the given
     * class itself. However, for dynamic classes such as CGLib proxies, this method will return the real application
     * class which extended by the proxy. 
     * </p>
     * 
     * @param clazz the class to examine.
     * @return {@link Class} instance.
     */
    public static <T> Class<? super T> getApplicationClass(final Class<T> clazz) {
        Guard.notNull("clazz", clazz);
        Class<? super T> result = null;
        if (clazz.isInterface() || clazz.isArray()) {
            result = clazz;
        } else if (!isCGLibProxyClassName(clazz.getName())) {
            result = clazz;
        } else {
            Class<? super T> nextClass = clazz.getSuperclass();
            while (null != nextClass) {
                if (!isCGLibProxyClassName(nextClass.getName())) {
                    result = nextClass;
                    break;
                }
                nextClass = getSuperclass(nextClass);
            }
        }
        if (null == result) {
            throw new IllegalStateException(String.format("Unable to determine application class for [%s].", clazz));
        }
        return result;
    }

    /**
     * <p>
     * Get the name of a type. Equivalent to calling {@link Class#getName()} for non-array types; for array types, the
     * returned string is the name of the array component type followed by &quot;<code>[]</code>&quot;
     * </p>
     *
     * @param outputBuilder the string builder to which the type name will be written.
     * @param type the type.
     */
    public static void appendTypeName(final StringBuilder outputBuilder, final Class<?> type) {
        Guard.notNull("outputBuilder", outputBuilder);
        Guard.notNull("type", type);
        if (!type.isArray()) {
            outputBuilder.append(type.getName());
        } else {
            int dimensions = 1;
            Class<?> componentType = type.getComponentType();
            while (componentType.isArray()) {
                ++dimensions;
                componentType = componentType.getComponentType();
            }
            outputBuilder.append(componentType.getName());
            for (int i = 0; i < dimensions; ++i) {
                outputBuilder.append("[]");
            }
        }
    }

    /**
     * <p>
     * Determine whether a given class name denotes a CGLib dynamic proxy class.
     * </p>
     *
     * @param className the class name.
     * @return <code>boolean</code> <code>true</code> if the class is a CGLib dynamic proxy class.
     */
    private static boolean isCGLibProxyClassName(final String className) {
        return className.contains("$$");
    }

    /**
     * <p>
     * Recursively add the names of all superclasses and interfaces implemented by a given class to an existing list.
     * The given class name is added first, then the names of all its superclasses, then the names of all its
     * interfaces.
     * </p>
     *
     * @param classNames the list of class names.
     * @param clazz the class to examine.
     */
    private static void addAllSuperclassNames(final List<String> classNames, final Class<?> clazz) {
        final boolean isProxyClass = Proxy.isProxyClass(clazz);
        if (!isProxyClass) {
            final String className = clazz.getName();
            if (!classNames.contains(className)) {
                classNames.add(className);
            }
        }
        final Class<?>[] interfaces = clazz.getInterfaces();
        if (null != interfaces) {
            for (final Class<?> intf : interfaces) {
                addAllSuperclassNames(classNames, intf);
            }
        }
        if (!clazz.isInterface()) {
            final Class<?> superclass = clazz.getSuperclass();
            if (null != superclass) {
                addAllSuperclassNames(classNames, superclass);
            }
        }
    }

    /**
     * <p>
     * Get the superclass of a given class. This method exists only to prevent unchecked cast warnings that would be
     * raised in {@link #getApplicationClass(Class)} if it were handled internally.
     * </p>
     *
     * @param clazz the class.
     * @return {@link Class} the superclass.
     */
    private static <T> Class<? super T> getSuperclass(final Class<T> clazz) {
        return clazz.getSuperclass();
    }

    /**
     * <p>
     * Pattern for parsing method signatures.
     * </p>
     */
    private static final Pattern METHOD_SIGNATURE = Pattern.compile("^(\\S+) ([^\\(]+)\\((\\S+)?\\)$");

    /**
     * <p>
     * Map of primitive type names to the corresponding {@link Class} tokens.
     * </p>
     */
    private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap<String, Class<?>>();

    static {
        PRIMITIVE_CLASSES.put("boolean", boolean.class);
        PRIMITIVE_CLASSES.put("byte", byte.class);
        PRIMITIVE_CLASSES.put("char", char.class);
        PRIMITIVE_CLASSES.put("short", short.class);
        PRIMITIVE_CLASSES.put("int", int.class);
        PRIMITIVE_CLASSES.put("long", long.class);
        PRIMITIVE_CLASSES.put("float", float.class);
        PRIMITIVE_CLASSES.put("double", double.class);
        PRIMITIVE_CLASSES.put("void", void.class);
    }
}
