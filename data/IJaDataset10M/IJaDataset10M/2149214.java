package org.avaje.ebean.server.net;

import org.avaje.ebean.server.lib.ConfigProperties;
import org.avaje.ebean.server.lib.GlobalProperties;

/**
 * Simple socket based server. 
 * <p>
 * Really just used for client server testing environment.
 * </p>
 */
public class ServerSocketMain {

    /**
	 * Start a Socket based server for some time.
	 */
    public static void main(String[] args) throws Exception {
        ConfigProperties properties = GlobalProperties.getConfigProperties();
        int sleep = properties.getIntProperty("ebean.serversocket.sleep", 60000);
        int port = properties.getIntProperty("ebean.serversocket.port", 10100);
        ServerSocketListener sl = new ServerSocketListener("ebsocket", port);
        sl.startListening();
        Thread.sleep(sleep);
    }
}
