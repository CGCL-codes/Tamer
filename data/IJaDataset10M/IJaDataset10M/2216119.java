package jalk;

import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * Every client has an instance of this class.
 * 
 * Notice: The Client does not know, which ID the Server gives him.
 * The Client send all his messages only to the server, but does not 
 * process them locally (e.g. it does not show the typed character) but
 * waits until the message is sent back from the server. At that moment
 * the Client cannot determine if an incomming message was originally
 * from himself or from any other Client.
 * So we can easily add a multi server support, we only need a seperate
 * "activeClient" Hashtable for each server (and we have to think about 
 * preventing loops or multiplication of messages).
 *  
 * @see TcpClientManager
 * @author tmfelser
 * @version 1.0
 *
 */
public class TcpClient implements JalkClient, TcpReceiver {

    private Socket s = null;

    private Config cfg = null;

    private TcpFunctions tcpFunc = null;

    private final Identification myId = new Identification();

    private final Identification unknownIdent = new Identification();

    private MessageList messageList = null;

    private final Hashtable unfinishedMessages = new Hashtable();

    private final ClientListListModel activeClients = new ClientListListModel();

    private MainWindow mainWin = null;

    private class AliveThread extends Thread {

        public AliveThread() {
            setDaemon(true);
        }

        public void run() {
            while (true) {
                sendAction(JalkClient.ACTION_NULL);
                try {
                    Thread.sleep(5 * 60 * 1000);
                } catch (Exception e) {
                }
            }
        }
    }

    TcpClient(Socket iSocket, Config iConfig) {
        this.s = iSocket;
        this.cfg = iConfig;
        tcpFunc = new TcpFunctions(s, cfg);
        myId.setName(cfg.getName());
        myId.setColor(cfg.getIDColor());
        if (cfg.doKeepAlive()) new AliveThread().start();
    }

    public final void setMainWindow(MainWindow win) {
        this.mainWin = win;
    }

    public final void setTextList(MessageList list) {
        this.messageList = list;
    }

    public final void setClientList(JList list) {
        list.setModel(activeClients);
    }

    public final void repaintClientList() {
        activeClients.changed(null);
    }

    public final void transfer(char c, int offset) {
        sendTransfer(c, offset);
    }

    public final Identification getIdent() {
        return myId;
    }

    public void transfer(String s, int offset) {
        sendTransfer(s, offset);
    }

    public void delete(int offset, int length) {
        sendDelete(offset, length);
    }

    public void close() {
        tcpFunc.close();
    }

    private final void writeOnScreen(Identification clientIdent, String s, int offset) {
        for (int i = 0; i < s.length(); writeOnScreen(clientIdent, s.charAt(i), offset + i++)) ;
    }

    /**
	 * This function is responsible for displaying messages on the screen.
	 * It manages unfinished messages.
	 * @param clientIdent the IDentification of the sending client
	 * @param c the newly received character
	 */
    private final void writeOnScreen(Identification clientIdent, char c, int offset) {
        Integer clientID = new Integer(clientIdent.getId());
        if (c == '\t') c = ' ';
        if (messageList != null) {
            if (c == '\n') {
                Message oldMsg = (Message) unfinishedMessages.remove(clientID);
                if (oldMsg != null) {
                    oldMsg.finished();
                    messageList.repaint();
                }
                return;
            }
            Message m = (Message) (unfinishedMessages.get(clientID));
            if (m == null) {
                m = new Message(clientIdent);
                messageList.addMessage(m);
                unfinishedMessages.put(clientID, m);
            }
            m.appendMessage(c, offset);
            messageList.repaint();
        }
    }

    private final void delete(Identification clientIdent, int offset, int length) {
        Integer clientID = new Integer(clientIdent.getId());
        if (length == 0) return;
        if (messageList != null) {
            Message m = (Message) (unfinishedMessages.get(clientID));
            if (m != null) {
                m.delete(offset, length);
                messageList.repaint();
            }
        }
    }

