package hambo.mdad;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import javax.mail.Session;
import java.util.Properties;
import hambo.util.SubProperties;
import hambo.svc.ServiceManagerException;
import hambo.messaging.Filters;
import hambo.messaging.Messaging;
import hambo.svc.*;
import hambo.svc.log.*;
import com.lutris.appserver.server.Enhydra;
import com.lutris.util.Config;
import com.lutris.util.ConfigFile;
import com.lutris.appserver.server.StandardApplication;
import com.lutris.logging.StandardLogger;

/**
 * DeliveryDaemon is the allways-running Java part of the hambo MDA.  The job
 * of this class is to wait on a socket and start a ClientThread for each
 * incoming message.
 */
public class DeliveryDaemon extends ServerSocket {

    /** 
     * The port no is the characters MDA used as an integer, -5060000, so
     * it's not too large. Linux seems to have a really low maxport number.
     */
    private static int PORTNO = 3745;

    private static MyApplication app = null;

    private Log log;

    public DeliveryDaemon() throws Exception {
        super(PORTNO);
        Properties prop = new Properties();
        prop.load(new FileInputStream("conf/mdad.prop"));
        registerWithEnhydra(prop);
        log = LogServiceManager.getLog("mail");
        ClientThread.init(log, new SubProperties(prop, "mdad.clientthread"));
        Messaging.init(new SubProperties(prop, "Messaging"));
    }

    public void mainLoop() {
        Properties props = new Properties();
        Session session = Session.getInstance(props, null);
        while (true) {
            try {
                Socket clientsock = accept();
                ClientThread client = new ClientThread(session, clientsock);
                client.start();
            } catch (Exception err) {
                log.println(Log.ERROR, "Failed to start ClientThread", err);
            }
        }
    }

    /**
     * Main called from outside. Creates a DeliveryDaemon and enters its
     * mainLoop.
     * @param args command line arguments, not used.
     */
    public static void main(String[] args) {
        try {
            DeliveryDaemon daemon = new DeliveryDaemon();
            daemon.mainLoop();
        } catch (Exception err) {
            System.err.println("Fatal error starting MDAD: " + err);
            err.printStackTrace(System.err);
            System.err.println("Giving up.  MDAD not started.");
        }
    }

    /**
     * Register a new application with enhydra and load HAMBO services.  This
     * has to be done before doing anything that requires database of log
     * info.
     */
    static void registerWithEnhydra(Properties prop) throws IOException, ServiceManagerException, com.lutris.util.ConfigException, com.lutris.appserver.server.ApplicationException {
        if (app == null) {
            app = new MyApplication();
            String configLocation = (String) prop.get("enhydra.config");
            app.setConfig((new ConfigFile(new File(configLocation))).getConfig());
            String logLocation = (String) prop.get("enhydra.logfile");
            File logFile = new File(logLocation);
            String[] levels = { "ERROR", "HAMBO_ERROR", "HAMBO_INFO", "HAMBO_DEBUG1", "HAMBO_DEBUG2", "HAMBO_DEBUG3" };
            StandardLogger logger = new StandardLogger(true);
            logger.configure(logFile, new String[] {}, levels);
            app.setLogChannel(logger.getChannel(""));
            app.startup(app.getConfig());
            Enhydra.register(app);
            ServiceManagerLoader loader = new ServiceManagerLoader(prop);
            loader.loadServices();
        }
    }

    /**
      * Register the current thread with the application of this service. This
      * <em>must</em> be called before each thread uses the service system.
      */
    public static void register() {
        Enhydra.register(app);
    }

    /**
      * Register the current thread with the application of this service.  This
      * <em>must</em> be called before a registered thread terminates.
      */
    public static void unRegister() {
        Enhydra.unRegister();
    }

    static class MyApplication extends StandardApplication {

        public void setConfig(Config config) {
            this.config = config;
        }
    }
}
