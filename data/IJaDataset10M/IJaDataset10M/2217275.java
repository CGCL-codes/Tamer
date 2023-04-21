package org.bbop.expression.util.introspection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Map;
import org.apache.log4j.*;

public class ClassMap {

    protected static final Logger logger = Logger.getLogger(ClassMap.class);

    /** represents a miss on the cached data. */
    private static final class CacheMiss {
    }

    /** constant for a miss on the cached data. */
    private static final CacheMiss CACHE_MISS = new CacheMiss();

    /** represents null or missing arguments. */
    private static final Object OBJECT = new Object();

    /**
     * Class passed into the constructor used to as the basis for the Method
     * map.
     */
    private Class clazz;

    /**
     * Cache of Methods, or CACHE_MISS, keyed by method name and actual
     * arguments used to find it.
     */
    private final Map methodCache = new Hashtable();

    /** map from method name and args to a {@link Method}. */
    private final MethodMap methodMap = new MethodMap();

    /**
     * Standard constructor.
     * @param aClass the class to deconstruct. 
     */
    public ClassMap(Class aClass) {
        clazz = aClass;
        populateMethodCache();
    }

    /**
     * @return the class object whose methods are cached by this map.
     */
    Class getCachedClass() {
        return clazz;
    }

    /**
     * Find a Method using the methodKey provided.
     * 
     * Look in the methodMap for an entry. If found, it'll either be a
     * CACHE_MISS, in which case we simply give up, or it'll be a Method, in
     * which case, we return it.
     * 
     * If nothing is found, then we must actually go and introspect the method
     * from the MethodMap.
     * 
     * @param name method name
     * @param params method parameters
     * @return CACHE_MISS or a {@link Method}
     * @throws MethodMap.AmbiguousException if the method and parameters are ambiguous.
     */
    public Method findMethod(String name, Object[] params) throws MethodMap.AmbiguousException {
        String methodKey = makeMethodKey(name, params);
        Object cacheEntry = methodCache.get(methodKey);
        if (cacheEntry == CACHE_MISS) {
            return null;
        }
        if (cacheEntry == null) {
            try {
                cacheEntry = methodMap.find(name, params);
            } catch (MethodMap.AmbiguousException ae) {
                methodCache.put(methodKey, CACHE_MISS);
                throw ae;
            }
            if (cacheEntry == null) {
                methodCache.put(methodKey, CACHE_MISS);
            } else {
                methodCache.put(methodKey, cacheEntry);
            }
        }
        return (Method) cacheEntry;
    }

