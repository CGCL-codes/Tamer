package net.sourceforge.signal.runtime.core.context.proxy;

import net.sourceforge.signal.runtime.core.context.IBeanReference;

public class LazyInitMethodInterceptor extends AbstractMethodInterceptor {

    private IBeanReference reference;

    private IProxyTarget bean;

    public LazyInitMethodInterceptor(IBeanReference reference) {
        this.reference = reference;
    }

    public Object invoke(IProxyTarget target, IMethodHandler handler, Object[] args) throws Exception {
        if (bean == null) {
            bean = (IProxyTarget) reference.getBean();
        }
        return nextHandler.invoke(bean, handler, args);
    }
}
