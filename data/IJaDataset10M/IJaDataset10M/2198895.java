package org.lcx.scrumvision.ui.wizzard;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.lcx.scrumvision.core.ScrumVisionCorePlugin;

/**
 * @author Laurent Carbonnaux
 */
public class ScrumVisionRepositorySettingsPageUserStory extends ScrumVisionRepositorySettingsPage {

    public ScrumVisionRepositorySettingsPageUserStory(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    public String getConnectorKind() {
        return ScrumVisionCorePlugin.CONNECTOR_KIND_USERSTORY;
    }
}
