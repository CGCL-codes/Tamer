package org.lcx.scrumvision.core;

import org.lcx.scrumvision.core.handler.AbstractScrumVisionTaskDataHandler;
import org.lcx.scrumvision.core.handler.ScrumVisionTaskDataHandlerSprint;

/**
 * @author Laurent Carbonnaux
 */
public class ScrumVisionRepositoryConnectorSprint extends ScrumVisionRepositoryConnector {

    public ScrumVisionRepositoryConnectorSprint() {
        AbstractScrumVisionTaskDataHandler taskDataHandler = new ScrumVisionTaskDataHandlerSprint(this);
        this.setTaskDataHandler(taskDataHandler);
    }

    @Override
    public String getConnectorKind() {
        return ScrumVisionCorePlugin.CONNECTOR_KIND_SPRINT;
    }

    @Override
    public String getLabel() {
        return ScrumVisionCorePlugin.CLIENT_LABEL_SPRINT;
    }
}
