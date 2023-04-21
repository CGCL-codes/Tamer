package org.apache.tapestry5.ioc.internal.services;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.services.ExceptionTracker;
import org.slf4j.Logger;

public class LoggingAdvice implements MethodAdvice {

    private final MethodLogger methodLogger;

    public LoggingAdvice(Logger logger, ExceptionTracker exceptionTracker) {
        methodLogger = new MethodLogger(logger, exceptionTracker);
    }

    public void advise(Invocation invocation) {
        boolean debug = methodLogger.isDebugEnabled();
        if (!debug) {
            invocation.proceed();
            return;
        }
        methodLogger.entry(invocation);
        try {
            invocation.proceed();
        } catch (RuntimeException ex) {
            methodLogger.fail(invocation, ex);
            throw ex;
        }
        if (invocation.isFail()) {
            Exception thrown = invocation.getThrown(Exception.class);
            methodLogger.fail(invocation, thrown);
            return;
        }
        methodLogger.exit(invocation);
    }
}
