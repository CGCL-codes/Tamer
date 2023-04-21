package org.dbe.composer.wfengine.bpel.server.deploy.bpr;

import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.bpel.server.deploy.ISdlDeploymentContext;
import org.dbe.composer.wfengine.bpel.server.deploy.SdlAbstractDeploymentContext;
import org.dbe.composer.wfengine.bpel.server.ref.ServiceKey;

/**
 *  Provides access to deployment context resources.
 */
public class SdlBprContext extends SdlAbstractDeploymentContext implements ISdlDeploymentContext {

    private static final Logger logger = Logger.getLogger(SdlBprContext.class.getName());

    /** deployment context (url) classloader */
    private ClassLoader mContextLoader;

    /** context sdl */
    private Set mSdl;

    /** context wsdl */
    private Set mWsdl;

    /** temp/working url */
    private URL mTempLocation;

    /**
     * Constructor.
     * @param aURL the deployment url - points to the bpr archive
     * @param aLoader the context class loader to extract resources from the bpr archive
     */
    public SdlBprContext(URL aURL, URL aTempLocation, ClassLoader aLoader) {
        super(aURL);
        mTempLocation = aTempLocation;
        mContextLoader = aLoader;
        mSdl = new HashSet();
        mWsdl = new HashSet();
        logger.debug("SdlBprContext() " + aURL + ", " + aTempLocation + ", " + aLoader);
    }

    /**
     * @return the context class loader
     */
    protected ClassLoader getContextLoader() {
        return mContextLoader;
    }

    /**
     * Gets input stream for given resource
     * @param aResource resource we want stream for
     */
    public InputStream getResourceAsStream(String aResource) {
        return getContextLoader().getResourceAsStream(aResource);
    }

    /**
     * Indicates if the resource is packaged in the deployment context.
     * @param aResource resource name
     * @return true if the resource is contained in the context
     */
    public boolean hasResource(String aResource) {
        return getResourceURL(aResource) != null;
    }

    public URL getResourceURL(String aContextResource) {
        return getContextLoader().getResource(aContextResource);
    }

    public void addContextSDL(ServiceKey aKey) {
        mSdl.add(aKey);
    }

    public void addContextWSDL(ServiceKey aKey) {
        mWsdl.add(aKey);
    }

    public Set getContextSdl() {
        return mSdl;
    }

    public Set getContextWsdl() {
        return mWsdl;
    }

    public URL getTempDeploymentLocation() {
        return mTempLocation;
    }
}