    /**
	 * This method sends a message to the server and does the error handling
	 * @param msg the Message to send
	 */
    private final void send(JalkTcpMessage msg) {
        if (!tcpFunc.send(msg)) {
            JOptionPane.showMessageDialog(null, "Send to Server failed! Server died?", "Send Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public final void sendRegisterRequest() {
        if (Main.debug(Main.DEBUG_CLIENT_REGISTER)) System.out.println("TcpClient:sendRegisterRequest: registering me");
        TcpMessageRegister req_msg = new TcpMessageRegister(myId);
        send(req_msg);
    }

    public final void sendUnregisterRequest() {
        if (Main.debug(Main.DEBUG_CLIENT_REGISTER)) System.out.println("TcpClient:sendUnregisterRequest: unregistering me");
        TcpMessageUnregister msg = new TcpMessageUnregister(myId);
        send(msg);
    }

    public final void sendTransfer(char c, int offset) {
        TcpMessageTransfer transferMsg = new TcpMessageTransfer(myId, offset, c);
        send(transferMsg);
    }

    public final void sendTransfer(String s, int offset) {
        TcpMessageTransfer transferMsg = new TcpMessageTransfer(myId, offset, s);
        send(transferMsg);
    }

    public void sendDelete(int offset, int length) {
        TcpMessageDelete deleteMsg = new TcpMessageDelete(myId, offset, length);
        send(deleteMsg);
    }

    public void sendUpdate() {
        TcpMessageUpdate updateMsg = new TcpMessageUpdate(myId);
        send(updateMsg);
    }

    public void sendAction(int actionId) {
        TcpMessageAction actionMsg = new TcpMessageAction(myId, actionId);
        send(actionMsg);
    }

    /**
	 * This is the main receive function, called in an endless loop until it returns false.
	 */
    public final boolean doReceive() {
        if (!tcpFunc.receiveCallback(this)) {
            tcpFunc.close();
            return false;
        }
        return true;
    }

    /**
	 * Gets the Idetification of the sender of msg. Sets the LastActiveDate.
	 * @param msg the received JalkTcpMessage
	 * @return the Ientification of the sender or null if unknown.
	 */
    private final Identification getIdent(JalkTcpMessage msg) {
        Identification ident = activeClients.getIdent(msg.getId());
        if (ident != null) {
            ident.setLastActiveDate();
        }
        return ident;
    }

    public final void rcvRegister(TcpMessageRegister msg, TcpFunctions tcpFunc) {
        final Identification remoteIdent = msg.getIdent();
        System.out.println("Client " + remoteIdent.getId() + " (" + remoteIdent.getName() + ") has registered");
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                activeClients.add(remoteIdent);
            }
        });
    }

    public final void rcvUnregister(TcpMessageUnregister msg, TcpFunctions tcpFunc) {
        final Identification remoteIdent = getIdent(msg);
        if (remoteIdent != null) {
            if (Main.debug(Main.DEBUG_CLIENT_REGISTER)) System.out.println("Client " + remoteIdent.getId() + " (" + remoteIdent.getName() + ") has unregistered");
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    activeClients.remove(remoteIdent);
                }
            });
        } else {
            System.out.println("TcpClient::rcvUnregister: Client with ID=" + msg.getId() + " unknown");
        }
    }

    public final void rcvTransfer(TcpMessageTransfer msg, TcpFunctions tcpFunc) {
        Identification ident = getIdent(msg);
        if (ident == null) ident = unknownIdent;
        writeOnScreen(ident, msg.getString(), msg.getOffset());
    }

    public final void rcvDelete(TcpMessageDelete msg, TcpFunctions tcpFunc) {
        Identification ident = getIdent(msg);
        if (ident != null) delete(ident, msg.getOffset(), msg.getLength());
    }

    public final void rcvUpdate(TcpMessageUpdate msg, TcpFunctions tcpFunc) {
        Identification ident = getIdent(msg);
        if (ident != null) {
            activeClients.updateIdent(ident, msg.getIdent());
        }
    }

    public final void rcvAction(TcpMessageAction msg, TcpFunctions tcpFunc) {
        Identification ident = getIdent(msg);
        switch(msg.getActionId()) {
            case ACTION_BUZZ:
                mainWin.buzz();
                break;
        }
    }
}
