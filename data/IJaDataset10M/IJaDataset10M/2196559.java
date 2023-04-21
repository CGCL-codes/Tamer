package goodsjpi;

import java.io.*;
import java.util.*;

/**
 * Class representing GOODS database connection 
 */
public class Database {

    protected Storage[] storages;

    protected IdentityHashtable notifications;

    protected CacheManager cacheManager;

    protected int allocBufSize;

    /**
     * Constructor of the database
     */
    public Database() {
        notifications = new IdentityHashtable();
    }

    /**
     * Factory of raw binary objects which is used to pack/unpack raw binary objects
     */
    public static RawBinaryFactory rawBinaryFactory;

    protected CondEvent onTransactionAbortEvent;

    /**
     * Attach current thread to the database.
     * This method is used in per-thread-transaction model associate
     * the current thread with cache manager
     */
    public void attach() {
        if (cacheManager == null) {
            cacheManager = new CacheManager();
        }
        cacheManager.attach();
    }

    /**
     * Detach current thread fro the database
     */
    public void detach() {
        cacheManager.detach();
    }

    /**
     * Transaction isolation levels.
     */
    public static final int PER_PROCESS_TRANSACTION = 0;

    public static final int PER_THREAD_TRANSACTION = 1;

    /**
     * Set isoaltion level
     */
    public void setIsolationLevel(int level) {
        getCacheManager().isolationLevel = level;
    }

    /**
     * Get database cache manager. In per-thread transaction model each 
     * database has its own cache manager, providing isolation of the threads 
     * from each other. In cooperative model all databases share the same cache
     * managers.
     */
    public CacheManager getCacheManager() {
        return cacheManager != null ? cacheManager : CacheManager.getCacheManager();
    }

    protected static Hashtable connections = new Hashtable();

    /**
     * Create or reuse existed connection from connection pool
     */
    public static synchronized Database getDatabase(String cfgFileName) {
        Thread t = Thread.currentThread();
        Database db = (Database) connections.get(t);
        if (db == null) {
            closeAllDead();
            db = new Database();
            if (!db.open(cfgFileName)) {
                return null;
            }
            connections.put(t, db);
            db.attach();
        }
        return db;
    }

    /**
     * Close all database active connections.
     */
    public static synchronized void closeAll() {
        Enumeration e = connections.keys();
        while (e.hasMoreElements()) {
            Thread t = (Thread) e.nextElement();
            Database db = (Database) connections.get(t);
            if (db.cacheManager != null) {
                db.cacheManager.detach(t);
                db.cacheManager = null;
            }
            db.close();
        }
        connections.clear();
    }

    /**
     * Iterate through the listy of all active conenction and close
     * ones associated with dead threads. 
     */
    public static synchronized void closeAllDead() {
        Enumeration e = connections.keys();
        while (e.hasMoreElements()) {
            Thread t = (Thread) e.nextElement();
            if (!t.isAlive()) {
                Database db = (Database) connections.get(t);
                if (db.cacheManager != null) {
                    db.cacheManager.detach(t);
                    db.cacheManager = null;
                }
                db.close();
                connections.remove(t);
            }
        }
    }

    /**
     * Open the database
     * @param cfgFileName path to the configuration file
     */
    public boolean open(String cfgFileName) {
        try {
            FileInputStream in = new FileInputStream(cfgFileName);
            StreamTokenizer scanner = new StreamTokenizer(in);
            scanner.wordChars('0', '9');
            scanner.wordChars('_', '_');
            scanner.whitespaceChars(':', ':');
            if (scanner.nextToken() != StreamTokenizer.TT_NUMBER) {
                in.close();
                handleError("Bad configuration file format");
                return false;
            }
            int nStorages = (int) scanner.nval;
            storages = new Storage[nStorages];
            while (--nStorages >= 0) {
                if (scanner.nextToken() != StreamTokenizer.TT_NUMBER) {
                    in.close();
                    handleError("Bad configuration file format");
                    return false;
                }
                int sid = (int) scanner.nval;
                if (sid < storages.length) {
                    if (storages[sid] != null) {
                        in.close();
                        handleError("Duplicated entry in configuration file");
                        return false;
                    }
                    int tkn = scanner.nextToken();
                    if (tkn != StreamTokenizer.TT_WORD && tkn != '"' && tkn != '\'') {
                        in.close();
                        handleError("Host name not specified for storage " + sid);
                        return false;
                    }
                    String hostname = scanner.sval;
                    if (scanner.nextToken() != StreamTokenizer.TT_NUMBER) {
                        in.close();
                        handleError("Port not specified for storage " + sid);
                        return false;
                    }
                    int port = (int) scanner.nval;
                    storages[sid] = createStorage(this, sid);
                    storages[sid].open(hostname, port);
                }
            }
            in.close();
            return true;
        } catch (IOException x) {
            handleException(x);
            return false;
        }
    }

