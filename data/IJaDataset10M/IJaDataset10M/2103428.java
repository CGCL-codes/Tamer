package org.postgresql.core.v2;

import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import org.postgresql.PGNotification;
import org.postgresql.core.*;

/**
 * V2 implementation of ProtocolConnection.
 *
 * @author Oliver Jowett (oliver@opencloud.com)
 */
class ProtocolConnectionImpl implements ProtocolConnection {

    ProtocolConnectionImpl(PGStream pgStream, String user, String database, Logger logger) {
        this.pgStream = pgStream;
        this.user = user;
        this.database = database;
        this.logger = logger;
        this.executor = new QueryExecutorImpl(this, pgStream, logger);
    }

    public String getHost() {
        return pgStream.getHost();
    }

    public int getPort() {
        return pgStream.getPort();
    }

    public String getUser() {
        return user;
    }

    public String getDatabase() {
        return database;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public synchronized boolean getStandardConformingStrings() {
        return standardConformingStrings;
    }

    public synchronized int getTransactionState() {
        return transactionState;
    }

    public synchronized PGNotification[] getNotifications() throws SQLException {
        PGNotification[] array = (PGNotification[]) notifications.toArray(new PGNotification[notifications.size()]);
        notifications.clear();
        return array;
    }

    public synchronized SQLWarning getWarnings() {
        SQLWarning chain = warnings;
        warnings = null;
        return chain;
    }

    public QueryExecutor getQueryExecutor() {
        return executor;
    }

    public void sendQueryCancel() throws SQLException {
        if (cancelPid <= 0) return;
        PGStream cancelStream = null;
        try {
            if (logger.logDebug()) logger.debug(" FE=> CancelRequest(pid=" + cancelPid + ",ckey=" + cancelKey + ")");
            cancelStream = new PGStream(pgStream.getHost(), pgStream.getPort());
            cancelStream.SendInteger4(16);
            cancelStream.SendInteger2(1234);
            cancelStream.SendInteger2(5678);
            cancelStream.SendInteger4(cancelPid);
            cancelStream.SendInteger4(cancelKey);
            cancelStream.flush();
            cancelStream.ReceiveEOF();
            cancelStream.close();
            cancelStream = null;
        } catch (IOException e) {
            if (logger.logDebug()) logger.debug("Ignoring exception on cancel request:", e);
        } finally {
            if (cancelStream != null) {
                try {
                    cancelStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void close() {
        if (closed) return;
        try {
            if (logger.logDebug()) logger.debug(" FE=> Terminate");
            pgStream.SendChar('X');
            pgStream.flush();
            pgStream.close();
        } catch (IOException ioe) {
            if (logger.logDebug()) logger.debug("Discarding IOException on close:", ioe);
        }
        closed = true;
    }

    public Encoding getEncoding() {
        return pgStream.getEncoding();
    }

    public boolean isClosed() {
        return closed;
    }

    void setEncoding(Encoding encoding) throws IOException {
        pgStream.setEncoding(encoding);
    }

    void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    void setBackendKeyData(int cancelPid, int cancelKey) {
        this.cancelPid = cancelPid;
        this.cancelKey = cancelKey;
    }

    synchronized void setStandardConformingStrings(boolean value) {
        standardConformingStrings = value;
    }

    synchronized void addWarning(SQLWarning newWarning) {
        if (warnings == null) warnings = newWarning; else warnings.setNextWarning(newWarning);
    }

    synchronized void addNotification(PGNotification notification) {
        notifications.add(notification);
    }

    synchronized void setTransactionState(int state) {
        transactionState = state;
    }

    public int getProtocolVersion() {
        return 2;
    }

    private String serverVersion;

    private int cancelPid;

    private int cancelKey;

    private boolean standardConformingStrings;

    private int transactionState;

    private SQLWarning warnings;

    private boolean closed = false;

    private final ArrayList notifications = new ArrayList();

    private final PGStream pgStream;

    private final String user;

    private final String database;

    private final QueryExecutorImpl executor;

    private final Logger logger;
}
