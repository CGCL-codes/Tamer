package org.t2framework.lucy.tx;

import java.lang.reflect.Method;
import java.util.List;
import org.t2framework.commons.annotation.composite.SingletonScope;
import org.t2framework.commons.aop.spi.Interceptor;
import org.t2framework.commons.aop.spi.Invocation;
import org.t2framework.commons.exception.InvocationException;
import org.t2framework.commons.util.CollectionsUtil;
import org.t2framework.lucy.annotation.core.Inject;

@SingletonScope
public class InterceptorGroup implements Interceptor {

    protected Interceptor[] interceptors;

    @Override
    public Object intercept(final Invocation<Method> invocation) throws InvocationException {
        try {
            final Interceptor[] interceptors = this.interceptors;
            final NestedInvocation nested = new NestedInvocation(invocation, interceptors);
            return nested.proceed();
        } catch (InvocationException e) {
            throw e;
        } catch (Throwable t) {
            throw new InvocationException(t, invocation.getType().getDeclaringClass());
        }
    }

    @Inject
    public void inject(Interceptor... interceptors) {
        List<Interceptor> list = CollectionsUtil.newArrayList();
        for (Interceptor interceptor : interceptors) {
            if (interceptor instanceof InterceptorGroup) {
                continue;
            }
            list.add(interceptor);
        }
        this.interceptors = list.toArray(new Interceptor[0]);
    }
}
