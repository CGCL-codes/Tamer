package org.apache.axis2.clustering.configuration.commands;

import org.apache.axis2.clustering.configuration.ConfigurationClusteringCommand;
import org.apache.axis2.context.ConfigurationContext;

/**
 * 
 */
public class ApplyServicePolicyCommand extends ConfigurationClusteringCommand {

    private String policy;

    private String serviceName;

    public void process(ConfigurationContext confiCtx) throws Exception {
    }

    public void prepare(ConfigurationContext configContext) {
    }

    public void commit(ConfigurationContext configContext) throws Exception {
    }

    public void rollback(ConfigurationContext configContext) throws Exception {
    }

    public String toString() {
        return "APPLY_SERVICE_POLICY_EVENT";
    }

    public int getCommandType() {
        return APPLY_SERVICE_POLICY;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
