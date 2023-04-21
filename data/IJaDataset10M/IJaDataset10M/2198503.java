package org.apache.axis2.clustering.configuration.commands;

import org.apache.axis2.clustering.configuration.ConfigurationClusteringCommand;
import org.apache.axis2.context.ConfigurationContext;

/**
 * 
 */
public class RollbackCommand extends ConfigurationClusteringCommand {

    public int getCommandType() {
        return ROLLBACK;
    }

    public void process(ConfigurationContext configContext) throws Exception {
    }

    public void prepare(ConfigurationContext configContext) {
    }

    public void commit(ConfigurationContext configContext) throws Exception {
    }

    public void rollback(ConfigurationContext configContext) throws Exception {
    }

    public String toString() {
        return "RollbackCommand";
    }
}
