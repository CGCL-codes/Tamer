package org.apache.axis2.jaxws.client.async;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.async.AsyncResult;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.java.security.AccessController;
import org.apache.axis2.jaxws.core.InvocationContext;
import org.apache.axis2.jaxws.core.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.WebServiceException;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * The CallbackFuture implements the Axis2 <link>org.apache.axis2.client.async.Callback</link> API
 * and will get registered with the Axis2 engine to receive the asynchronous callback responses.
 * This object is also responsible for taking the <link>java.util.concurrent.Executor</link> given
 * to it by the JAX-WS client and using that as the thread on which to deliver the async response
 * the JAX-WS <link>javax.xml.ws.AsynchHandler</link>.
 */
public class CallbackFuture extends Callback {

    private static final Log log = LogFactory.getLog(CallbackFuture.class);

    private static final boolean debug = log.isDebugEnabled();

    private CallbackFutureTask cft;

    private Executor executor;

    private FutureTask task;

    private InvocationContext invocationCtx;

    @SuppressWarnings("unchecked")
    public CallbackFuture(InvocationContext ic, AsyncHandler handler) {
        cft = new CallbackFutureTask(ic.getAsyncResponseListener(), handler);
        task = new FutureTask(cft);
        executor = ic.getExecutor();
        invocationCtx = ic;
    }

    public Future<?> getFutureTask() {
        return (Future<?>) task;
    }

    @Override
    public void onComplete(AsyncResult result) {
        if (debug) {
            log.debug("JAX-WS received the async response");
        }
        MessageContext response = null;
        try {
            response = AsyncUtils.createJAXWSMessageContext(result);
            response.setInvocationContext(invocationCtx);
            response.setMEPContext(invocationCtx.getRequestMessageContext().getMEPContext());
        } catch (WebServiceException e) {
            cft.setError(e);
            if (debug) {
                log.debug("An error occured while processing the async response.  " + e.getMessage());
            }
        }
        if (response == null) {
        }
        cft.setMessageContext(response);
        execute();
    }

    @Override
    public void onError(Exception e) {
        if (e.getClass().isAssignableFrom(AxisFault.class)) {
            AxisFault fault = (AxisFault) e;
            MessageContext faultMessageContext = null;
            try {
                faultMessageContext = AsyncUtils.createJAXWSMessageContext(fault.getFaultMessageContext());
                faultMessageContext.setInvocationContext(invocationCtx);
                faultMessageContext.setMEPContext(invocationCtx.getRequestMessageContext().getMEPContext());
            } catch (WebServiceException wse) {
                cft.setError(wse);
            }
            cft.setError(e);
            cft.setMessageContext(faultMessageContext);
        } else {
            cft.setError(e);
        }
        execute();
    }

    private void execute() {
        if (log.isDebugEnabled()) {
            log.debug("Executor task starting to process async response");
        }
        if (executor != null) {
            if (task != null && !task.isCancelled()) {
                try {
                    executor.execute(task);
                } catch (Exception executorExc) {
                    if (log.isDebugEnabled()) {
                        log.debug("CallbackFuture.execute():  executor exception [" + executorExc.getClass().getName() + "]");
                    }
                    task.cancel(true);
                }
                if (log.isDebugEnabled()) {
                    log.debug("Task submitted to Executor");
                }
                try {
                    synchronized (cft) {
                        if (!cft.done) {
                            cft.wait(180000);
                        }
                    }
                } catch (InterruptedException e) {
                    if (debug) {
                        log.debug("cft.wait() was interrupted");
                        log.debug("Exception: " + e.getMessage());
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Executor task was not sumbitted as Async Future task was cancelled by clients");
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Executor task completed");
        }
    }
}

class CallbackFutureTask implements Callable {

    private static final Log log = LogFactory.getLog(CallbackFutureTask.class);

    private static final boolean debug = log.isDebugEnabled();

    AsyncResponse response;

    MessageContext msgCtx;

    AsyncHandler handler;

    Exception error;

    boolean done = false;

    CallbackFutureTask(AsyncResponse r, AsyncHandler h) {
        response = r;
        handler = h;
    }

    protected AsyncHandler getHandler() {
        return handler;
    }

    void setMessageContext(MessageContext mc) {
        msgCtx = mc;
    }

    void setError(Exception e) {
        error = e;
    }

    @SuppressWarnings("unchecked")
    public Object call() throws Exception {
        try {
            final ClassLoader classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    return handler.getClass().getClassLoader();
                }
            });
            if (log.isDebugEnabled()) {
                log.debug("Setting up the thread's ClassLoader");
                log.debug(classLoader.toString());
            }
            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    Thread.currentThread().setContextClassLoader(classLoader);
                    return null;
                }
            });
            if (error != null) {
                response.onError(error, msgCtx, classLoader);
            } else {
                response.onComplete(msgCtx, classLoader);
            }
            if (debug) {
                log.debug("Calling JAX-WS AsyncHandler with the Response object");
                log.debug("AyncHandler class: " + handler.getClass());
            }
            handler.handleResponse(response);
        } catch (Throwable t) {
            if (debug) {
                log.debug("An error occured while invoking the callback object.");
                log.debug("Error: " + t.getMessage());
            }
        } finally {
            synchronized (this) {
                done = true;
                this.notifyAll();
            }
        }
        return null;
    }
}
