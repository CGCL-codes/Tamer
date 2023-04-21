package org.icenigrid.gridsam.core.plugin.transfer.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icenigrid.gridsam.core.ConfigurationException;
import org.icenigrid.gridsam.core.plugin.manager.InitRegistry;
import org.icenigrid.gridsam.core.plugin.transfer.persistence.FileJobInstanceStore;
import org.quartz.Scheduler;

/**
 * <strong>Purpose:</strong><br>
 * This is an Adapter Class to get File job Storage Object
 * 
 * @author Haojie Zhou create : 2008-8-15
 * 
 * 
 */
public class RegistryUtil {

    /**
	 * Logger.
	 */
    private static final Log LOG = LogFactory.getLog(RegistryUtil.class);

    /**
	 * get FileJobInstanceStore from Hivemind registry 1. find
	 * gridsam.FileJobInstanceStore from common.xml 2. find
	 * gridsam.FileJobInstanceStoreConfig from database.xml 3. using hivemind to
	 * inject a object (using singleton mode)
	 * 
	 * @return the {@link FileJobInstanceStore}.
	 * @throws ConfigurationException
	 */
    public static synchronized FileJobInstanceStore getFileJobInstanceStore() throws ConfigurationException {
        if (!InitRegistry.getRegistry().containsService("gridsam.FileJobInstanceStore", FileJobInstanceStore.class)) {
            LOG.error("cannot find 'gridsam.FileJobInstanceStore' from configuration");
            throw new ConfigurationException("cannot find 'gridsam.FileJobInstanceStore' from configuration");
        }
        FileJobInstanceStore xFileJobInstanceStore = (FileJobInstanceStore) InitRegistry.getRegistry().getService("gridsam.FileJobInstanceStore", FileJobInstanceStore.class);
        return xFileJobInstanceStore;
    }

    /**
	 * get {@link Scheduler} from Hivemind registry 1. find
	 * gridsam.FileSchedulerDelegate from openpbs.xml 2. find
	 * gridsam.FileSchedulerDelegateConfig from ..... 3. using hivemind to inject
	 * a object (using singleton mode)
	 * 
	 * @return the File's {@link Scheduler}
	 * @throws ConfigurationException
	 */
    public static synchronized Scheduler getAftScheduler() throws ConfigurationException {
        if (!InitRegistry.getRegistry().containsService("gridsam.Scheduler", Scheduler.class)) {
            LOG.error("cannot find 'gridsam.AftSchedulerDelegate' from configuration");
            throw new ConfigurationException("cannot find 'gridsam.AftSchedulerDelegate' from configuration");
        }
        Scheduler xScheduler = (Scheduler) InitRegistry.getRegistry().getService("gridsam.Scheduler", Scheduler.class);
        return xScheduler;
    }
}
