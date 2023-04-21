package org.eclipseguru.gwt.core;

import org.eclipseguru.gwt.core.preferences.GwtCorePreferenceConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A GWT Project
 */
public class GwtProject extends GwtElement {

    /**
	 * Adds the GWT nature to the project.
	 * 
	 * @param project
	 *            the project
	 * @param monitor
	 *            the progress monitor
	 */
    public static void addGwtNature(final IProject project, final IProgressMonitor monitor) throws CoreException {
        final IProjectDescription description = project.getDescription();
        final String[] prevNatures = description.getNatureIds();
        final String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = GwtCore.NATURE_ID;
        description.setNatureIds(newNatures);
        if (project.getFile(IProjectDescription.DESCRIPTION_FILE_NAME).isReadOnly()) {
            ResourcesPlugin.getWorkspace().validateEdit(new IFile[] { project.getFile(IProjectDescription.DESCRIPTION_FILE_NAME) }, null);
        }
        project.setDescription(description, IResource.FORCE, null);
    }

    /**
	 * Indicates if the project is a valid GWT project, i.e. has the GWT nature
	 * attached and enabled.
	 * 
	 * @param project
	 * @return <code>true</code> if the project nature is attached an enabled,
	 *         <code>false</code> otherwise
	 */
    public static boolean hasGwtNature(final IProject project) {
        if (!project.isAccessible()) {
            return false;
        }
        try {
            return project.isNatureEnabled(GwtCore.NATURE_ID);
        } catch (final CoreException e) {
            return false;
        }
    }

    /** project */
    private final IProject project;

    /** javaProject */
    private final IJavaProject javaProject;

    /** modules */
    private GwtModule[] modules;

    /** includedModules */
    private GwtModule[] includedModules;

    /**
	 * Creates a new instance.
	 * 
	 * @param parent
	 */
    GwtProject(final IProject project, final GwtModel parent) {
        super(parent);
        if (null == project) {
            throw new IllegalArgumentException("project cannot be null");
        }
        this.project = project;
        javaProject = JavaCore.create(project);
    }

    /**
	 * Creates a binary module.
	 * 
	 * @param moduleDescriptor
	 * @param packageFragment
	 * @return
	 */
    GwtModule createBinaryModule(final IStorage moduleDescriptor, final IPackageFragment packageFragment) {
        return new GwtModule(moduleDescriptor, packageFragment, this);
    }

    /**
	 * Creates a GWT module for the specified module descriptor.
	 * 
	 * @param file
	 * @return
	 */
    GwtModule createModule(final IFile moduleDescriptor) {
        return new GwtModule(moduleDescriptor, this);
    }

    /**
	 * Indicates if the project exists.
	 * 
	 * @return <code>true</code> if the project exists
	 */
    public boolean exists() {
        return project.exists();
    }

    /**
	 * Finds all included modules.
	 * 
	 * @return
	 */
    protected GwtModule[] findIncludedModules() {
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        final GwtModel model = (GwtModel) getParent();
        final IEclipsePreferences preferences = new ProjectScope(project).getNode(GwtCore.PLUGIN_ID);
        final List<GwtModule> modules = new ArrayList<GwtModule>(5);
        final String includedModulesString = preferences.get(GwtCorePreferenceConstants.PREF_INCLUDED_MODULES, null);
        if (null != includedModulesString) {
            final StringTokenizer includedModulesTokenizer = new StringTokenizer(includedModulesString, ";");
            while (includedModulesTokenizer.hasMoreTokens()) {
                final String token = includedModulesTokenizer.nextToken();
                final IPath path = Path.fromPortableString(token);
                if (root.getWorkspace().validatePath(path.toString(), IResource.FILE).isOK()) {
                    final IFile file = root.getFile(path);
                    if (file.isAccessible() && GwtUtil.isModuleDescriptor(file)) {
                        final GwtProject project = model.createProject(file);
                        modules.add(project.createModule(file));
                    }
                }
            }
        }
        return modules.toArray(new GwtModule[modules.size()]);
    }

