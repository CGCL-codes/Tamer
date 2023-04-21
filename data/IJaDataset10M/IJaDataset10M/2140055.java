package ch.eivd.sas.teamedit.network.server;

import ch.eivd.sas.teamedit.TeamEdit;
import ch.eivd.sas.teamedit.document.User;
import ch.eivd.sas.teamedit.event.ConnectionEvent;
import ch.eivd.sas.teamedit.event.ConnectionListener;
import ch.eivd.sas.teamedit.exception.IllegalIdException;
import ch.eivd.sas.teamedit.network.AbstractNetworkManager;
import ch.eivd.sas.teamedit.network.Connection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javax.swing.SwingUtilities;

/**
 * Cette classe lance le serveur de teamedit
 *
 * @author Daniel Lifschitz
 */
public class ServerManager extends AbstractNetworkManager {

    /**
     * Instance du ServerManager
     */
    private static ServerManager serverManager = null;

    /**
     * Socket du serveur
     */
    protected ServerSocket serverSocket = null;

    /**
     * Liste des connexions
     */
    protected Vector connectionList = null;

    /**
     * Constructeur priv�
     */
    protected ServerManager() {
        connectionList = new Vector(10, 10);
    }

    /**
     * Retourne l'instance du ServerManager
     * @return - l'instance du ServerManager
     */
    public static ServerManager getServerManager() {
        if (serverManager == null) {
            serverManager = new ServerManager();
        }
        return serverManager;
    }

    /**
     * Initialise la connexion en �coutant sur le ports de TeamEdit
     * @throws IOException - si l'�coute sur le port est impossible
     */
    public void start() throws IOException {
        if (serverSocket == null) {
            int portNumber = Integer.parseInt(TeamEdit.getProgramProperties().getProperty("general.port"));
            if (portNumber < 1024) {
                portNumber = 5555;
            }
            serverSocket = new ServerSocket(portNumber);
            new SocketManager();
            TeamEdit.getUserList().clear();
            setStatus(Connection.CONNECTED);
            try {
                TeamEdit.getUserList().addLocalUser(0, TeamEdit.getProgramProperties().getProperty("general.name"));
            } catch (IllegalIdException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ferme la connexion et d�connecte tous les utilisateurs en cours.
     */
    public void stop() {
        try {
            TeamEdit.getUserList().clear();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = null;
    }

    /**
     * D�connecte l'utilisateur pass� en param�tre
     * @param user - l'utilisateur � d�connecter
     */
    public void disconnectUser(User user) {
    }

    /**
     * Ajoute un utilisateur � la liste des users
     * @param e
     */
    synchronized void newUser(final ConnectionEvent e) {
        Runnable safeThread = new Runnable() {

            public void run() {
                fireConnectionEvent(e);
            }
        };
        SwingUtilities.invokeLater(safeThread);
    }

    /**
     * Ajoute un listener permettant de recevoir les notifications d'arriv�e
     * d'un nouvel utilisateur
     *
     * @param l - le listener invoqu� lors de la notification
     */
    public synchronized void addConnectionListener(ConnectionListener l) {
        listenerList.add(ConnectionListener.class, l);
    }

    /**
     * Supprime le listenenr permettant de recevoir les notifications
     * d'arriv�e d'un nouvel utilisateur
     *
     * @param l - le listener invoqu� lors de la notification
     */
    public synchronized void removeConnectionListener(ConnectionListener l) {
        listenerList.remove(ConnectionListener.class, l);
    }

    /**
     * Envoie un �v�nement � tous les objets inscrits
     */
    protected synchronized void fireConnectionEvent(ConnectionEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ConnectionListener.class) {
                ((ConnectionListener) listeners[i + 1]).userEvent(e);
            }
        }
    }

    /**
     * Ajoute un socket, donc une connexion � la liste des connexions en cours
     *
     * @param socket - le socket � ajouter
     */
    protected synchronized void addSocket(Socket socket) {
        try {
            ServerConnection serverConnection = new ServerConnection(socket);
            connectionList.add(serverConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime la connexion avec un client de la liste
     * @param serverConnection - la connexion � supprimer.
     */
    synchronized void removeServerConnection(ServerConnection serverConnection) {
        connectionList.remove(serverConnection);
    }

    /**
     * Cette classe �coute sur le port de TeamEdit dans l'attente des connexions
     * clientes.
     *
     * @author Daniel Lifschitz
     */
    private class SocketManager implements Runnable {

        /**
         * Le thread courant
         */
        private Thread serverThread = null;

        /**
         * Constructeur. D�marre le thread
         */
        private SocketManager() {
            serverThread = new Thread(this);
            serverThread.start();
        }

        /**
         * m�thode invoqu�e au d�marrage du thread
         */
        public void run() {
            Thread currentThread = Thread.currentThread();
            while (currentThread == serverThread) {
                try {
                    Socket socket = ServerManager.this.serverSocket.accept();
                    ServerManager.this.addSocket(socket);
                } catch (IOException ioe) {
                    ServerManager.this.setStatus(Connection.SOCKET_CLOSED);
                    return;
                }
            }
        }
    }
}
