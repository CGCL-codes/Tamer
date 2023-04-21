package org.jsonhttpd;

import org.jsonhttpd.customhandler.TestHandler;
import org.jsonhttpd.jsonrpc.services.ExampleService;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Viktor Nordell on 2010-mar-21
 */
public class Main {

    public static String webRoot = null;

    public static Level logLevel = Level.INFO;

    public static boolean noDemo = false;

    public static int port = 8080;

    public static void printHelp() {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("\njson-httpd version: 0.7.0 by Viktor Nordell <viktor.nordell@gmail.com>\n\n");
        builder.append("  -p --port    \t Set the port on which the web server will listen on.\n\t\t (default: 8080)\n");
        builder.append("  -r --webroot \t Set the web root location. (default: ./webroot)\n");
        builder.append("  -d --debug   \t Set the debug level, valid values are (default: NORMAL): \n");
        builder.append("\t\t SEVERE\n");
        builder.append("\t\t WARNING\n");
        builder.append("\t\t INFO\n");
        builder.append("\t\t CONFIG\n");
        builder.append("\t\t FINE\n");
        builder.append("\t\t FINER\n");
        builder.append("\t\t FINEST\n");
        builder.append("  -n --nodemo   \t Disable the demo services.\n");
        builder.append("  -h --help    \t Prints this text.\n");
        System.out.print(builder);
    }

    public static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String nextArg = null;
            if ((i + 1) < args.length) nextArg = args[i + 1];
            if (arg.equals("--port") || arg.equals("-p") && nextArg != null) {
                try {
                    port = Integer.parseInt(nextArg);
                    i++;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port, not a number.");
                    printHelp();
                    System.exit(1);
                }
                if (port < 10 || port > 65536) {
                    System.out.println("Invalid port, number is not in port range.");
                    printHelp();
                    System.exit(1);
                }
            } else if (arg.equals("--webroot") || arg.equals("-r") && nextArg != null) {
                if (!new File(nextArg).exists()) {
                    System.out.println("Invalid web root, cant find folder.");
                    printHelp();
                    System.exit(1);
                } else {
                    webRoot = nextArg;
                    i++;
                }
            } else if (arg.equals("--debug") || arg.equals("-d") && nextArg != null) {
                try {
                    logLevel = Level.parse(nextArg);
                    i++;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid log level.");
                    printHelp();
                    System.exit(1);
                }
            } else if (arg.equals("--nodemo") || arg.equals("-n")) {
                noDemo = true;
            } else {
                printHelp();
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        Logger logger;
        HTTPServer httpd;
        parseArgs(args);
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        try {
            httpd = new HTTPServer(port, 20);
            logger.setLevel(logLevel);
            logger.setUseParentHandlers(false);
            httpd.setLogger(logger);
            if (!noDemo) {
                httpd.addService(new ExampleService());
                httpd.addCustomHandler(new TestHandler("/test"));
            }
            if (webRoot != null) {
                httpd.setWebroot(webRoot);
            }
            httpd.start();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create server (" + e.getMessage() + ")");
        }
    }
}
