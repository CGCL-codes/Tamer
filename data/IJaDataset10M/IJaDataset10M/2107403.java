package uk.co.q3c.deplan.dao;

import java.io.File;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import uk.co.q3c.deplan.domain.resource.BaseCalendar;
import uk.co.q3c.deplan.domain.resource.ResourcePool;
import uk.co.q3c.deplan.domain.task.ResourcedTask;
import uk.co.q3c.deplan.domain.task.Task;
import uk.co.q3c.deplan.user.CurrentUser;
import uk.co.q3c.deplan.util.LogFactory;
import com.db4o.Db4o;
import com.db4o.config.ConfigScope;
import com.db4o.config.Configuration;
import com.db4o.reflect.jdk.JdkReflector;
import com.db4o.ta.TransparentPersistenceSupport;

public class DatabaseFactory {

    private static int activationDepth = 5;

    private static ArrayList<DatabaseCommitListener> commitListeners = new ArrayList<DatabaseCommitListener>();

    protected static final transient Logger logger = LogFactory.newLogSetup(DatabaseFactory.class.getName());

    private static int updateDepth = 2;

    private static boolean useTransparentPersistence = true;

    public static void addListener(DatabaseCommitListener listener) {
        commitListeners.add(listener);
    }

    public static int getActivationDepth() {
        return activationDepth;
    }

    public static int getUpdateDepth() {
        return updateDepth;
    }

    public static String localFileName(String name) {
        String localPath = fileOnHomePath(name + ".yap");
        return localPath;
    }

    public static String fileOnHomePath(String filename) {
        return CurrentUser.dePlanDirectoryName() + File.separator + filename;
    }

    public static SynchronisedDatabaseConnection_db4o newSynchronisedDatabaseConnection(String masterFileName, ConnectionStateListener listener, String reason) {
        String replicaFileName = localFileName(FilenameUtils.getBaseName(masterFileName));
        SynchronisedDatabaseConnection_db4o sdb = new SynchronisedDatabaseConnection_db4o(replicaFileName, masterFileName, listener, reason);
        return sdb;
    }

    public static SynchronisedDatabaseConnection_db4o newSynchronisedDatabaseConnection(String replicaFileName, String masterFileName, ConnectionStateListener listener, String reason) {
        SynchronisedDatabaseConnection_db4o sdb = new SynchronisedDatabaseConnection_db4o(replicaFileName, masterFileName, listener, reason);
        return sdb;
    }

    /**
	 * Use this for creating a simple direct connection to a database file. If
	 * you want a connection synchronised between a master and local copy use
	 * {@link #newSynchronisedDatabaseConnection()} instead.
	 * 
	 * @param file
	 * @param forceCreate
	 *           if true and no file database file exists, a new file is created.
	 *           if false and no file exists, a DatabasePathException is thrown
	 * @param listener
	 *           a listener to pass status messages to
	 * @param reason
	 *           reason for the connection, used primarily in the LockFile to
	 *           notify other users why a database is in use
	 * @return a new connection
	 * @throws DatabasePathException
	 */
    public static DatabaseConnection newDatabaseConnection(File file, boolean forceCreate, ConnectionStateListener listener, String reason) throws DatabasePathException {
        if (file == null) {
            throw new DatabasePathException("file cannot be null");
        }
        if (file.isDirectory()) {
            throw new DatabasePathException("Database must be a valid file name " + file.toString());
        }
        DatabaseConnection dbc = new DatabaseConnection_db4o(configure(), file);
        dbc.addConnectionListener(listener);
        dbc.setPurpose(reason);
        dbc.open();
        return dbc;
    }

    public static void configure(Configuration config) {
        config.reflectWith(new JdkReflector(Task.class.getClassLoader()));
        config.generateUUIDs(ConfigScope.GLOBALLY);
        config.generateVersionNumbers(ConfigScope.GLOBALLY);
        config.allowVersionUpdates(true);
        if (useTransparentPersistence) {
            config.add(new TransparentPersistenceSupport());
        } else {
            config.updateDepth(updateDepth);
            config.activationDepth(activationDepth);
            config.classActivationDepthConfigurable(true);
            config.objectClass(ResourcedTask.class).minimumActivationDepth(6);
            config.objectClass(ResourcePool.class).minimumActivationDepth(8);
            config.objectClass(BaseCalendar.class).minimumActivationDepth(4);
        }
        if (logger.isDebugEnabled()) {
            config.diagnostic().addListener(new Db4oDiagnostic());
        }
    }

    private static Configuration configure() {
        Configuration config = Db4o.newConfiguration();
        configure(config);
        return config;
    }

    public static void removeListener(DatabaseCommitListener listener) {
        commitListeners.remove(listener);
    }

    public static void setActivationDepth(int activationDepth) {
        DatabaseFactory.activationDepth = activationDepth;
    }

    public static void setUpdateDepth(int updateDepth) {
        DatabaseFactory.updateDepth = updateDepth;
    }

    private DatabaseFactory() {
    }
}
