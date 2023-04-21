package org.exist;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.avalon.excalibur.cli.CLArgsParser;
import org.apache.avalon.excalibur.cli.CLOption;
import org.apache.avalon.excalibur.cli.CLOptionDescriptor;
import org.apache.avalon.excalibur.cli.CLUtil;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpc;
import org.exist.http.HttpServer;
import org.exist.storage.BrokerPool;
import org.exist.util.Configuration;
import org.exist.xmldb.ShutdownListener;
import org.exist.xmlrpc.AuthenticatedHandler;

/**
 *  Main class to start the stand-alone server. By default,
 *  an XML-RPC listener is started at port 8081. The HTTP server
 *  will be available at port 8088. Use command-line options to
 *  change this.
 *  
 *@author     Wolfgang Meier <meier@ifs.tu-darmstadt.de>
 */
public class Server {

    private static final int HELP_OPT = 'h';

    private static final int DEBUG_OPT = 'd';

    private static final int HTTP_PORT_OPT = 'p';

    private static final int XMLRPC_PORT_OPT = 'x';

    private static final int THREADS_OPT = 't';

    private static WebServer webServer;

    private static HttpServer http;

    private static final CLOptionDescriptor OPTIONS[] = new CLOptionDescriptor[] { new CLOptionDescriptor("help", CLOptionDescriptor.ARGUMENT_DISALLOWED, HELP_OPT, "print help on command line options and exit."), new CLOptionDescriptor("debug", CLOptionDescriptor.ARGUMENT_DISALLOWED, DEBUG_OPT, "debug XMLRPC calls."), new CLOptionDescriptor("http-port", CLOptionDescriptor.ARGUMENT_REQUIRED, HTTP_PORT_OPT, "set HTTP port."), new CLOptionDescriptor("xmlrpc-port", CLOptionDescriptor.ARGUMENT_REQUIRED, XMLRPC_PORT_OPT, "set XMLRPC port."), new CLOptionDescriptor("threads", CLOptionDescriptor.ARGUMENT_REQUIRED, THREADS_OPT, "set max. number of parallel threads allowed by the db.") };

    /**
     *  Main method to start the stand-alone server.
     *
     *@param  args           Description of the Parameter
     *@exception  Exception  Description of the Exception
     */
    public static void main(String args[]) throws Exception {
        printNotice();
        CLArgsParser optParser = new CLArgsParser(args, OPTIONS);
        if (optParser.getErrorString() != null) {
            System.err.println("ERROR: " + optParser.getErrorString());
            return;
        }
        List opt = optParser.getArguments();
        int size = opt.size();
        CLOption option;
        int httpPort = 8088;
        int rpcPort = 8081;
        int threads = 5;
        for (int i = 0; i < size; i++) {
            option = (CLOption) opt.get(i);
            switch(option.getId()) {
                case HELP_OPT:
                    printHelp();
                    return;
                case DEBUG_OPT:
                    XmlRpc.setDebug(true);
                    break;
                case HTTP_PORT_OPT:
                    try {
                        httpPort = Integer.parseInt(option.getArgument());
                    } catch (NumberFormatException e) {
                        System.err.println("option -p requires a numeric argument");
                        return;
                    }
                    break;
                case XMLRPC_PORT_OPT:
                    try {
                        rpcPort = Integer.parseInt(option.getArgument());
                    } catch (NumberFormatException e) {
                        System.err.println("option -x requires a numeric argument");
                        return;
                    }
                    break;
                case THREADS_OPT:
                    try {
                        threads = Integer.parseInt(option.getArgument());
                    } catch (NumberFormatException e) {
                        System.err.println("option -t requires a numeric argument");
                        return;
                    }
                    break;
            }
        }
        String pathSep = System.getProperty("file.separator", "/");
        String home = System.getProperty("exist.home");
        if (home == null) home = System.getProperty("user.dir");
        System.out.println("loading configuration from " + home + pathSep + "conf.xml");
        Configuration config = new Configuration("conf.xml", home);
        BrokerPool.configure(1, threads, config);
        BrokerPool.getInstance().registerShutdownListener(new ShutdownListenerImpl());
        System.out.println("starting HTTP listener at port " + httpPort);
        http = new HttpServer(config, httpPort, 1, threads);
        http.start();
        System.out.println("starting XMLRPC listener at port " + rpcPort);
        XmlRpc.setEncoding("UTF-8");
        webServer = new WebServer(rpcPort);
        AuthenticatedHandler handler = new AuthenticatedHandler(config);
        webServer.addHandler("$default", handler);
        webServer.start();
        System.err.println("waiting for connections ...");
    }

    public static void shutdown() {
        System.err.println("Shutdown ...");
        webServer.shutdown();
        http.shutdown();
        try {
            http.join();
        } catch (InterruptedException e) {
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java " + Server.class.getName() + " [options]");
        System.out.println(CLUtil.describeOptions(OPTIONS).toString());
    }

    public static void printNotice() {
        System.out.println("eXist version 1.0, Copyright (C) 2004 Wolfgang Meier");
        System.out.println("eXist comes with ABSOLUTELY NO WARRANTY.");
        System.out.println("This is free software, and you are welcome to " + "redistribute it\nunder certain conditions; " + "for details read the license file.\n");
    }

    static class ShutdownListenerImpl implements ShutdownListener {

        public void shutdown(String dbname, int remainingInstances) {
            if (remainingInstances == 0) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    public void run() {
                        System.out.println("killing threads ...");
                        http.shutdown();
                        http.interrupt();
                        webServer.shutdown();
                        System.exit(0);
                    }
                }, 1000);
            }
        }
    }
}
