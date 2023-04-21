package org.codescale.edependency.finder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.codescale.eDependency.Bundle;
import org.codescale.eDependency.Feature;
import org.codescale.eDependency.RequireBundle;
import org.codescale.eDependency.Workspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class DependencyCycleFinder {

    private List<Bundle> bundles;

    /**
     * @param bundles
     */
    public DependencyCycleFinder(List<Bundle> bundles) {
        setBundles(bundles);
    }

    /**
     * @param workspace
     */
    public DependencyCycleFinder(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("To detect cycles a workspace object is required.");
        }
        List<Bundle> bundles = new ArrayList<Bundle>();
        bundles.addAll(workspace.getInstalledBundlesList());
        for (Feature feature : workspace.getAvailableFeaturesList()) {
            bundles.addAll(feature.getBundlesList());
        }
        setBundles(bundles);
    }

    /**
     * @param dependencyCycles
     * @param cycle
     * @return
     */
    private boolean contains(List<DependencyCycle> dependencyCycles, DependencyCycle cycle) {
        boolean result = false;
        List<Bundle> newPath = cycle.getPath();
        for (DependencyCycle dependencyCycle : dependencyCycles) {
            List<Bundle> oldPath = dependencyCycle.getPath();
            int startIndex = oldPath.indexOf(newPath.get(0));
            if ((oldPath.size() != newPath.size()) || (startIndex == -1)) {
                continue;
            }
            boolean equal = true;
            Iterator<Bundle> iterator = newPath.iterator();
            for (int i = startIndex; (i < oldPath.size()) && equal; i++) {
                equal &= oldPath.get(i) == iterator.next();
            }
            for (int i = 0; (i < startIndex) && equal; i++) {
                equal &= oldPath.get(i) == iterator.next();
            }
            result |= equal;
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * @param bundle
     * @param path
     * @param dependencyCycles
     * @param arrayList
     */
    private void detectCycle(Bundle nextNode, List<Bundle> path, Set<Bundle> enlistedInCycle, List<DependencyCycle> dependencyCycles) {
        if (nextNode == null) {
            return;
        }
        if (enlistedInCycle.contains(nextNode)) {
            return;
        }
        boolean cycleDetected = false;
        if (path.size() >= 2) {
            for (int i = 0; i <= path.size() - 2; i++) {
                if (nextNode == path.get(i)) {
                    cycleDetected = true;
                    DependencyCycle cycle = new DependencyCycle(path, i, path.size());
                    if (!contains(dependencyCycles, cycle)) {
                        dependencyCycles.add(cycle);
                        for (int j = i; j < path.size(); j++) {
                            enlistedInCycle.add(path.get(j));
                        }
                    }
                    break;
                }
            }
        }
        if (!cycleDetected) {
            path.add(nextNode);
            for (RequireBundle requireBundle : nextNode.getRequireBundleList()) {
                detectCycle(requireBundle.getTarget(), path, enlistedInCycle, dependencyCycles);
            }
            path.remove(nextNode);
        }
    }

    /**
     * @return
     */
    public List<DependencyCycle> findCycle() {
        final List<Bundle> path = new ArrayList<Bundle>(getBundles().size());
        final List<DependencyCycle> dependencyCycle = new ArrayList<DependencyCycle>();
        WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

            @Override
            protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
                monitor.beginTask("", getBundles().size());
                for (Bundle bundle : getBundles()) {
                    monitor.subTask("Detect through dependencies of " + bundle.getSymbolicName());
                    detectCycle(bundle, path, new HashSet<Bundle>(getBundles().size()), dependencyCycle);
                    monitor.worked(1);
                }
            }
        };
        try {
            PlatformUI.getWorkbench().getProgressService().busyCursorWhile(operation);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return dependencyCycle;
    }

    /**
     * @return the bundles
     */
    public List<Bundle> getBundles() {
        return bundles;
    }

    /**
     * @param bundles
     *            the bundles to set
     */
    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }
}
