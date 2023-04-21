package org.furthurnet.client;

import org.furthurnet.clientgui.ClientGuiConstants;
import org.furthurnet.datastructures.Message;
import org.furthurnet.datastructures.supporting.Constants;
import org.furthurnet.datastructures.supporting.Handler;
import org.furthurnet.socket.SocketHandler;

public class RepositionHandler extends Handler {

    private ClientHandler ch;

    private volatile boolean done;

    private SocketHandler repositionSocket;

    private volatile boolean needChildReposition;

    private String childId0;

    private String childId1;

    private volatile boolean needParentConnections;

    private boolean started = false;

    private boolean returnImmediately = false;

    private RepositionHandler() {
    }

    public RepositionHandler(ClientHandler _ch) {
        ch = _ch;
        done = false;
        repositionSocket = new SocketHandler(this);
        needChildReposition = false;
        childId0 = childId1 = null;
        needParentConnections = false;
    }

    public void run() {
        started = true;
        if (returnImmediately == true) return;
        org.furthurnet.furi.FurthurThread.logPid("Client.RepositionHandler " + hashCode());
        if (ch != null) initializeLogFile(ch.clientId + "_reposition.log");
        while (!done) {
            while ((!needChildReposition) && (!needParentConnections) && (!done)) Wait();
            if (!done) if (needChildReposition) repositionChildren();
            if (!done) if (needParentConnections) contactServerForParentConnections();
        }
        repositionSocket.closeAll();
        closeLogFile();
        ch = null;
        repositionSocket = null;
    }

    public synchronized void repositionChildren(String _childId0, String _childId1) {
        if ((!needChildReposition) && (_childId0 != null) && (_childId1 != null)) {
            needChildReposition = true;
            childId0 = _childId0;
            childId1 = _childId1;
            notify();
        }
    }

    private synchronized void repositionChildren() {
        boolean success = false;
        LogMessage(4, "Trying to connect to server for child repositioning.");
        repositionSocket.connect(ch.serverIp, ch.serverOpenPort, ch.serverPort + Constants.MAINTENANCE_PORT_OFFSET);
        if (!repositionSocket.isConnected()) Sleep(1000);
        if (repositionSocket.isConnected()) success = serveMessage(repositionSocket, Constants.REQUEST_CHILD_REPOSITION + "\n" + "3" + "\n" + ch.clientId + "\n" + childId0 + "\n" + childId1 + "\n");
        if (!success) {
            LogMessage(1, "Could not connect to server, exiting.");
            repositionSocket.close();
            ch.monitor.unsuccessfulTransmit(ClientGuiConstants.ERROR_DISCONNECTED, 423, this);
        }
        needChildReposition = false;
    }

    public synchronized void getParentConnectionsFromServer() {
        needParentConnections = true;
        notify();
    }

    private synchronized void contactServerForParentConnections() {
        boolean success = false;
        LogMessage(4, "Trying to notify server that I am orphaned.");
        repositionSocket.connect(ch.serverIp, ch.serverOpenPort, ch.serverPort);
        if (repositionSocket.isConnected()) success = serveMessage(repositionSocket, Constants.REQUEST_PARENT_CONNECTION_UPDATE + "\n" + "1" + "\n" + ch.clientId + "\n");
        if (!success) {
            LogMessage(1, "Could not connect to server. Exiting.");
            ch.monitor.unsuccessfulTransmit(ClientGuiConstants.ERROR_HOST_OFFLINE, 307, this);
        }
        needParentConnections = false;
    }

    protected synchronized void setDone() {
        if (started == true) {
            done = true;
            if (repositionSocket != null) repositionSocket.closeAll();
            notify();
        } else {
            ch = null;
            repositionSocket = null;
        }
    }

    protected boolean decodeMessage(Message m, SocketHandler repositionSocket) {
        try {
            if (m == null) return false; else if (m.messageType == null) return false; else if (m.messageType.equals(Constants.OK_BYE)) {
                LogMessage(4, "Communication successful.  Closing connection.");
                repositionSocket.close();
            } else if (m.messageType.equals(Constants.RESEND)) {
                return true;
            } else if (m.messageType.equals(Constants.ANCESTOR_INFO)) {
                repositionSocket.send(Constants.OK_BYE + "\n" + "0" + "\n");
                repositionSocket.close();
                contactAncestor(m.params[0], repositionSocket);
            } else if (m.messageType.equals(Constants.REPOSITION_INFO)) {
                repositionSocket.close();
                contactAncestor(m.params[0], repositionSocket);
            } else if (m.messageType.equals(Constants.KEEP_CHANNEL)) {
                LogMessage(1, "Parent wants to keep this channel open for packets.  Passing the connection to my parent handler");
                ch.updateParent(m.params[0], new Long(m.params[1]).longValue(), repositionSocket.getSocketExtraction());
                ch.updateChildren(null, m.params[2], new Long(m.params[1]).longValue(), null, false);
            } else if (m.messageType.equals(Constants.DISCONNECTED)) {
                repositionSocket.send(Constants.OK_BYE + "\n" + "0" + "\n");
                repositionSocket.close();
                LogMessage(1, "Server has removed me from the tree.  I cannot continue.");
                if (isFirewall) ch.monitor.unsuccessfulTransmit(ClientGuiConstants.ERROR_FIREWALL_NODE_DELETED, 401, this); else ch.monitor.unsuccessfulTransmit(ClientGuiConstants.ERROR_DISCONNECTED, 417, this);
            } else return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Handler getClientHandler() {
        return ch;
    }

    public void cleanup() {
        if (!started) {
            returnImmediately = true;
            start();
        }
        ch = null;
    }
}
