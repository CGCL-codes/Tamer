package org.dbe.composer.wfengine.bpel.webserver.web;

import java.io.InputStream;
import java.net.URL;
import org.apache.axis.AxisEngine;
import org.apache.axis.ConfigurationException;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.deployment.wsdd.WSDDGlobalConfiguration;
import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;

/**
 * This class extends the axis file provider so we can create our own deployment
 * context, which can then be classloader aware.
 */
public class SdlResourceProvider extends FileProvider {

    /** for deployment logging purposes */
    private static final Logger logger = Logger.getLogger(SdlResourceProvider.class.getName());

    /** The configuration we load. */
    protected URL mConfigResource;

    /** Input stream cache */
    protected InputStream mInputStreamCache;

    /**
     * Constructs a resource provider from the passed config url.
     * @param aConfigResource The config file resource to load
     */
    public SdlResourceProvider(URL aConfigResource) {
        super((InputStream) null);
        logger.info("SdlResourceProvider() url=" + aConfigResource);
        mConfigResource = aConfigResource;
    }

    /**
     * Override input stream setter to sync protected cache.
     * @see org.apache.axis.configuration.FileProvider#setInputStream(java.io.InputStream)
     */
    public void setInputStream(InputStream aStream) {
        super.setInputStream(aStream);
        mInputStreamCache = aStream;
    }

    /**
     * Configures the given AxisEngine with the given descriptor
     * @see org.apache.axis.EngineConfiguration#configureEngine(org.apache.axis.AxisEngine)
     */
    public void configureEngine(AxisEngine aEngine) throws ConfigurationException {
        buildDeployment();
        getDeployment().configureEngine(aEngine);
        aEngine.refreshGlobalOptions();
    }

    /**
     * @return New deployment, which is classloader context aware.
     * @throws ConfigurationException
     */
    public synchronized BprDeployment buildDeployment() throws ConfigurationException {
        logger.debug("buildDeployment()");
        if (getDeployment() == null) {
            try {
                if (mInputStreamCache == null) {
                    setInputStream(mConfigResource.openStream());
                }
                setDeployment(new BprDeployment(XMLUtils.newDocument(mInputStreamCache).getDocumentElement()));
                setInputStream(null);
                if (getDeployment().getGlobalConfiguration() == null) {
                    WSDDGlobalConfiguration config = new WSDDGlobalConfiguration();
                    config.setOptionsHashtable(new java.util.Hashtable());
                    getDeployment().setGlobalConfiguration(config);
                }
            } catch (Exception e) {
                logger.error("Exception: " + e);
                throw new ConfigurationException(e);
            }
        }
        return getMyDeployment();
    }

    /**
     * Override, since we will rebuild deployment on startup.
     * @todo should we write out cache?
     * @see org.apache.axis.EngineConfiguration#writeEngineConfig(org.apache.axis.AxisEngine)
     */
    public void writeEngineConfig(AxisEngine engine) {
    }

    /**
     * Helper method for casting our deployment to the type we create.
     */
    protected BprDeployment getMyDeployment() {
        return (BprDeployment) getDeployment();
    }
}
