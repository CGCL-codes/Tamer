package org.jmlspecs.eclipse.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.jmlspecs.eclipse.jmlchecker.CompilerProblem;
import org.jmlspecs.eclipse.jmlchecker.JmlChecker;
import org.jmlspecs.eclipse.jmlchecker.JmlProblemRequestor;
import org.jmlspecs.eclipse.jmlchecker.Options;
import org.jmlspecs.eclipse.jmlchecker.ProjectInfo;
import org.jmlspecs.eclipse.util.Log;
import org.jmlspecs.eclipse.util.Timer;

/** This class implements a builder for JML tools.  The builder is 
 *  run as part of the compilation cycle and appears in the list of
 *  builders under project properties.  It is enabled when the project 
 *  has a JML Nature.
 */
public class JML3Builder extends IncrementalProjectBuilder {

    /** This class is used to walk the tree of incremental changes */
    static class DeltaVisitor implements IResourceDeltaVisitor {

        /** Local variable to store the resources to be built.  This list is
     * accumulated while walking the tree, and then the JML tools are activated
     * on the entire list at once.
     */
        private List<IResource> resourcesToBuild = new LinkedList<IResource>();

        public boolean visit(IResourceDelta delta) throws CoreException {
            IResource resource = delta.getResource();
            switch(delta.getKind()) {
                case IResourceDelta.ADDED:
                    accumulate(resourcesToBuild, resource, true);
                    break;
                case IResourceDelta.REMOVED:
                    break;
                case IResourceDelta.CHANGED:
                    accumulate(resourcesToBuild, resource, true);
                    break;
            }
            return true;
        }
    }

    /** This class is used to walk the tree of full build changes */
    static class ResourceVisitor implements IResourceVisitor {

        /** Local variable to store the resources to be built.  This list is
     * accumulated while walking the tree, and then the JML tools are activated
     * on the entire list at once.
     */
        public List<IResource> resourcesToBuild = new LinkedList<IResource>();

        public boolean visit(IResource resource) {
            accumulate(resourcesToBuild, resource, false);
            return true;
        }
    }

    /** The ID of the Builder, which must match that in the plugin file */
    public static final String JML_BUILDER_ID = Activator.PLUGIN_ID + ".JML3Builder";

