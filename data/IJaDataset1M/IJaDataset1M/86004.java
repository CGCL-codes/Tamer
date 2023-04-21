package org.nox.util.lang;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@SuppressWarnings("unchecked")
public class MethodFinder {

    private static WeakHashMap<MethodFinder, Method> cache = new WeakHashMap<MethodFinder, Method>();

    public static Method find(Class clazz, String methodName, Class[] parameterTypes, boolean staticOnly) throws SecurityException, NoSuchMethodException {
        MethodFinder mf = new MethodFinder(clazz, methodName, parameterTypes).withStaticOnly(staticOnly);
        Method method = cache.get(mf);
        if (method == null) {
            method = mf.find();
            cache.put(mf, method);
        }
        return method;
    }

    private Class clazz;

    private String methodName;

    private Class[] parameterTypes;

    private boolean staticOnly;

    public MethodFinder(Class clazz, String methodName, Class[] parameterTypes) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    public MethodFinder withStaticOnly(boolean staticOnly) {
        this.staticOnly = staticOnly;
        return this;
    }

    private int hash = -1;

    public int hashCode() {
        if (hash == -1) {
            hash = methodName.hashCode();
            hash = hash + 17 * clazz.hashCode();
            if (false) for (int i = 0; i < parameterTypes.length; i++) hash = hash + 17 * parameterTypes[i].hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MethodFinder)) {
            return false;
        }
        MethodFinder mf = (MethodFinder) obj;
        return (methodName.equals(mf.methodName) && clazz.equals(mf.clazz) && staticOnly == mf.staticOnly && java.util.Arrays.equals(parameterTypes, mf.parameterTypes));
    }

    public Method find() throws SecurityException, NoSuchMethodException {
        Method found = null;
        try {
            found = findDirect();
        } catch (SecurityException e) {
            found = findMatching();
        } catch (NoSuchMethodException e) {
            found = findMatching();
        }
        return found;
    }

    public Method findDirect() throws SecurityException, NoSuchMethodException {
        return clazz.getDeclaredMethod(methodName, parameterTypes);
    }

    public Method findMatching() throws SecurityException, NoSuchMethodException {
        final int paramSize = parameterTypes.length;
        final Method[] bestMatchingMethod = new Method[1];
        final int[] bestMatchingWeight = new int[] { Integer.MAX_VALUE };
        parseMethods(clazz, new MethodVisitor() {

            public void visit(Method method) throws IllegalArgumentException, IllegalAccessException {
                if (!method.getName().equals(methodName)) return;
                Class[] methodsParams = method.getParameterTypes();
                int methodParamSize = methodsParams.length;
                if (methodParamSize != paramSize) return;
                if (staticOnly && !Modifier.isStatic(method.getModifiers())) return;
                int weight = computeWeight(methodsParams, parameterTypes);
                if (weight < bestMatchingWeight[0]) {
                    bestMatchingWeight[0] = weight;
                    bestMatchingMethod[0] = method;
                }
            }
        });
        return bestMatchingMethod[0];
    }

    interface MethodVisitor {

        public void visit(Method method) throws IllegalArgumentException, IllegalAccessException;
    }

    public static void parseMethods(Class targetClass, MethodVisitor mc) throws IllegalArgumentException {
        do {
            Method[] methods = targetClass.getDeclaredMethods();
            for (Method method : methods) {
                try {
                    mc.visit(method);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Shouldn't be illegal to access method '" + method.getName() + "': " + ex);
                }
            }
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null);
    }

    public static int computeWeight(Class[] paramsTypes, Class[] argParams) {
        int result = 0;
        for (int i = 0; i < paramsTypes.length; i++) {
            if (!isAssignmentCompatible(paramsTypes[i], argParams[i])) {
                return Integer.MAX_VALUE;
            }
            if (argParams[i] != null) {
                Class paramType = paramsTypes[i];
                Class superClass = argParams[i].getSuperclass();
                while (superClass != null) {
                    if (paramType.equals(superClass)) {
                        result = result + 2;
                        superClass = null;
                    } else if (isAssignmentCompatible(paramType, superClass)) {
                        result = result + 2;
                        superClass = superClass.getSuperclass();
                    } else {
                        superClass = null;
                    }
                }
                if (paramType.isInterface()) {
                    result = result + 1;
                }
            }
        }
        return result;
    }

    public static final boolean isAssignmentCompatible(Class parameterType, Class parameterization) {
        if (parameterization == null) return !parameterType.isPrimitive();
        if (parameterType.isAssignableFrom(parameterization)) {
            return true;
        }
        if (parameterType.isPrimitive()) {
            Class parameterWrapperClazz = primitiveWrappers.get(parameterType);
            if (parameterWrapperClazz != null) {
                return parameterWrapperClazz.equals(parameterization);
            }
        }
        return false;
    }

    static Map<Class, Class> primitiveWrappers = createPrimitiveWrappers();

    private static Map<Class, Class> createPrimitiveWrappers() {
        Map<Class, Class> primitiveWrappers = new HashMap<Class, Class>();
        primitiveWrappers.put(boolean.class, Boolean.class);
        primitiveWrappers.put(float.class, Float.class);
        primitiveWrappers.put(long.class, Long.class);
        primitiveWrappers.put(int.class, Integer.class);
        primitiveWrappers.put(short.class, Short.class);
        primitiveWrappers.put(byte.class, Byte.class);
        primitiveWrappers.put(short.class, Short.class);
        primitiveWrappers.put(double.class, Double.class);
        primitiveWrappers.put(char.class, Character.class);
        return primitiveWrappers;
    }
}
