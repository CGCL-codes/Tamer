package net.guoquan.network.chat.chatRoom.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import net.guoquan.network.chat.chatRoom.client.UI.ClientGUI;
import net.guoquan.network.chat.chatRoom.client.UI.LoginDialog;
import net.guoquan.network.chat.chatRoom.client.context.ClientSession;
import net.guoquan.network.chat.chatRoom.client.context.ClientSessionContext;
import net.guoquan.network.chat.chatRoom.client.context.ClientSessionHandler;
import net.guoquan.network.chat.chatRoom.client.context.Context;
import net.guoquan.network.chat.chatRoom.client.context.Session;

public class ClientImplement implements Client {

    private Socket socket;

    private Session session;

    private ClientSessionHandler handler;

    private Context context;

    private static final int TIMEOUT = 60000;

    public ClientImplement() throws UnknownHostException, IOException {
        initialise();
    }

    private boolean initialise() throws UnknownHostException, IOException {
        socket = new Socket();
        socket.setSoTimeout(TIMEOUT);
        session = new ClientSession(socket);
        context = new ClientSessionContext(session);
        handler = new ClientSessionHandler(session, context);
        return true;
    }

    public boolean run() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ClientGUI cg = new ClientGUI(handler);
                LoginDialog ld = new LoginDialog(cg, true, handler);
                cg.setLoginDialog(ld);
                ld.setVisible(true);
            }
        });
        return true;
    }

    public static void main(String args[]) throws UnknownHostException, IOException {
        Client c = new ClientImplement();
        c.run();
    }
}
