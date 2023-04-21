package org.jage.workplace.pico;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.jage.ComponentException;
import org.jage.config.ConfigurationException;
import org.jage.config.IConfiguration;
import org.jage.workplace.IWorkplace;
import org.jage.workplace.WorkplaceManager;
import org.jage.workplace.address.IWorkplaceAddress;
import org.jage.workplace.pico.config.Configuration;
import org.jage.workplace.pico.config.ConfigurationContainerBuilder;
import org.jage.workplace.pico.config.XmlConfigurationBuilder;
import org.picocontainer.PicoContainer;

/**
 * Basic implementation of IWorkplace Manager.
 * 
 * @author Pawel Kedzior
 */
public class PicoWorkplaceManager extends WorkplaceManager {

    /**
	 * Contained workplaces.
	 * Operations on the hashmap should
	 * be synchronized by locking this object (not the field).
	 */
    private final HashMap<IWorkplaceAddress, IWorkplace> _workplaces = new HashMap<IWorkplaceAddress, IWorkplace>();

    /**
	 * Constructor
	 * @param configuration
	 * @throws ConfigurationException
	 */
    public PicoWorkplaceManager(IConfiguration configuration) throws ConfigurationException {
        super(configuration);
        IConfiguration workplacesGroupConfig = configuration.getParameterGroup("workplaces");
        Collection<IConfiguration> workplacesConfigs = workplacesGroupConfig.getParameterGroups("workplace");
        ConfigurationContainerBuilder builder = new ConfigurationContainerBuilder();
        int index = 0;
        for (IConfiguration config : workplacesConfigs) {
            String fileName = config.getParameterValue("@pico", "workplace" + Integer.toString(index) + ".xml");
            File file = new File(fileName);
            if (file.exists()) {
                XmlConfigurationBuilder configBuilder = new XmlConfigurationBuilder();
                Configuration picoConfiguration = configBuilder.getConfiguration(fileName);
                PicoContainer pico = builder.getPicoContainer(picoConfiguration);
                IWorkplace workplace = (IWorkplace) pico.getComponentInstance("workplace");
                if (workplace != null) {
                    try {
                        addWorkplace(workplace);
                    } catch (ComponentException e) {
                        throw new ConfigurationException("Failed to add workplace from file: " + file.getAbsolutePath(), e);
                    }
                } else {
                    throw new ConfigurationException("Failed to construct IWorkplace from file: " + file.getAbsolutePath());
                }
            } else {
                throw new ConfigurationException("File: " + file.getAbsolutePath() + " does not exist.");
            }
        }
    }

    /**
	 * @see org.jage.workplace.WorkplaceManager#addWorkplace(org.jage.workplace.IWorkplace)
	 */
    @Override
    public synchronized void addWorkplace(IWorkplace workplace) throws ComponentException {
        IWorkplaceAddress address = workplace.getWorkplaceAddress();
        if (!_workplaces.containsKey(address)) {
            _workplaces.put(address, workplace);
        } else {
            throw new ComponentException("IWorkplace of address: " + address + " already exists in the manager.");
        }
    }

    /**
	 * @see org.jage.workplace.WorkplaceManager#getWorkplace(org.jage.workplace.address.IWorkplaceAddress)
	 */
    @Override
    public synchronized IWorkplace getWorkplace(IWorkplaceAddress address) {
        return _workplaces.get(address);
    }

    @Override
    public synchronized IWorkplace[] getWorkplaces() {
        return _workplaces.values().toArray(new IWorkplace[0]);
    }

    /**
	 * @see org.jage.workplace.WorkplaceManager#removeWorkplace(org.jage.workplace.address.IWorkplaceAddress)
	 */
    @Override
    public synchronized void removeWorkplace(IWorkplaceAddress address) throws ComponentException {
        if (_workplaces.containsKey(address)) {
            _workplaces.remove(address);
        } else {
            throw new ComponentException("IWorkplace of address: " + address + " does not exist in the manager.");
        }
    }

    /**
	 * @see org.jage.workplace.WorkplaceManager#start()
	 */
    @Override
    public synchronized void start() throws ComponentException {
        super.start();
        startAllWorkplaces();
    }

    /**
	 * @see org.jage.workplace.WorkplaceManager#stop()
	 */
    @Override
    public synchronized void stop() throws ComponentException {
        stopAllWorkplaces();
        super.stop();
    }

    /**
	 * Logger
	 */
    @SuppressWarnings("unused")
    private static final Logger _log = Logger.getLogger(PicoWorkplaceManager.class);
}
