package com.ixora.rms.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import com.ixora.common.Service;
import com.ixora.common.xml.exception.XMLException;
import com.ixora.rms.repository.AgentInstallationData;

/**
 * @author Daniel Moraru
 */
public interface AgentRepositoryService extends Service {

    /**
	 * @return a map of AgentInstallationInfo keyed by agent id.
	 */
    public Map<String, AgentInstallationData> getInstalledAgents();

    /**
	 * @param productRoot another product instance installation folder
	 * @return a map of AgentInstallationInfo keyed by agent id.
	 * @throws FileNotFoundException
	 * @throws XMLException
	 */
    public Map<String, AgentInstallationData> getInstalledAgents(File productRoot) throws XMLException, FileNotFoundException;
}
