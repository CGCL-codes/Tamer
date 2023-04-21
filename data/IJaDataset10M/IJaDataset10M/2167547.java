package com.vladium.util.exit;

import java.util.HashMap;
import java.util.Map;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import com.vladium.util.IJREVersion;
import com.vladium.util.Property;
import com.vladium.emma.IAppConstants;

/**
 * @author Vlad Roubtsov, (C) 2003
 */
public abstract class ExitHookManager implements IJREVersion {

    public abstract boolean addExitHook(Runnable runnable);

    public abstract boolean removeExitHook(Runnable runnable);

    public static synchronized ExitHookManager getSingleton() {
        if (s_singleton == null) {
            if (JRE_1_3_PLUS) {
                s_singleton = new JRE13ExitHookManager();
            } else if (JRE_SUN_SIGNAL_COMPATIBLE) {
                s_singleton = new SunJREExitHookManager();
            } else {
                throw new UnsupportedOperationException("no shutdown hook manager available [JVM: " + Property.getSystemFingerprint() + "]");
            }
        }
        return s_singleton;
    }

    protected ExitHookManager() {
    }

    private static final class JRE13ExitHookManager extends ExitHookManager {

        public synchronized boolean addExitHook(final Runnable runnable) {
            if ((runnable != null) && !m_exitThreadMap.containsKey(runnable)) {
                final Thread exitThread = new Thread(runnable, IAppConstants.APP_NAME + " shutdown handler thread");
                try {
                    Runtime.getRuntime().addShutdownHook(exitThread);
                    m_exitThreadMap.put(runnable, exitThread);
                    return true;
                } catch (Exception e) {
                    System.out.println("exception caught while adding a shutdown hook:");
                    e.printStackTrace(System.out);
                }
            }
            return false;
        }

        public synchronized boolean removeExitHook(final Runnable runnable) {
            if (runnable != null) {
                final Thread exitThread = (Thread) m_exitThreadMap.get(runnable);
                if (exitThread != null) {
                    try {
                        Runtime.getRuntime().removeShutdownHook(exitThread);
                        m_exitThreadMap.remove(runnable);
                        return true;
                    } catch (Exception e) {
                        System.out.println("exception caught while removing a shutdown hook:");
                        e.printStackTrace(System.out);
                    }
                }
            }
            return false;
        }

        JRE13ExitHookManager() {
            m_exitThreadMap = new HashMap();
        }

        private final Map m_exitThreadMap;
    }

    private static final class SunJREExitHookManager extends ExitHookManager {

        public synchronized boolean addExitHook(final Runnable runnable) {
            if ((runnable != null) && !m_signalHandlerMap.containsKey(runnable)) {
                final INTSignalHandler handler = new INTSignalHandler(runnable);
                try {
                    handler.register();
                    m_signalHandlerMap.put(runnable, handler);
                    return true;
                } catch (Throwable t) {
                    System.out.println("exception caught while adding a shutdown hook:");
                    t.printStackTrace(System.out);
                }
            }
            return false;
        }

        public synchronized boolean removeExitHook(final Runnable runnable) {
            if (runnable != null) {
                final INTSignalHandler handler = (INTSignalHandler) m_signalHandlerMap.get(runnable);
                if (handler != null) {
                    try {
                        handler.unregister();
                        m_signalHandlerMap.remove(runnable);
                        return true;
                    } catch (Exception e) {
                        System.out.println("exception caught while removing a shutdown hook:");
                        e.printStackTrace(System.out);
                    }
                }
            }
            return false;
        }

        SunJREExitHookManager() {
            m_signalHandlerMap = new HashMap();
        }

        private final Map m_signalHandlerMap;
    }

    private static final class INTSignalHandler implements SignalHandler {

        public synchronized void handle(final Signal signal) {
            if (m_runnable != null) {
                try {
                    m_runnable.run();
                } catch (Throwable ignore) {
                }
            }
            m_runnable = null;
            if ((m_previous != null) && (m_previous != SIG_DFL) && (m_previous != SIG_IGN)) {
                try {
                    m_previous.handle(signal);
                } catch (Throwable ignore) {
                }
            } else {
                System.exit(0);
            }
        }

        INTSignalHandler(final Runnable runnable) {
            m_runnable = runnable;
        }

        synchronized void register() {
            m_previous = Signal.handle(new Signal("INT"), this);
        }

        synchronized void unregister() {
            m_runnable = null;
        }

        private Runnable m_runnable;

        private SignalHandler m_previous;
    }

    private static ExitHookManager s_singleton;
}
