package org.apache.harmony.awt;

import org.apache.harmony.awt.datatransfer.*;
import org.apache.harmony.awt.internal.nls.Messages;
import org.apache.harmony.awt.wtk.*;
import com.google.code.appengine.awt.*;

public final class ContextStorage {

    private static volatile boolean multiContextMode = false;

    private volatile boolean shutdownPending = false;

    private static final ContextStorage globalContext = new ContextStorage();

    private Toolkit toolkit;

    private ComponentInternals componentInternals;

    private DTK dtk;

    private WTK wtk;

    private GraphicsEnvironment graphicsEnvironment;

    private class ContextLock {
    }

    private final Object contextLock = new ContextLock();

    private final Synchronizer synchronizer = new Synchronizer();

    public static void activateMultiContextMode() {
        multiContextMode = true;
    }

    public static void setDefaultToolkit(Toolkit newToolkit) {
        getCurrentContext().toolkit = newToolkit;
    }

    public static Toolkit getDefaultToolkit() {
        return getCurrentContext().toolkit;
    }

    public static void setDTK(DTK dtk) {
        getCurrentContext().dtk = dtk;
    }

    public static DTK getDTK() {
        return getCurrentContext().dtk;
    }

    public static Synchronizer getSynchronizer() {
        return getCurrentContext().synchronizer;
    }

    public static ComponentInternals getComponentInternals() {
        return getCurrentContext().componentInternals;
    }

    static void setComponentInternals(ComponentInternals internals) {
        getCurrentContext().componentInternals = internals;
    }

    public static Object getContextLock() {
        return getCurrentContext().contextLock;
    }

    public static WindowFactory getWindowFactory() {
        return getCurrentContext().wtk.getWindowFactory();
    }

    public static void setWTK(WTK wtk) {
        getCurrentContext().wtk = wtk;
    }

    public static NativeIM getNativeIM() {
        return getCurrentContext().wtk.getNativeIM();
    }

    public static NativeEventQueue getNativeEventQueue() {
        return getCurrentContext().wtk.getNativeEventQueue();
    }

    public static GraphicsEnvironment getGraphicsEnvironment() {
        return getCurrentContext().graphicsEnvironment;
    }

    public static void setGraphicsEnvironment(GraphicsEnvironment environment) {
        getCurrentContext().graphicsEnvironment = environment;
    }

    private static ContextStorage getCurrentContext() {
        return multiContextMode ? getContextThreadGroup().context : globalContext;
    }

    private static ContextThreadGroup getContextThreadGroup() {
        Thread thread = Thread.currentThread();
        ThreadGroup group = thread.getThreadGroup();
        while (group != null) {
            if (group instanceof ContextThreadGroup) {
                return (ContextThreadGroup) group;
            }
            group = group.getParent();
        }
        throw new RuntimeException(Messages.getString("awt.59"));
    }

    public static boolean shutdownPending() {
        return getCurrentContext().shutdownPending;
    }

    void shutdown() {
        if (!multiContextMode) {
            return;
        }
        shutdownPending = true;
        componentInternals.shutdown();
        synchronized (contextLock) {
            toolkit = null;
            componentInternals = null;
            dtk = null;
            wtk = null;
            graphicsEnvironment = null;
        }
    }
}
