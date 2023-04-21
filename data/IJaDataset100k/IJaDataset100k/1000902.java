package org.ozoneDB.core.storage.experimental.wizardStore;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.ozoneDB.DxLib.*;
import org.ozoneDB.Setup;
import org.ozoneDB.io.stream.ResolvingObjectInputStream;
import org.ozoneDB.core.*;
import org.ozoneDB.core.storage.ClusterID;
import org.ozoneDB.core.storage.Cluster;
import org.ozoneDB.core.storage.AbstractClusterStore;
import org.ozoneDB.core.storage.StorageObjectContainer;
import org.ozoneDB.core.storage.StreamFactory;

/**
 * The ClusterStore is the back-end store of the wizardStore. It maintains the
 * cluster cache, activation/passivation and the actual persistent commits.
 *
 *
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @author <a href="http://www.medium.net/">Medium.net</a>
 * @version $Revision: 1.20 $Date: 2004/12/28 16:01:32 $
 */
public final class ClusterStore extends AbstractClusterStore {

    private static final Logger logger = Logger.getLogger(ClusterStore.class.getName());

    protected DxMap cachedClusters;

    protected int maxClusterSize = 64 * 1024;

    /**
     * Table that maps Permissions to ClusterIDs.
     */
    protected DxMap growingClusterIDs;

    ClusterStore(Server server) {
        super(server);
        maxClusterSize = getServer().getConfig().getIntProperty(WizardStore.CLUSTER_SIZE, -1);
        cachedClusters = new DxHashMap(64);
    }

    public void startup() {
        growingClusterIDs = new DxHashMap(32);
    }

    public void shutdown() {
    }