    /**
	 * @param project
	 * @return
	 * @throws CoreException
	 */
    protected GwtModule[] findModules() throws CoreException {
        final IJavaProject javaProject = getJavaProject();
        final List<GwtModule> moduleFiles = new ArrayList<GwtModule>();
        for (final IPackageFragmentRoot aRoot : javaProject.getPackageFragmentRoots()) {
            if (aRoot.getKind() != IPackageFragmentRoot.K_SOURCE) {
                continue;
            }
            for (final IJavaElement aPackage : aRoot.getChildren()) {
                if (aPackage.getElementType() != IJavaElement.PACKAGE_FRAGMENT) {
                    continue;
                }
                for (final Object aResource : ((IPackageFragment) aPackage).getNonJavaResources()) {
                    if (!(aResource instanceof IFile)) {
                        continue;
                    }
                    final IFile aFile = (IFile) aResource;
                    if (GwtUtil.isModuleDescriptor(aFile)) {
                        moduleFiles.add(createModule(aFile));
                    }
                }
            }
        }
        return moduleFiles.toArray(new GwtModule[moduleFiles.size()]);
    }

    /**
	 * Returns the list of included modules.
	 * 
	 * @param project
	 * @return the list of included modules
	 */
    public GwtModule[] getIncludedModules() {
        if (null == includedModules) {
            includedModules = findIncludedModules();
        }
        return includedModules;
    }

    /**
	 * Returns the {@link IJavaProject}.
	 * 
	 * @return the Java project
	 */
    public IJavaProject getJavaProject() {
        return javaProject;
    }

    /**
	 * Returns the module with the specified id, returns <code>null</code> if no
	 * such module could be found in this project.
	 * 
	 * @param moduleId
	 * @return the module with the specified id or <code>null</code> if no such
	 *         module could be found in this project.
	 * @throws GwtModelException
	 *             if an error occured while accessing the project
	 */
    public GwtModule getModule(final String moduleId) throws GwtModelException {
        final GwtModule[] modules = getModules();
        for (final GwtModule module : modules) {
            if (module.getModuleId().equals(moduleId)) {
                return module;
            }
        }
        return null;
    }

    /**
	 * Returns the modules of this project.
	 * 
	 * @return the modules
	 * @throws GwtModelException
	 *             if an error occured while accessing the project
	 */
    public GwtModule[] getModules() throws GwtModelException {
        if (null == modules) {
            try {
                modules = findModules();
            } catch (final CoreException e) {
                throw newGwtModelException(e);
            }
        }
        return modules;
    }

    /**
	 * Returns the project name.
	 * 
	 * @return the project name
	 */
    @Override
    public String getName() {
        return project.getName();
    }

    /**
	 * Returns the project preferences.
	 * <p>
	 * Returns <code>null</code> if the project is not a valid GWT project.
	 * </p>
	 * 
	 * @return the project preferences (maybe <code>null</code>)
	 */
    public IEclipsePreferences getProjectPreferences() {
        if (!hasGwtNature(project)) {
            return null;
        }
        return new ProjectScope(project).getNode(GwtCore.PLUGIN_ID);
    }

    /**
	 * Returns the {@link IProject}.
	 * 
	 * @return the project resource
	 */
    public IProject getProjectResource() {
        return project;
    }

    @Override
    public int getType() {
        return GWT_PROJECT;
    }

    /**
	 * Sets the included modules for the specified project.
	 * 
	 * @param project
	 */
    public void setIncludedModules(final List modules) {
        final IEclipsePreferences projectPreferences = new ProjectScope(project).getNode(GwtCore.PLUGIN_ID);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < modules.size(); i++) {
            final GwtModule module = (GwtModule) modules.get(i);
            if (i > 0) {
                builder.append(';');
            }
            builder.append(module.getModuleDescriptor().getFullPath().toPortableString());
        }
        if (builder.length() > 0) {
            projectPreferences.put(GwtCorePreferenceConstants.PREF_INCLUDED_MODULES, builder.toString());
        } else {
            projectPreferences.remove(GwtCorePreferenceConstants.PREF_INCLUDED_MODULES);
        }
    }
}
