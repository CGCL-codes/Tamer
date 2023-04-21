package tornado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import nl.chess.it.util.config.ConfigValidationResult;
import tornado.requestHandler.RequestHandlerFactory;

public class Tornado {

    private ServerPool serverPool;

    private ThreadManager threadManager;

    /** Interface to the config file. */
    private static Configuration config = null;

    /** Interface to the logging subsystem. */
    public static Logger log = null;

    /** Looks up the MIME type for a specified file extension. */
    public static MIMEDictionary mime = null;

    /** Mapping of the registered request handlers */
    private static HashMap<String, RequestHandlerFactory> RequestHandler;

    private final OptionSet commandLineOptions;

    /**
	 * Constructs a server with the specified options. The server is prepared
	 * for production state, but it is fully started: we start
	 * <code>ServerThread</code>s, but don't bind to a local port. It is passed
	 * the command-line arguments specified, and it processes these.
	 * 
	 * @see #start()
	 */
    public Tornado(String[] args) {
        OptionParser parser = new OptionParser("c:");
        commandLineOptions = parser.parse(args);
        RequestHandler = new HashMap<String, RequestHandlerFactory>();
    }

    /**
	 * Bootup the server from the console interface. This is very simple - it
	 * just creates a new instance of <code>Tornado</code> and starts it.
	 * 
	 * @see #Tornado(String[])
	 */
    public static void main(String[] args) {
        Tornado server = new Tornado(args);
        server.execute();
    }

    public void execute() {
        String confDir = "conf";
        if (commandLineOptions.hasArgument("c")) confDir = (String) commandLineOptions.valueOf("c");
        if (Tornado.getConfig(confDir) != null) {
            log = new Logger();
            serverPool = new ServerPool(config.getStartThreads());
            threadManager = new ThreadManager(serverPool);
            mime = new MIMEDictionary(config.getMimeTypes());
            int[] ports = Tornado.config.getPorts();
            for (int i = 0; i < ports.length; ++i) {
                Thread t = new ListenThread(serverPool, ports[i]);
                t.start();
            }
            System.out.println("Tornado is ready to accept connections");
            threadManager.run();
        } else {
            log.alert("Unable to load configuration");
        }
    }

    /**
	 * Registers a request handler which implemets the
	 * <code>RequestHandlerInterface</code> The pattern is used to determin
	 * which request will be handled by this handler.
	 */
    public static void registerRequestHandler(String pattern, RequestHandlerFactory interf) {
        Tornado.getRequestHandler().put(pattern, interf);
    }

    public static Configuration getConfig() {
        return getConfig(".");
    }

    public static Configuration getConfig(final String path) {
        if (config == null) {
            String file = path + File.separator + Configuration.RESOURCE_NAME;
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(file));
                prop.setProperty("configurationDir", path);
                config = new Configuration(prop);
                ConfigValidationResult configResult = config.validateConfiguration();
                if (configResult.thereAreErrors()) {
                    System.out.println("Errors in configuration");
                    for (Iterator iter = configResult.getErrors().iterator(); iter.hasNext(); ) {
                        System.out.println(" > " + iter.next());
                    }
                    System.exit(1);
                }
                prop.store(new FileOutputStream(file), "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public static HashMap<String, RequestHandlerFactory> getRequestHandler() {
        return RequestHandler;
    }
}
