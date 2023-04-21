package com.ajdigital.chat.server.remote;

import java.io.IOException;
import java.net.Socket;
import javax.servlet.ServletContext;
import com.lyrisoft.chat.server.remote.ChatClient;
import com.lyrisoft.chat.server.remote.ChatServer;

/**
 * This is the Flash Chat Server
 */
public class FlashChatServer extends ChatServer {

    public FlashChatServer() throws Exception {
        super();
    }

    public FlashChatServer(String conf) throws Exception {
        super(conf);
    }

    public FlashChatServer(String conf, ServletContext context) throws Exception {
        super(conf, context);
    }

    public FlashChatServer(ServletContext context) throws Exception {
        super(context);
    }

    /**
     * We use a FlashChatClient b/c the message format is slightly different
     * 
     * @param s
     * @return FlashChatClient
     * @exception IOException
     */
    protected ChatClient createChatClient(Socket s) throws IOException {
        ChatServer.debug("! Creating FlashChatClient");
        return new FlashChatClient(this, s);
    }

    /**
     * Instantiate a FlashChatServer and start accepting connections.
     * It would be nice to have one chat server be able to handle multiple
     * client types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            FlashChatServer server = null;
            if (args.length > 1) {
                if ("-f".equals(args[0])) {
                    server = new FlashChatServer(args[1]);
                }
            }
            if (server == null) {
                server = new FlashChatServer();
            }
            server.acceptConnections();
        } catch (Exception e) {
            System.err.println("Error creating server");
            e.printStackTrace();
        }
    }
}
