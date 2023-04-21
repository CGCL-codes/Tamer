package org.eclipse.emf.builder.internal;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.builder.AbstractProvider;
import org.eclipse.emf.builder.EMFResourceLoader;
import org.eclipse.emf.builder.embmodel.EProject;
import org.eclipse.emf.builder.embmodel.EResource;
import org.eclipse.emf.builder.embmodel.EWorkspace;
import org.eclipse.emf.builder.embmodel.ModelFactory;
import org.eclipse.emf.builder.embmodel.ModelPackage;
import org.eclipse.emf.builder.internal.extensions.ExtensionManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.osgi.framework.Bundle;

/**
 * Tracks the EMF resources and trigger dependent builds when necessary
 * 
 * @author hceylan
 * 
 */
public class EMFDependencyManager implements IResourceChangeListener {

    private final class Visitor implements IResourceVisitor, IResourceDeltaVisitor {

        final List<IResource> added;

        private final List<IResource> changed;

        final List<IResource> removed;

        public Visitor() {
            super();
            this.added = new ArrayList<IResource>();
            this.changed = new ArrayList<IResource>();
            this.removed = new ArrayList<IResource>();
        }

        private void inspectDelta(IResourceDelta delta, IResource resource) {
            if (resource.getType() == IResource.FILE) {
                switch(delta.getKind()) {
                    case IResourceDelta.ADDED:
                        this.added.add(resource);
                        break;
                    case IResourceDelta.CHANGED:
                        this.changed.add(resource);
                        break;
                    case IResourceDelta.REMOVED:
                        this.removed.add(resource);
                    default:
                        break;
                }
            }
        }

        public boolean visit(IResource resource) throws CoreException {
            if (EMFResourceLoader.accept(resource)) {
                this.added.add(resource);
            }
            return true;
        }

        public boolean visit(IResourceDelta delta) throws CoreException {
            final IResource resource = delta.getResource();
            if (resource instanceof IProject) {
                final IProject project = (IProject) resource;
                if (delta.getKind() == IResourceDelta.REMOVED) {
                    EMFDependencyManager.this.unregisterProject(project, null);
                } else {
                    if (project.isAccessible() && project.hasNature(EMFNature.NATURE_ID)) {
                        EMFDependencyManager.this.registerProject(project, null, true);
                    } else if (!project.isAccessible()) {
                        EMFDependencyManager.this.unregisterProject(project, null);
                    }
                }
            } else if (EMFResourceLoader.accept(resource)) {
                this.inspectDelta(delta, resource);
            }
            return true;
        }
    }

    private static final String[] EMF_BUNDLES = new String[] { "org.eclipse.emf.ecore" };

    private static EMFDependencyManager instance = new EMFDependencyManager();

    private static final String STATE_FILE = EMFBuilderPlugin.getDefault().getStateLocation().append("emfbuilder.dat").toOSString();

    /**
	 * Returns the singleton Dependency Manager
	 * 
	 * @return
	 */
    public static EMFDependencyManager getDefault() {
        String ERR_MANAGER_NOT_READY = "EMF Dependency Manager not yet ready to process registries";
        if (EMFDependencyManager.instance == null) {
            throw new IllegalStateException(ERR_MANAGER_NOT_READY);
        }
        try {
            while (!EMFDependencyManager.instance.initialized) {
                Thread.sleep(100);
            }
        } catch (final InterruptedException e) {
        }
        if (!EMFDependencyManager.instance.initialized) {
            throw new IllegalStateException(ERR_MANAGER_NOT_READY);
        }
        return EMFDependencyManager.instance;
    }

    /**
	 * Starts the tracker. Exclusively for use by {@link InitializeAfterLoadJob}
	 * 
	 * @param monitor
	 * 
	 * @throws CoreException
	 */
    public static void start(IProgressMonitor monitor) throws CoreException {
        final long START = System.currentTimeMillis();
        try {
            EMFDependencyManager.instance.initialize(monitor);
        } finally {
            if (EMFBuilderPlugin.DEBUG) {
                System.out.println(MessageFormat.format("Dependency tracker initialized in total {0} msecs.", (System.currentTimeMillis() - START)));
            }
        }
    }

