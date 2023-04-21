package org.kaleidofoundry.core.context;

import java.lang.annotation.Annotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.kaleidofoundry.core.lang.annotation.Immutable;
import org.slf4j.Logger;

/**
 * Interceptor used for injecting {@link RuntimeContext} information using the {@link Context} method annotation <br/>
 * <br/>
 * Only the first {@link RuntimeContext} argument would be processing
 * <p>
 * Context injection handle by :
 * <ul>
 * <li>constructor (not yet handled)
 * <li>field
 * <li>method
 * <li>method argument
 * </ul>
 * </p>
 * 
 * @author Jerome RADUGET
 */
@Immutable
class ContextInjectionMethodInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = AbstractModule.LOGGER;

    public ContextInjectionMethodInterceptor() {
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        debugInvovation(invocation);
        int cptArg = 0;
        RuntimeContext<?> runtimeContextArg = null;
        for (final Object arg : invocation.getArguments()) {
            if (arg != null && arg instanceof RuntimeContext<?>) {
                runtimeContextArg = (RuntimeContext<?>) arg;
                break;
            } else {
                cptArg++;
            }
        }
        if (runtimeContextArg != null) {
            boolean contextInjected = false;
            Context contextAnnot = null;
            final Annotation[] argsAnnot = invocation.getMethod().getParameterAnnotations()[cptArg];
            for (final Annotation a : argsAnnot) {
                if (a instanceof Context) {
                    contextAnnot = (Context) a;
                }
            }
            if (contextAnnot != null) {
                RuntimeContext.createFrom(contextAnnot, null, runtimeContextArg);
                contextInjected = true;
            }
            if (!contextInjected) {
                contextAnnot = invocation.getMethod().getAnnotation(Context.class);
                if (contextAnnot != null) {
                    RuntimeContext.createFrom(contextAnnot, null, runtimeContextArg);
                    contextInjected = true;
                }
            }
        }
        return invocation.proceed();
    }

    protected void debugInvovation(final MethodInvocation invocation) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method #{} invoked", invocation.getMethod().getName());
            int cpt = 0;
            for (final Object arg : invocation.getArguments()) {
                LOGGER.debug("\tmethod arg[{}]={}.toString()={}", new Object[] { cpt++, arg.getClass().getName(), arg });
            }
            for (final Annotation annot : invocation.getMethod().getDeclaredAnnotations()) {
                LOGGER.debug("\tmethod {} annotated by {}", invocation.getMethod().getName(), annot.annotationType().getName());
            }
            LOGGER.debug("\tinstance class is \"{}\" toString()=\"{}\"", invocation.getThis().getClass().getName(), invocation.getThis());
        }
    }
}