    /**
     * Close database connection
     */
    public void close() {
        if (storages != null) {
            for (int i = storages.length; --i >= 0; ) {
                storages[i].close();
            }
            getCacheManager().removeFromCache(this);
        }
    }

    /**
     * Get number of storages in the database
     */
    public int getNumberOfStorages() {
        return storages.length;
    }

    /**
     * Get root object of the first storage in the database
     */
    public Object getRoot() {
        return storages[0].getRoot();
    }

    /**
     * Get root of the specified storage
     * @param sid storage identifier
     */
    public Object getRoot(int sid) {
        return storages[sid].getRoot();
    }

    /**
     * Get size of the database file of the storage
     * @param sid storage identifier
     */
    public long getSize(int sid) {
        return storages[sid].getSize();
    }

    /**
     * Set root object for the first storage in the database
     * @param root root object
     */
    public void setRoot(Object root) {
        storages[0].setRoot((Persistent) root);
    }

    /**
     * Set root object for the specified storage in the database
     * @param sid storage identifier 
     * @param root root object
     */
    public void setRoot(int sid, Object root) {
        storages[sid].setRoot((Persistent) root);
    }

    /**
     * Associate persistent capable object with particular storage.
     * Object should not yet be persistent (not belongs to soe storage)
     * @param obj persistent capable object
     * @param sid storage identifier      
     */
    public void attach(Object obj, int sid) {
        Persistent p = (Persistent) obj;
        Assert.that("persistent object can't change its location", p.opid == 0);
        p.storage = storages[sid];
    }

    /** 
     * Set buffer size for bulk alloc. Bulk alloc alllows
     * to minimize number of messages sent o the server by preallocating 
     * several OIDs during one request. 
     * @param size bulk allocation buffer size
     */
    public void setAllocBufferSize(int size) {
        allocBufSize = size;
    }

    /**
    * Parse oid into number and get that id from the first storage
    */
    public Persistent getObject(String oid) {
        try {
            int id = Integer.parseInt(oid);
            return getObject(0, id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get object from the storage by OID
     * @param storageId storage
     * @param oid object identifer 
     * @return fethed object or <code>null</code> if not found
     */
    public Persistent getObject(int storageId, int oid) {
        return storages[storageId].getObject(oid);
    }

    /**
     * Register event to be signaled on object modification
     * @param obj inspected object
     * @param event event which will be signalled when object is modified by some other client
     */
    public synchronized void notifyOnModification(Object obj, CondEvent event) {
        Persistent p = (Persistent) obj;
        if (event == null) {
            p.metaobject.notifyOnModification(p, false);
            notifications.remove(p);
        } else {
            p.metaobject.notifyOnModification(p, true);
            notifications.put(p, event);
        }
    }

    /**
     * Register event to be signaled when transaction is aborted by server
     */
    public synchronized void notifyOnTransactionAbort(CondEvent event) {
        onTransactionAbortEvent = event;
    }

    /**
     * Error handler. Override this method in derived class to provide 
     * applciation specified error handling policy.
     * @param text error message
     */
    protected void handleError(String text) {
        System.err.println(text);
        throw new Error("Application terminated due to critical error");
    }

    /**
     * Error handler. Override this method in derived class to provide 
     * applciation specified error handling policy.
     * @param x IOException
     */
    protected void handleException(IOException x) {
        x.printStackTrace();
        throw new Error("Application terminated due to critical error");
    }

    /**
     * Create storage
     * @param db database
     * @param id storage idenrtifier
     * @return created storage
     */
    protected Storage createStorage(Database db, int id) {
        return new Storage(db, id);
    }

    /**
     * Handler of client server connection failure. This method is called
     * when connection with server is broken.
     * Override this method in derived class to provide 
     * applciation specified error handling policy.
     * @param sid storage identifier 
     */
    protected void disconnected(int sid) {
        throw new SessionDisconnectedError("Server " + sid + " disconnect client session");
    }

    protected void abortTransaction() {
        for (int i = storages.length; --i >= 0; ) {
            storages[i].abortTransaction();
        }
        if (onTransactionAbortEvent != null) {
            onTransactionAbortEvent.signal();
        }
    }
}
