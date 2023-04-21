package com.taliasplayground.convert.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import com.taliasplayground.convert.Converter;
import com.taliasplayground.lang.Assert;
import com.taliasplayground.lang.ClassUtils;
import com.taliasplayground.lang.Pair;
import com.taliasplayground.lang.reflect.Invokable;
import com.taliasplayground.lang.reflect.MethodUtils;
import com.taliasplayground.lang.reflect.TypeUtils;

public abstract class ConvertMethodUtils {

    public static Pair<Invokable, Object[]> getMatchingAccessibleMethod(Class<?> targetClass, String methodName, boolean mustBeStatic, Object[] args, Class<?>[] explicitTypes, Converter converter) {
        Assert.notNullArg(targetClass, "'targetClass' may not be null");
        Assert.notNullArg(methodName, "'methodName' may not be null");
        if (args != null && args.length > 0) {
            Assert.notNullArg(converter, "'converter' may not be null if" + " arguments are supplied");
        }
        if (explicitTypes == null) {
            explicitTypes = new Class<?>[0];
        }
        Invokable invokable = null;
        Object[] convertArgs = null;
        boolean isDeprecated = true;
        ConvertDifference minConvertDiff = null;
        Invokable[] candidates = MethodUtils.getAccessibleMethods(targetClass, mustBeStatic);
        for (Invokable candidate : candidates) {
            if (!candidate.getName().equals(methodName)) {
                continue;
            }
            boolean candidateIsDeprecated = candidate.isAnnotationPresent(Deprecated.class);
            Class<?>[] candidateTypes = candidate.getParameterTypes();
            int index = 0;
            for (Class<?> type : explicitTypes) {
                if (type != null && (index >= candidateTypes.length || !type.equals(candidateTypes[index]))) {
                    break;
                }
                index++;
            }
            if (index < explicitTypes.length) {
                continue;
            }
            Type[] genericTypes = candidate.getGenericParameterTypes();
            for (int i = 0; i < genericTypes.length; i++) {
                Type genericType = genericTypes[i];
                while (genericType instanceof TypeVariable<?>) {
                    genericType = TypeUtils.getImplicitBounds((TypeVariable<?>) genericType)[0];
                }
                genericTypes[i] = genericType;
            }
            if (index == explicitTypes.length && (!candidateIsDeprecated || invokable == null || isDeprecated)) {
                Pair<Object[], ConvertDifference> pair = ConvertTypeUtils.getConvertDifferencesOfObjects(args, genericTypes, candidate.isVarArgs(), converter, minConvertDiff);
                if (pair != null) {
                    minConvertDiff = pair.get2();
                    invokable = candidate;
                    convertArgs = pair.get1();
                    isDeprecated = candidateIsDeprecated;
                }
            }
        }
        if (minConvertDiff == null) {
            return null;
        }
        return new Pair<Invokable, Object[]>(invokable, convertArgs);
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @see #getInvokableAndPreparedArguments(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(Object object, String methodName, Object[] args, Class<?>[] explicitTypes, Converter converter) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Pair<Invokable, Object[]> pair = getMatchingAccessibleMethod(object.getClass(), methodName, false, args, explicitTypes, converter);
        if (pair == null) {
            throw new NoSuchMethodException("Could not find method named '" + methodName + "' in " + object.getClass() + " to support" + " the following arguments:  " + Arrays.deepToString(args) + "; of the following types:  " + Arrays.deepToString(explicitTypes));
        }
        try {
            return pair.get1().invoke(object, pair.get2());
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @see #getInvokableAndPreparedArguments(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(Class<?> targetClass, String methodName, Object[] args, Class<?>[] explicitTypes, Converter converter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Pair<Invokable, Object[]> pair = getMatchingAccessibleMethod(targetClass, methodName, true, args, explicitTypes, converter);
        if (pair == null) {
            StringBuilder strBuf = new StringBuilder("Could not find static" + " method named '").append(methodName).append("' in ").append(targetClass).append(" to support the following" + " arguments:  ").append(Arrays.deepToString(args)).append("; of the following types:  ").append(Arrays.deepToString(explicitTypes));
            throw new NoSuchMethodException(strBuf.toString());
        }
        try {
            return pair.get1().invoke(null, pair.get2());
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @see #getInvokableAndPreparedArguments(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(String qualifiedStaticMethodName, Object[] args, Class<?>[] explicitTypes, Converter converter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        int lastDotIndex = qualifiedStaticMethodName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == qualifiedStaticMethodName.length()) {
            throw new IllegalArgumentException("'qualifiedStaticMethodName'" + " must be a fully qualified class plus method name: e.g." + " 'example.MyExampleClass.myExampleMethod'");
        }
        return invokeMatchingAccessibleMethod(ClassUtils.getClass(ConvertMethodUtils.class, qualifiedStaticMethodName.substring(0, lastDotIndex)), qualifiedStaticMethodName.substring(lastDotIndex + 1), args, explicitTypes, converter);
    }
}
