package pl.kernelpanic.dbmonster;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import pl.kernelpanic.dbmonster.connection.ConnectionProvider;
import pl.kernelpanic.dbmonster.connection.Transaction;
import pl.kernelpanic.dbmonster.schema.Schema;
import pl.kernelpanic.dbmonster.util.Converter;
import pl.kernelpanic.dbmonster.util.ScriptReaderIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DBMonster is a test data generation tool for SQL databases.
 *
 * @author Piotr Maj &lt;pm@jcake.com&gt;
 *
 * @version $Id: DBMonster.java,v 1.8 2006/01/05 16:29:37 majek Exp $
 */
public class DBMonster {

    /**
     * DBMonster's version.
     */
    public static final String VERSION = "1.0.4";

    /**
     * A key under which the logger is stored in DBMonsterContext.
     */
    public static final String LOGGER_KEY = "pl.kernelpanic.dbmonster.LOGGER_KEY";

    /**
     * A key under which the connection provider is stored in the
     * DBMonsterContext.
     */
    public static final String CONNECTION_PROVIDER_KEY = "pl.kernelpanic.dbmonster.CONNECTION_PROVIDER_KEY";

    /**
     * A key under which the dictionaries manager is stored in
     * context.
     */
    public static final String DICTIONARY_MANAGER_KEY = "pl.kernelpanic.dbmonster.DICTIONARY_MANAGER_KEY";

    /**
     * A key under which the random number generator is stored in context.
     */
    public static final String RANDOM_KEY = "pl.kernelpanic.dbmonster.RANDOM_KEY";

    /**
     * A key under which progress monitor (if any) is stored  in the context.
     */
    public static final String PROGRESS_MONITOR_KEY = "pl.kernelpanic.dbmonster.PROGRESS_MONITOR_KEY";

    /**
     * A key under which transaction size is stored.
     */
    public static final String TRANSACTION_SIZE_KEY = "pl.kernelpanic.dbmonster.TRANSACTION_SIZE_KEY";

    /**
     * A connection provider used by DBMonster.
     */
    private ConnectionProvider connProvider = null;

    /**
     * A list of schemas that will be generated.
     */
    private ArrayList schemaList = new ArrayList();

    /**
     * A logger used.
     */
    private Log logger = LogFactory.getLog(DBMonster.class);

    /**
     * A context.
     */
    private DBMonsterContext context = new DBMonsterContext();

    /**
     * Progress monitor.
     */
    private ProgressMonitor progressMonitor = null;

    /**
     * Pre-generation script
     */
    private File preScript;

    /**
     * Post-generation script
     */
    private File postScript;

    /**
     * Random number generator.
     */
    private Random random = new Random();

    /**
     * The number of insert statements performed in a single transaction.
     */
    private int transactionSize;

    /**
     * Returns a connection provider used by this instance.
     *
     * @return connection provider
     */
    public ConnectionProvider getConnectionProvider() {
        return connProvider;
    }

    /**
     * Sets a connection provider.
     *
     * @param cp a connection provider
     */
    public void setConnectionProvider(ConnectionProvider cp) {
        connProvider = cp;
    }

    /**
     * Adds a schema.
     *
     * @param schema a schema to add.
     *
     * @throws Exception if schema with the same name already exists
     */
    public void addSchema(Schema schema) throws Exception {
        if (schemaExists(schema)) {
            throw new Exception("Schema named <" + schema.getName() + "> already exists.");
        }
        schemaList.add(schema);
    }

    /**
     * Sets a logger for this DBMonster instance.
     *
     * @param log a logger
     */
    public void setLogger(Log log) {
        logger = log;
    }

    /**
     * Returns logger.
     *
     * @return a logger.
     */
    public Log getLogger() {
        return logger;
    }

    /**
     * Sets the properties.
     *
     * @param props the properties
     */
    public void setProperties(Properties props) {
        Enumeration propertyKeys = props.propertyNames();
        while (propertyKeys.hasMoreElements()) {
            String key = (String) propertyKeys.nextElement();
            context.setProperty(key, props.get(key));
        }
    }

