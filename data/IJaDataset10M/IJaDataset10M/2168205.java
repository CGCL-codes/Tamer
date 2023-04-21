package org.embedded.tomcat.starter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.embedded.tomcat.config.Configuration;
import org.embedded.tomcat.config.Reader;

/**
 * 
 */
public class TomcatStop extends TomcatRunner {

    private static Logger logger = Logger.getLogger(TomcatStop.class);

    public TomcatStop(Configuration configuration) {
        super(configuration);
    }

    public static void main(String[] args) throws Exception {
        File configFile = null;
        if (args.length == 1) {
            configFile = new File(args[0]);
            if (!configFile.exists()) {
                throw new RuntimeException("Config-File: " + configFile.getCanonicalPath() + " does not exitst ...");
            }
            logger.info("Reading config-file:" + configFile.getAbsolutePath());
        } else {
            logger.info("No config-file found ");
        }
        Configuration configuration = Reader.readConfiguration(configFile);
        TomcatStop tomcatStop = new TomcatStop(configuration);
        tomcatStop.stopServer();
    }

    @Override
    public void stopServer() {
        try {
            String hostAddress = InetAddress.getByName("localhost").getHostAddress();
            Socket socket = new Socket(hostAddress, SERVER_LISTENER_PORT);
            OutputStream stream = socket.getOutputStream();
            String shutdown = SERVER_SHUTDOWN_TOKEN;
            for (int i = 0; i < shutdown.length(); i++) stream.write(shutdown.charAt(i));
            stream.flush();
            stream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Catalina.stop: " + e.getMessage());
            System.exit(1);
        }
    }
}
