package org.apache.tapestry5.ioc.services;

import org.apache.tapestry5.ioc.MethodAdviceReceiver;

/**
 * A builder may be obtained from the {@link org.apache.tapestry5.ioc.services.AspectDecorator} and allows more
 * controlled creation of the created interceptor; it allows different methods to be given different advice, and allows
 * methods to be omitted (in which case the method invocation passes through without advice).
 */
public interface AspectInterceptorBuilder<T> extends MethodAdviceReceiver {

    /**
     * Builds and returns the interceptor.  Any methods that have not been advised will become "pass thrus".
     */
    T build();
}
