package org.apache.axis2.deployment;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.AxisFault;

/**
 * this interface is used to inform deployment lifecycle realated events to
 * listners
 */
public interface DeploymentLifeCycleListener {

    /**
     * calls before creating the configuration context to do any initializing work.
     * @param axisConfig
     * @throws AxisFault
     */
    public void preDeploy(AxisConfiguration axisConfig) throws AxisFault;

    /**
     * calls after starting the configuration context to resume any activity.
     * @param configurationContext
     * @throws AxisFault
     */
    public void postDeploy(ConfigurationContext configurationContext) throws AxisFault;
}
