package com.acciente.commons.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * This class contains java reflection helper methods to invoke constructors and
 * methods. The methods in this class are used to do constructor and method
 * dependency injection based on type matching.
 *
 * @created Mar 15, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class Invoker {

    /**
    * This method calls the specified class constructor using the specified args as follows.
    * Each arg in aoArgs is expected to be of a unique type, if there is more than one value
    * with the same type the first value is used.
    *
    * This method provides a value to each parameter of the constructor from aoArgs based on
    * the type of the expected parameter.
    *
    * @param oConstructor a constructor object to be invoked
    * @param aoArgs an array containing a set of arguments each of a distinct type
    *
    * @param oParameterProvider an interface that allows a developer to provide the
    * value of a parameter based on the parameter type
    * @return the new object instance created by calling the constructor
    *
    * @throws InvocationTargetException propagated from Constructor.newInstance()
    * @throws IllegalAccessException propagated from Constructor.newInstance()
    * @throws InstantiationException propagated from Constructor.newInstance()
    * @throws ParameterProviderException propagated from the supplied ParameterProvider instance
    */
    public static Object invoke(Constructor oConstructor, Object[] aoArgs, ParameterProvider oParameterProvider) throws InvocationTargetException, IllegalAccessException, InstantiationException, ParameterProviderException {
        Class[] aoParameterTypes = oConstructor.getParameterTypes();
        Object[] aoParameterValues = new Object[aoParameterTypes.length];
        if (aoParameterValues.length != 0) {
            for (int i = 0; i < aoParameterValues.length; i++) {
                Object oArg = null;
                if (aoArgs != null) {
                    oArg = getByType(aoArgs, aoParameterTypes[i]);
                }
                if (oArg == null && oParameterProvider != null) {
                    oArg = oParameterProvider.getParameter(aoParameterTypes[i]);
                }
                aoParameterValues[i] = oArg;
            }
        }
        return oConstructor.newInstance(aoParameterValues);
    }

    /**
    * This method calls the specified method using the specified args as follows.
    * Each arg in aoArgs is expected to be of a unique type, if there is more than one value
    * with the same type the first value is used.
    *
    * This method provides a value to each parameter of the method from aoArgs based on
    * the type of the expected parameter.
    *
    * @param oMethod a method object to be invoked
    * @param oTarget the target object on which the method should be invoked
    * @param aoArgs an array containing a set of arguments each of a distinct type
    *
    * @param oParameterProvider an interface that allows a developer to provide the
    * value of a parameter based on the parameter type
    * @return the value returned by the called method
    *
    * @throws InvocationTargetException propagated from Constructor.newInstance()
    * @throws IllegalAccessException propagated from Constructor.newInstance()
    * @throws ParameterProviderException propagated from the supplied ParameterProvider instance
    */
    public static Object invoke(Method oMethod, Object oTarget, Object[] aoArgs, ParameterProvider oParameterProvider) throws InvocationTargetException, IllegalAccessException, ParameterProviderException {
        Class[] aoParameterTypes = oMethod.getParameterTypes();
        Object[] aoParameterValues = new Object[aoParameterTypes.length];
        if (aoParameterValues.length != 0) {
            for (int i = 0; i < aoParameterValues.length; i++) {
                Object oArg = null;
                if (aoArgs != null) {
                    oArg = getByType(aoArgs, aoParameterTypes[i]);
                }
                if (oArg == null && oParameterProvider != null) {
                    oArg = oParameterProvider.getParameter(aoParameterTypes[i]);
                }
                aoParameterValues[i] = oArg;
            }
        }
        return oMethod.invoke(oTarget, aoParameterValues);
    }

    /**
    * This method returns the first element in the specified array that matches the specified type.
    *
    * @param aoArgs an array of values
    * @param oType the type to search within the array of values
    * @return the element from the array that matches the specified type
    */
    private static Object getByType(Object[] aoArgs, Class oType) {
        Object oArgMatch = null;
        if (oType.isPrimitive()) {
            for (int i = 0; i < aoArgs.length; i++) {
                if (aoArgs[i] != null && isAssignableToPrimitive(aoArgs[i].getClass(), oType)) {
                    oArgMatch = aoArgs[i];
                    break;
                }
            }
        } else {
            for (int i = 0; i < aoArgs.length; i++) {
                if (aoArgs[i] != null && oType.isAssignableFrom(aoArgs[i].getClass())) {
                    oArgMatch = aoArgs[i];
                    break;
                }
            }
        }
        return oArgMatch;
    }

    private static boolean isAssignableToPrimitive(Class oClassType, Class oPrimitiveType) {
        return ((oPrimitiveType == Boolean.TYPE && oClassType.equals(Boolean.class)) || (oPrimitiveType == Character.TYPE && oClassType.equals(Character.class)) || (oPrimitiveType == Byte.TYPE && oClassType.equals(Byte.class)) || (oPrimitiveType == Short.TYPE && oClassType.equals(Short.class)) || (oPrimitiveType == Integer.TYPE && oClassType.equals(Integer.class)) || (oPrimitiveType == Long.TYPE && oClassType.equals(Long.class)) || (oPrimitiveType == Float.TYPE && oClassType.equals(Float.class)) || (oPrimitiveType == Double.TYPE && oClassType.equals(Double.class)));
    }
}
