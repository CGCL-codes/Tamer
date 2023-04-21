package jgnash.engine.db4o;

import com.db4o.ObjectContainer;
import jgnash.engine.Config;
import jgnash.engine.dao.ConfigDAO;
import java.util.List;
import java.util.logging.Logger;

/**
 * Hides all the db4o config code
 *
 * @author Craig Cavanaugh
 * @version $Id: Db4oConfigDAO.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
class Db4oConfigDAO extends AbstractDb4oDAO implements ConfigDAO {

    private static final String CONFIG_SEMAPHORE = "ConfigLock";

    private final Logger logger = Logger.getLogger(Db4oConfigDAO.class.getName());

    Db4oConfigDAO(final ObjectContainer container, final boolean isRemote) {
        super(container, isRemote);
    }

    @Override
    public synchronized Config getDefaultConfig() {
        Config defaultConfig = null;
        if (container.ext().setSemaphore(CONFIG_SEMAPHORE, SEMAPHORE_WAIT_TIME)) {
            List<Config> list = container.query(Config.class);
            if (!list.isEmpty()) {
                defaultConfig = list.get(0);
            }
            if (defaultConfig == null) {
                defaultConfig = new Config();
                container.set(defaultConfig);
                commit();
                logger.info("Generating new default config");
            }
            container.ext().releaseSemaphore(CONFIG_SEMAPHORE);
        } else {
            logger.severe(SEMAPHORE_WARNING);
        }
        return defaultConfig;
    }

    @Override
    public void commit(Config config) {
        if (container.ext().setSemaphore(CONFIG_SEMAPHORE, SEMAPHORE_WAIT_TIME)) {
            container.set(config);
            commit();
            container.ext().releaseSemaphore(CONFIG_SEMAPHORE);
        } else {
            logger.severe(SEMAPHORE_WARNING);
        }
    }
}