    /** The ID of the marker, which must match that in the plugin file. */
    static final String JML_MARKER_ID = Activator.PLUGIN_ID + ".JMLProblem";

    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        if (kind == FULL_BUILD) {
            fullBuild(monitor);
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild(monitor);
            } else {
                incrementalBuild(delta, monitor);
            }
        }
        return null;
    }

    protected void clean(IProgressMonitor monitor) throws CoreException {
        Log.log("Cleaning " + getProject().getName());
        deleteMarkers(getProject(), true);
        if (false) throw new CoreException(Status.OK_STATUS);
    }

    /** Called during tree walking; it records the java files visited and
   *  deletes any markers.
   * @param resourcesToBuild the list accumulated so far
   * @param resource the resource visited
   * @param delete if true, markers are deleted as we walk the tree
   */
    static void accumulate(List<IResource> resourcesToBuild, IResource resource, boolean delete) {
        if (!(resource instanceof IFile)) return;
        String name = resource.getName();
        if (ProjectInfo.suffixOK(name) >= 0) {
            IFile file = (IFile) resource;
            resourcesToBuild.add(file);
            if (delete) deleteMarkers(file, false);
        }
    }

    /** Perform JML checking on all identified items - called in UI mode
   * @param jproject the Java project all the resources are in
   * @param resourcesToBuild the resources to build
   * @param monitor the monitor to record progress and cancellation
   */
    static void doChecking(IJavaProject jproject, List<IResource> resourcesToBuild, IProgressMonitor monitor) {
        Options options = Activator.options;
        ProjectInfo pi = new ProjectInfo(options, preq);
        pi.setJavaProject(jproject);
        pi.specsproject = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().getProject("specsProject"));
        pi.racFolder = null;
        ICompilationUnit[] cus = new ICompilationUnit[resourcesToBuild.size()];
        int i = 0;
        for (IResource item : resourcesToBuild) {
            IJavaElement e = JavaModelManager.create(item, jproject);
            Log.log("JML: " + item);
            cus[i++] = (ICompilationUnit) e;
        }
        Log.log(Timer.getTimeString() + " Starting actual JML checking");
        try {
            (new JmlChecker(pi)).doProcessing(cus, monitor);
        } catch (Exception e) {
            Log.errorlog("Failure during JML checking " + e, e);
        }
    }

    /** This holds an instance of an IProblemRequestor, implemented to
   * convert problems returned by JML checking into markers to be persisted
   * and displayed by the Eclipse workbench.
   */
    public static final JmlProblemRequestor preq = new JmlProblemRequestor() {

        public void acceptProblem(IProblem p) {
            if (!(p instanceof CompilerProblem)) {
                int id = p.getID();
                if (!(id == IProblem.MethodRequiresBody || id == IProblem.UninitializedBlankFinalField)) return;
                String s = new String(p.getOriginatingFileName());
                if (s.endsWith(".java")) return;
                return;
            }
            if (p.isWarning() && level == 2) return;
            try {
                IResource f = null;
                char[] ch = p.getOriginatingFileName();
                IWorkspace w = ResourcesPlugin.getWorkspace();
                IWorkspaceRoot root = w.getRoot();
                if (ch != null) {
                    Path path = new Path(new String(ch));
                    f = root.findMember(path);
                } else {
                    f = root;
                }
                final IResource r = f;
                final int finalLineNumber = p.getSourceLineNumber();
                final int finalOffset = p.getSourceStart();
                final int finalEnd = p.getSourceEnd() + 1;
                final String finalErrorMessage = p.getMessage();
                final int finalSeverity = p.isError() ? IMarker.SEVERITY_ERROR : p.isWarning() ? IMarker.SEVERITY_WARNING : IMarker.SEVERITY_INFO;
                IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

                    public void run(IProgressMonitor monitor) throws CoreException {
                        IMarker marker = r.createMarker(JML_MARKER_ID);
                        marker.setAttribute(IMarker.LINE_NUMBER, finalLineNumber >= 1 ? finalLineNumber : 1);
                        if (finalOffset >= 0) {
                            marker.setAttribute(IMarker.CHAR_START, finalOffset);
                            marker.setAttribute(IMarker.CHAR_END, finalEnd);
                        }
                        marker.setAttribute(IMarker.SEVERITY, finalSeverity);
                        marker.setAttribute(IMarker.MESSAGE, finalErrorMessage);
                    }
                };
                r.getWorkspace().run(runnable, null);
            } catch (Exception e) {
                Log.errorlog("Failed to make a marker " + e, e);
            }
        }
    };

    /** Called when a full build is requested on the current project. 
   * @param monitor the progress monitor to use
   * @throws CoreException if something is wrong with the Eclipse resources
   */
    protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
        IProject project = getProject();
        IJavaProject jproject = JavaCore.create(project);
        if (jproject == null || !jproject.exists()) {
            Log.errorlog("JMLBuilder has been invoked on a non-Java Project - " + project.getName(), null);
            return;
        }
        Log.log("Full build " + project.getName());
        Timer.markTime();
        deleteMarkers(project, true);
        ResourceVisitor v = new ResourceVisitor();
        project.accept(v);
        doChecking(jproject, v.resourcesToBuild, monitor);
        v.resourcesToBuild.clear();
    }

    /** Called to do a build on a resource, recursively; this is a utility
   * to be called by client code elsewhere in the program.
   * @param jp the java project to which the resources belong 
   * @param resources the resources to JML check, each one recursively
   * @param monitor the progress monitor on which to report progress
   * @return true if the build was cancelled
   * @throws CoreException when the JML model is out of whack
   */
    public static boolean doBuild(IJavaProject jp, List<IResource> resources, IProgressMonitor monitor) throws CoreException {
        ResourceVisitor v = new ResourceVisitor();
        for (IResource r : resources) {
            r.accept(v);
        }
        monitor.beginTask("JML Manual Build", 5 * v.resourcesToBuild.size());
        doChecking(jp, v.resourcesToBuild, monitor);
        boolean cancelled = monitor.isCanceled();
        monitor.done();
        v.resourcesToBuild.clear();
        return cancelled;
    }

    /** Called when an incremental build is requested. 
   * @param delta the system supplied tree of changes
   * @param monitor the progress monitor to use
   * @throws CoreException if something is wrong with the Eclipse resources
   */
    protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
        IProject project = getProject();
        Log.log("Incremental build " + project.getName());
        Timer.markTime();
        DeltaVisitor v = new DeltaVisitor();
        delta.accept(v);
        doChecking(JavaCore.create(getProject()), v.resourcesToBuild, monitor);
        v.resourcesToBuild.clear();
    }

    /** Deletes all JML problem markers on the given resource 
   * 
   * @param resource the resource whose markers are to be deleted
   * @param recursive if true, resources contained in the first argument,
   *                recursively, also have markers deleted; if false, only
   *                this specific resource has markers deleted
   */
    public static void deleteMarkers(IResource resource, boolean recursive) {
        try {
            resource.deleteMarkers(JML_MARKER_ID, false, recursive ? IResource.DEPTH_INFINITE : IResource.DEPTH_ZERO);
        } catch (CoreException e) {
            Log.errorlog("Failed to delete markers on " + resource.getName(), e);
        }
    }

    /** Checks the JML in each of the given resources, in order
   * @param resources the list of resources to check
   * @param monitor the monitor to use to report progress and check for
   *                cancellation
   * @return true if the checking was cancelled
   */
    public static boolean checkJML(List<IResource> resources, IProgressMonitor monitor) {
        for (IResource r : resources) {
            checkJML(r, monitor);
            if (monitor.isCanceled()) return true;
        }
        return false;
    }

    /** Checks the JML on the given resource
   * @param resource the resource to check
   * @param monitor the monitor to use to report progress and cancellation
   */
    public static void checkJML(final IResource resource, IProgressMonitor monitor) {
        Log.log("Checking JML in " + resource.getName());
        Timer.markTime();
        deleteMarkers(resource, true);
        try {
            List<IResource> list = new LinkedList<IResource>();
            list.add(resource);
            boolean cancelled = doBuild(JavaCore.create(resource.getProject()), list, monitor);
            Log.log(Timer.getTimeString() + " Manual build " + (cancelled ? "cancelled" : "ended"));
        } catch (Exception e) {
            Log.errorlog(Timer.getTimeString() + " Manual build ended in error", e);
        }
    }
}
