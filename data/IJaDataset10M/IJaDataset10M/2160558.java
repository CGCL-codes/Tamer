package org.netuno;

import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * Services Monitor.
 * @author eduveks
 */
public class ServiceMonitor implements Cloneable, Runnable {

    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(ServiceMonitor.class);

    /**
     * Thread Runner.
     */
    private Thread runner = null;

    /**
     * All Services Thread.
     */
    private static Service[] services = new Service[Config.getThreads()];

    /**
     * Allow New Connections.
     */
    private static boolean allowNewConnections = true;

    /**
     * Services Monitor.
     */
    public ServiceMonitor() {
        for (int x = 0; x < services.length; x++) {
            services[x] = new Service(x);
        }
    }

    /**
     * Get Service.
     * @return Service
     */
    public static Service getService() {
        Service service = null;
        for (int x = 0; x < services.length; x++) {
            if (services[x].sleeping()) {
                service = services[x];
                break;
            }
        }
        if (service == null) {
            logger.error("All threads busy!");
        }
        return service;
    }

    /**
     * Get Services.
     * @return Services
     */
    public static Service[] getServices() {
        return services;
    }

    /**
     * Set allow new connections.
     * @param v Allow connections
     */
    public static void setAllowNewConnections(boolean v) {
        allowNewConnections = v;
    }

    /**
     * Get allow new connections.
     * @return Allow connections
     */
    public static boolean isAllowNewConnections() {
        return allowNewConnections;
    }

    /**
     * Run.
     */
    public final void run() {
        while (true) {
            try {
                Socket clientSocket = Config.getServerSocket().accept();
                new ServiceLoad(clientSocket).start();
            } catch (Exception e) {
                logger.error("Client connection.", e);
            }
        }
    }

    /**
     * Start.
     */
    public final synchronized void go() {
        if (runner == null) {
            runner = new Thread(this);
        }
        runner.start();
    }

    /**
     * Time Out.
     */
    public static synchronized void timeout() {
        for (int x = 0; x < services.length; x++) {
            if (!services[x].sleeping() && services[x].getTime() > 0 && System.currentTimeMillis() - services[x].getTime() > Config.getTimeOut()) {
                services[x].finish();
            }
        }
    }

    /**
     * Terminate the service and mark the time.
     */
    public final synchronized void finish() {
        try {
            runner.interrupt();
        } catch (Exception e) {
            logger.error("Thread interrupt.", e);
        }
    }
}