    /**
     * Check if the ClusterStore was cleanly shutted down. We need never do this
     * test, because any transaction that for the first time acquires a write
     * lock calls incWritingTransactionCount(), which results in always having
     * a flag set (read: file that exists) while any transaction is writing.
     */
    public boolean isCleanShutdown() {
        File file = new File(getServer().getPath() + Env.DATA_DIR);
        String[] fileList = file.list();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].endsWith(POSTFIX_TEMP)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Search the DATA dir and recover all ClusterIDs.
     */
    public DxSet recoverClusterIDs() {
        File file = new File(getServer().getPath() + Env.DATA_DIR);
        String[] fileList = file.list();
        DxSet result = new DxHashSet();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].endsWith(POSTFIX_CLUSTER)) {
                String cidString = fileList[i].substring(0, fileList[i].indexOf('.'));
                long cid = Long.parseLong(cidString);
                result.add(new ClusterID(cid));
            }
        }
        return result;
    }

    public long currentCacheSize() {
        long result = 0;
        DxIterator it = cachedClusters.iterator();
        Cluster cluster;
        while ((cluster = (Cluster) it.next()) != null) {
            result += cluster.size();
        }
        return result;
    }

    public int currentBytesPerContainer() {
        int result = getServer().getConfig().getIntProperty(WizardStore.CLUSTER_SIZE_RATIO, 256);
        return result;
    }

    /**
     * @param perms Permissions of the cluster to search.
     * @return WizardCluster with the specified permissions that is good to store a
     * new container in it.
     */
    protected synchronized Cluster growingCluster(Permissions perms) throws Exception {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("growingCluster() ");
        }
        Cluster cluster = null;
        ClusterID cid = (ClusterID) growingClusterIDs.elementForKey(perms);
        if (cid != null) {
            cluster = (Cluster) cachedClusters.elementForKey(cid);
            if (cluster == null) {
                cluster = loadCluster(cid, true);
                if (cluster instanceof WizardCluster) {
                    ((WizardCluster) cluster).unpin();
                }
            }
            if (cluster.lock() == null || cluster.size() >= maxClusterSize || cluster.lock().level(null) > Lock.LEVEL_NONE && !cluster.lock().isAcquiredBy(getServer().getTransactionManager().currentTA())) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("growingCluster(): growing cluster not usable: cid=" + cluster.clusterID() + " size=" + cluster.size() + " lockLevel=" + (cluster.lock() != null ? String.valueOf(cluster.lock().level(null)) : "null"));
                }
                growingClusterIDs.removeForKey(perms);
                cluster = null;
            }
        }
        if (cluster == null) {
            DxIterator it = cachedClusters.iterator();
            Cluster cursor;
            while ((cursor = (Cluster) it.next()) != null) {
                if (cursor.size() < maxClusterSize && cursor.permissions().equals(perms)) {
                    cluster = cursor;
                    trimCache();
                    if (cluster.lock() == null) {
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("growingCluster(): loaded cluster was deactivated: " + cluster.clusterID());
                        }
                        cluster = null;
                    } else if (cluster.lock().level(null) > Lock.LEVEL_NONE && !cluster.lock().isAcquiredBy(getServer().getTransactionManager().currentTA())) {
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("growingCluster(): loaded cluster is locked by another transaction: " + cluster.clusterID());
                        }
                        cluster = null;
                    } else {
                        growingClusterIDs.addForKey(cluster.clusterID(), perms);
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("growingCluster(): loaded cluster is now growing cluster: " + cluster.clusterID() + " size:" + cluster.size());
                        }
                        break;
                    }
                }
            }
        }
        if (cluster == null) {
            cluster = createANewEmptyAndUsableCluster(perms);
        } else {
            trimCache();
        }
        return cluster;
    }

    /**
     * Creates a cluster which is
     * <UL>
     * <LI>new</LI>
     * <LI>empty</LI>
     * <LI>usable and</LI>
     * <LI>not locked</LI>
     * </UL>
     */
    protected synchronized Cluster createANewEmptyAndUsableCluster(Permissions perms) throws IOException, ClassNotFoundException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("growingCluster(): creating new cluster...");
        }
        trimCache();
        Cluster cluster = new WizardCluster(new ClusterID(getServer().getKeyGenerator().nextID()), perms, getServer().getTransactionManager().newLock());
        ((MROWLock) cluster.lock()).setDebugInfo("clusterID=" + cluster.clusterID());
        activateCluster(cluster);
        cachedClusters.addForKey(cluster, cluster.clusterID());
        growingClusterIDs.addForKey(cluster.clusterID(), perms);
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("growingCluster(): new cluster created: " + cluster.clusterID());
        }
        return cluster;
    }

    /**
     * Returns or creates a cluster which is not locked so that locking it will succeed.
     * The returned cluster is only guaranteed to be not locked by any other thread as long as this
     * method is called during synchronization to this ClusterStore.
     */
    protected Cluster giveMeAnUnlockedCluster(Permissions perms) throws IOException, ClassNotFoundException {
        return createANewEmptyAndUsableCluster(perms);
    }

    /**
     * Associates the specified container with a cluster.
     *
     * Iff this method returns normally (without exception), the container is pinned and thus
     * has to be unpinned.
     *
     * Iff this method returns normally (without exception), the container (and thus the cluster of the container)
     * is write locked
     *
     * @param container Container to be registered with one cluster.
     */
    public void registerContainerAndLock(StorageObjectContainer container, Permissions perms, Transaction locker, int lockLevel) throws Exception {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("registerContainer()");
        }
        Cluster cluster = null;
        boolean pinned = false;
        boolean locked = false;
        boolean alright = false;
        try {
            synchronized (this) {
                cluster = growingCluster(perms);
                Lock clusterLock = cluster.lock();
                int prevLevel = clusterLock.tryAcquire(locker, lockLevel);
                if (prevLevel == Lock.NOT_ACQUIRED) {
                    cluster = giveMeAnUnlockedCluster(perms);
                    clusterLock = cluster.lock();
                    prevLevel = clusterLock.tryAcquire(locker, lockLevel);
                    if (prevLevel == Lock.NOT_ACQUIRED) {
                        throw new Error("BUG! We could not acquire a lock for an unlocked cluster.");
                    }
                }
                locked = true;
                cluster.registerContainer(container);
                container.pin();
                pinned = true;
            }
            cluster.updateLockLevel(locker);
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("    cluster: " + cluster.clusterID());
            }
            alright = true;
        } finally {
            if (!alright) {
                if (locked) {
                    cluster.lock().release(locker);
                }
                if (pinned) {
                    container.unpin();
                }
            }
        }
    }

    public void invalidateContainer(StorageObjectContainer container) {
        synchronized (container) {
            container.getCluster().removeContainer(container);
            container.setCluster(null);
        }
    }

    protected Cluster restoreCluster(ClusterID cid) throws Exception {
        String basename = basename(cid);
        Cluster cluster;
        new File(basename + POSTFIX_LOCK).delete();
        cluster = (Cluster) loadData(basename + POSTFIX_CLUSTER);
        activateCluster(cluster);
        return cluster;
    }

    /**
     * Make sure the corresponding cluster is in the cache. While loading
     * clusters, we may have to throw away (and maybe store) some currently
     * cached clusters.
     *
     *
     * @param cid ClusterID of the cluster to load.
     * @param pin
     * wether the loaded cluster should be pinned as soon as it is loaded
     * so that there may be no chance to unload unless it is unpinned.
     * If this parameter is set to true, the user has to unpin the cluster.
     * If this parameter is set to false, the cluster may already be unloaded when this method returns.
     * after using it.
     */
    public Cluster loadCluster(ClusterID cid, boolean pin) throws IOException, ClassNotFoundException {
        Cluster cluster = (Cluster) cachedClusters.elementForKey(cid);
        if (cluster == null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("loadCluster(): load cluster from disk: " + cid.toString());
            }
            String basename = basename(cid);
            String lockName = basename + POSTFIX_LOCK;
            trimCache();
            synchronized (this) {
                Cluster interimCluster = (Cluster) cachedClusters.elementForKey(cid);
                if (interimCluster != null) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("loadCluster(): cluster was loaded by another thread too; droping my copy");
                    }
                    cluster = interimCluster;
                    if (pin && cluster instanceof WizardCluster) {
                        ((WizardCluster) cluster).pin();
                    }
                } else {
                    Lock lock = null;
                    try {
                        lock = (Lock) loadData(lockName);
                        new File(lockName).delete();
                    } catch (Exception e) {
                        if (logger.isLoggable(Level.FINEST)) {
                            logger.finest("    Unable to load lock from disk - creating a new lock.");
                        }
                        lock = (Lock) getServer().getTransactionManager().newLock();
                    }
                    ((MROWLock) lock).setDebugInfo("clusterID=" + cid);
                    String clusterFileName = clusterFileName(cid, lock);
                    cluster = (Cluster) loadData(clusterFileName);
                    synchronized (cluster) {
                        cluster.setLock(lock);
                        if (pin && cluster instanceof WizardCluster) {
                            ((WizardCluster) cluster).pin();
                        }
                        activateCluster(cluster);
                    }
                    cachedClusters.addForKey(cluster, cluster.clusterID());
                    trimCache();
                }
            }
        } else {
            synchronized (cluster) {
                if (pin && cluster instanceof WizardCluster) {
                    ((WizardCluster) cluster).pin();
                }
            }
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("returning WizardCluster: " + cluster);
        }
        return cluster;
    }

    /**
     * Remove cluster from the cluster cache.
     * @param cid
     */
    public void unloadCluster(ClusterID cid, boolean deactivate) throws IOException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("unloadCluster(" + cid + "," + deactivate + ").");
        }
        Cluster cluster = (Cluster) cachedClusters.removeForKey(cid);
        if (deactivate) {
            deactivateCluster(cluster);
        }
    }

    /**
     * Ensure that there is at least the specified size of free space in the
     * cluster cache. Under some circumstances clusters (currently invoked)
     * cannot be deactivated. Therefore this method cannot guarantee that the
     * needed space is free afterwards.<p>
     *
     * This is the central method of the deactivation of containers that are
     * currently in use. This is different from the commit behaviour.
     */
    protected void trimCache() throws IOException {
        long freeSpace = Env.getEnv().freeMemory();
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("trimCache():  free:" + freeSpace);
        }
        if (freeSpace <= 0) {
            synchronized (this) {
                long cacheSize = 0;
                DxMap priorityQueue = new DxTreeMap();
                DxIterator it = cachedClusters.iterator();
                Cluster cluster;
                while ((cluster = (Cluster) it.next()) != null) {
                    priorityQueue.addForKey(cluster, cluster.cachePriority());
                    cacheSize += cluster.size();
                }
                long cacheSizeToRemove = cacheSize / 5;
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("   cache: " + cacheSize + " to be freed:" + cacheSizeToRemove);
                }
                it = priorityQueue.iterator();
                while (cacheSizeToRemove > 0 && (cluster = (WizardCluster) it.next()) != null) {
                    if (cluster instanceof WizardCluster) {
                        WizardCluster wizardCluster = (WizardCluster) cluster;
                        if (!wizardCluster.isPinned()) {
                            if (logger.isLoggable(Level.FINER)) {
                                logger.finer("DEACTIVATE cluster: " + cluster.clusterID());
                            }
                            cluster = (Cluster) it.removeObject();
                            cacheSizeToRemove -= cluster.size();
                            unloadCluster(cluster.clusterID(), true);
                        }
                    } else {
                        logger.warning("the cluster is not a WizardCluster, not sure what to do");
                    }
                }
                System.gc();
            }
        }
    }

    protected void activateCluster(Cluster cluster, int size) {
    }

    /**
     * This method is called right after the specified WizardCluster was loaded from
     * disk.
     */
    protected void activateCluster(Cluster cluster) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("activateCluster(): " + cluster.clusterID());
        }
        cluster.setClusterStore(this);
        cluster.touch();
    }

    /**
     * Deactivate the specified cluster before it is written to disk. The
     * specified cluster will be removed from the cluster cache.
     */
    protected void deactivateCluster(Cluster cluster) throws IOException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("deactivateCluster(): " + cluster.clusterID() + " priority: " + cluster.cachePriority());
            logger.fine("    lock: " + cluster.lock().level(null));
        }
        String basename = basename(cluster.clusterID());
        synchronized (this) {
            synchronized (cluster) {
                if (cluster.lock().level(null) >= Lock.LEVEL_READ) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("    write lock to disk: " + cluster.clusterID());
                    }
                    storeData(cluster.lock(), basename + POSTFIX_LOCK);
                } else {
                    File lockFile = new File(basename + POSTFIX_LOCK);
                    if (lockFile.exists()) {
                        lockFile.delete();
                    }
                }
                if (cluster.lock().level(null) >= Lock.LEVEL_WRITE) {
                    if (((WizardCluster) cluster).isDirty()) {
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("    write cluster: " + cluster.clusterID());
                        }
                        storeData(cluster, clusterFileName(cluster.clusterID(), cluster.lock()));
                    } else {
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("    cluster was not changed, don't write, cid=" + cluster.clusterID());
                        }
                    }
                }
                cluster.setLock(null);
            }
        }
    }

    /**
     * Store the specified cluster in temp file.
     * If this write fails, the original cluster data are still valid.
     * The cluster may has been written to the disk already, if is was deactivated
     * while transaction.
     *
     * Note: This only writes all currently committed transaction results to the
     * disk. This is different from the deactivation behaviour.
     *
     *
     * @param cid WizardCluster to be prepare-committed.
     * @exception java.io.IOException None of the clusters are written to disk.
     */
    public synchronized void prepareCommitCluster(Transaction ta, ClusterID cid) throws IOException, ClassNotFoundException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("prepareCommitCluster(): " + cid);
        }
        Cluster cluster = loadCluster(cid, true);
        if (cluster instanceof WizardCluster) {
            ((WizardCluster) cluster).unpin();
        }
        cluster.prepareCommit(ta);
        if (cluster.lock().level(ta) >= Lock.LEVEL_WRITE) {
            String tempFilename = basename(cid) + "-" + ta.taID() + POSTFIX_TEMP;
            storeData(cluster, tempFilename);
        }
    }

    /**
     * Actually commit the specified cluster.
     * This renames a given temp cluster file to the cluster file
     * and updates the lock file.
     *
     * @param cid WizardCluster to be committed.
     */
    public synchronized void commitCluster(Transaction ta, ClusterID cid) throws IOException, ClassNotFoundException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("commitCluster(): " + cid);
        }
        String basename = basename(cid);
        String lockFileName = basename + POSTFIX_LOCK;
        Cluster cluster = (Cluster) cachedClusters.elementForKey(cid);
        if (cluster != null) {
            cluster.lock().release(ta);
            updateLockOnDisk(cluster);
        } else {
            File lockFile = new File(lockFileName);
            if (lockFile.exists()) {
                Lock lock = null;
                try {
                    lock = (Lock) loadData(lockFileName);
                } catch (Exception e) {
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.finest("Unable to load lock from disk.");
                    }
                }
                if (lock != null) {
                    lock.release(ta);
                    updateLockOnDisk(cid, lock);
                }
            }
        }
        File tempFile = new File(basename + "-" + ta.taID() + POSTFIX_TEMP);
        File clusterFile = new File(basename + POSTFIX_CLUSTER);
        if (tempFile.exists()) {
            if (clusterFile.exists() && !clusterFile.delete() || !tempFile.renameTo(clusterFile)) {
                throw new IOException("Unable to commit cluster.");
            }
        }
    }

    /**
     * Actually abort the specified cluster.
     * @param cid WizardCluster to be aborted.
     */
    public synchronized void abortCluster(Transaction ta, ClusterID cid) throws IOException, ClassNotFoundException {
        String basename = basename(cid);
        String lockFileName = basename + POSTFIX_LOCK;
        boolean abortChanges = false;
        Cluster cluster = (Cluster) cachedClusters.elementForKey(cid);
        if (cluster != null) {
            abortChanges = cluster.lock().level(ta) >= Lock.LEVEL_WRITE;
            cluster.lock().release(ta);
            if (cluster instanceof WizardCluster && ((WizardCluster) cluster).isPinned()) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("abortCluster(): Unloading pinned cluster " + cid);
                }
                unloadCluster(cid, false);
            } else {
                unloadCluster(cid, false);
            }
            updateLockOnDisk(cluster);
        } else {
            File lockFile = new File(lockFileName);
            if (lockFile.exists()) {
                Lock lock = null;
                try {
                    lock = (Lock) loadData(lockFileName);
                } catch (Exception e) {
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.finest("Unable to load lock from disk.");
                    }
                }
                if (lock != null) {
                    abortChanges = lock.level(ta) >= Lock.LEVEL_WRITE;
                    lock.release(ta);
                    updateLockOnDisk(cid, lock);
                }
            }
        }
        if (abortChanges) {
            File tempFile = new File(basename + "-" + ta.taID() + POSTFIX_TEMP);
            if (tempFile.exists() && !tempFile.delete()) {
                throw new IOException("Unable to delete temp cluster file.");
            }
        }
    }

    protected void updateLockOnDisk(Cluster cluster) throws IOException {
        updateLockOnDisk(cluster.clusterID(), cluster.lock());
    }

    protected void updateLockOnDisk(ClusterID cid, Lock lock) throws IOException {
        if (lock == null || lock.level(null) == Lock.LEVEL_NONE) {
            File lockFile = new File(basename(cid) + POSTFIX_LOCK);
            if (lockFile.exists() && !lockFile.delete()) {
                throw new IOException("Unable to delete lock file.");
            }
        } else {
            storeData(lock, basename(cid) + POSTFIX_LOCK);
        }
    }

    /**
     * Prepare a cluster file name.
     * If a given cluster is write locked this file name contains
     * the id number of transaction which keeps write lock.
     */
    protected String clusterFileName(ClusterID cid, Lock lock) {
        TransactionID taID = ((MROWLock) lock).getWriteLockingTransactionID();
        return basename(cid) + (taID == null ? POSTFIX_CLUSTER : "-" + taID + POSTFIX_TEMP);
    }

    /**
     * Serialize and store the specified object for the specified key. This
     * current implementation uses the file system as back end store.
     */
    protected void storeData(Object obj, String key) throws IOException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("storeData(): " + key);
        }
        OutputStream out = new FileOutputStream(key);
        StreamFactory streamFactory = getServer().getEncodeDecodeStreamFactory();
        if (streamFactory != null) {
            out = new BufferedOutputStream(streamFactory.createOutputStream(out), 3 * 4096);
        } else {
            out = new BufferedOutputStream(out, 3 * 4096);
        }
        ObjectOutputStream oout = new ObjectOutputStream(out);
        try {
            oout.writeObject(obj);
        } finally {
            oout.close();
        }
    }

    /**
     * Load the data that previously has been stored for the given key.
     */
    protected Object loadData(String key) throws IOException, ClassNotFoundException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("loadData(): " + key);
        }
        InputStream in = new FileInputStream(key);
        StreamFactory streamFactory = getServer().getEncodeDecodeStreamFactory();
        if (streamFactory != null) {
            in = new BufferedInputStream(streamFactory.createInputStream(in), 3 * 4096);
        } else {
            in = new BufferedInputStream(in, 3 * 4096);
        }
        ResolvingObjectInputStream oin = new ResolvingObjectInputStream(in);
        oin.setDatabase(getServer().getDatabase());
        try {
            Object result = oin.readObject();
            return result;
        } finally {
            oin.close();
        }
    }
}
