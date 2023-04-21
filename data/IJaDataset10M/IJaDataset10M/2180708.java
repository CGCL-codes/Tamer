package com.sun.jini.outrigger.logstore;

import com.odi.DatabaseRootNotFoundException;
import com.odi.ObjectStore;
import com.odi.util.OSHashtable;
import com.odi.util.OSTreeMapByteArray;
import com.sun.jini.constants.TimeConstants;
import com.sun.jini.logging.Levels;
import com.sun.jini.outrigger.Recover;
import com.sun.jini.outrigger.StoredObject;
import com.sun.jini.outrigger.OutriggerServerImpl;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jini.space.InternalSpaceException;

/**
 * Back end of log store. This class consumes logs written by LogOutputFile and
 * stores the state in the ODI database. The class processes the logs to
 * optimize what is stored in the database. For example, a take log record will
 * cause the removal of a write log record with the same id (if the transaction
 * is null).
 * <p>
 * Likewise, cancels will cause the removal of write and register records. Also
 * renew records update the expiration of the entry or registration and are not
 * stored directly in the database.
 */
class BackEnd implements Observer {

    private Long sessionId;

    private StoredObject joinState;

    private OSTreeMapByteArray entries;

    private OSHashtable registrations;

    private OSHashtable pendingTxns;

    private byte topUuid[];

    private int gcInterval;

    /** Number of times to attempt to restart the consumer thread. */
    private int retry = 3;

    /**
	 * The count of logs we've read.
	 */
    private int logCount = 0;

    /**
	 * The base name for the log files.
	 */
    private String logFileBase;

    /**
	 * The interface to the database
	 */
    private ODIStoreDB db;

    /**
	 * Log file consumer thread.
	 */
    private ConsumerThread consumer;

    /** Max time to wait for the consumer thread to die on destroy */
    private static final long WAIT_FOR_THREAD = 1 * TimeConstants.MINUTES;

    /**
	 * The name of the database roots.
	 */
    private static final String ENTRIES_ROOT = "Entries";

    private static final String REGISTRATIONS_ROOT = "Registrations";

    private static final String PENDING_TXNS_ROOT = "PendingTxns";

    private static final String SEQ_SESSION_ROOT = "SeqSession";

    private static final String JOINSTATE_ROOT = "JoinState";

    private static final String UUID_ROOT = "topUuid";

    /**
	 * The name of the root that stores the name of the last log file read.
	 */
    private static final String LAST_LOG_ROOT = "LastLog";

    /**
	 * Root for the database version
	 */
    private static final String DB_VERSION_ROOT = "dbVersion";

    private static final int currentDBVersion = 2;

    /** Logger for logging persistent store related information */
    private static final Logger logger = Logger.getLogger(OutriggerServerImpl.storeLoggerName);

    /**
	 * Create a new <code>BackEnd</code> with the given <code>path</code>.
	 */
    BackEnd(String path, int gcInterval) {
        this.gcInterval = gcInterval;
        logFileBase = new File(path, LogFile.LOG_TYPE).getAbsolutePath();
        db = new ODIStoreDB(path, LogFile.LOG_TYPE);
    }

