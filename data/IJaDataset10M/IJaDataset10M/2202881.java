package org.apache.tapestry5.ioc.internal.services;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ObjectCreator;
import org.apache.tapestry5.ioc.annotations.NotLazy;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.LazyAdvisor;
import org.apache.tapestry5.ioc.services.ThunkCreator;
import java.lang.reflect.Method;

public class LazyAdvisorImpl implements LazyAdvisor {

    private final ThunkCreator thunkCreator;

    public LazyAdvisorImpl(ThunkCreator thunkCreator) {
        this.thunkCreator = thunkCreator;
    }

    public void addLazyMethodInvocationAdvice(MethodAdviceReceiver methodAdviceReceiver) {
        for (Method m : methodAdviceReceiver.getInterface().getMethods()) {
            if (filter(m)) addAdvice(m, methodAdviceReceiver);
        }
    }

    private void addAdvice(Method method, MethodAdviceReceiver receiver) {
        final Class thunkType = method.getReturnType();
        final String description = String.format("<%s Thunk for %s>", thunkType.getName(), InternalUtils.asString(method));
        MethodAdvice advice = new MethodAdvice() {

            public void advise(final Invocation invocation) {
                ObjectCreator deferred = new ObjectCreator() {

                    public Object createObject() {
                        invocation.proceed();
                        return invocation.getResult();
                    }
                };
                ObjectCreator cachingObjectCreator = new CachingObjectCreator(deferred);
                Object thunk = thunkCreator.createThunk(thunkType, cachingObjectCreator, description);
                invocation.overrideResult(thunk);
            }
        };
        receiver.adviseMethod(method, advice);
    }

    private boolean filter(Method method) {
        if (method.getAnnotation(NotLazy.class) != null) return false;
        if (!method.getReturnType().isInterface()) return false;
        for (Class extype : method.getExceptionTypes()) {
            if (!RuntimeException.class.isAssignableFrom(extype)) return false;
        }
        return true;
    }
}
