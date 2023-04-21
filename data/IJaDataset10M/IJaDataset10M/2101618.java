package net.sourceforge.clownfish.core.support.test;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;

/**
 * MyDeploymentFactory is a test class that implements 
 * <code>javax.enterprise.deploy.spi.factories.DeploymentFactory</code>.
 * 
 * @author lyeung
 */
public class MyDeploymentFactory implements DeploymentFactory {

    /**
     * {@inheritDoc}
     */
    public boolean handlesURI(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public DeploymentManager getDeploymentManager(String arg0, String arg1, String arg2) throws DeploymentManagerCreationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public DeploymentManager getDisconnectedDeploymentManager(String arg0) throws DeploymentManagerCreationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public String getProductVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
