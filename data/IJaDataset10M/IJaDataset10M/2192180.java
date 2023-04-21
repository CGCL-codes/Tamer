package org.fudaa.fudaa.tr;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author fred deniger
 * @version $Id: TrInvocationHandler.java,v 1.1 2006-12-05 10:18:20 deniger Exp $
 */
public final class TrInvocationHandler {

    private TrInvocationHandler() {
    }

    public static class ErrorHandler implements InvocationHandler {

        public Object invoke(final Object _proxy, final Method _method, final Object[] _args) throws Throwable {
            TrUncaughtExceptionHandler.uncaughtExceptionOccured((Thread) _args[0], (Throwable) _args[1]);
            return null;
        }
    }

    public static Object createUncaughtExceptionHandler() {
        try {
            final Class c = getClassUncaughtExceptionHandler();
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { c }, new ErrorHandler());
        } catch (final IllegalArgumentException _evt) {
            _evt.printStackTrace();
        } catch (final ClassNotFoundException _evt) {
            _evt.printStackTrace();
        }
        return null;
    }

    private static Class getClassUncaughtExceptionHandler() throws ClassNotFoundException {
        return Class.forName("java.lang.Thread$UncaughtExceptionHandler");
    }

    public static void initThreaExceptionHandler() {
        boolean isOk = false;
        try {
            final String r = System.getProperty("java.version");
            isOk = r != null && r.compareTo("1.5") >= 0;
        } catch (final SecurityException _evt1) {
        }
        if (!isOk) {
            return;
        }
        try {
            final Method m = Thread.class.getMethod("setDefaultUncaughtExceptionHandler", new Class[] { getClassUncaughtExceptionHandler() });
            m.invoke(Thread.currentThread(), new Object[] { createUncaughtExceptionHandler() });
        } catch (final Exception _evt) {
            _evt.printStackTrace();
        }
    }
}
