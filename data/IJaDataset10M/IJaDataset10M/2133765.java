package com.googlecode.objectify.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.Future;
import com.google.appengine.api.utils.SystemProperty;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.ApiProxy.LogRecord;

/**
 * <p>This bit of appengine magic hooks into the ApiProxy and does the heavy lifting of
 * making the TriggerFuture<?> work.</p>
 * 
 * <p>This class maintains a thread local list of all the outstanding Future<?> objects
 * that have pending triggers.  When a Future<?> is done and its trigger is executed,
 * it is removed from the list.  At various times (anytime an API call is made) the registered
 * futures are checked for doneness and processed.</p>
 * 
 * <p>The AsyncCacheFilter is necessary to guarantee that any pending triggers are processed
 * at the end of the request.  A future GAE SDK which allows us to hook into the Future<?>
 * creation process might make this extra Filter unnecessary.</p>
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class TriggerFutureHook implements Delegate<Environment> {

    /** */
    Delegate<Environment> parent;

    /** Automatically install when loaded */
    static {
        install();
    }

    /** The thread local value will be removed (null) if there are none pending */
    private static ThreadLocal<Pending> pending = new ThreadLocal<Pending>();

    /**
	 * Install our hook in the delegate system.  This happens automatically when this class is
	 * loaded, which is typically what you want when working with Objectify.  It gets a lot
	 * more complicated when you're working with the RemoteApiInstaller.
	 */
    public static synchronized void install() {
        if (ApiProxy.getDelegate() instanceof TriggerFutureHook) return;
        @SuppressWarnings("unchecked") TriggerFutureHook hook = new TriggerFutureHook(ApiProxy.getDelegate());
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) ApiProxy.setDelegate(hook); else ApiProxy.setDelegate(wrapPartially(ApiProxy.getDelegate(), hook));
    }

    /**
	 * Register a pending Future that has a callback.
	 * @param future must have at least one callback
	 */
    public static void addPending(Future<?> future) {
        Pending pend = pending.get();
        if (pend == null) {
            pend = new Pending();
            pending.set(pend);
        }
        pend.add(future);
    }

    /**
	 * Deregister a pending Future that had a callback.
	 */
    public static void removePending(Future<?> future) {
        Pending pend = pending.get();
        if (pend != null) {
            pend.remove(future);
            if (pend.isEmpty()) pending.remove();
        }
    }

    /** */
    public TriggerFutureHook(Delegate<Environment> parent) {
        this.parent = parent;
    }

    @Override
    public void log(Environment arg0, LogRecord arg1) {
        this.parent.log(arg0, arg1);
    }

    @Override
    public Future<byte[]> makeAsyncCall(Environment arg0, String arg1, String arg2, byte[] arg3, ApiConfig arg4) {
        this.checkPendingFutures();
        return this.parent.makeAsyncCall(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public byte[] makeSyncCall(Environment arg0, String arg1, String arg2, byte[] arg3) throws ApiProxyException {
        this.checkPendingFutures();
        return this.parent.makeSyncCall(arg0, arg1, arg2, arg3);
    }

    /**
	 * If any futures are pending, check if they are done.  This will process their callbacks.
	 */
    private void checkPendingFutures() {
        Pending pend = pending.get();
        if (pend != null) pend.checkPendingFutures();
    }

    /**
	 * Iterate through all pending futures and get() them, forcing any callbacks to be called.
	 * This is used only by the AsyncCacheFilter because we don't have a proper hook otherwise.
	 */
    public static void completeAllPendingFutures() {
        Pending pend = pending.get();
        if (pend != null) pend.completeAllPendingFutures();
    }

    /**
	 * This is a bit of cleverness from Max Ross (Google) to work around the problem
	 * that the local unit testing framework explicitly casts the delegate to ApiProxyLocal.
	 * Until that bug is removed, this method uses a dynamic proxy to fake the interface.
	 * 
	 * Create a proxy that implements all the interfaces that the original
	 * implements. Whenever a method is called that the wrapper supports, the
	 * wrapper will be called. Otherwise, the method will be invoked on the
	 * original object.
	 * 
	 * CAREFUL!  The call to getClassLoader() throws a security exception on prod GAE,
	 * so only use this mechanism when not on prod.
	 */
    @SuppressWarnings("unchecked")
    static <S, T extends S> S wrapPartially(final S original, final T wrapper) {
        Class<?>[] interfaces = original.getClass().getInterfaces();
        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Method wrapperMethod = null;
                try {
                    wrapperMethod = wrapper.getClass().getMethod(method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException e) {
                    try {
                        return method.invoke(original, args);
                    } catch (InvocationTargetException ex) {
                        throw ex.getTargetException();
                    }
                }
                try {
                    return wrapperMethod.invoke(wrapper, args);
                } catch (InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            }
        };
        return (S) Proxy.newProxyInstance(original.getClass().getClassLoader(), interfaces, handler);
    }

    @Override
    public void flushLogs(Environment paramE) {
        parent.flushLogs(paramE);
    }

    @Override
    public List<Thread> getRequestThreads(Environment paramE) {
        return parent.getRequestThreads(paramE);
    }
}