    /**
	 * Stops the tracker
	 * 
	 * @throws CoreException
	 */
    public static void stop() throws CoreException {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(EMFDependencyManager.getDefault());
        EMFDependencyManager.getDefault().dumpState();
        EMFDependencyManager.instance = null;
    }

    private boolean initialized;

    private Resource stateResource;

    private EWorkspace workspace;

    private EMFDependencyManager() {
        super();
    }

    private boolean checkEMFVersion(String bundleName) {
        final Bundle[] bundles = Platform.getBundles(bundleName, null);
        final String newVersion = bundles[0].getVersion().toString();
        final String oldVersion = this.workspace.getEmfVersions().get(bundleName);
        if (!newVersion.equals(oldVersion)) {
            this.workspace.getEmfVersions().put(bundleName, newVersion);
            return false;
        }
        return true;
    }

    private boolean checkEMFVersions() {
        boolean result = true;
        for (final String bundleName : EMFDependencyManager.EMF_BUNDLES) {
            result &= this.checkEMFVersion(bundleName);
        }
        return result;
    }

    private void createNoResourcesMarker(IProject project) throws CoreException {
        ExtensionManager.deleteMarkers(project);
        final IMarker marker = project.createMarker(AbstractProvider.MARKER_TYPE);
        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
        marker.setAttribute(IMarker.MESSAGE, Messages.EMFDependencyManager_MarkerNoResource);
        marker.setAttribute(IMarker.LOCATION, "");
    }

    private void doInitialize(List<IProject> projectList) throws CoreException {
        for (final IProject project : projectList) {
            final long START = System.currentTimeMillis();
            try {
                this.registerProject(project, null, false);
            } finally {
                if (EMFBuilderPlugin.DEBUG_DEPENDENCY) {
                    System.out.println(MessageFormat.format("Initial dependencies for project {1} calculated in total {0} msecs.", (System.currentTimeMillis() - START), project.getName()));
                }
            }
        }
    }

    private void dumpState() throws CoreException {
        try {
            this.workspace.setOpen(false);
            this.stateResource.save(null);
        } catch (final IOException e) {
            this.saveStateException(e);
        }
    }

    private void flushUnusedResources() {
        boolean changed = false;
        do {
            changed = false;
            for (final Iterator<EResource> i = this.workspace.getResources().iterator(); i.hasNext(); ) {
                final EResource eResource = i.next();
                if (eResource.getDependencies().isEmpty()) {
                    i.remove();
                    changed = true;
                }
            }
        } while (changed == true);
    }

    /**
	 * Returns an array of projects that the project depends on
	 * 
	 * @param project
	 * @return
	 */
    public IProject[] getDependencies(IProject project) {
        final Set<EResource> dependencies = new HashSet<EResource>();
        final EProject eProject = this.getEProject(project);
        if (eProject != null) {
            for (final EResource eResource : eProject.getResources()) {
                this.getDependencies(dependencies, eResource);
            }
        }
        final Set<IProject> projectDependencies = new HashSet<IProject>();
        for (final EResource eResource : dependencies) {
            final EProject eProject2 = eResource.getProject();
            if (eProject2 != null) {
                projectDependencies.add(eProject2.getDelegate());
            }
        }
        return projectDependencies.toArray(new IProject[projectDependencies.size()]);
    }

    private void getDependencies(Set<EResource> resourcesToExtend, EResource eResource) {
        resourcesToExtend.add(eResource);
        final List<EResource> dependencies = new ArrayList<EResource>();
        dependencies.addAll(eResource.getDependencies());
        dependencies.removeAll(resourcesToExtend);
        for (final EResource dependent : dependencies) {
            this.getDependencies(resourcesToExtend, dependent);
        }
    }

    private void getDependents(Set<EResource> resourcesToExtend, EResource eResource) {
        resourcesToExtend.add(eResource);
        final List<EResource> dependents = new ArrayList<EResource>();
        dependents.addAll(eResource.getDependents());
        dependents.removeAll(resourcesToExtend);
        for (final EResource dependent : dependents) {
            this.getDependents(resourcesToExtend, dependent);
        }
    }

