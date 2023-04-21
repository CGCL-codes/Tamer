package com.leclercb.taskunifier.gui.components.configuration;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.lists.TaskStatusesFieldType;

public class TaskStatusesConfigurationPanel extends DefaultConfigurationPanel {

    public TaskStatusesConfigurationPanel(ConfigurationGroup configuration) {
        super(configuration);
        this.initialize();
        this.pack();
    }

    private void initialize() {
        this.addField(new ConfigurationField("TASK_STATUSES", null, false, new TaskStatusesFieldType()));
    }
}