    /**
     * Does the job. ;) Before calling this method ensure that:
     * <ol>
     *  <li>Connection provider is set.</li>
     *  <li>Progress monitor (if any) is set.</li>
     *  <li>Valid schemas are added.</li>
     * </ol>
     *
     * @throws Exception on errors.
     */
    public void doTheJob() throws Exception {
        long t0 = System.currentTimeMillis();
        context.setProperty(LOGGER_KEY, logger);
        if (logger.isInfoEnabled()) {
            logger.info("Let's feed this hungry database.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Checking the connection provider.");
        }
        if (connProvider == null) {
            throw new Exception("No connection provider.");
        }
        connProvider.testConnection();
        if (logger.isDebugEnabled()) {
            logger.debug("Connection provider is OK.");
        }
        if (transactionSize > 1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Transaction size is set to " + transactionSize + ". Turning autoCommit off...");
            }
            context.setProperty(TRANSACTION_SIZE_KEY, new Integer(transactionSize));
            connProvider.setAutoCommit(false);
        }
        context.setProperty(CONNECTION_PROVIDER_KEY, connProvider);
        context.setProperty(RANDOM_KEY, random);
        context.setProperty(PROGRESS_MONITOR_KEY, progressMonitor);
        DictionaryManager dm = new DictionaryManager();
        dm.setRandom(random);
        context.setProperty(DICTIONARY_MANAGER_KEY, dm);
        if (preScript != null) {
            executeScript(connProvider, preScript);
        }
        Iterator it = schemaList.iterator();
        try {
            if (progressMonitor != null) {
                progressMonitor.setUp();
                progressMonitor.setSchemaCount(schemaList.size());
            }
            while (it.hasNext()) {
                Schema schema = (Schema) it.next();
                if (progressMonitor != null) {
                    progressMonitor.setSchemaName(schema.getName());
                }
                schema.initialize(context);
                schema.generate();
                if (progressMonitor != null) {
                    progressMonitor.schemaComplete();
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (progressMonitor != null) {
                progressMonitor.tearDown();
            }
        }
        if (postScript != null) {
            executeScript(connProvider, postScript);
        }
        t0 = System.currentTimeMillis() - t0;
        logger.info("Finished in " + Converter.formatTime(t0));
    }

    /**
     * Returns the version.
     *
     * @return version
     */
    public static final String getVersion() {
        return VERSION;
    }

    /**
     * Check if schema of this name already exists.
     *
     * @param schema schema
     *
     * @return <code>true</code> if schema already exists.
     */
    private boolean schemaExists(Schema schema) {
        for (int i = 0; i < schemaList.size(); i++) {
            if (schema.compareTo(schemaList.get(i)) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the progress monitor.
     *
     * @return progress monitor.
     */
    public ProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    /**
     * Sets the progress monitor.
     *
     * @param monitor progress monitor.
     */
    public void setProgressMonitor(ProgressMonitor monitor) {
        progressMonitor = monitor;
    }

    public void setPostScript(File postScript) {
        this.postScript = postScript;
    }

    public void setPreScript(File preScript) {
        this.preScript = preScript;
    }

    public void setTransactionSize(int transactionSize) {
        this.transactionSize = transactionSize;
    }

    private void executeScript(ConnectionProvider cp, File scriptFile) throws Exception {
        ScriptReaderIterator it = new ScriptReaderIterator(scriptFile);
        Transaction tx = new Transaction(cp);
        try {
            tx.begin();
            while (it.hasNext()) {
                String query = (String) it.next();
                if (logger.isDebugEnabled()) {
                    logger.debug("Executing query: " + query);
                }
                tx.prepareStatement(query);
                tx.execute();
            }
            tx.commit();
        } catch (Exception e) {
            tx.abort();
            throw e;
        } finally {
            tx.close();
        }
    }
}
