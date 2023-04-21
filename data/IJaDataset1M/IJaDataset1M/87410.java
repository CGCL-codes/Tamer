package org.commons.database.jconnectionpool;

import java.sql.SQLException;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import org.apache.log4j.Logger;

/**
 * Classe wrapper per la poolled connessione
 * 
 * @author <a href="mailto:msperanza@users.sourceforge.net">Marco Speranza</a>
 * @version $Id: DataBaseManager.java,v 1.1 2006/08/10 09:48:09 msperanza Exp $
 */
public class WrapPooledConnection implements ConnectionEventListener {

    private static Logger log = Logger.getLogger(WrapPooledConnection.class);

    private final PoolConnectionManager poolConnectionManager;

    private final PooledConnection connPool;

    private boolean isAvaiable;

    private long avaiableTime = -1L;

    /**
     * Costruttore
     * 
     * @param connPool
     *            la connection pool da wrappare *
     */
    public WrapPooledConnection(PooledConnection connPool, PoolConnectionManager poolConnectionManager) {
        super();
        this.connPool = connPool;
        this.poolConnectionManager = poolConnectionManager;
        setAvaiable(true);
    }

    /**
     * Recupera la conneccione pool wrappata
     * 
     * @return the connPool <code>PooledConnection</code>
     */
    public synchronized PooledConnection getPooledConnection() {
        return connPool;
    }

    /**
     * Testa se la connessione è disponibile
     * 
     * @return torna true nel caso in cui la connessione e' disponibile, false
     *         altrimenti
     */
    public synchronized boolean isAvaiable() {
        return isAvaiable;
    }

    /**
     * @return Returns the avaiableTime.
     */
    public long getAvaiableTime() {
        return avaiableTime;
    }

    /**
     * Setta lo stato della connessione
     * 
     * @param isAvaiable
     *            true per settare la connessioen disponibile.
     */
    public synchronized void setAvaiable(boolean isAvaiable) {
        this.isAvaiable = isAvaiable;
        if (isAvaiable) {
            avaiableTime = System.currentTimeMillis();
        } else {
            avaiableTime = -1L;
        }
    }

    public void connectionClosed(ConnectionEvent event) {
        setAvaiable(true);
        try {
            poolConnectionManager.fairedConnectionAvaiable(this);
        } catch (SQLException e) {
            poolConnectionManager.fairedConnectionErrorOccurred(this, new ConnectionEvent(connPool, e));
        }
    }

    public void connectionErrorOccurred(ConnectionEvent event) {
        poolConnectionManager.fairedConnectionErrorOccurred(this, event);
    }
}
