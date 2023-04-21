package br.com.dimag.safetycar.data.transaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Annotation implementation of HibernateInterceptor
 * 
 */
public class HibernateInterceptorAnnotation extends HibernateInterceptor {

    public boolean isTransactional(Object object, Method method) throws Exception {
        Annotation annotation = method.getAnnotation(HibernateTransaction.class);
        return annotation != null;
    }
}
