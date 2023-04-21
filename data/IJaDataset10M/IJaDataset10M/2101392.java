package com.framework.intercept;

/**
 * TODO_DOCUMENT_ME
 * 
 * @since 1.0
 */
public interface IInterceptor extends Comparable<IInterceptor> {

    /**
    * Invokes this intercepter on the specified parameter.
    * 
    * @param invocation 
    * @since 1.0
    */
    public Object invoke(IInvocation invocation) throws InterceptException;

    /**
    * Indicates whether this intercepter should be applied to the given
    * <code>invocation</code>.
    * 
    * @param invocation
    * @return 
    * @since 1.0
    */
    public boolean canIntercept(IInvocation invocation);

    /**
    * The priority of the intercepter in case multiple are registered.
    * 
    * @return priority
    * @since 1.0
    */
    public int getOrder();

    /**
    * Friendly name for the intercepter
    * 
    * @return friendly name of intercepter
    * @since 1.0
    */
    public String getName();
}
