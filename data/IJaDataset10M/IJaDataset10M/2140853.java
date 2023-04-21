package rafa.dep;

import java.util.Collection;
import java.util.HashSet;

/**
 * Dependent manager.
 * @author  rafa
 */
public class DependencyManager {

    /**
     * The object whose dependencies have to be managed.
     * @uml.property  name="dependent"
     * @uml.associationEnd  
     */
    private final Dependent dependent;

    /**
     * Managed dependencies.
     * @uml.property  name="dependencies"
     */
    private Collection<Dependency> dependencies;

    private Collection<Object> depTargets;

    public DependencyManager(Dependent dependent) {
        this.dependent = dependent;
    }

    /**
     * Checks that a dependency is not included in a dependency tree.
     * @param dep 
     * @return <code>true</code> if newDependency is already present in deps
     */
    public boolean hasDependency(Object dep) {
        if (dependencies == null) return false;
        for (Dependency d : dependencies) {
            if (d.getTarget() == dep) return true;
            Collection<Dependency> subDeps = d.search(dep);
            if (subDeps != null && !subDeps.isEmpty()) return true;
        }
        return false;
    }

    /**
     * Gets the dependencies, that is the directly required objects themselves,
     * with no further details.
     * @return
     */
    public Collection<Object> getDependencyTargets() {
        return depTargets;
    }

    /**
     * @return
     * @uml.property  name="dependencies"
     */
    public Collection<Dependency> getDependencies() {
        return dependencies;
    }

    /**
     * Gets the dependencies on a given object, including the details.
     * @param dep
     * @return a collection of dependencies on the passed object, or
     *      <code>null</code> if there is none.
     */
    public Collection<Dependency> getDependencies(Object dep) {
        if (dependencies == null) return null;
        Collection<Dependency> result = null;
        for (Dependency d : dependencies) {
            if (d.getTarget() == dep) {
                if (result == null) result = new HashSet<Dependency>();
                result.add(d);
            } else {
                Collection<Dependency> subDeps = d.search(dep);
                if (subDeps != null && !subDeps.isEmpty()) {
                    result.addAll(subDeps);
                }
            }
        }
        return result;
    }

    /**
     * Adds a dependency.
     * @param newDependency the added dependency
     * @throws CircularDependencyException if there is a circular dependency
     */
    public void addDependency(Dependency newDependency) throws CircularDependencyException {
        if (newDependency.getTarget() == dependent) {
            newDependency.validateCircularDependency();
        } else {
            Collection<Dependency> selfDeps = newDependency.search(dependent);
            if (selfDeps != null && !selfDeps.isEmpty()) {
                for (Dependency selfDep : selfDeps) {
                    selfDep.validateCircularDependency();
                }
            }
        }
        if (dependencies == null) {
            dependencies = new HashSet<Dependency>();
            depTargets = new HashSet<Object>();
        }
        dependencies.add(newDependency);
        depTargets.add(newDependency.getTarget());
    }

    /**
     * Removes a single dependency.
     * @param dep 
     */
    public void removeDependency(Dependency dep) {
        if (dependencies != null) dependencies.remove(dep);
    }

    /**
     * Removes every dependency on a given target.
     * @param target
     */
    public void removeDependencies(Object target) {
        for (Dependency dependency : getDependencies(target)) {
            dependencies.remove(dependency);
        }
    }

    /**
     * Removes all of the dependencies.
     */
    public void clearDependencies() {
        if (dependencies != null) dependencies.clear();
    }
}
