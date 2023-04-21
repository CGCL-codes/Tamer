package com.dhev.constraints.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.hibernate.validator.Validator;

@SuppressWarnings("unchecked")
public class ValidatorAnnotationProxy {

    public static <T> T createProxy(final Validator validator, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Method method2 = validator.getClass().getMethod(method.getName());
                return method2.invoke(validator, args);
            }
        });
    }
}
