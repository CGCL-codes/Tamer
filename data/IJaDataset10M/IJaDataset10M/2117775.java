package org.dbe.composer.wfengine.bpel.server.deploy;

import java.lang.reflect.Constructor;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.bpel.server.logging.ISdlLogWrapper;

/**
 * Factory for creating new deployment handlers.
 * In this impl, the handlers ARE NOT thread safe and <code>newInstance</code>
 * should be called for each deployment.
 */
public class ServiceDeploymentHandlerFactory implements IServiceDeploymentHandlerFactory {

    /** for deployment logging purposes */
    protected static final Logger logger = Logger.getLogger(ServiceDeploymentHandlerFactory.class.getName());

    /**
     * Deployment factory used by the handler impl.
     */
    protected IServiceDeploymentFactory mDeploymentFactory;

    /**
     * Constructor.
     * @param aParams Any aeEngineConfig params.
     */
    public ServiceDeploymentHandlerFactory(Map aParams) throws Exception {
        logger.debug("ServiceDeploymentHandlerFactory(Map)");
        Map deployerParams = (Map) aParams.get("DeploymentFactory");
        String deploymentFactoryImpl = (String) deployerParams.get("Class");
        Class deployerFactoryImplClass = Class.forName(deploymentFactoryImpl);
        Constructor xTor = deployerFactoryImplClass.getConstructor(new Class[] { Map.class });
        mDeploymentFactory = (IServiceDeploymentFactory) xTor.newInstance(new Object[] { deployerParams });
    }

    /**
     * Accessor for the deployment factory impl.
     */
    public IServiceDeploymentFactory getSdlDeploymentFactory() {
        return mDeploymentFactory;
    }

    public IServiceDeploymentHandler newInstance(ISdlLogWrapper aLogWrapper) {
        logger.debug("newInstance(ISdlLogWrapper)");
        return new ServiceDeploymentHandler(aLogWrapper, getSdlDeploymentFactory());
    }
}
