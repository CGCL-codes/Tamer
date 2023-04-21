package org.xmlvm.iphone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;
import org.xmlvm.XMLVMIgnore;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly(references = { NSString.class, Class.class, Method.class })
public class NSObject {

    @XMLVMIgnore
    private static class RunnableInstance1<A> implements Runnable {

        final NSSelector<A> selector;

        final A arg;

        final double delay;

        public RunnableInstance1(NSSelector<A> selector, A arg, double delay) {
            this.selector = selector;
            this.arg = arg;
            this.delay = delay;
        }

        @Override
        public void run() {
            if (delay > 0) {
                try {
                    Thread.sleep((long) (delay * 1000));
                } catch (InterruptedException ex) {
                }
            }
            selector.invokeWithArgument(arg);
        }
    }

    @XMLVMIgnore
    private static class RunnableInstance2 implements Runnable {

        final Object target;

        final String method;

        final Object arg;

        final double delay;

        public RunnableInstance2(Object target, String method, Object arg, double delay) {
            this.target = target;
            this.method = method;
            this.arg = arg;
            this.delay = delay;
        }

        @Override
        public void run() {
            Class<?>[] paramTypes = { Object.class };
            Object[] params = { arg };
            Class targetClass = target.getClass();
            Method m = null;
            while (targetClass != null && m == null) {
                try {
                    m = targetClass.getDeclaredMethod(method, paramTypes);
                } catch (SecurityException e) {
                } catch (NoSuchMethodException e) {
                }
                targetClass = targetClass.getSuperclass();
            }
            if (m == null) {
                throw new RuntimeException("Unable fo find method " + method + " in class " + target.getClass().getName());
            }
            try {
                m.setAccessible(true);
                if (delay > 0) {
                    try {
                        Thread.sleep((long) (delay * 1000));
                    } catch (InterruptedException ex) {
                    }
                }
                m.invoke(target, params);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <A> void performSelector(NSSelector<A> selector, A arg, double delay) {
        performSelector(selector, arg, false, delay);
    }

    public static <A> void performSelectorOnMainThread(NSSelector<A> selector, A arg, boolean waitUntilDone) {
        performSelector(selector, arg, waitUntilDone, 0);
    }

    private static <A> void performSelector(NSSelector<A> selector, A arg, boolean waitUntilDone, double delay) {
        final Runnable runnable = new RunnableInstance1(selector, arg, delay);
        try {
            if (waitUntilDone) {
                if (SwingUtilities.isEventDispatchThread()) {
                    runnable.run();
                } else {
                    SwingUtilities.invokeAndWait(runnable);
                }
            } else {
                SwingUtilities.invokeLater(runnable);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void finalize() {
    }

    public NSObject retain() {
        return this;
    }

    public void release() {
    }

    public void dealloc() {
    }
}
