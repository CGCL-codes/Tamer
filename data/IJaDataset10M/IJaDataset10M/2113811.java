package org.ozoneDB.core;

import org.ozoneDB.DxLib.DxIterator;
import org.ozoneDB.DxLib.net.DxMultiServer;
import org.ozoneDB.DxLib.net.DxMultiServerClient;
import org.ozoneDB.OzoneInternalException;
import org.ozoneDB.OzoneProxy;
import org.ozoneDB.PermissionDeniedException;
import org.ozoneDB.core.DbRemote.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @version $Revision: 1.26 $Date: 2004/11/07 13:49:45 $
 */
public class InvokeServer extends DxMultiServer {

    private static final Logger logger = Logger.getLogger(InvokeServer.class.getName());

    private transient Server server;

    public InvokeServer(Server server, int port) throws IOException {
        super(port);
        this.server = server;
    }

    public void startup() {
        logger.fine("startup...");
    }

    public void shutdown() {
        logger.fine("shutdown...");
        try {
            close();
        } catch (IOException e) {
            throw new OzoneInternalException(e);
        }
    }

    public void handleClientEvent(DxMultiServerClient client, Object event) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("received from client: " + event);
        }
        DbInvokeClient dbInvokeClient = (DbInvokeClient) client;
        DbCommand cmd = (DbCommand) event;
        cmd.perform(this, getServer(), dbInvokeClient);
    }

    public void handleClientException(DxMultiServerClient client, Exception e) {
        logger.warning("handleClientException(): " + e);
        e.printStackTrace();
        removeClient(client);
    }

    public DxMultiServerClient newClient(Socket sock) {
        try {
            DbInvokeClient dc = new DbInvokeClient();
            dc.connect(sock, this, server);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("connection established...");
            }
            return dc;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void removeClient(DxMultiServerClient client) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("close connection...");
            logger.finest("close pending transaction...");
        }
        getServer().getTransactionManager().closeConnection(new DbCloseConn());
        super.removeClient(client);
        String userName = ((DbInvokeClient) client).user != null ? ((DbInvokeClient) client).user.name() : "none";
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("connection closed (user: " + userName + ")");
        }
    }

    public Thread newThread(Runnable run) {
        Thread thread = new Thread(threadGroup(), run);
        thread.setPriority(Env.ACCEPT_THREAD_PRIORITY);
        thread.setDaemon(true);
        return thread;
    }

    /**
		Starts filtering references to database objects ({@link OzoneProxy}s) which
		are exported to clients at all client connections.
		Every reference which is exported will be notified to the given GarbageCollector.
		Additionally, references which are known to be used by clients are notified to the
		given GarbageCollector within this call.
    */
    public void startFilterDatabaseObjectReferencesExports(GarbageCollector garbageCollector) {
        synchronized (this) {
            for (Iterator i = getClients().iterator(); i.hasNext(); ) {
                ((DbInvokeClient) i.next()).getProxyObjectGate().startFilterDatabaseObjectReferencesExports(garbageCollector);
            }
        }
    }

    private Server getServer() {
        return server;
    }
}
