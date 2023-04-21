package xlwrap;

import java.net.InetAddress;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.langegger.xlwrap.common.Config;
import at.langegger.xlwrap.common.LogBufferDispatcher;

/**
 * @author dorgon
 *
 */
public class server {

    private static Options opts;

    private static final Logger log = LoggerFactory.getLogger(server.class);

    private static Server server;

    private static WebAppContext context;

    private server(int port) {
        LogBufferDispatcher.init(Config.DEFAULT_LOGBUFFER_LINES);
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignore) {
            hostname = "localhost";
        }
        log.info("Starting XLWrap Server at " + hostname + ":" + port + " ...");
        try {
            server = new Server(port);
            context = new WebAppContext(server, Config.CONTEXT_DIR, "");
            server.addHandler(context);
            server.start();
            while (server != null && server.isStarting()) try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
            Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        } catch (Exception e) {
            log.error("Failed to start XLWrap server.", e);
            shutdown();
        }
    }

    public static void main(String[] args) {
        Option port = new Option("p", "port", true, "port");
        port.setArgName("port");
        port.setOptionalArg(true);
        Option watch = new Option("w", "watch", false, "watch this directory for mapping files (*.trig), default: mapping");
        watch.setArgName("dir");
        watch.setOptionalArg(true);
        Option infer = new Option("i", "inference", false, "inference, default: false");
        infer.setOptionalArg(true);
        Option help = new Option("h", "help", false, "help");
        help.setOptionalArg(true);
        opts = new Options();
        opts.addOption(port);
        opts.addOption(infer);
        opts.addOption(help);
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(opts, args);
            if (cmd.hasOption("h")) {
                printUsage(null);
            } else {
                if (cmd.hasOption("i")) Config.setInference(true);
                if (cmd.hasOption("w")) Config.setWatchDirectory(cmd.getOptionValue("w"));
                int p = cmd.hasOption("p") ? Integer.parseInt((String) cmd.getOptionValue("p")) : Config.DEFAULT_PORT;
                new server(p);
            }
        } catch (Exception e) {
            printUsage(e.getMessage());
        }
    }

    public static void shutdown() {
        if (context != null) try {
            context.stop();
        } catch (Exception ignore) {
        }
        if (server != null) try {
            server.stop();
        } catch (Exception ignore) {
        }
    }

    class ShutdownThread extends Thread {

        public void run() {
            shutdown();
        }
    }

    private static void printUsage(String msg) {
        System.out.println("XLWrap server");
        if (msg != null) System.out.println(msg + '\n');
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("parameters: ", opts);
    }
}
