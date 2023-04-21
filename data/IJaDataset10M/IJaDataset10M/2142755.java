package org.nomadpim.module.schedule;

import org.nomadpim.core.LogFacade;
import org.nomadpim.core.ResourceFacade;

public interface IScheduleModuleConstants {

    String PLUGIN_NAME = "org.nomadpim.module.schedule";

    String PLUGIN_PREFIX = PLUGIN_NAME + ".";

    ResourceFacade RESOURCES = new ResourceFacade(PLUGIN_NAME);

    LogFacade LOG = new LogFacade(PLUGIN_NAME);
}
