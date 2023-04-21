package org.nightlabs.jfire.shutdownafterstartup;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.nightlabs.jfire.servermanager.JFireServerManagerFactory;
import org.nightlabs.jfire.servermanager.ra.JFireServerManagerFactoryImpl;

public class ShutdownAfterStartupManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ShutdownAfterStartupManager.class);

    private String shutdownAfterStartupManagerInstanceID = Long.toString(System.currentTimeMillis(), 36);

    private int lastShutdownControlHandleID = 0;

    private Set<ShutdownControlHandle> shutdownControlHandles = new HashSet<ShutdownControlHandle>();

    private JFireServerManagerFactoryImpl jfireServerManagerFactoryImpl;

    public ShutdownAfterStartupManager(JFireServerManagerFactoryImpl jfireServerManagerFactoryImpl) {
        this.jfireServerManagerFactoryImpl = jfireServerManagerFactoryImpl;
    }

    public synchronized ShutdownControlHandle createShutdownControlHandle() {
        ShutdownControlHandle handle = new ShutdownControlHandle(shutdownAfterStartupManagerInstanceID, ++lastShutdownControlHandleID);
        shutdownControlHandles.add(handle);
        return handle;
    }

    public synchronized void shutdown(ShutdownControlHandle shutdownControlHandle) {
        if (!shutdownControlHandles.remove(shutdownControlHandle)) throw new IllegalArgumentException("Unknown shutdownControlHandle: " + shutdownControlHandle);
        if (shutdownControlHandles.isEmpty()) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }
                    String shutdownAfterStartup = System.getProperty(JFireServerManagerFactory.class.getName() + ".shutdownAfterStartup");
                    if (Boolean.TRUE.toString().equals(shutdownAfterStartup)) {
                        try {
                            jfireServerManagerFactoryImpl.getJ2EEVendorAdapter().shutdown();
                        } catch (Throwable x) {
                            logger.error("shutdown via JavaEE-vendor-adapter failed!", x);
                        }
                    }
                }
            }.start();
        }
    }
}
