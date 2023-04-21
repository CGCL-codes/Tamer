package mobi.ilabs.restroom;

import mobi.ilabs.EILog;
import mobi.ilabs.Misc;
import mobi.ilabs.restroom.storage.StorageFactory;
import mobi.ilabs.restroom.storage.hibernate.HibernateStorage;
import org.apache.commons.logging.Log;
import org.restlet.Component;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.util.Series;

/**
 * Start up a RestRoom storage facility from parameters given
 * at the command line and in configuration files.
 */
public class RestRoom {

    /**
     * If set and XXX should be settable from config file, 
     * log profusely about SSL setup and operation.
     */
    private static final boolean ENABLE_SSL_DEBUG_TRACING = true;

    /**
     * Logging situations ocuring in this class.
     */
    private static final Log LOG = EILog.getLog(RestRoom.class);

    /**
     * Component accepting connections through the HTTP protocol
     */
    private Component httpComponent;

    /**
     * Component accepting comnnections through the HTTPS protocol.
     */
    private Component httpsComponent;

    /**
     * Initialize using standard connectors for ports.
     *
     * @param ipAddress
     *            IP address to listen on.
     * @throws Exception
     *             when things go bad XXX
     */
    public RestRoom(final String ipAddress) throws Exception {
        this(ipAddress, Protocol.HTTP.getDefaultPort(), Protocol.HTTPS.getDefaultPort());
    }

    /**
     *  The current HTTP port. (-1 indicates value not set)
     */
    private int httpPort = -1;

    /**
     * The current HTTPS port. (-1 indicates value not set)
     */
    private int httpsPort = -1;

    /**
     * Initialize using standard connectors for HTTPS port, and parameterized
     * port for HTTP.
     *
     * @param ipAddress
     *            IP address to listen on.
     * @param http
     *            Port to listen on for HTTP connections
     * @throws Exception
     *             when things go bad XXX
     */
    public RestRoom(final String ipAddress, final int http) throws Exception {
        this(ipAddress, http, Protocol.HTTPS.getDefaultPort());
    }

    /**
     * Constructor.
     *
     * @param ipAddress
     *            IP address to listen on.
     * @param http
     *            Port to listen on for HTTP connections
     * @param https
     *            Port to listen on for HTTPS (SSL) connections
     * @throws Exception
     *             when things go bad XXX
     */
    public RestRoom(final String ipAddress, final int http, final int https) throws Exception {
        this.httpPort = http;
        this.httpsPort = https;
        assert (http < https);
        httpComponent = new Component();
        httpComponent.getLogService().setLoggerName(Config.getLoggerName());
        LOG.debug("RestRoom: Setting up HTTP for port " + http);
        httpComponent.getServers().add(Protocol.HTTP, http);
        httpComponent.getClients().add(Protocol.FILE);
        httpsComponent = new Component();
        httpsComponent.getLogService().setLoggerName(Config.getLoggerName());
        LOG.info("RestRoom: Setting up HTTPS for port " + https);
        final Series<Parameter> httpsParams = httpsComponent.getContext().getParameters();
        final Series<Parameter> httpParams = httpComponent.getContext().getParameters();
        httpsParams.add("keystorePath", Config.getKeystorePath());
        httpsParams.add("keystorePassword", Config.getKeystorePassword());
        httpsParams.add("keyPassword", Config.getKeyPassword());
        httpsParams.add("maxThreads", "255");
        httpParams.add("maxThreads", "255");
        LOG.info("Trying to start HTTPS on port " + https);
        httpsComponent.getServers().add(Protocol.HTTPS, https);
        LOG.info("HTTPS started");
    }

    private void startSubComponent(final Component c, final String name) throws Exception {
        try {
            LOG.debug("About to start component " + name);
            c.start();
            LOG.info("Started component " + name);
        } catch (java.net.BindException e) {
            final String msg = "Could not bind socket for component " + name;
            LOG.error(msg, e);
            throw new RuntimeException(msg);
        }
    }

    private RestRoomApplication httpApp;

    private RestRoomApplication httpsApp;

    /**
     * Start the component.
     *
     */
    public final void start() {
        httpApp = new RestRoomApplication(httpComponent);
        httpComponent.getDefaultHost().attach("", httpApp);
        httpsApp = new RestRoomApplication(httpsComponent);
        httpsComponent.getDefaultHost().attach("", httpsApp);
        try {
            startSubComponent(httpComponent, "HTTP  component, port " + httpPort);
            startSubComponent(httpsComponent, "HTTPS component, port " + httpsPort);
            if (ENABLE_SSL_DEBUG_TRACING) {
                System.setProperty("javax.net.debug", "ssl,handshake,record");
            }
        } catch (final Exception e) {
            LOG.error(Misc.stacktraceToReport("Component caught exception while starting subcomponent", e));
        }
    }

    /**
     * Stop the components.
     */
    public final void stop() {
        try {
            if (httpComponent.isStarted()) {
                httpComponent.stop();
            }
            if (httpsComponent.isStarted()) {
                httpsComponent.stop();
            }
        } catch (final Exception e) {
            System.out.println("Component caught exception while stopping subcomponent  " + e);
            e.printStackTrace();
        }
    }

    /**
     * Print a description on how to use commandline parameters to the standard
     * output.
     *
     */
    public static void printUsage() {
        System.out.println("Usage:  <command> hostname");
    }

    /**
     * Main method.
     * 
     * XXX Move work from main to method (possibly static), and use LOG for crying out loud.
     *
     * @param args
     *            Program arguments.
     */
    public static void main(final String[] args) {
        LOG.info("iLabs storage server");
        if (args.length == 0) {
            printUsage();
        } else {
            try {
                final String persistenceProfile = "production-restroom";
                HibernateStorage.setStorageProfileName(persistenceProfile);
                StorageFactory.enableHibernateStorage();
                final String connectivityErrorMessage = HibernateStorage.probeDatabaseConnectivity(null);
                if (connectivityErrorMessage != null) {
                    throw new RuntimeException("No connectivity to persistence profile \"" + HibernateStorage.getStorageProfileName() + "\": " + connectivityErrorMessage);
                }
                LOG.info("I have got args.length = " + args.length);
                String hostname = "localhost";
                if (args.length > 0) {
                    hostname = args[0];
                }
                int httpPort = Protocol.HTTP.getDefaultPort();
                if (args.length > 1) {
                    httpPort = Integer.parseInt(args[1]);
                }
                int httpsPort = Protocol.HTTPS.getDefaultPort();
                if (args.length > 2) {
                    httpsPort = Integer.parseInt(args[2]);
                }
                LOG.info("Starting server on host '" + hostname + "' port " + httpPort);
                try {
                    final RestRoom component = new RestRoom(hostname, httpPort, httpsPort);
                    component.start();
                } catch (final java.net.BindException e) {
                    final String msg = "Could not bind server port http/https: " + httpPort + "/" + httpsPort + ": " + e.toString();
                    LOG.error(msg, e);
                }
            } catch (final Exception e) {
                final String msg = "Can't launch the web server.\n" + "An unexpected exception occured: ";
                LOG.error(Misc.stacktraceToReport(msg, e), e);
            }
        }
    }
}
