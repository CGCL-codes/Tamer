package org.destecs.ide.internal.core;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import org.destecs.ide.core.DestecsCorePlugin;
import org.destecs.ide.core.internal.core.resources.DestecsProject;
import org.destecs.ide.core.resources.IDestecsProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ResourceManager implements IResourceChangeListener {

    Map<String, IDestecsProject> projects = new Hashtable<String, IDestecsProject>();

    Queue<IDestecsProject> lastAccessed = new LinkedList<IDestecsProject>();

    /**
	 * A handle to the unique Singleton instance.
	 */
    private static volatile ResourceManager _instance = null;

    /**
	 * @return The unique instance of this class.
	 */
    public static ResourceManager getInstance() {
        if (null == _instance) {
            _instance = new ResourceManager();
        }
        return _instance;
    }

    public boolean hasProject(IProject project) {
        return projects.containsKey(project.getName());
    }

    public IDestecsProject getProject(IProject project) {
        return projects.get(project.getName());
    }

    public synchronized IDestecsProject addProject(IDestecsProject project) {
        if (projects.containsKey(project.getName())) return projects.get(project.getName()); else {
            IDestecsProject p = getLeastAccessed();
            if (p != null) {
                lastAccessed.remove(p);
                lastAccessed.add(project);
            }
            projects.put(project.getName(), project);
            return project;
        }
    }

    private IDestecsProject getLeastAccessed() {
        return lastAccessed.poll();
    }

    public void resourceChanged(IResourceChangeEvent event) {
        try {
            IResource res = event.getResource();
            switch(event.getType()) {
                case IResourceChangeEvent.PRE_DELETE:
                case IResourceChangeEvent.PRE_CLOSE:
                    remove(res);
                    break;
                case IResourceChangeEvent.PRE_BUILD:
                    break;
                case IResourceChangeEvent.POST_CHANGE:
                    event.getDelta().accept(new DeltaPrinter());
                    break;
                default:
                    break;
            }
        } catch (CoreException e) {
            DestecsCorePlugin.log(e);
        }
    }

    private synchronized void remove(IResource res) {
        if (res instanceof IProject && hasProject((IProject) res)) {
            if (projects.containsKey(res.getName())) {
                projects.remove(res.getName());
            }
        } else if (res instanceof IFile) {
        } else if (res instanceof IFolder) {
        } else {
            System.err.println("Resource not handled in remove: " + res);
        }
    }

    class DeltaPrinter implements IResourceDeltaVisitor {

        public boolean visit(IResourceDelta delta) {
            IResource res = delta.getResource();
            switch(delta.getKind()) {
                case IResourceDelta.ADDED:
                    add(res);
                    break;
                case IResourceDelta.REMOVED:
                    remove(res);
                    break;
                case IResourceDelta.CHANGED:
                    break;
            }
            return true;
        }

        private void add(final IResource res) {
            if (res instanceof IProject && DestecsProject.isDestecsProject((IProject) res)) {
                addBuilderProject((IProject) res);
            } else if (res instanceof IFile) {
            }
        }
    }

    private List<IProject> addBuilders = new Vector<IProject>();

    private boolean addBuilderThreadRunning = false;

    private synchronized IProject getAddBuidlerProject() {
        if (addBuilders.size() > 0) {
            IProject p = addBuilders.get(0);
            addBuilders.remove(p);
            return p;
        }
        return null;
    }

    private synchronized void addBuilderProject(IProject project) {
        if (!addBuilders.contains(project)) {
            addBuilders.add(project);
            if (!addBuilderThreadRunning) {
                System.out.println("starting add builder thread");
                new AddBuilderThread().start();
                addBuilderThreadRunning = true;
            }
        }
    }

    private class AddBuilderThread extends Thread {

        @Override
        public void run() {
            try {
                while (ResourcesPlugin.getWorkspace().isTreeLocked()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

                    public void run(IProgressMonitor monitor) throws CoreException {
                        IProject p = null;
                        while (addBuilders.size() > 0) {
                            p = getAddBuidlerProject();
                            if (p == null) {
                                addBuilderThreadRunning = false;
                                System.out.println("ended add builder thread");
                                return;
                            }
                            System.out.println("Adding builder for: " + p);
                            IDestecsProject project = DestecsProject.createProject(p);
                            Assert.isNotNull(project, "VDM Project creation faild for project: " + p);
                        }
                        System.out.println("DONE adding");
                    }
                }, null);
            } catch (CoreException e) {
                DestecsCorePlugin.log("Error in ResourceManager: AddBuilderThread", e);
            }
        }
    }
}
