package risk.connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;
import risk.network.GameUpdate;
import risk.network.JRiskNet;
import risk.network.UpdateObject;
import risk.ui.gui.player.ImagePlayer;

public class ConnectionManager extends Thread {

    private ManagerToServerExchanger toServer;

    private UpdateObjectExchanger toManager;

    private Logger logger;

    private Hashtable connections;

    private Hashtable playerConns;

    private Hashtable sockets;

    private int port;

    private InetAddress ip;

    private boolean listen;

    private boolean connectLocal;

    public ConnectionManager(InetAddress bindIP, int port, ManagerToServerExchanger toServer, UpdateObjectExchanger toManager) {
        super();
        logger = Logger.getLogger("de.javaRisk.v2.JRiskServer");
        logger.info("+++ ConnectionManager: Constructing Instance. +++");
        ip = bindIP;
        this.port = port;
        this.toServer = toServer;
        this.toManager = toManager;
        connections = new Hashtable(6);
        playerConns = new Hashtable(6);
        sockets = new Hashtable(5);
    }

    private void startListening() {
        try {
            ServerSocket server = new ServerSocket(port, 5, ip);
            logger.info("+++ ConnectionManager: Listening on port " + port + " on " + ip + ". +++");
            listen = true;
            while (listen) {
                try {
                    Socket s = server.accept();
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    UpdateObject updateObj = (UpdateObject) in.readObject();
                    GameUpdate gameUpdate = (GameUpdate) updateObj;
                    if (gameUpdate.getGameUpdateType() != GameUpdate.CONNECT) {
                        s.close();
                        continue;
                    } else {
                        connectLocal = false;
                        toServer.putUpdate(updateObj, null);
                        updateObj = toManager.getUpdate();
                        gameUpdate = (GameUpdate) updateObj;
                        if (gameUpdate.getGameUpdateType() == GameUpdate.CONNECT) {
                            ImagePlayer player = (ImagePlayer) gameUpdate.getTransportedObject();
                            sockets.put(player, s);
                            ServerRemoteConnector servConn = new ServerRemoteConnector(this, player, in, out);
                            servConn.start();
                            connections.put(player, servConn);
                            playerConns.put(servConn, player);
                            logger.info("+++ ConnectionManager: Added remote connection of player #" + player.getID() + ": " + player.getName() + ".");
                        }
                        out.writeObject(updateObj);
                    }
                } catch (ClassCastException cce) {
                } catch (ClassNotFoundException cnfe) {
                } catch (IOException ioe) {
                }
            }
            server.close();
            logger.info("+++ ConnectionManager: Stopped listening. +++");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
        setName("ConnectionManager");
        logger.info("+++ ConnectionManager: Starting Thread. +++");
        startListening();
    }

    public boolean addLocalPlayer(UpdateObjectExchanger in, UpdateObjectExchanger out) {
        UpdateObject updateObj = in.getUpdate();
        GameUpdate gameUpdate;
        try {
            gameUpdate = (GameUpdate) updateObj;
            if (gameUpdate.getGameUpdateType() != GameUpdate.CONNECT) return false; else {
                connectLocal = true;
                toServer.putUpdate(updateObj, null);
                updateObj = toManager.getUpdate();
                gameUpdate = (GameUpdate) updateObj;
            }
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            return false;
        }
        if (gameUpdate.getGameUpdateType() == GameUpdate.CONNECT) {
            ImagePlayer player = (ImagePlayer) gameUpdate.getTransportedObject();
            ServerLocalConnector servConn = new ServerLocalConnector(this, in, out);
            servConn.start();
            connections.put(player, servConn);
            playerConns.put(servConn, player);
            out.putUpdate(updateObj);
            logger.info("+++ ConnectionManager: Added local connection of player #" + player.getID() + ": " + player.getName() + ".");
            return true;
        } else {
            out.putUpdate(updateObj);
            return false;
        }
    }

    public void removeConnection(ImagePlayer imagePlayer) {
        try {
            ((ServerConnector) connections.get(imagePlayer)).shutdown();
            ((Socket) sockets.get(imagePlayer)).close();
        } catch (NullPointerException npe) {
        } catch (IOException ioe) {
        }
        connections.remove(imagePlayer);
        logger.info("+++ ConnectionManager: Removed connection of player #" + imagePlayer.getID() + ": " + imagePlayer.getName() + ".");
    }

    public void removeConnections(Vector imagePlayers) {
        Enumeration en = imagePlayers.elements();
        while (en.hasMoreElements()) removeConnection((ImagePlayer) en.nextElement());
    }

    public boolean sendObjectToPlayer(ImagePlayer player, UpdateObject updateObject) {
        try {
            return getConnector(player).send(updateObject);
        } catch (NullPointerException npe) {
            return false;
        }
    }

    public void sendUpdateObjectToServer(UpdateObject updateObject, ServerConnector serverConn) {
        toServer.putUpdate(updateObject, (ImagePlayer) playerConns.get(serverConn));
    }

    public boolean isActualConnectionLocal() {
        return connectLocal;
    }

    public ServerConnector getConnector(ImagePlayer player) {
        return (ServerConnector) connections.get(player);
    }

    public Enumeration getAllConnectors() {
        return connections.elements();
    }

    public InetAddress getInetAddress() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void stopListening() {
        listen = false;
        try {
            Socket s = new Socket(ip, port);
            s.close();
        } catch (IOException ioe) {
        }
    }

    public void shutDown(int gameId) {
        try {
            JRiskNet.endGameServer(gameId);
        } catch (RemoteException e) {
            logger.info("  JRiskServer: error while end game...\n" + e.getMessage());
        }
        stopListening();
        Enumeration e = connections.elements();
        while (e.hasMoreElements()) {
            ((ServerConnector) e.nextElement()).shutdown();
        }
        e = sockets.elements();
        while (e.hasMoreElements()) {
            try {
                ((Socket) e.nextElement()).close();
            } catch (IOException ioe) {
            }
        }
        logger.info("+++ ConnectionManager: Shutted down instance; closed all connections. +++");
    }
}
