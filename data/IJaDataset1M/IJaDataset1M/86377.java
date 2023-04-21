package de.schlund.pfixxml.config.impl;

import java.util.Properties;
import de.schlund.pfixxml.config.PageFlowStepActionConfig;

/**
 * Stores configuration for an action that is triggered by a PageFlow step.
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class PageFlowStepActionConfigImpl implements PageFlowStepActionConfig {

    private Class actionType = null;

    private Properties params = new Properties();

    public void setActionType(Class clazz) {
        this.actionType = clazz;
    }

    public Class getActionType() {
        return this.actionType;
    }

    public void setParam(String name, String value) {
        this.params.setProperty(name, value);
    }

    public String getParam(String name) {
        return this.params.getProperty(name);
    }

    public Properties getParams() {
        return this.params;
    }
}
