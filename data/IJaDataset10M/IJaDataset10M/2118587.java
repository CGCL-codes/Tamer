package ch.iserver.ace.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;

/**
 * AsyncInterceptor is a special method interceptor that adds the intercepted
 * MethodInvocation objects to a BlockingQueue and then returns immediately.
 * Together with a worker thread that reads and executes invocations from
 * the same queue this can be used to execute methods asynchronously.
 * 
 * @see ch.iserver.ace.util.AsyncWorker
 */
public class AsyncInterceptor implements MethodInterceptor {

    /**
	 * The logger for instances of this class.
	 */
    private static final Logger LOG = Logger.getLogger(AsyncInterceptor.class);

    /**
	 * The queue used to store MethodInvocation objects.
	 */
    private final BlockingQueue queue;

    /**
	 * Flag indicating whether to accept invocations.
	 */
    private boolean disposed;

    /**
	 * Creates a new AsyncInterceptor that adds MethodInvocations to the given
	 * <var>queue</var>.
	 * 
	 * @param queue the queue used to store method invocation objects
	 */
    public AsyncInterceptor(BlockingQueue queue) {
        ParameterValidator.notNull("queue", queue);
        this.queue = queue;
    }

    /**
	 * Disposes this AsyncInterceptor. After disposing, additional calls to 
	 * {@link #invoke(MethodInvocation)} should throw an
	 * {@link IllegalStateException}.
	 */
    public synchronized void dispose() {
        this.disposed = true;
    }

    /**
	 * Determines whether this AsyncInterceptor has been disposed.
	 * 
	 * @return true iff the interceptor has been disposed
	 */
    public boolean isDisposed() {
        return disposed;
    }

    /**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
    public synchronized Object invoke(MethodInvocation invocation) throws Throwable {
        if (isDisposed()) {
            throw new IllegalStateException("AsyncInterceptor has been disposed");
        }
        if (!invocation.getMethod().getReturnType().equals(Void.TYPE)) {
            return invokeNonVoidMethod(invocation);
        } else {
            invokeAsync(invocation);
            return null;
        }
    }

    /**
	 * Invokes the method invocation asynchronously.
	 * 
	 * @param invocation the invocation
	 */
    protected void invokeAsync(MethodInvocation invocation) {
        AsyncMethodInvocation wrapper = new AsyncMethodInvocation(invocation);
        wrapper.setCallerStackTrace(new Throwable().getStackTrace());
        queue.add(wrapper);
    }

    /**
	 * Invokes a method with a non-void return type. This method can be
	 * overriden to customize the behavior in this case.
	 * 
	 * @param invocation the method invocation
	 * @return the result of the invocation or null if the method was invoked
	 *         asynchronously
	 */
    protected Object invokeNonVoidMethod(MethodInvocation invocation) {
        LOG.info("invoking void method asynchronously: " + invocation);
        invokeAsync(invocation);
        return null;
    }
}