    /**
     * Populate the Map of direct hits. These are taken from all the public
     * methods that our class provides.
     */
    private void populateMethodCache() {
        Method[] methods = getAccessibleMethods(clazz);
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            Method publicMethod = getPublicMethod(method);
            if (publicMethod != null) {
                methodMap.add(publicMethod);
                methodCache.put(makeMethodKey(publicMethod), publicMethod);
            }
        }
    }

    /**
     * Make a methodKey for the given method using the concatenation of the name
     * and the types of the method parameters.
     */
    private String makeMethodKey(Method method) {
        Class[] parameterTypes = method.getParameterTypes();
        StringBuffer methodKey = new StringBuffer(method.getName());
        for (int j = 0; j < parameterTypes.length; j++) {
            if (parameterTypes[j].isPrimitive()) {
                if (parameterTypes[j].equals(Boolean.TYPE)) methodKey.append("java.lang.Boolean"); else if (parameterTypes[j].equals(Byte.TYPE)) methodKey.append("java.lang.Byte"); else if (parameterTypes[j].equals(Character.TYPE)) methodKey.append("java.lang.Character"); else if (parameterTypes[j].equals(Double.TYPE)) methodKey.append("java.lang.Double"); else if (parameterTypes[j].equals(Float.TYPE)) methodKey.append("java.lang.Float"); else if (parameterTypes[j].equals(Integer.TYPE)) methodKey.append("java.lang.Integer"); else if (parameterTypes[j].equals(Long.TYPE)) methodKey.append("java.lang.Long"); else if (parameterTypes[j].equals(Short.TYPE)) methodKey.append("java.lang.Short");
            } else {
                methodKey.append(parameterTypes[j].getName());
            }
        }
        return methodKey.toString();
    }

    private static String makeMethodKey(String method, Object[] params) {
        StringBuffer methodKey = new StringBuffer().append(method);
        for (int j = 0; j < params.length; j++) {
            Object arg = params[j];
            if (arg == null) {
                arg = OBJECT;
            }
            methodKey.append(arg.getClass().getName());
        }
        return methodKey.toString();
    }

    /**
     * Retrieves public methods for a class. In case the class is not public,
     * retrieves methods with same signature as its public methods from public
     * superclasses and interfaces (if they exist). Basically upcasts every
     * method to the nearest acccessible method.
     */
    private static Method[] getAccessibleMethods(Class clazz) {
        Method[] methods = clazz.getMethods();
        if (Modifier.isPublic(clazz.getModifiers())) {
            return methods;
        }
        MethodInfo[] methodInfos = new MethodInfo[methods.length];
        for (int i = methods.length; i-- > 0; ) {
            methodInfos[i] = new MethodInfo(methods[i]);
        }
        int upcastCount = getAccessibleMethods(clazz, methodInfos, 0);
        if (upcastCount < methods.length) {
            methods = new Method[upcastCount];
        }
        int j = 0;
        for (int i = 0; i < methodInfos.length; ++i) {
            MethodInfo methodInfo = methodInfos[i];
            if (methodInfo.upcast) {
                methods[j++] = methodInfo.method;
            }
        }
        return methods;
    }

    /**
     * Recursively finds a match for each method, starting with the class, and
     * then searching the superclass and interfaces.
     * 
     * @param clazz Class to check
     * @param methodInfos array of methods we are searching to match
     * @param upcastCount current number of methods we have matched
     * @return count of matched methods
     */
    private static int getAccessibleMethods(Class clazz, MethodInfo[] methodInfos, int upcastCount) {
        int l = methodInfos.length;
        if (Modifier.isPublic(clazz.getModifiers())) {
            for (int i = 0; i < l && upcastCount < l; ++i) {
                try {
                    MethodInfo methodInfo = methodInfos[i];
                    if (!methodInfo.upcast) {
                        methodInfo.tryUpcasting(clazz);
                        upcastCount++;
                    }
                } catch (NoSuchMethodException e) {
                }
            }
            if (upcastCount == l) {
                return upcastCount;
            }
        }
        Class superclazz = clazz.getSuperclass();
        if (superclazz != null) {
            upcastCount = getAccessibleMethods(superclazz, methodInfos, upcastCount);
            if (upcastCount == l) {
                return upcastCount;
            }
        }
        Class[] interfaces = clazz.getInterfaces();
        for (int i = interfaces.length; i-- > 0; ) {
            upcastCount = getAccessibleMethods(interfaces[i], methodInfos, upcastCount);
            if (upcastCount == l) {
                return upcastCount;
            }
        }
        return upcastCount;
    }

    /**
     * For a given method, retrieves its publicly accessible counterpart. This
     * method will look for a method with same name and signature declared in a
     * public superclass or implemented interface of this method's declaring
     * class. This counterpart method is publicly callable.
     * 
     * @param method a method whose publicly callable counterpart is requested.
     * @return the publicly callable counterpart method. Note that if the
     *         parameter method is itself declared by a public class, this
     *         method is an identity function.
     */
    public static Method getPublicMethod(Method method) {
        Class clazz = method.getDeclaringClass();
        if ((clazz.getModifiers() & Modifier.PUBLIC) != 0) {
            return method;
        }
        return getPublicMethod(clazz, method.getName(), method.getParameterTypes());
    }

    /**
     * Looks up the method with specified name and signature in the first public
     * superclass or implemented interface of the class.
     * 
     * @param class the class whose method is sought
     * @param name the name of the method
     * @param paramTypes the classes of method parameters
     */
    private static Method getPublicMethod(Class clazz, String name, Class[] paramTypes) {
        if ((clazz.getModifiers() & Modifier.PUBLIC) != 0) {
            try {
                return clazz.getMethod(name, paramTypes);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        Class superclazz = clazz.getSuperclass();
        if (superclazz != null) {
            Method superclazzMethod = getPublicMethod(superclazz, name, paramTypes);
            if (superclazzMethod != null) {
                return superclazzMethod;
            }
        }
        Class[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            Method interfaceMethod = getPublicMethod(interfaces[i], name, paramTypes);
            if (interfaceMethod != null) {
                return interfaceMethod;
            }
        }
        return null;
    }

    /**
     * Used for the iterative discovery process for public methods.
     */
    private static final class MethodInfo {

        Method method;

        String name;

        Class[] parameterTypes;

        boolean upcast;

        MethodInfo(Method method) {
            this.method = null;
            name = method.getName();
            parameterTypes = method.getParameterTypes();
            upcast = false;
        }

        void tryUpcasting(Class clazz) throws NoSuchMethodException {
            method = clazz.getMethod(name, parameterTypes);
            name = null;
            parameterTypes = null;
            upcast = true;
        }
    }
}