    /**
	 * Setup the database store and recover any existing state.
	 */
    void setupStore(Recover space) {
        if (db.setupStore()) {
            try {
                db.startTxn();
                db.setRoot(DB_VERSION_ROOT, new Integer(currentDBVersion));
            } finally {
                db.endTxn(true);
            }
        }
        getFields();
        consumeLogs(true);
        if (sessionId != null) space.recoverSessionId(sessionId.longValue());
        if (topUuid != null) {
            try {
                db.startTxn();
                space.recoverUuid(ByteArrayWrapper.toUuid(topUuid));
            } finally {
                db.endTxn(true);
            }
        }
        if (joinState != null) {
            try {
                db.startTxn();
                space.recoverJoinState(joinState);
            } catch (Exception e) {
                throw logAndThrowRecoveryException("Error recovering join state", e);
            } finally {
                db.endTxn(true);
            }
        }
        try {
            db.startTxn();
            Iterator i = entries.values().iterator();
            while (i.hasNext()) {
                space.recoverWrite((Resource) i.next(), null);
            }
        } catch (Exception e) {
            throw logAndThrowRecoveryException("Error recovering entries", e);
        } finally {
            db.endTxn(true);
        }
        try {
            db.startTxn();
            Iterator i = pendingTxns.values().iterator();
            while (i.hasNext()) {
                PendingTxn pt = (PendingTxn) i.next();
                if (!pt.recover(space)) i.remove();
            }
        } catch (Exception e) {
            throw logAndThrowRecoveryException("Error recovering transactions", e);
        } finally {
            db.endTxn(true);
        }
        try {
            db.startTxn();
            Iterator i = registrations.values().iterator();
            while (i.hasNext()) {
                Registration reg = (Registration) i.next();
                final BaseObject[] templates = reg.getTemplates();
                ObjectStore.deepFetch(templates);
                space.recoverRegister(reg, reg.getType(), templates);
            }
        } catch (Exception e) {
            throw logAndThrowRecoveryException("Error recovering registrations", e);
        } finally {
            db.endTxn(true);
        }
        if (logCount > 0) {
            doStoreGC();
            logCount = 0;
        }
        startConsumer();
    }

    private void startConsumer() {
        consumer = new ConsumerThread();
        consumer.start();
    }

    /**
	 * Thread to consume log files. <code>LogOutputFile</code> calls
	 * <code>update</code> (through the <code>Observer</code> interface each
	 * time a log file is written.
	 */
    private class ConsumerThread extends Thread {

        private boolean more = false;

        private volatile boolean interrupted = false;

        ConsumerThread() {
        }

        public void run() {
            try {
                while (!interrupted) {
                    synchronized (this) {
                        while (!more) wait();
                        more = false;
                    }
                    consumeLogs(false);
                }
            } catch (InterruptedException exit) {
            }
        }

        private synchronized void update() {
            more = true;
            notify();
        }

        public void interrupt() {
            interrupted = true;
            super.interrupt();
        }
    }

    public void update(Observable source, Object arg) {
        if (!consumer.isAlive()) {
            if (retry > 0) {
                logger.log(Level.INFO, "Consumer thread died, attempting restart");
                retry--;
                startConsumer();
            } else {
                logger.log(Level.SEVERE, "Consumer thread no longer running");
                return;
            }
        }
        consumer.update();
    }

