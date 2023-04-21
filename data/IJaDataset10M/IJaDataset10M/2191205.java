package org.middleheaven.core.reflection;

public abstract class InterceptorProxyHandler implements ProxyHandler, WrapperProxy {

    private Object original;

    public InterceptorProxyHandler(Object original) {
        this.original = original;
    }

    public Object getWrappedObject() {
        return original;
    }

    @Override
    public final Object invoke(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable {
        if (willIntercept(proxy, args, delegator)) {
            return doInstead(proxy, args, delegator);
        } else {
            return doOriginal(args, delegator);
        }
    }

    protected final Object doOriginal(Object[] args, MethodDelegator delegator) throws Throwable {
        return delegator.invoke(original, args);
    }

    protected abstract boolean willIntercept(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable;

    protected abstract Object doInstead(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable;
}
