package org.japelserver;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *
 * @author pballeux
 */
public class WebServer {

    private Configuration conf = null;

    private TCPServer tcpServer = null;

    private boolean stopMe = false;

    public WebServer(File configFile) {
        if (configFile == null) {
            conf = new Configuration();
            Logger.info(getClass().getSimpleName(), "Default Configuration loaded");
        } else {
            conf = new Configuration(configFile);
            Logger.info(getClass().getSimpleName(), configFile.getName() + " Configuration loaded");
        }
        tcpServer = new TCPServer(new Integer(conf.getValue("gstreamerport")));
    }

    private boolean validateConfiguration() {
        File dir = new File(conf.getValue("moviefolder"));
        if (!(dir.exists() && dir.isDirectory())) {
            Logger.error("WebServer", Logger.CRITICAL, "Movie Folder could not be found, Aborting...");
            return false;
        } else if (conf.getValue("indexhtml") != null && !new File(conf.getValue("indexhtml")).exists()) {
            Logger.error("WebServer", Logger.CRITICAL, "File for index.hml could not be found...  See your configuration path for 'indexhtml'");
            return false;
        } else {
            return true;
        }
    }

    public void start() {
        stopMe = false;
        if (validateConfiguration()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    ServerSocket socket = null;
                    String host = "127.0,0,1";
                    try {
                        socket = new ServerSocket(new Integer(conf.getValue("httplisteningport")));
                        socket.setSoTimeout(1000);
                    } catch (IOException ex) {
                        Logger.error("WebServer", Logger.CRITICAL, ex.getMessage());
                        stopMe = true;
                    }
                    if (conf.getValue("externalhost") == null || conf.getValue("externalhost").length() == 0) {
                        try {
                            host = "http://" + InetAddress.getLocalHost().getHostName() + ":" + conf.getValue("httplisteningport");
                        } catch (UnknownHostException ex) {
                            host = "http://127.0.0.1:" + conf.getValue("httplisteningport");
                        }
                    } else {
                        host = conf.getValue("externalhost");
                        if (!host.startsWith("http://")) {
                            host = "http://" + host;
                        }
                    }
                    Logger.info("WebServer", "Accepting connection on " + host);
                    while (!stopMe) {
                        Socket connection = null;
                        try {
                            connection = socket.accept();
                            connection.setSoTimeout(10000);
                            Logger.info("WebServer", "Connection from " + connection.getInetAddress().getHostName() + "(" + connection.getInetAddress().getHostAddress() + ")");
                            new Connection(conf, tcpServer).setSocket(connection);
                        } catch (SocketTimeoutException ex) {
                            continue;
                        } catch (IOException ex) {
                            Logger.error("WebServer", Logger.CRITICAL, ex.getMessage());
                            stopMe = true;
                        }
                    }
                }
            }).start();
        }
    }

    public void stop() {
        stopMe = true;
    }
}