    /**
	 * Get the persistent fields from a database. If the fields are not found (a
	 * clean start) create them. This is called during setup and after a
	 * database GC.
	 */
    private void getFields() {
        try {
            db.startTxn();
            Integer dbVersion = null;
            try {
                dbVersion = (Integer) db.getRoot(DB_VERSION_ROOT);
                entries = (OSTreeMapByteArray) db.getRoot(ENTRIES_ROOT);
                pendingTxns = (OSHashtable) db.getRoot(PENDING_TXNS_ROOT);
                registrations = (OSHashtable) db.getRoot(REGISTRATIONS_ROOT);
                sessionId = (Long) db.getRoot(SEQ_SESSION_ROOT);
                joinState = (StoredObject) db.getRoot(JOINSTATE_ROOT);
                topUuid = (byte[]) db.getRoot(UUID_ROOT);
            } catch (DatabaseRootNotFoundException ignore) {
            }
            if ((dbVersion == null) || (dbVersion.intValue() != currentDBVersion)) throw logAndThrowRecoveryException("Corrupt or mismatched database version", null);
            if (entries == null) {
                entries = new OSTreeMapByteArray(db.getDb());
                db.setRoot(ENTRIES_ROOT, entries);
            }
            if (pendingTxns == null) {
                pendingTxns = new OSHashtable();
                db.setRoot(PENDING_TXNS_ROOT, pendingTxns);
            }
            if (registrations == null) {
                registrations = new OSHashtable();
                db.setRoot(REGISTRATIONS_ROOT, registrations);
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "getFields: number of entries:{0}, " + "number of pendingTxns:{1}, number of registrations:{2}", new Object[] { new Integer(entries.size()), new Integer(pendingTxns.size()), new Integer(registrations.size()) });
            }
        } finally {
            db.endTxn(true);
        }
    }

    /**
	 * Destroy the consumer thread and database
	 */
    void destroy() {
        try {
            consumer.interrupt();
            consumer.join(WAIT_FOR_THREAD);
        } catch (InterruptedException ignore) {
        } finally {
            try {
                db.destroy();
            } catch (Throwable t) {
                logger.log(Level.INFO, "Exception encounter while destroying store", t);
            }
        }
    }

    /**
	 * Stop the consumer and close the database.
	 */
    void close() {
        consumer.interrupt();
        try {
            consumer.join();
        } catch (InterruptedException e) {
        }
        db.close();
    }

    /**
	 * Return the pending transaction description for the given transaction,
	 * creating the object and adding it to the table if necessary.
	 */
    private PendingTxn pendingTxn(Long txnId) {
        PendingTxn pt = (PendingTxn) pendingTxns.get(txnId);
        if (pt == null) {
            pt = new PendingTxn(txnId);
            pendingTxns.put(txnId, pt);
        }
        return pt;
    }

    /**
	 * Remove a pending transaction from the table. If it isn't there, this call
	 * is harmless.
	 */
    private void removePendingTxn(Long txnId) {
        pendingTxns.remove(txnId);
    }

    /**
	 * This method sets the session id in the database. It's value is only used
	 * during recovery after a restart.
	 */
    void bootOp(long time, long session) {
        sessionId = new Long(session);
        if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "bootOp({0})", new Date(time));
        db.setRoot(SEQ_SESSION_ROOT, sessionId);
    }

    /**
	 * Record the join state.
	 */
    void joinStateOp(StoredObject state) {
        joinState = state;
        if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "joinStateOp()");
        db.setRoot(JOINSTATE_ROOT, joinState);
    }

    /**
	 * This method records a logged write operation. If under a transaction the
	 * resource is held in a list for the pending transaction. When committed
	 * this method will be called again with the resource and a null transaction
	 * id.
	 */
    void writeOp(Resource entry, Long txnId) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "writeOp({0},{1})", new Object[] { entry, txnId });
        }
        if (txnId != null) pendingTxn(txnId).addWrite(entry); else entries.put(entry.getCookie(), entry);
    }

    /**
	 * This method records a logged take operation. If under a transaction the
	 * resource is held in a list for the pending transaction. When committed
	 * this method will be called again with the resource and a null transaction
	 * id.
	 */
    void takeOp(byte cookie[], Long txnId) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "takeOp({0},{1})", new Object[] { ByteArrayWrapper.toUuid(cookie), txnId });
        }
        if (txnId != null) pendingTxn(txnId).addTake(cookie); else entries.remove(cookie);
    }

    void registerOp(Registration registration) {
        logger.log(Level.FINE, "registerOp({0})", registration);
        registrations.put(registration.getCookieAsWrapper(), registration);
    }

    /**
	 * This method processes a logged renew operation. Renew operations apply to
	 * resources passed into writeOp and registerOp.
	 */
    void renewOp(byte cookie[], long expiration) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "renewOp({0},{1})", new Object[] { ByteArrayWrapper.toUuid(cookie), new Long(expiration) });
        }
        Resource resource;
        if ((resource = (Resource) entries.get(cookie)) == null) {
            final ByteArrayWrapper baw = new ByteArrayWrapper(cookie);
            if ((resource = (Resource) registrations.get(baw)) == null) {
                Enumeration e = pendingTxns.elements();
                while (e.hasMoreElements()) {
                    if ((resource = ((PendingTxn) e.nextElement()).get(baw)) != null) break;
                }
            }
        }
        if (resource != null) resource.setExpiration(expiration);
    }

    /**
	 * This method processes a logged cancel operation. Cancel operations apply
	 * to resources passed into writeOp and registerOp.
	 */
    void cancelOp(byte cookie[]) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "cancelOp({0})", ByteArrayWrapper.toUuid(cookie));
        }
        if (entries.remove(cookie) == null) {
            final ByteArrayWrapper baw = new ByteArrayWrapper(cookie);
            if (registrations.remove(baw) == null) {
                Enumeration e = pendingTxns.elements();
                while (e.hasMoreElements()) {
                    if (((PendingTxn) e.nextElement()).remove(baw) != null) break;
                }
            }
        }
    }

    /**
	 * This method prepares a pending transaction.
	 */
    void prepareOp(Long txnId, StoredObject transaction) {
        logger.log(Level.FINE, "prepareOp({0})", txnId);
        PendingTxn pt = pendingTxn(txnId);
        pt.prepare(transaction);
    }

    /**
	 * This method commits a pending transaction.
	 */
    void commitOp(Long txnId) {
        logger.log(Level.FINE, "commitOp({0})", txnId);
        PendingTxn pt = pendingTxn(txnId);
        pt.commit(this);
        removePendingTxn(txnId);
    }

    /**
	 * This method aborts a pending transaction.
	 */
    void abortOp(Long txnId) {
        logger.log(Level.FINE, "abortOp({0})", txnId);
        removePendingTxn(txnId);
    }

    /**
	 * This method records the service's top level <code>Uuid</code>
	 * 
	 * @param uuid
	 *            The service's <code>Uuid</code> represented as a
	 *            <code>byte[16]</code>.
	 */
    void uuidOp(byte[] uuid) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "uuidOp({0})", ByteArrayWrapper.toUuid(uuid));
        }
        topUuid = uuid;
        db.setRoot(UUID_ROOT, topUuid);
    }

    /**
	 * Calculate when it is appropriate to perform persistent garbage
	 * collection.
	 */
    private void doStoreGC() {
        db.gc();
        getFields();
    }

    /**
	 * Consume the log files that exist. If <code>all</code> is
	 * <code>true</code>, all found log files will be processed. If
	 * <code>log</code> is <code>false</code>, then all but the most recent will
	 * be processed; this will prevent the back end from reading the log file
	 * that is currently being produced by the front end.
	 */
    private void consumeLogs(boolean all) {
        Iterator it;
        try {
            it = LogInputFile.logs(logFileBase, all);
        } catch (IOException e) {
            final String msg = "couldn't open logs";
            final InternalSpaceException ise = new InternalSpaceException(msg, e);
            logger.log(Level.SEVERE, msg, ise);
            throw ise;
        }
        while (it.hasNext()) {
            LogInputFile log = (LogInputFile) it.next();
            logger.log(Level.FINE, "processing {0})", log);
            if (log == null) continue;
            try {
                db.startTxn();
                LastLog lg = (LastLog) db.getRoot(LAST_LOG_ROOT);
                String logFile = log.toString();
                if (lg == null || !lg.sameAs(logFile)) log.consume(this);
                db.setRoot(LAST_LOG_ROOT, new LastLog(logFile));
            } finally {
                db.endTxn(true);
            }
            log.finished();
            if ((++logCount % gcInterval) == 0) {
                if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "running database GC");
                doStoreGC();
            }
        }
    }

    /**
	 * This class remembers which log file was the last to be successfully
	 * consumed. If the recovery mechanism reopens this file, then it will skip
	 * its contents -- this indicates a crash happened after the contents were
	 * committed to the DB but before the file was unlinked.
	 */
    private static class LastLog {

        private String logFile;

        private long timeStamp;

        LastLog(String path) {
            logFile = path;
            timeStamp = new File(logFile).lastModified();
        }

        boolean sameAs(String otherPath) {
            if (!logFile.equals(otherPath)) return false;
            return (new File(otherPath).lastModified() == timeStamp);
        }
    }

    /**
	 * Log and throw an InternalSpaceException to flag a store recovery problem.
	 */
    private InternalSpaceException logAndThrowRecoveryException(String msg, Throwable nested) {
        final InternalSpaceException e = new InternalSpaceException(msg, nested);
        logger.log(Level.SEVERE, msg, e);
        throw e;
    }
}
