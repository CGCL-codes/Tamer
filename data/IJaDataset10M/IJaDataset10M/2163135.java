package net.sf.stump.eclipse.configuration;

import net.sf.stump.eclipse.util.ProjectHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;

/**
 * @author Joni Suominen
 */
public class ConfigurationTemplate {

    private final IProject project;

    public ConfigurationTemplate(IProject project) {
        this.project = project;
    }

    public void execute(Callback callback) {
        if (!project.isOpen()) {
            return;
        }
        IProjectDescription description = ProjectHelper.getDescription(project);
        callback.configure(description);
        ProjectHelper.setDescription(project, description);
    }

    public static interface Callback {

        void configure(IProjectDescription description);
    }
}
