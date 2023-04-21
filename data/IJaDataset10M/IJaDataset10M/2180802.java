package ch.iserver.ace.threaddomain.internal;

import org.aopalliance.intercept.MethodInterceptor;
import ch.iserver.ace.threaddomain.ExceptionHandler;

public interface InterceptorFactory {

    MethodInterceptor createInterceptor(boolean sync, ExceptionHandler exceptionHandler);
}
