package org.apache.axis2.deployment;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;

/**
 * This interface is used to provide the custom deployment mechanism , where you
 * can write your own Deployer to process a particular type and make that to
 * a service or a module.
 */
public interface Deployer {

    /**
     * Initialize the Deployer
     * @param configCtx our ConfigurationContext
     */
    void init(ConfigurationContext configCtx);

    /**
     * Process a file and add it to the configuration
     * @param deploymentFileData the DeploymentFileData object to deploy
     * @throws DeploymentException if there is a problem
     */
    void deploy(DeploymentFileData deploymentFileData) throws DeploymentException;

    /**
     * Set the directory
     * @param directory directory name
     */
    void setDirectory(String directory);

    /**
     * Set the extension to look for
     * TODO: Support multiple extensions?
     * @param extension the file extension associated with this Deployer
     */
    void setExtension(String extension);

    /**
     * Remove a given file from the configuration
     * @param fileName name of item to remove
     * @throws DeploymentException if there is a problem
     */
    void unDeploy(String fileName) throws DeploymentException;
}
