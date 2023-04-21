package org.granite.messaging.service;

import org.granite.config.flex.Destination;
import flex.messaging.messages.Message;

/**
 * @author Marcelo SCHROEDER
 */
public abstract class AbstractServiceExceptionHandler implements ServiceExceptionHandler {

    private static final long serialVersionUID = 1L;

    private final boolean logException;

    public AbstractServiceExceptionHandler(boolean logException) {
        this.logException = logException;
    }

    protected boolean getLogException() {
        return logException;
    }

    protected ServiceException getServiceException(ServiceInvocationContext context, Throwable e) {
        String method = (context.getMethod() != null ? context.getMethod().toString() : "null");
        return getServiceException(context.getMessage(), context.getDestination(), method, e);
    }

    protected ServiceException getServiceException(Message request, Destination destination, String method, Throwable e) {
        if (e instanceof ServiceException) return (ServiceException) e;
        String detail = "\n" + "- destination: " + (destination != null ? destination.getId() : "") + "\n" + "- method: " + method + "\n" + "- exception: " + e.toString() + "\n";
        return new ServiceException(getClass().getSimpleName() + ".Call.Failed", e.getMessage(), detail, e);
    }
}
