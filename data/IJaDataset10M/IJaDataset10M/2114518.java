package net.sf.syracuse.threads.impl;

import net.sf.syracuse.threads.ThreadState;
import net.sf.syracuse.threads.ThreadStateManager;
import org.apache.commons.logging.Log;
import org.apache.hivemind.InterceptorStack;
import org.apache.hivemind.ServiceInterceptorFactory;
import org.apache.hivemind.internal.Module;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * A service interceptor which automatically updates the {@link ThreadState} of a {@code Thread}.  The
 * {@code Thread}'s {@code ThreadState} will be set to the name specified in the {@code name} attribute
 * before any method invocation.  After a method invocation, any attachement to the {@code ThreadState} will be
 * cleared and the {@code Thread}'s state will be reset to what it was before the method was invoked.
 *
 * @author Chris Conrad
 * @since 1.0.0
 */
public class ThreadStateInterceptor implements ServiceInterceptorFactory {

    private ThreadStateManager threadStateManager;

    public void createInterceptor(InterceptorStack stack, Module invokingModule, List parameters) {
        InvocationHandler handler = new ProxyThreadStateInvocationHandler(threadStateManager, stack.peek(), parameters, stack.getServiceLog());
        Object interceptor = Proxy.newProxyInstance(invokingModule.getClassResolver().getClassLoader(), new Class[] { stack.getServiceInterface() }, handler);
        stack.push(interceptor);
    }

    /**
     * Sets the {@code ThreadStateManager}.
     *
     * @param threadStateManager the {@code ThreadStateManager} to set
     */
    public void setThreadStateManager(ThreadStateManager threadStateManager) {
        this.threadStateManager = threadStateManager;
    }

    private static final class ProxyThreadStateInvocationHandler implements InvocationHandler {

        private final ThreadStateManager threadStateManager;

        private final Object delegate;

        private final ThreadState threadState;

        private final Log log;

        public ProxyThreadStateInvocationHandler(ThreadStateManager threadStateManager, Object delegate, List parameters, Log log) {
            this.threadStateManager = threadStateManager;
            this.delegate = delegate;
            this.log = log;
            ThreadStateParameter parameter = (ThreadStateParameter) parameters.get(0);
            threadState = parameter.getThreadState();
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            ThreadState previousState = threadStateManager.getThreadState();
            log.debug("Setting thread state to " + threadState);
            threadStateManager.setThreadState(threadState);
            Object result;
            try {
                result = method.invoke(delegate, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            } finally {
                threadState.setAttachment(null);
                threadStateManager.setThreadState(previousState);
                log.debug("Resetting thread state to " + previousState);
            }
            return result;
        }
    }
}
