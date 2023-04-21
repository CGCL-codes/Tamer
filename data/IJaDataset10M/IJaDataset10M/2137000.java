package br.com.arsmachina.module.service.internal;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;

/**
 * {@link MethodAdvice} that changes a {@link Class} parameter into its superclass if the 
 * parameter is a Hibernate Javassist proxy class.
 * 
 * @author Thiago H. de Paula Figueiredo.
 */
public final class JavassistMethodAdvice implements MethodAdvice {

    /**
	 * Checks if the given {@link Invocation} has only one parameter of type {@link Class}.
	 * If yes, it solves the Javassist proxy class problem by overriding the parameter before
	 * the invocation is proceeded.
	 */
    @SuppressWarnings("unchecked")
    public void advise(Invocation invocation) {
        if (invocation.getParameterCount() == 1 && invocation.getParameterType(0).equals(Class.class)) {
            Class clasz = (Class) invocation.getParameter(0);
            if (clasz.getName().contains("$$_javassist_")) {
                invocation.override(0, clasz.getSuperclass());
            }
        }
        invocation.proceed();
    }
}
