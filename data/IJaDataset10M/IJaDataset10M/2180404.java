package net.sourceforge.signal.runtime.core.context.proxy;

/**
 * A common super-class for implementations of {@link IProxy}.
 * 
 * @author Marek
 */
public abstract class AbstractProxy implements IProxy {

    protected IProxyTarget target;

    protected IInvocationHandler handler;

    void setTarget(IProxyTarget target) {
        this.target = target;
    }

    void setHandler(IInvocationHandler handler) {
        this.handler = handler;
    }

    protected Object invokeHandler(IMethodHandler methodHandler, Object[] args) throws Exception {
        return handler.invoke(target, methodHandler, args);
    }

    /**
	 * Wraps an exception that might have been thrown by
	 * a proxied method.
	 * 
	 * @param e  an exception to be wrapped
	 * @return wrapped exception
	 */
    protected RuntimeException wrap(Exception e) {
        return new RuntimeException(e.toString());
    }
}
