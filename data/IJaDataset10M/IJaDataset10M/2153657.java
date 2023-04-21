package org.jgentleframework.core.interceptor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jgentleframework.configure.aopweaving.annotation.Throws;
import org.jgentleframework.context.aop.advice.ThrowsAdvice;
import org.jgentleframework.context.injecting.Provider;
import org.jgentleframework.core.factory.RequiredException;
import org.jgentleframework.core.intercept.AbstractInterceptedAdviceInvocation;
import org.jgentleframework.core.intercept.InterceptionException;
import org.jgentleframework.reflection.metadata.Definition;
import org.jgentleframework.utils.Assertor;
import org.jgentleframework.utils.ReflectUtils;

/**
 * Intercepts with a stack of {@link ThrowsAdvice}.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Aug 26, 2008
 * @see MethodInterceptor
 */
class ThrowsAdviceStackMethodInterceptor implements MethodInterceptor {

    /** The advice map. */
    @SuppressWarnings("unchecked")
    Map<Class<? extends Throwable>, ThrowsAdvice> adviceMap = new HashMap<Class<? extends Throwable>, ThrowsAdvice>();

    /** The throws advices. */
    List<ThrowsAdvice<Throwable>> throwsAdvices = new ArrayList<ThrowsAdvice<Throwable>>();

    /** The provider. */
    final Provider provider;

    /** The runtime loading. */
    private boolean runtimeLoading = false;

    /** The definition. */
    Definition definition;

    /**
	 * Instantiates a new throws advice stack method interceptor.
	 * 
	 * @param definition
	 *            the definition
	 * @param provider
	 *            the provider
	 * @param runtimeLoading
	 *            the runtime loading
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
    public ThrowsAdviceStackMethodInterceptor(Definition definition, Provider provider, boolean runtimeLoading) throws ClassNotFoundException {
        Assertor.notNull(definition, "The given definition must not be null!");
        this.definition = definition;
        this.provider = provider;
        this.runtimeLoading = runtimeLoading;
    }

    /**
	 * Find advice instances.
	 * 
	 * @param throwz
	 *            the throwz
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
    @SuppressWarnings("unchecked")
    private void findAdviceInstances(Throws throwz) throws ClassNotFoundException {
        List<ThrowsAdvice> list = new ArrayList<ThrowsAdvice>();
        String[] objStr = throwz.value();
        if (objStr != null && !(objStr.length == 1 && objStr[0].isEmpty())) {
            for (String str : objStr) {
                if (!str.isEmpty()) {
                    Object obj = this.provider.getBean(str);
                    if (obj != null) if (ReflectUtils.isCast(ThrowsAdvice.class, obj)) {
                        if (!list.contains(obj)) list.add((ThrowsAdvice) obj);
                        addThrowsAdvice((ThrowsAdvice) obj);
                    } else throw new InterceptionException("The registered object of throws advice can not be casted to '" + ThrowsAdvice.class + "'!"); else {
                        if (throwz.required()) {
                            throw new RequiredException("The reference of  Throws Advice instance must not be null!");
                        }
                    }
                }
            }
        }
    }

    /**
	 * Adds the throws advice.
	 * 
	 * @param advice
	 *            the advice
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
    @SuppressWarnings("unchecked")
    public void addThrowsAdvice(ThrowsAdvice<?> advice) throws ClassNotFoundException {
        List<Type> typeList = ReflectUtils.getAllGenericInterfaces(advice.getClass(), true);
        for (Type type : typeList) {
            if (ReflectUtils.isCast(ParameterizedType.class, type)) {
                ParameterizedType pType = (ParameterizedType) type;
                if (pType.getRawType().equals(ThrowsAdvice.class)) {
                    String annoStrClass = pType.getActualTypeArguments()[0].toString().split(" ")[1];
                    Class<? extends Throwable> annoClass = (Class<? extends Throwable>) Class.forName(annoStrClass);
                    if (annoClass.equals(Throwable.class)) {
                        if (!this.throwsAdvices.contains(advice)) this.throwsAdvices.add((ThrowsAdvice<Throwable>) advice);
                    } else {
                        this.adviceMap.put(annoClass, advice);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            Definition defMethod = definition.getMemberDefinition(invocation.getMethod());
            Throws throwz = defMethod != null && defMethod.isAnnotationPresent(Throws.class) ? defMethod.getAnnotation(Throws.class) : null;
            if (throwz != null) {
                Class<?> throwableClass = e.getClass();
                if (throwz.invocation() || runtimeLoading || this.adviceMap.isEmpty()) {
                    this.adviceMap.clear();
                    findAdviceInstances(throwz);
                }
                if (adviceMap.containsKey(throwableClass)) adviceMap.get(throwableClass).afterThrowing(invocation, e); else new InterceptedThrowsAdviceInvocation(invocation, e).proceed();
            }
        }
        return result;
    }

    /**
	 * The Class InterceptedThrowsAdviceInvocation.
	 */
    class InterceptedThrowsAdviceInvocation extends AbstractInterceptedAdviceInvocation implements MethodInvocation {

        /** The index. */
        int index = -1;

        /** The throwable. */
        Throwable throwable = null;

        /**
		 * The Constructor.
		 * 
		 * @param invocation
		 *            the invocation
		 * @param throwable
		 *            the throwable
		 */
        public InterceptedThrowsAdviceInvocation(MethodInvocation invocation, Throwable throwable) {
            super(invocation);
            this.throwable = throwable;
        }

        @Override
        public Object proceed() throws Throwable {
            Definition defMethod = definition.getMemberDefinition(invocation.getMethod());
            Throws throwz = defMethod != null && defMethod.isAnnotationPresent(Throws.class) ? defMethod.getAnnotation(Throws.class) : null;
            if (throwz != null) {
                if (throwz.invocation() || runtimeLoading || throwsAdvices.isEmpty()) {
                    throwsAdvices.clear();
                    findAdviceInstances(throwz);
                }
            }
            try {
                index++;
                if (index != throwsAdvices.size()) throwsAdvices.get(index).afterThrowing(this, throwable);
            } finally {
                index--;
            }
            return null;
        }
    }
}
