package de.schwarzrot.app.support;

import java.io.File;
import java.net.InetAddress;
import java.util.List;
import de.schwarzrot.SRConstants;
import de.schwarzrot.app.Service;
import de.schwarzrot.app.config.ServiceConfig;
import de.schwarzrot.data.access.UserNameProvider;
import de.schwarzrot.jar.JarExtension;
import de.schwarzrot.jar.JarExtensionHandler;

/**
 * a helper class to start background/system services. This class provides
 * property parsing, logfile setup and instantiation of the real service
 * instance.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * @param <C>
 *            - config type for service configuration
 * 
 */
public class ServiceLauncher<C extends ServiceConfig> extends AbstractApplicationLauncher<C> {

    public static final String SERVICE_START_TOKEN = "ok, logger configured, let's move to underworld";

    public static final String KEY_CLASSPATH = "java.class.path";

    public static final String KEY_JAR_FILENAME = "service.jar.filename";

    public static final String KEY_PID_FILE = "service.pidfile";

    public static final String KEY_SLEEP_TIME = "service.sleep.time";

    private static boolean debug = false;

    public ServiceLauncher(Service<C> service, String[] appContextFiles, List<String> appArgs) {
        super(service, appContextFiles, appArgs);
        init();
    }

    public void init() {
        assert getService() != null : "invalid setup - need a service to start!";
        if (this.getStarter().getAppArgs().containsKey("debug")) ServiceLauncher.setDebug(true);
        try {
            setupConfig();
            daemonize();
            getService().setupEarly();
        } catch (Throwable t) {
            getLogger().error("startup failed", t);
        }
    }

    /**
     * a shutdown request for a service may occure from inside (the service) or
     * from outside (the operating system). This class cares about the outside
     * view of the service.
     * 
     * @return whether the service should stop
     */
    public boolean isShutdownRequested() {
        return shutdownRequested || getService().isShutdownRequested();
    }

    /**
     * the mainloop of all services. The service itself should not loop itself
     * to be able to shutdown gracefully.
     */
    @Override
    public void start() {
        mainThread = Thread.currentThread();
        try {
            getLogger().warn("initialize environment for service ...");
            super.start();
        } catch (Throwable t) {
            getLogger().error("failed to setup environment for service!", t);
        }
        ApplicationServiceProvider.registerService(UserNameProvider.class, getService());
        getService().setupLate();
        while (!isShutdownRequested()) {
            try {
                getLogger().info("let the service work a little bit ...");
                getService().run();
            } catch (Throwable t) {
                getLogger().error("request-processing failed:", t);
            } finally {
                if (getService().isIdle()) {
                    try {
                        getLogger().info("I'm so tired (" + sleepTime + ") ...");
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        getLogger().warn("Ok, someone want's me to wakeup ...");
                    }
                }
            }
        }
        getLogger().fatal("time to say: good bye!");
        System.exit(0);
    }

    /**
     * detach the service, so it can run in the background
     */
    protected void daemonize() {
        System.out.flush();
        if (getService().getConfig().getLocalAddress() != null) {
            getLogger().warn("service host: " + getService().getConfig().getLocalAddress().getCanonicalHostName());
            getLogger().warn("service addr: " + getService().getConfig().getLocalAddress().getHostAddress());
        }
        System.out.println(SERVICE_START_TOKEN);
        if (!debug) {
            getLogger().error(SERVICE_START_TOKEN);
            System.out.close();
            System.err.close();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                shutdown();
            }
        });
    }

    protected final Service<C> getService() {
        return (Service<C>) getStarter();
    }

    /**
     * accessor to the main thread of the service
     * 
     * @return the services main thread
     */
    protected Thread getServiceManagerThread() {
        return mainThread;
    }

    /**
     * process common service parameters from jvms system properties
     */
    protected void setupConfig() {
        String pidFN = System.getProperty(KEY_PID_FILE);
        String myself = System.getProperty(KEY_JAR_FILENAME);
        String tmp = System.getProperty(KEY_SLEEP_TIME);
        InetAddress addr = null;
        shutdownRequested = true;
        if (pidFN != null && !pidFN.isEmpty()) {
            pidFile = new File(pidFN);
            pidFile.deleteOnExit();
        }
        if (tmp != null && !tmp.isEmpty()) sleepTime = Integer.valueOf(tmp) * 1000;
        if (myself == null) myself = System.getProperty(KEY_CLASSPATH);
        if (myself == null) {
            throw new UnsupportedOperationException("HU? - this is not a proper usage!");
        }
        File myArch = new File(myself);
        if (!ServiceLauncher.debug && !(myArch.exists() && myArch.isFile() && myArch.canRead())) throw new UnsupportedOperationException("change your playground, kiddies!");
        JarExtensionHandler jeh = new JarExtensionHandler();
        JarExtension je = jeh.verifyArchive(myArch);
        if (!ServiceLauncher.debug) {
            if (je.getType().compareTo(SRConstants.JAR_SERVICE_TYPE) != 0) throw new UnsupportedOperationException("invalid setup!");
            getService().getConfig().setVersion(je.getVersion());
            getService().getConfig().setArchive(je);
        }
        try {
            addr = InetAddress.getLocalHost();
        } catch (Exception e) {
        }
        if (addr != null) {
            getService().getConfig().setLocalAddress(addr);
            shutdownRequested = false;
        }
    }

    /**
     * initiate the shutdown procedure.
     */
    protected void shutdown() {
        shutdownRequested = true;
        getLogger().warn("someone want's me to die!");
        mainThread.interrupt();
    }

    public static final boolean isDebug() {
        return debug;
    }

    public static final void setDebug(boolean debug) {
        ServiceLauncher.debug = debug;
    }

    private volatile boolean shutdownRequested = false;

    private int sleepTime = 1000;

    private Thread mainThread;

    private File pidFile;
}
