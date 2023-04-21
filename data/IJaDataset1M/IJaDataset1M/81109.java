package com.guzzservices.action.vo;

import com.guzzservices.business.ConfigurationGroup;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ConfigurationGroupForm {

    private ConfigurationGroup configurationGroup;

    private boolean newGroup;

    public ConfigurationGroupForm(ConfigurationGroup configurationGroup) {
        if (configurationGroup != null) {
            this.configurationGroup = configurationGroup;
            this.newGroup = false;
        } else {
            this.configurationGroup = new ConfigurationGroup();
            this.newGroup = true;
        }
    }

    public ConfigurationGroup getConfigurationGroup() {
        return configurationGroup;
    }

    public void setConfigurationGroup(ConfigurationGroup configurationGroup) {
        this.configurationGroup = configurationGroup;
    }

    public boolean isNewGroup() {
        return newGroup;
    }

    public void setNewGroup(boolean newGroup) {
        this.newGroup = newGroup;
    }
}
