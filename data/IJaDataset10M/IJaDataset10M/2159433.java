package org.netbeans.module.flexbean.modules.project.dependency;

import java.util.List;
import org.netbeans.api.project.Project;

/**
 *
 * @author arnaud
 */
public final class FlexProjectDependenciesProvider {

    private final Project _project;

    public FlexProjectDependenciesProvider(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project missing");
        }
        _project = project;
    }

    public List<FlexProjectDependency> getDependencies() {
        FlexProjectDependencyFactory factory = FlexProjectDependencyFactory.getFactory(_project);
        if (!factory.isLastVersion()) {
            final List<FlexProjectDependency> dependencies = getDependencies();
            setDependencies(dependencies);
            factory = FlexProjectDependencyFactory.getLastVersionFactory(_project);
        }
        return factory.getDependencies();
    }

    public void setDependencies(List<FlexProjectDependency> dependencies) {
        final FlexProjectDependencyFactory factory = FlexProjectDependencyFactory.getLastVersionFactory(_project);
        factory.setDependencies(dependencies);
    }
}