    private EProject getEProject(IProject project) {
        for (final EProject eProject : this.workspace.getProjects()) {
            if (eProject.getDelegate().equals(project)) {
                return eProject;
            }
        }
        return null;
    }

    private EResource getEResource(IResource resource) {
        for (final EResource eResource : this.workspace.getResources()) {
            if (eResource.getDelegate().equals(resource)) {
                return eResource;
            }
        }
        for (final EProject eProject : this.workspace.getProjects()) {
            for (final EResource eResource : eProject.getResources()) {
                if (eResource.getDelegate().equals(resource)) {
                    return eResource;
                }
            }
        }
        return null;
    }

    /**
	 * Returns extended dependencies. Dependencies are sorted by dependency
	 * order and filtered out for the project
	 * 
	 * @param resources
	 * @param project
	 * @return
	 * @throws CircularDependencyError
	 */
    public List<EResource> getResourcesWithDependents(IResource[] resources, IProject project) throws CircularDependencyError {
        final Set<EResource> extendedEResources = new HashSet<EResource>();
        for (final IResource resource : resources) {
            this.getDependents(extendedEResources, this.getEResource(resource));
        }
        final List<EResource> sortedDependents = this.sortByDependencies(extendedEResources);
        for (final Iterator<EResource> i = sortedDependents.iterator(); i.hasNext(); ) {
            final EProject eProject = i.next().getProject();
            if ((eProject == null) || !project.equals(eProject.getDelegate())) {
                i.remove();
            }
        }
        return sortedDependents;
    }

    /**
	 * Returns if the {@link EResource} passed has dependency problem
	 * 
	 * @param eResource
	 * @return
	 */
    public String hasDependencyProblem(EResource eResource) {
        if (!eResource.isLinked()) {
            return eResource.getURI().toPlatformString(true);
        }
        for (final EResource dependency : eResource.getDependencies()) {
            final String problem = this.hasDependencyProblem(dependency);
            if (problem != null) {
                return problem;
            }
        }
        return null;
    }

