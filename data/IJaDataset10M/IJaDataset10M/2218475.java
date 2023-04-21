package br.com.rapidrest.util;

import java.lang.reflect.Method;

public class MethodUtil {

    public static Method findMethod(Class<?> clazz, String methodName, int numParams) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == numParams) {
                return method;
            }
        }
        return null;
    }
}
