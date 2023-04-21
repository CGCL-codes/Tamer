package org.timothyb89.jtelirc.plugins.server;

import org.timothyb89.jtelirc.plugin.Plugin;
import org.timothyb89.jtelirc.server.Server;

/**
 *
 * @author tim
 */
public class ServerPlugin extends Plugin {

    private CServer cserver;

    @Override
    public String getName() {
        return "Server Plugin";
    }

    @Override
    public String getDescription() {
        return "A simple, multithreaded server that allows other scripts to " + "have the framework send messages via TCP/IP.";
    }

    @Override
    public String getURL() {
        return "http://jtelirc.sourceforge.net/";
    }

    @Override
    public void init(Server server) {
        super.init(server);
        cserver = new CServer(server);
        cserver.start();
    }

    @Override
    public void destroy(Server server) {
        super.destroy(server);
        cserver.shutdown();
    }
}
