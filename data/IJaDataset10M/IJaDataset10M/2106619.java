package org.xtreemfs.babudb.mock;

import java.util.Map;
import org.xtreemfs.babudb.api.StaticInitialization;
import org.xtreemfs.babudb.api.dev.BabuDBInternal;
import org.xtreemfs.babudb.api.dev.CheckpointerInternal;
import org.xtreemfs.babudb.api.dev.DatabaseManagerInternal;
import org.xtreemfs.babudb.api.dev.ResponseManagerInternal;
import org.xtreemfs.babudb.api.dev.SnapshotManagerInternal;
import org.xtreemfs.babudb.api.dev.transaction.TransactionManagerInternal;
import org.xtreemfs.babudb.api.exception.BabuDBException;
import org.xtreemfs.babudb.config.BabuDBConfig;
import org.xtreemfs.babudb.lsmdb.DBConfig;
import org.xtreemfs.babudb.lsmdb.LSMDBWorker;
import org.xtreemfs.babudb.lsmdb.LSN;
import org.xtreemfs.foundation.LifeCycleThread;
import org.xtreemfs.foundation.logging.Logging;

/**
 * @author flangner
 * @since 02/21/2011
 */
public class BabuDBMock implements BabuDBInternal {

    private final String name;

    private final BabuDBConfig conf;

    private final TransactionManagerInternal perMan;

    private final DatabaseManagerInternal dbMan;

    private final CheckpointerInternal cp;

    public BabuDBMock(String name, BabuDBConfig conf, LSN onDisk) throws BabuDBException {
        this.name = name;
        this.conf = conf;
        TransactionManagerMock localPersMan = new TransactionManagerMock(name, onDisk);
        this.perMan = localPersMan;
        this.dbMan = new DatabaseManagerMock();
        this.cp = new CheckpointerMock(localPersMan);
    }

    @Override
    public CheckpointerInternal getCheckpointer() {
        Logging.logMessage(Logging.LEVEL_INFO, this, "Mock '%s' tried to access CP.", name);
        return cp;
    }

    @Override
    public DatabaseManagerInternal getDatabaseManager() {
        Logging.logMessage(Logging.LEVEL_INFO, this, "Mock '%s' tried to access DBMan.", name);
        return dbMan;
    }

    @Override
    public SnapshotManagerInternal getSnapshotManager() {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to access SMan.", name);
        return null;
    }

    @Override
    public void shutdown() throws BabuDBException {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to access shutdown.", name);
    }

    @Override
    public DBConfig getDBConfigFile() {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to access DBConfig.", name);
        return null;
    }

    @Override
    public BabuDBConfig getConfig() {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to access BabuDBConfig.", name);
        return conf;
    }

    @Override
    public TransactionManagerInternal getTransactionManager() {
        Logging.logMessage(Logging.LEVEL_INFO, this, "Mock '%s' tried to access PerMan.", name);
        return perMan;
    }

    @Override
    public ResponseManagerInternal getResponseManager() {
        Logging.logMessage(Logging.LEVEL_INFO, this, "Mock '%s' tried to access RespMan.", name);
        return null;
    }

    @Override
    public void replaceTransactionManager(TransactionManagerInternal perMan) {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to replace PerMan.", name);
    }

    @Override
    public LSMDBWorker getWorker(int dbId) {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to access Worker for DB %d.", name, dbId);
        return null;
    }

    @Override
    public int getWorkerCount() {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to get worker count.", name);
        return 0;
    }

    @Override
    public void addPluginThread(LifeCycleThread plugin) {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to add plugin.", name);
    }

    @Override
    public void init(StaticInitialization staticInit) throws BabuDBException {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to init.", name);
    }

    @Override
    public void stop() {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to stop.", name);
    }

    @Override
    public LSN restart() throws BabuDBException {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to restart.", name);
        return null;
    }

    @Override
    public void startupPerformed() {
    }

    @Override
    public void shutdownPerformed() {
    }

    @Override
    public void crashPerformed(Throwable cause) {
    }

    @Override
    public void shutdown(boolean graceful) throws BabuDBException {
        Logging.logMessage(Logging.LEVEL_ERROR, this, "Mock '%s' tried to access shutdown (%s).", name, graceful);
    }

    @Override
    public Object getRuntimeState(String propertyName) {
        return null;
    }

    @Override
    public Map<String, Object> getRuntimeState() {
        return null;
    }
}
