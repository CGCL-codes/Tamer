package org.enerj.apache.commons.beanutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * <p> Utility reflection methods focussed on constructors, modelled after {@link MethodUtils}. </p>
 *
 * <h3>Known Limitations</h3>
 * <h4>Accessing Public Constructors In A Default Access Superclass</h4>
 * <p>There is an issue when invoking public constructors contained in a default access superclass.
 * Reflection locates these constructors fine and correctly assigns them as public.
 * However, an <code>IllegalAccessException</code> is thrown if the constructors is invoked.</p>
 *
 * <p><code>ConstructorUtils</code> contains a workaround for this situation.
 * It will attempt to call <code>setAccessible</code> on this constructor.
 * If this call succeeds, then the method can be invoked as normal.
 * This call will only succeed when the application has sufficient security privilages.
 * If this call fails then a warning will be logged and the method may fail.</p>
 *
 * @author Craig R. McClanahan
 * @author Ralph Schaer
 * @author Chris Audley
 * @author Rey Fran�ois
 * @author Gregor Ra�man
 * @author Jan Sorensen
 * @author Robert Burrell Donkin
 * @author Rodney Waldhoff
 * @version $Revision: 1.7 $ $Date: 2004/02/28 13:18:33 $
 */
public class ConstructorUtils {

    /** An empty class array */
    private static final Class[] emptyClassArray = new Class[0];

    /** An empty object array */
    private static final Object[] emptyObjectArray = new Object[0];

    /**
     * <p>Convenience method returning new instance of <code>klazz</code> using a single argument constructor.
     * The formal parameter type is inferred from the actual values of <code>arg</code>.
     * See {@link #invokeExactConstructor(Class, Object[], Class[])} for more details.</p>
     *
     * <p>The signatures should be assignment compatible.</p>
     *
     * @param klass the class to be constructed.
     * @param arg the actual argument
     * @return new instance of <code>klazz</code>
     *
     * @see #invokeConstructor(java.lang.Class, java.lang.Object[], java.lang.Class[])
     */
    public static Object invokeConstructor(Class klass, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = { arg };
        return invokeConstructor(klass, args);
    }

    /**
     * <p>Returns new instance of <code>klazz</code> created using the actual arguments <code>args</code>.
     * The formal parameter types are inferred from the actual values of <code>args</code>.
     * See {@link #invokeExactConstructor(Class, Object[], Class[])} for more details.</p>
     *
     * <p>The signatures should be assignment compatible.</p>
     *
     * @param klass the class to be constructed.
     * @param args actual argument array
     * @return new instance of <code>klazz</code>
     *
     * @see #invokeConstructor(java.lang.Class, java.lang.Object[], java.lang.Class[])
     */
    public static Object invokeConstructor(Class klass, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (null == args) {
            args = emptyObjectArray;
        }
        int arguments = args.length;
        Class parameterTypes[] = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeConstructor(klass, args, parameterTypes);
    }

