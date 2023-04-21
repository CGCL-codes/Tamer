package org.snipsnap.jetty;

import org.apache.xmlrpc.WebServer;
import org.jdesktop.jdic.desktop.Desktop;
import org.mortbay.http.HttpListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.mortbay.util.InetAddrPort;
import org.mortbay.util.MultiException;
import org.snipsnap.xmlrpc.AdminXmlRpcHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Application Server
 *
 * @author Matthias L. Jugel
 * @version $Id: ApplicationServer.java 1802 2005-03-14 14:44:53Z stephan $
 */
public class ApplicationServer {

    protected static Properties serverConf;

    protected static Server jettyServer;

    /**
   * Start application server.
   */
    public static void main(String args[]) {
        serverConf = new Configuration();
        System.setProperty(Configuration.VERSION, serverConf.getProperty(Configuration.VERSION, "<unknown version>"));
        String enc = serverConf.getProperty(Configuration.ENCODING, "UTF-8");
        System.setProperty("file.encoding", enc);
        System.setProperty("org.mortbay.util.URI.charset", enc);
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                Shutdown.shutdown();
            }
        });
        printCopyright();
        parseArguments(args);
        startJettyServer();
        startXmlRpcServer();
        String webAppRoot = serverConf.getProperty(Configuration.WEBAPP_ROOT);
        System.out.println(">> Applications: " + new File(webAppRoot).getAbsolutePath());
        URL installerURL = ApplicationServer.class.getResource("/snipsnap-installer.war");
        if (installerURL != null) {
            try {
                WebApplicationContext context = jettyServer.addWebApplication("/install", installerURL.toString());
                context.setAttribute("server.config", serverConf);
                context.start();
            } catch (Exception e) {
                System.out.println("INFO: SnipSnap Installer missing ...");
                e.printStackTrace();
            }
        }
        int errors = WebAppLoader.loadApplications(serverConf.getProperty(Configuration.WEBAPP_ROOT));
        if (jettyServer.getContexts().length == 0) {
            System.out.println("FATAL: No web applications found, shutting down ...");
            System.exit(0);
        }
        if (errors == 0 && WebAppLoader.getApplicationCount() == 0) {
            System.out.println("ATTENTION: Application Server is still unconfigured!");
            System.out.println("ATTENTION: Point your browser to the following address:");
            HttpListener listener = jettyServer.getListeners()[0];
            String host = listener.getHost();
            if (InetAddrPort.__0_0_0_0.equals(host)) {
                try {
                    host = InetAddress.getLocalHost().getCanonicalHostName();
                } catch (UnknownHostException e) {
                    host = System.getProperty("host", "localhost");
                }
            }
            String url = "http://" + host + ":" + listener.getPort() + "/install/" + serverConf.getProperty(Configuration.ADMIN_PASS, "");
            System.out.println("ATTENTION: " + url);
            System.out.println("ATTENTION: To force setup of a specific host and port add '?expert=true'");
            System.out.println("ATTENTION: to the above URL.");
            String system = System.getProperty("os.name");
            if (system.startsWith("Mac OS X")) {
                try {
                    Runtime.getRuntime().exec("/usr/bin/open " + url);
                } catch (IOException e) {
                    System.err.println("ApplicationServer: unable to execute open web browser on MacOS X");
                }
            } else {
                try {
                    Desktop.browse(new URL(url));
                } catch (Exception e) {
                    System.err.println("ApplicationServer: unable to open web browser on " + system);
                } catch (Error err) {
                    System.err.println("ApplicationServer: unable to open web browser on " + system);
                }
            }
        } else {
            System.out.println(WebAppLoader.getApplicationCount() + " applications loaded and running (" + errors + " errors).");
        }
    }

    /**
   * Start the XML-RPC server for handling the standalone jetty server.
   */
    private static void startXmlRpcServer() {
        try {
            URL xmlRpcServerUrl = new URL(serverConf.getProperty(Configuration.ADMIN_URL));
            WebServer xmlRpcServer = new WebServer(xmlRpcServerUrl.getPort());
            xmlRpcServer.addHandler("$default", new AdminXmlRpcHandler(serverConf));
            xmlRpcServer.start();
        } catch (Exception e) {
            System.out.println("ERROR: can't start administrative server interface (XML-RPC): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
   * Start the jetty web server.
   */
    private static void startJettyServer() {
        try {
            jettyServer = new Server(getResource("/conf/jetty.xml", "conf/jetty.xml"));
            jettyServer.start();
        } catch (IOException e) {
            System.err.println("ApplicationServer: warning: admin server configuration not found: " + e);
        } catch (MultiException e) {
            Iterator exceptions = e.getExceptions().iterator();
            while (exceptions.hasNext()) {
                Exception ex = (Exception) exceptions.next();
                ex.printStackTrace();
                System.out.println("ERROR: can't start server: " + ex.getMessage());
            }
            System.exit(-1);
        }
    }

    private static void printCopyright() {
        System.out.println("SnipSnap Application Server " + System.getProperty(Configuration.VERSION));
        InputStream in = null;
        try {
            in = ApplicationServer.class.getResourceAsStream("/conf/copyright.txt");
            BufferedReader copyrightReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = copyrightReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (Throwable ignore) {
            }
        }
    }

    /**
   * Parse argument from command line and exit in case of erroneous options.
   * The server will immedialy exit if there is a problem and display a usage message
   *
   * @param args the command line arguments
   */
    private static void parseArguments(String args[]) {
        for (int i = 0; i < args.length; i++) {
            if ("-help".equals(args[i])) {
                usage("");
                System.exit(0);
            }
            if ("-root".equals(args[i])) {
                if (args.length >= i + 1 && !args[i + 1].startsWith("-")) {
                    serverConf.put(Configuration.WEBAPP_ROOT, args[++i]);
                } else {
                    usage("an argument is required for -root");
                }
            } else if ("-url".equals(args[i])) {
                if (args.length >= i + 1 && !args[i + 1].startsWith("-")) {
                    serverConf.put(Configuration.ADMIN_URL, args[++i]);
                } else {
                    usage("an argument is required for -url");
                }
            }
        }
    }

    /**
   * Get a resource from file if the file exists, else just return the jar resource.
   *
   * @param jarResource  the jar file resource (fallback)
   * @param fileResource the file resource
   */
    private static URL getResource(String jarResource, String fileResource) {
        File file = new File(fileResource);
        URL url = null;
        if (file.exists()) {
            try {
                url = file.toURL();
            } catch (MalformedURLException e) {
                System.err.println("Warning: unable to load '" + file + "': " + e.getMessage());
            }
        }
        if (null == url) {
            url = ApplicationServer.class.getResource(jarResource);
        }
        return url;
    }

    /**
   * Display a a message in addition to a usage message.
   *
   * @param message an additional informational text
   */
    private static void usage(String message) {
        System.out.println(message);
        System.out.println("usage: " + ApplicationServer.class.getName() + " [-root <dir>] [-url <url>]");
        System.out.println("  -root       directory, where to find the applications for this server");
        System.out.println("  -url        URL, admin server URL (http://host:port/)");
        System.out.println("  -help       this help text");
        System.exit(0);
    }
}
