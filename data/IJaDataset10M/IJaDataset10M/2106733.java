package org.jadira.dependencynavigator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.jadira.dependencynavigator.controller.ArtifactResolver;
import org.jadira.dependencynavigator.implementations.IProgressMeter;

public class PomFile extends Artifact {

    protected List<Artifact> dependencies;

    protected MavenProject model;

    public PomFile(MavenProject model, Artifact parent, String scope) throws ArtifactInitialisationException {
        super(parent, model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId(), model.getArtifactId(), model.getVersion() == null ? model.getParent().getVersion() : model.getVersion(), scope);
        this.model = model;
    }

    public void resolveDependencies(ArtifactResolver resolver, IProgressMeter progress, Stack<LeafDependency> path) {
        progress.resolving(this);
        dependencies = new ArrayList<Artifact>();
        for (Object o : model.getDependencies()) {
            Dependency dependency = (Dependency) o;
            try {
                LeafDependency leafDependency = new LeafDependency(this, dependency, dependency.getScope());
                if (!path.contains(leafDependency)) {
                    path.push(leafDependency);
                    Artifact artifact = resolver.resolveArtifact(this, dependency, progress, path);
                    if (artifact != null) {
                        dependencies.add(artifact);
                    }
                }
            } catch (ArtifactInitialisationException e) {
                e.printStackTrace();
            }
        }
    }

    public MavenProject getModel() {
        return model;
    }

    @Override
    public boolean isLeaf() {
        return dependencies.size() == 0;
    }

    @Override
    public List<Artifact> getDependencies() {
        return dependencies;
    }

    @Override
    public int dependencyCount() {
        if (isLeaf()) {
            return 1;
        }
        int dependencyCount = 0;
        for (Artifact artifact : dependencies) {
            dependencyCount += artifact.dependencyCount();
        }
        return dependencyCount;
    }
}
