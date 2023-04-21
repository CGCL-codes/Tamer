package org.ozoneDB.core.storage;

import java.io.File;
import java.io.IOException;
import org.ozoneDB.core.Env;
import org.ozoneDB.core.Transaction;
import org.ozoneDB.core.Permissions;
import org.ozoneDB.core.Server;

/**
 * @author Per Nyfelt
 */
public abstract class AbstractClusterStore {

    public static final String POSTFIX_SEPARATOR = ".";

    public static final String POSTFIX_CLUSTER = ".cl";

    public static final String POSTFIX_LOCK = ".lk";

    public static final String POSTFIX_TEMP = ".tm";

    public transient long touchCount;

    protected transient Server server;

    public AbstractClusterStore(Server server) {
        this.server = server;
    }

    public String basename(ClusterID cid) {
        StringBuffer filename = new StringBuffer(getServer().getDirectory().getAbsolutePath());
        filename.append(File.separator).append(Env.DATA_DIR);
        filename.append(cid.value());
        return filename.toString();
    }

    public abstract int currentBytesPerContainer();

    public abstract void registerContainerAndLock(StorageObjectContainer container, Permissions perms, Transaction locker, int lockLevel) throws Exception;

    protected abstract void activateCluster(Cluster cluster, int size);

    protected abstract void deactivateCluster(Cluster cluster) throws IOException;

    protected abstract void prepareCommitCluster(Transaction ta, ClusterID cid) throws IOException, ClassNotFoundException;

    protected abstract void commitCluster(Transaction ta, ClusterID cid) throws IOException, ClassNotFoundException;

    protected abstract void abortCluster(Transaction ta, ClusterID cid) throws IOException, ClassNotFoundException;

    public Server getServer() {
        return server;
    }
}
