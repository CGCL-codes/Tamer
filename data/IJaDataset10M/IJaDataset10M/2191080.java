package org.impalaframework.module.holder.graph;

import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.holder.DefaultModuleStateHolder;

/**
 * Extension of {@link DefaultModuleStateHolder}, which also holds a reference
 * to the {@link DependencyManager} used to arrange modules in a graph.
 */
public class GraphModuleStateHolder extends DefaultModuleStateHolder {

    private DependencyManager dependencyManager;

    public void setDependencyManager(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }
}
