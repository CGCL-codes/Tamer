package net.sourceforge.signal.runtime.core.context.proxy;

/**
 * A metod interceptor used by AOP proxies.
 * 
 * @author Marek
 */
public interface IMethodInterceptor extends IInvocationHandler {

    /**
	 * Sets the next handler in the interceptor chain.
	 * A method interceptor may choose whether to forward
	 * the call to the next interceptor or not.
	 * 
	 * @param handler next handler
	 */
    void setNextHandler(IInvocationHandler handler);
}