    private void initialize(IProgressMonitor monitor) throws CoreException {
        try {
            this.workspace = this.loadState();
            final List<IProject> projectList = new ArrayList<IProject>(Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects()));
            final Iterator<IProject> i = projectList.iterator();
            while (i.hasNext()) {
                final IProject project = i.next();
                if (!project.isAccessible() || !project.hasNature(EMFNature.NATURE_ID)) {
                    i.remove();
                }
            }
            final SubMonitor subMonitor = SubMonitor.convert(monitor, 50);
            subMonitor.subTask(Messages.EMFDependencyManager_MessageValidating);
            this.validateWorkspace();
            subMonitor.worked(1);
            subMonitor.subTask(Messages.EMFDependencyManager_MessageInitializingProjects);
            this.doInitialize(projectList);
            this.unRegisterPhantoms(projectList);
            subMonitor.worked(4);
            subMonitor.subTask(Messages.EMFDependencyManager_MessageValidatingBuilderState);
            this.reLink(subMonitor.newChild(32));
        } finally {
            ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
            this.initialized = true;
        }
    }

    /**
	 * Returns if the project is tracked
	 * 
	 * @param project
	 * @return
	 */
    public boolean isProjectTracked(IProject project) {
        return this.getEProject(project) != null;
    }

    private EWorkspace loadState() throws CoreException {
        ModelPackage.eINSTANCE.getModelFactory();
        final URI uri = URI.createFileURI(EMFDependencyManager.STATE_FILE);
        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap());
        try {
            this.stateResource = resourceSet.getResource(uri, true);
            return (EWorkspace) this.stateResource.getContents().get(0);
        } catch (final WrappedException e) {
            final EWorkspace workspaceInstance = ModelFactory.eINSTANCE.createEWorkspace();
            workspaceInstance.setOpen(true);
            this.stateResource = new XMIResourceImpl(uri);
            this.stateResource.getContents().add(workspaceInstance);
            try {
                this.stateResource.save(null);
            } catch (final IOException e1) {
                this.saveStateException(e1);
            }
            return workspaceInstance;
        }
    }

    /**
	 * Ensures the project is registered with the manager and its dependencies
	 * are tracked
	 * 
	 * @param project
	 * @param monitor
	 * 
	 */
    public void registerProject(IProject project, IProgressMonitor monitor) {
        this.registerProject(project, monitor, true);
    }

    void registerProject(IProject project, IProgressMonitor monitor, boolean linkImmediately) {
        if (this.isProjectTracked(project)) {
            return;
        }
        final EProject eProject = ModelFactory.eINSTANCE.createEProject();
        eProject.setName(project.getName());
        eProject.setWorkspace(this.workspace);
        final Visitor visitor = new Visitor();
        try {
            project.accept(visitor);
            if (visitor.added.size() == 0) {
                this.createNoResourcesMarker(project);
                return;
            }
            for (final IResource resource : visitor.added) {
                this.registerResource(resource);
            }
            if (linkImmediately) {
                this.reLink(monitor);
            }
        } catch (final CoreException e) {
            EMFBuilderPlugin.getDefault().log(MessageFormat.format(Messages.EMFDependencyManager_ErrorRegisteringProject, project.getName()), e);
            return;
        }
        return;
    }

    /**
	 * Ensures the resource is registered. Returns true if already registered,
	 * false otherwise
	 * 
	 * @param resource
	 */
    protected void registerResource(IResource resource) {
        final EResource eResource = ModelFactory.eINSTANCE.createEResource();
        eResource.setProject(this.getEProject(resource.getProject()));
        final URI uri = URI.createPlatformResourceURI(resource.getFullPath().toString(), true);
        eResource.setURI(uri);
    }

    private void reLink(IProgressMonitor monitor) throws CoreException {
        final List<EResource> eResources = new ArrayList<EResource>();
        final Set<EProject> needsRebuild = new HashSet<EProject>();
        for (final EProject eProject : this.workspace.getProjects()) {
            for (final EResource eResource : eProject.getResources()) {
                if (!eResource.isLinked() || (this.hasDependencyProblem(eResource) != null)) {
                    eResources.add(eResource);
                    if (eResource.getProject() != null) {
                        needsRebuild.add(eResource.getProject());
                    }
                }
            }
        }
        final SubMonitor subMonitor = SubMonitor.convert(monitor, eResources.size());
        int i = 0;
        for (final EResource eResource : eResources) {
            final String taskName = MessageFormat.format(Messages.EMFDependencyManager_7, eResource.getDelegate().getName(), ++i, eResources.size());
            subMonitor.subTask(taskName);
            this.relinkDependencies(eResource);
            subMonitor.worked(1);
        }
        this.flushUnusedResources();
        this.triggerBuildJob(needsRebuild);
    }

    private void relinkDependencies(EResource eResource) throws CoreException {
        Resource modelResource;
        try {
            modelResource = EMFResourceLoader.load(eResource.getDelegate());
            EcoreUtil.resolveAll(modelResource);
        } catch (final Exception e) {
            EMFBuilderPlugin.getDefault().log(MessageFormat.format(Messages.EMFDependencyManager_8, eResource.getURI().toString()), e);
            eResource.setDependecyError(e.getMessage());
            return;
        }
        eResource.setDependecyError(null);
        eResource.getDependencies().clear();
        for (final Resource dependency : modelResource.getResourceSet().getResources()) {
            final EResource eDependent = ModelFactory.eINSTANCE.createEResource();
            eDependent.setURI(dependency.getURI());
            if (eResource.getDelegate().equals(eDependent.getDelegate())) {
                continue;
            }
            final EResource existing = this.getEResource(eDependent.getDelegate());
            if (existing == null) {
                eDependent.setWorkspace(this.workspace);
                eResource.getDependencies().add(eDependent);
                this.relinkDependencies(eDependent);
            } else {
                eResource.getDependencies().add(existing);
            }
            if (EMFBuilderPlugin.DEBUG_DEPENDENCY) {
                System.out.println(MessageFormat.format("Resource {0} depends on {1}", eResource.getDelegate().getName(), eDependent.getDelegate().getName()));
            }
        }
        eResource.setLinked(true);
    }

    public void resourceChanged(IResourceChangeEvent event) {
        try {
            if (event.getDelta() != null) {
                final Visitor visitor = new Visitor();
                event.getDelta().accept(visitor);
                boolean needsRelinking = false;
                for (final IResource resource : visitor.added) {
                    this.registerResource(resource);
                    needsRelinking = true;
                }
                for (final IResource resource : visitor.removed) {
                    this.unregisterResource(resource);
                    needsRelinking = true;
                }
                if (needsRelinking) {
                    this.reLink(null);
                }
            }
        } catch (final CoreException e) {
            EMFBuilderPlugin.getDefault().log(MessageFormat.format(Messages.EMFDependencyManager_9, event.getResource().getName()), e);
        }
    }

    private void saveStateException(Throwable e) throws CoreException {
        throw new CoreException(new Status(IStatus.ERROR, EMFBuilderPlugin.PLUGIN_ID, Messages.EMFDependencyManager_10, e));
    }

    private List<EResource> sortByDependencies(Collection<EResource> resources) throws CircularDependencyError {
        final TopologicalSorter sorter = new TopologicalSorter(resources);
        for (final EResource eResource : resources) {
            for (final EResource dependency : eResource.getDependencies()) {
                sorter.addDependency(dependency, eResource);
            }
        }
        final EResource[] sortedResources = sorter.sort();
        final List<EResource> sortedList = new ArrayList<EResource>();
        for (final EResource eResource : sortedResources) {
            sortedList.add(eResource);
        }
        return sortedList;
    }

    private void triggerBuildJob(final Set<EProject> needsRebuild) {
        new Job(Messages.EMFDependencyManager_11) {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                for (final EProject eProject : needsRebuild) {
                    try {
                        eProject.getDelegate().build(IncrementalProjectBuilder.CLEAN_BUILD, EMFBuilder.BUILDER_ID, Collections.EMPTY_MAP, monitor);
                    } catch (final CoreException e) {
                        EMFBuilderPlugin.getDefault().log(MessageFormat.format(Messages.EMFDependencyManager_12, eProject.getDelegate().getName()), e);
                    }
                }
                return Status.OK_STATUS;
            }
        }.schedule();
    }

    private void unRegisterPhantoms(List<IProject> projectList) throws CoreException {
        final List<EProject> toUnRegister = new ArrayList<EProject>();
        for (final EProject eProject : this.workspace.getProjects()) {
            if (!projectList.contains(eProject.getDelegate())) {
                toUnRegister.add(eProject);
            }
        }
        for (final EProject eProject : toUnRegister) {
            this.unregisterProject(eProject);
            if ((eProject.getDelegate() != null) && eProject.getDelegate().isAccessible()) {
                ExtensionManager.deleteMarkers(eProject.getDelegate());
            }
        }
    }

    private void unregisterProject(EProject eProject) throws CoreException {
        for (final EResource eResource : eProject.getResources()) {
            this.unregisterResource(eResource);
        }
        EcoreUtil.delete(eProject, true);
    }

    /**
	 * Stops watching for this project. Implicitly stops the resources found in
	 * the project unless are dependent by other resources
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
    public void unregisterProject(IProject project, IProgressMonitor monitor) throws CoreException {
        final EProject eProject = this.getEProject(project);
        if (eProject != null) {
            this.unregisterProject(eProject);
            this.reLink(monitor);
        }
    }

    private void unregisterResource(EResource eResource) {
        for (final Iterator<EResource> i = eResource.getDependents().iterator(); i.hasNext(); ) {
            final EResource dependent = i.next();
            i.remove();
            dependent.setLinked(false);
        }
    }

    private void unregisterResource(IResource resource) {
        final EResource eResource = this.getEResource(resource);
        if (eResource != null) {
            this.unregisterResource(eResource);
        }
    }

    private void validateWorkspace() throws CoreException {
        if (!this.checkEMFVersions() || this.workspace.isOpen()) {
            EMFBuilderPlugin.getDefault().log(Messages.EMFDependencyManager_13);
            this.workspace.getProjects().clear();
            return;
        }
        try {
            this.workspace.setOpen(true);
            this.stateResource.save(null);
        } catch (final IOException e) {
            this.saveStateException(e);
        }
        return;
    }
}
