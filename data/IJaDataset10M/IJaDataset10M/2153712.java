package org.opennms.features.reporting.dao.remoterepository;

import org.opennms.features.reporting.model.remoterepository.RemoteRepositoryConfig;
import org.opennms.features.reporting.model.remoterepository.RemoteRepositoryDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;
import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>DefaultRemoteRepositoryConfigDao class.</p>
 * <p/>
 * Class realize the data access to remote-repository.xml.
 *
 * @author Markus Neumann <markus@opennms.com>
 * @author Ronny Trommer <ronny@opennms.com>
 * @version $Id: $
 * @since 1.8.1
 */
@ContextConfiguration(locations = { "classpath:META-INF/opennms/applicationContext-reportingDao.xml" })
public class DefaultRemoteRepositoryConfigDao implements RemoteRepositoryConfigDao {

    /**
     * Logging
     */
    private Logger logger = LoggerFactory.getLogger("OpenNMS.Report." + DefaultRemoteRepositoryConfigDao.class.getName());

    /**
     * Config resource for remote repository configuration file
     */
    private Resource m_configResource;

    /**
     * Remote repository model
     */
    private RemoteRepositoryConfig m_remoteRepositoryConfig;

    /**
     * Version number for jasper report
     */
    private String m_jasperReportsVersion;

    /**
     * Default constructor load the configuration file
     */
    public DefaultRemoteRepositoryConfigDao(Resource configResource) {
        m_configResource = configResource;
        Assert.notNull(m_configResource, "property configResource must be set to a non-null value");
        logger.debug("Config resource is set to " + m_configResource.toString());
        try {
            loadConfiguration();
        } catch (Exception e) {
            logger.error("Error could not load remote-repository.xml. Error message: '{}'", e.getMessage());
        }
        logger.debug("Configuration '{}' successfully loaded and unmarshalled.", m_configResource.getFilename());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadConfiguration() throws Exception {
        InputStream stream = null;
        long lastModified;
        File file = null;
        try {
            file = m_configResource.getFile();
            Assert.notNull(file, "config file must be sot to a non-null value");
        } catch (IOException e) {
            logger.error("Resource '{}' does not seem to have an underlying File object.", m_configResource);
        }
        if (file != null) {
            lastModified = file.lastModified();
            stream = new FileInputStream(file);
        } else {
            lastModified = System.currentTimeMillis();
            stream = m_configResource.getInputStream();
        }
        setRemoteRepositoryConfig(JAXB.unmarshal(file, RemoteRepositoryConfig.class));
        Assert.notNull(m_remoteRepositoryConfig, "unmarshall config file returned a null value.");
        logger.debug("Unmarshalling config file '{}'", file.getAbsolutePath());
        logger.debug("Remote repository configuration assigned: '{}'", m_remoteRepositoryConfig.toString());
        setJasperReportsVersion(m_remoteRepositoryConfig.getJasperReportsVersion());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getConfigResource() {
        return m_configResource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfigResource(Resource configResource) {
        m_configResource = configResource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJasperReportsVersion() {
        return m_jasperReportsVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isRepositoryActive(String repositoryID) {
        return this.getRepositoryById(repositoryID).isRepositoryActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getURI(String repositoryID) {
        return this.getRepositoryById(repositoryID).getURI();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLoginUser(String repositoryID) {
        return this.getRepositoryById(repositoryID).getLoginUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLoginRepoPassword(String repositoryID) {
        return this.getRepositoryById(repositoryID).getLoginRepoPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRepositoryName(String repositoryID) {
        return this.getRepositoryById(repositoryID).getRepositoryName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRepositoryDescription(String repositoryID) {
        return this.getRepositoryById(repositoryID).getRepositoryDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRepositoryManagementURL(String repositoryID) {
        return this.getRepositoryById(repositoryID).getRepositoryManagementURL();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RemoteRepositoryDefinition> getAllRepositories() {
        List<RemoteRepositoryDefinition> resultList = new ArrayList<RemoteRepositoryDefinition>();
        resultList.addAll(this.m_remoteRepositoryConfig.getRepositoryList());
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RemoteRepositoryDefinition> getActiveRepositories() {
        List<RemoteRepositoryDefinition> resultList = new ArrayList<RemoteRepositoryDefinition>();
        for (RemoteRepositoryDefinition repository : this.m_remoteRepositoryConfig.getRepositoryList()) {
            if (repository.isRepositoryActive()) {
                resultList.add(repository);
            }
        }
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteRepositoryDefinition getRepositoryById(String repositoryID) {
        RemoteRepositoryDefinition result = null;
        for (RemoteRepositoryDefinition repository : this.getAllRepositories()) {
            if (repositoryID.equals(repository.getRepositoryId())) {
                return repository;
            }
        }
        return result;
    }

    /**
     * <p>setRemoteRepositoryConfig</p>
     * <p/>
     * Set remote repository configuration
     *
     * @param remoteRepositoryConfig a {@link org.opennms.features.reporting.model.remoterepository.RemoteRepositoryConfig} object
     */
    private void setRemoteRepositoryConfig(RemoteRepositoryConfig remoteRepositoryConfig) {
        m_remoteRepositoryConfig = remoteRepositoryConfig;
    }

    /**
     * <p>getRemoteRepositoryConfig</p>
     * <p/>
     * Get remote repository configuration
     *
     * @return a {@link org.opennms.features.reporting.model.remoterepository.RemoteRepositoryConfig} object
     */
    private RemoteRepositoryConfig getRemoteRepositoryConfig() {
        return m_remoteRepositoryConfig;
    }

    /**
     * <p>setJasperReportsVersion</p>
     * <p/>
     * Set version for jasper report
     *
     * @param jasperReportsVersion a {@link java.lang.String} object
     */
    private void setJasperReportsVersion(String jasperReportsVersion) {
        m_jasperReportsVersion = jasperReportsVersion;
    }
}
