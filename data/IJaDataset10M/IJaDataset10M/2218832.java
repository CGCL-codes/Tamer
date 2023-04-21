package org.dbe.composer.wfengine.bpel.server.deploy;

import org.dbe.composer.wfengine.bpel.server.deploy.bpr.IBprFile;
import org.w3c.dom.Document;

/**
 * Top level interface for wrapping of the deployment details.
 */
public interface IServiceDeploymentContainer extends IBprFile, IServiceDeploymentContext {

    /**
    * Get the web services specific deployment/undeployment document.
    */
    public Document getWsddData();

    /**
    * Set the web services specific deployment/undeployment document.
    * @param aDocument
    */
    public void setWsddData(Document aDocument);

    /**
    * Return any special classloaders needed for web services deployment.
    */
    public ClassLoader getWebServicesClassLoader();
}
