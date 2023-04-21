package org.hold.droolsflow.service;

import org.drools.runtime.process.WorkItemHandler;
import org.hold.core.service.TaskService;

/**
 * @author pcornish
 *
 */
public class WorkItemHandlerFactory {

    private TaskService renderService;

    /**
	 * @return
	 */
    public WorkItemHandler getHandler() {
        final DroolsFlowHumanTaskHandler handler = new DroolsFlowHumanTaskHandler();
        handler.setRenderService(renderService);
        return handler;
    }

    /**
	 * @param renderService the renderService to set
	 */
    public void setRenderService(TaskService renderService) {
        this.renderService = renderService;
    }
}
