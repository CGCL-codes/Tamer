package edu.rabbit.kernel.db;

import edu.rabbit.kernel.IConfig;
import edu.rabbit.kernel.DbUtility;

/**
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 * 
 */
public class DbConfig implements IConfig {

    private static final String DB_SYNCHRONIZED_THREADING = "DB_SYNCHRONIZED_THREADING";

    private boolean synchronizedThreading = DbUtility.getBoolSysProp(DB_SYNCHRONIZED_THREADING, true);

    private static final String DB_SHARED_CACHE = "DB_SHARED_CACHE";

    private boolean sharedCacheEnabled = DbUtility.getBoolSysProp(DB_SHARED_CACHE, false);

    public boolean isSharedCacheEnabled() {
        return sharedCacheEnabled;
    }

    /**
     * @return the synchronizedThreading
     */
    public boolean isSynchronizedThreading() {
        return synchronizedThreading;
    }
}
