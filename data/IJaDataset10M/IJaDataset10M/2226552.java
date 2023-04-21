package org.javaldap.server;

import java.net.*;
import java.io.*;
import java.util.*;
import org.javaldap.server.schema.InitSchema;
import org.javaldap.server.util.Logger;
import org.javaldap.server.util.ServerConfig;

public class LDAPServer {

    public static void main(java.lang.String[] args) throws Exception {
        ServerConfig.getInstance().init();
        new InitSchema().init();
        org.javaldap.server.acl.ACLChecker.getInstance().initialize();
        String configPort = (String) ServerConfig.getInstance().get(ServerConfig.JAVALDAP_SERVER_PORT);
        int serverPort = new Integer(configPort).intValue();
        org.javaldap.server.backend.BackendHandler.Handler();
        Logger.getInstance().log(Logger.LOG_NORMAL, "Server Starting on port " + serverPort);
        ServerSocket serverSock = new ServerSocket(serverPort);
        while (true) {
            Logger.getInstance().log(Logger.LOG_DEBUG, "Connection Initiated.");
            new ConnectionHandler(serverSock.accept()).start();
        }
    }
}