    /**
     * <p>Returns new instance of <code>klazz</code> created using constructor
     * with signature <code>parameterTypes</code> and actual arguments <code>args</code>.</p>
     *
     * <p>The signatures should be assignment compatible.</p>
     *
     * @param klass the class to be constructed.
     * @param args actual argument array
     * @param parameterTypes parameter types array
     * @return new instance of <code>klazz</code>
     *
     * @throws NoSuchMethodException if matching constructor cannot be found
     * @throws IllegalAccessException thrown on the constructor's invocation
     * @throws InvocationTargetException thrown on the constructor's invocation
     * @throws InstantiationException thrown on the constructor's invocation
     * @see Constructor#newInstance
     */
    public static Object invokeConstructor(Class klass, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (parameterTypes == null) {
            parameterTypes = emptyClassArray;
        }
        if (args == null) {
            args = emptyObjectArray;
        }
        Constructor ctor = getMatchingAccessibleConstructor(klass, parameterTypes);
        if (null == ctor) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + klass.getName());
        }
        return ctor.newInstance(args);
    }

    /**
     * <p>Convenience method returning new instance of <code>klazz</code> using a single argument constructor.
     * The formal parameter type is inferred from the actual values of <code>arg</code>.
     * See {@link #invokeExactConstructor(Class, Object[], Class[])} for more details.</p>
     *
     * <p>The signatures should match exactly.</p>
     *
     * @param klass the class to be constructed.
     * @param arg the actual argument
     * @return new instance of <code>klazz</code>
     *
     * @see #invokeExactConstructor(java.lang.Class, java.lang.Object[], java.lang.Class[])
     */
    public static Object invokeExactConstructor(Class klass, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = { arg };
        return invokeExactConstructor(klass, args);
    }

    /**
     * <p>Returns new instance of <code>klazz</code> created using the actual arguments <code>args</code>.
     * The formal parameter types are inferred from the actual values of <code>args</code>.
     * See {@link #invokeExactConstructor(Class, Object[], Class[])} for more details.</p>
     *
     * <p>The signatures should match exactly.</p>
     *
     * @param klass the class to be constructed.
     * @param args actual argument array
     * @return new instance of <code>klazz</code>
     *
     * @see #invokeExactConstructor(java.lang.Class, java.lang.Object[], java.lang.Class[])
     */
    public static Object invokeExactConstructor(Class klass, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (null == args) {
            args = emptyObjectArray;
        }
        int arguments = args.length;
        Class parameterTypes[] = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactConstructor(klass, args, parameterTypes);
    }

    /**
     * <p>Returns new instance of <code>klazz</code> created using constructor
     * with signature <code>parameterTypes</code> and actual arguments
     * <code>args</code>.</p>
     *
     * <p>The signatures should match exactly.</p>
     *
     * @param klass the class to be constructed.
     * @param args actual argument array
     * @param parameterTypes parameter types array
     * @return new instance of <code>klazz</code>
     *
     * @throws NoSuchMethodException if matching constructor cannot be found
     * @throws IllegalAccessException thrown on the constructor's invocation
     * @throws InvocationTargetException thrown on the constructor's invocation
     * @throws InstantiationException thrown on the constructor's invocation
     * @see Constructor#newInstance
     */
    public static Object invokeExactConstructor(Class klass, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = emptyObjectArray;
        }
        if (parameterTypes == null) {
            parameterTypes = emptyClassArray;
        }
        Constructor ctor = getAccessibleConstructor(klass, parameterTypes);
        if (null == ctor) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + klass.getName());
        }
        return ctor.newInstance(args);
    }

    /**
     * Returns a constructor with single argument.
     * @param klass the class to be constructed
     * @return null if matching accessible constructor can not be found.
     * @see Class#getConstructor
     * @see #getAccessibleConstructor(java.lang.reflect.Constructor)
     */
    public static Constructor getAccessibleConstructor(Class klass, Class parameterType) {
        Class[] parameterTypes = { parameterType };
        return getAccessibleConstructor(klass, parameterTypes);
    }

    /**
     * Returns a constructor given a class and signature.
     * @param klass the class to be constructed
     * @param parameterTypes the parameter array
     * @return null if matching accessible constructor can not be found
     * @see Class#getConstructor
     * @see #getAccessibleConstructor(java.lang.reflect.Constructor)
     */
    public static Constructor getAccessibleConstructor(Class klass, Class[] parameterTypes) {
        try {
            return getAccessibleConstructor(klass.getConstructor(parameterTypes));
        } catch (NoSuchMethodException e) {
            return (null);
        }
    }

    /**
     * Returns accessible version of the given constructor.
     * @param ctor prototype constructor object.
     * @return <code>null</code> if accessible constructor can not be found.
     * @see java.lang.SecurityManager
     */
    public static Constructor getAccessibleConstructor(Constructor ctor) {
        if (ctor == null) {
            return (null);
        }
        if (!Modifier.isPublic(ctor.getModifiers())) {
            return (null);
        }
        Class clazz = ctor.getDeclaringClass();
        if (Modifier.isPublic(clazz.getModifiers())) {
            return (ctor);
        }
        return null;
    }

    /**
     * <p>Find an accessible constructor with compatible parameters.
     * Compatible parameters mean that every method parameter is assignable from
     * the given parameters. In other words, it finds constructor that will take
     * the parameters given.</p>
     *
     * <p>First it checks if there is constructor matching the exact signature.
     * If no such, all the constructors of the class are tested if their signatures
     * are assignment compatible with the parameter types.
     * The first matching constructor is returned.</p>
     *
     * @param clazz find constructor for this class
     * @param parameterTypes find method with compatible parameters
     * @return a valid Constructor object. If there's no matching constructor, returns <code>null</code>.
     */
    private static Constructor getMatchingAccessibleConstructor(Class clazz, Class[] parameterTypes) {
        try {
            Constructor ctor = clazz.getConstructor(parameterTypes);
            try {
                ctor.setAccessible(true);
            } catch (SecurityException se) {
            }
            return ctor;
        } catch (NoSuchMethodException e) {
        }
        int paramSize = parameterTypes.length;
        Constructor[] ctors = clazz.getConstructors();
        for (int i = 0, size = ctors.length; i < size; i++) {
            Class[] ctorParams = ctors[i].getParameterTypes();
            int ctorParamSize = ctorParams.length;
            if (ctorParamSize == paramSize) {
                boolean match = true;
                for (int n = 0; n < ctorParamSize; n++) {
                    if (!MethodUtils.isAssignmentCompatible(ctorParams[n], parameterTypes[n])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    Constructor ctor = getAccessibleConstructor(ctors[i]);
                    if (ctor != null) {
                        try {
                            ctor.setAccessible(true);
                        } catch (SecurityException se) {
                        }
                        return ctor;
                    }
                }
            }
        }
        return null;
    }
}
