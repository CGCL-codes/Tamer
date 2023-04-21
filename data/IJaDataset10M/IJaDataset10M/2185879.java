package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Classpath container initializer responsible for binding {@link AndroidClasspathContainer} to
 * {@link IProject}s. This removes the hard-coded path to the android.jar.
 */
public class AndroidClasspathContainerInitializer extends ClasspathContainerInitializer {

    /** The container id for the android framework jar file */
    private static final String CONTAINER_ID = "com.android.ide.eclipse.adt.ANDROID_FRAMEWORK";

    /** path separator to store multiple paths in a single property. This is guaranteed to not
     * be in a path.
     */
    private static final String PATH_SEPARATOR = "";

    private static final String PROPERTY_CONTAINER_CACHE = "androidContainerCache";

    private static final String PROPERTY_TARGET_NAME = "androidTargetCache";

    private static final String CACHE_VERSION = "01";

    private static final String CACHE_VERSION_SEP = CACHE_VERSION + PATH_SEPARATOR;

    private static final int CACHE_INDEX_JAR = 0;

    private static final int CACHE_INDEX_SRC = 1;

    private static final int CACHE_INDEX_DOCS_URI = 2;

    private static final int CACHE_INDEX_OPT_DOCS_URI = 3;

    private static final int CACHE_INDEX_ADD_ON_START = CACHE_INDEX_OPT_DOCS_URI;

    public AndroidClasspathContainerInitializer() {
    }

    /**
     * Binds a classpath container  to a {@link IClasspathContainer} for a given project,
     * or silently fails if unable to do so.
     * @param containerPath the container path that is the container id.
     * @param project the project to bind
     */
    @Override
    public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
        if (CONTAINER_ID.equals(containerPath.toString())) {
            JavaCore.setClasspathContainer(new Path(CONTAINER_ID), new IJavaProject[] { project }, new IClasspathContainer[] { allocateAndroidContainer(project) }, new NullProgressMonitor());
        }
    }

    /**
     * Creates a new {@link IClasspathEntry} of type {@link IClasspathEntry#CPE_CONTAINER}
     * linking to the Android Framework.
     */
    public static IClasspathEntry getContainerEntry() {
        return JavaCore.newContainerEntry(new Path(CONTAINER_ID));
    }

    /**
     * Checks the {@link IPath} objects against the android framework container id and
     * returns <code>true</code> if they are identical.
     * @param path the <code>IPath</code> to check.
     */
    public static boolean checkPath(IPath path) {
        return CONTAINER_ID.equals(path.toString());
    }

    /**
     * Updates the {@link IJavaProject} objects with new android framework container. This forces
     * JDT to recompile them.
     * @param androidProjects the projects to update.
     * @return <code>true</code> if success, <code>false</code> otherwise.
     */
    public static boolean updateProjects(IJavaProject[] androidProjects) {
        try {
            int projectCount = androidProjects.length;
            IClasspathContainer[] containers = new IClasspathContainer[projectCount];
            for (int i = 0; i < projectCount; i++) {
                containers[i] = allocateAndroidContainer(androidProjects[i]);
            }
            JavaCore.setClasspathContainer(new Path(CONTAINER_ID), androidProjects, containers, new NullProgressMonitor());
            return true;
        } catch (JavaModelException e) {
            return false;
        }
    }

    /**
     * Allocates and returns an {@link AndroidClasspathContainer} object with the proper
     * path to the framework jar file.
     * @param javaProject The java project that will receive the container.
     */
    private static IClasspathContainer allocateAndroidContainer(IJavaProject javaProject) {
        final IProject iProject = javaProject.getProject();
        String markerMessage = null;
        boolean outputToConsole = true;
        try {
            AdtPlugin plugin = AdtPlugin.getDefault();
            Object lock = plugin.getSdkLockObject();
            synchronized (lock) {
                boolean sdkIsLoaded = plugin.getSdkLoadStatus() == LoadStatus.LOADED;
                IAndroidTarget target = null;
                if (sdkIsLoaded) {
                    target = Sdk.getCurrent().getTarget(iProject);
                }
                if (sdkIsLoaded && target != null) {
                    Sdk.getCurrent().checkAndLoadTargetData(target, null);
                    String targetName = target.getClasspathName();
                    return new AndroidClasspathContainer(createClasspathEntries(iProject, target, targetName), new Path(CONTAINER_ID), targetName);
                }
                String hashString = Sdk.getProjectTargetHashString(iProject);
                if (hashString == null || hashString.length() == 0) {
                    if (sdkIsLoaded) {
                        markerMessage = String.format("Project has no target set. Edit the project properties to set one.");
                    }
                } else if (sdkIsLoaded) {
                    markerMessage = String.format("Unable to resolve target '%s'", hashString);
                } else {
                    AndroidClasspathContainer container = getContainerFromCache(iProject);
                    if (container == null) {
                        plugin.setProjectToResolve(javaProject);
                        markerMessage = String.format("Unable to resolve target '%s' until the SDK is loaded.", hashString);
                        outputToConsole = false;
                    } else {
                        plugin.setProjectToCheck(javaProject);
                        return container;
                    }
                }
                return new IClasspathContainer() {

                    public IClasspathEntry[] getClasspathEntries() {
                        return new IClasspathEntry[0];
                    }

                    public String getDescription() {
                        return "Unable to get system library for the project";
                    }

                    public int getKind() {
                        return IClasspathContainer.K_DEFAULT_SYSTEM;
                    }

                    public IPath getPath() {
                        return null;
                    }
                };
            }
        } finally {
            if (markerMessage != null) {
                if (outputToConsole) {
                    AdtPlugin.printErrorToConsole(iProject, markerMessage);
                }
                try {
                    BaseProjectHelper.markProject(iProject, AndroidConstants.MARKER_TARGET, markerMessage, IMarker.SEVERITY_ERROR, IMarker.PRIORITY_HIGH);
                } catch (CoreException e) {
                    final String fmessage = markerMessage;
                    Job markerJob = new Job("Android SDK: Resolving error markers") {

                        @Override
                        protected IStatus run(IProgressMonitor monitor) {
                            try {
                                BaseProjectHelper.markProject(iProject, AndroidConstants.MARKER_TARGET, fmessage, IMarker.SEVERITY_ERROR, IMarker.PRIORITY_HIGH);
                            } catch (CoreException e2) {
                                return e2.getStatus();
                            }
                            return Status.OK_STATUS;
                        }
                    };
                    markerJob.setPriority(Job.BUILD);
                    markerJob.schedule();
                }
            } else {
                try {
                    if (iProject.exists()) {
                        iProject.deleteMarkers(AndroidConstants.MARKER_TARGET, true, IResource.DEPTH_INFINITE);
                    }
                } catch (CoreException ce) {
                    Job markerJob = new Job("Android SDK: Resolving error markers") {

                        @Override
                        protected IStatus run(IProgressMonitor monitor) {
                            try {
                                iProject.deleteMarkers(AndroidConstants.MARKER_TARGET, true, IResource.DEPTH_INFINITE);
                            } catch (CoreException e2) {
                                return e2.getStatus();
                            }
                            return Status.OK_STATUS;
                        }
                    };
                    markerJob.setPriority(Job.BUILD);
                    markerJob.schedule();
                }
            }
        }
    }

    /**
     * Creates and returns an array of {@link IClasspathEntry} objects for the android
     * framework and optional libraries.
     * <p/>This references the OS path to the android.jar and the
     * java doc directory. This is dynamically created when a project is opened,
     * and never saved in the project itself, so there's no risk of storing an
     * obsolete path.
     * The method also stores the paths used to create the entries in the project persistent
     * properties. A new {@link AndroidClasspathContainer} can be created from the stored path
     * using the {@link #getContainerFromCache(IProject)} method.
     * @param project
     * @param target The target that contains the libraries.
     * @param targetName
     */
    private static IClasspathEntry[] createClasspathEntries(IProject project, IAndroidTarget target, String targetName) {
        String[] paths = getTargetPaths(target);
        IClasspathEntry[] entries = createClasspathEntriesFromPaths(paths);
        StringBuilder sb = new StringBuilder(CACHE_VERSION);
        for (String p : paths) {
            sb.append(PATH_SEPARATOR);
            sb.append(p);
        }
        ProjectHelper.saveStringProperty(project, PROPERTY_CONTAINER_CACHE, sb.toString());
        ProjectHelper.saveStringProperty(project, PROPERTY_TARGET_NAME, targetName);
        return entries;
    }

    /**
     * Generates an {@link AndroidClasspathContainer} from the project cache, if possible.
     */
    private static AndroidClasspathContainer getContainerFromCache(IProject project) {
        String cache = ProjectHelper.loadStringProperty(project, PROPERTY_CONTAINER_CACHE);
        String targetNameCache = ProjectHelper.loadStringProperty(project, PROPERTY_TARGET_NAME);
        if (cache == null || targetNameCache == null) {
            return null;
        }
        if (cache.startsWith(CACHE_VERSION_SEP) == false) {
            return null;
        }
        cache = cache.substring(CACHE_VERSION_SEP.length());
        String[] paths = cache.split(Pattern.quote(PATH_SEPARATOR));
        if (paths.length < 3 || paths.length == 4) {
            return null;
        }
        try {
            if (new File(paths[CACHE_INDEX_JAR]).exists() == false || new File(new URI(paths[CACHE_INDEX_DOCS_URI])).exists() == false) {
                return null;
            }
            if (paths.length > CACHE_INDEX_ADD_ON_START) {
                if (new File(new URI(paths[CACHE_INDEX_OPT_DOCS_URI])).exists() == false) {
                    return null;
                }
                for (int i = CACHE_INDEX_ADD_ON_START + 1; i < paths.length; i++) {
                    String path = paths[i];
                    if (path.length() > 0) {
                        File f = new File(path);
                        if (f.exists() == false) {
                            return null;
                        }
                    }
                }
            }
        } catch (URISyntaxException e) {
            return null;
        }
        IClasspathEntry[] entries = createClasspathEntriesFromPaths(paths);
        return new AndroidClasspathContainer(entries, new Path(CONTAINER_ID), targetNameCache);
    }

    /**
     * Generates an array of {@link IClasspathEntry} from a set of paths.
     * @see #getTargetPaths(IAndroidTarget)
     */
    private static IClasspathEntry[] createClasspathEntriesFromPaths(String[] paths) {
        ArrayList<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
        IPath android_lib = new Path(paths[CACHE_INDEX_JAR]);
        IPath android_src = new Path(paths[CACHE_INDEX_SRC]);
        IClasspathAttribute cpAttribute = JavaCore.newClasspathAttribute(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, paths[CACHE_INDEX_DOCS_URI]);
        IAccessRule accessRule = JavaCore.newAccessRule(new Path("com/android/internal/**"), IAccessRule.K_NON_ACCESSIBLE);
        IClasspathEntry frameworkClasspathEntry = JavaCore.newLibraryEntry(android_lib, android_src, null, new IAccessRule[] { accessRule }, new IClasspathAttribute[] { cpAttribute }, false);
        list.add(frameworkClasspathEntry);
        if (paths.length >= 5) {
            String docPath = paths[CACHE_INDEX_OPT_DOCS_URI];
            int i = 4;
            while (i < paths.length) {
                Path jarPath = new Path(paths[i++]);
                IClasspathAttribute[] attributes = null;
                if (docPath.length() > 0) {
                    attributes = new IClasspathAttribute[] { JavaCore.newClasspathAttribute(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, docPath) };
                }
                IClasspathEntry entry = JavaCore.newLibraryEntry(jarPath, null, null, null, attributes, false);
                list.add(entry);
            }
        }
        return list.toArray(new IClasspathEntry[list.size()]);
    }

    /**
     * Checks the projects' caches. If the cache was valid, the project is removed from the list.
     * @param projects the list of projects to check.
     */
    public static void checkProjectsCache(ArrayList<IJavaProject> projects) {
        int i = 0;
        projectLoop: while (i < projects.size()) {
            IJavaProject javaProject = projects.get(i);
            IProject iProject = javaProject.getProject();
            if (iProject.isOpen() == false) {
                projects.remove(i);
                continue;
            }
            IAndroidTarget target = Sdk.getCurrent().getTarget(javaProject.getProject());
            if (target == null) {
                i++;
                continue;
            }
            String[] targetPaths = getTargetPaths(target);
            String cache = ProjectHelper.loadStringProperty(iProject, PROPERTY_CONTAINER_CACHE);
            if (cache == null) {
                i++;
                continue;
            }
            String[] cachedPaths = cache.split(Pattern.quote(PATH_SEPARATOR));
            if (cachedPaths.length < 3 || cachedPaths.length == 4) {
                i++;
                continue;
            }
            if (targetPaths.length != cachedPaths.length) {
                i++;
                continue;
            }
            if (new File(targetPaths[CACHE_INDEX_JAR]).equals(new File(cachedPaths[CACHE_INDEX_JAR])) == false || new File(targetPaths[CACHE_INDEX_SRC]).equals(new File(cachedPaths[CACHE_INDEX_SRC])) == false || new File(targetPaths[CACHE_INDEX_DOCS_URI]).equals(new File(cachedPaths[CACHE_INDEX_DOCS_URI])) == false) {
                i++;
                continue;
            }
            if (cachedPaths.length > CACHE_INDEX_OPT_DOCS_URI) {
                if (new File(targetPaths[CACHE_INDEX_OPT_DOCS_URI]).equals(new File(cachedPaths[CACHE_INDEX_OPT_DOCS_URI])) == false) {
                    i++;
                    continue;
                }
                targetLoop: for (int tpi = 4; tpi < targetPaths.length; tpi++) {
                    String targetPath = targetPaths[tpi];
                    for (int cpi = 4; cpi < cachedPaths.length; cpi++) {
                        if (new File(targetPath).equals(new File(cachedPaths[cpi]))) {
                            continue targetLoop;
                        }
                    }
                    i++;
                    continue projectLoop;
                }
            }
            projects.remove(i);
        }
    }

    /**
     * Returns the paths necessary to create the {@link IClasspathEntry} for this targets.
     * <p/>The paths are always in the same order.
     * <ul>
     * <li>Path to android.jar</li>
     * <li>Path to the source code for android.jar</li>
     * <li>Path to the javadoc for the android platform</li>
     * </ul>
     * Additionally, if there are optional libraries, the array will contain:
     * <ul>
     * <li>Path to the librairies javadoc</li>
     * <li>Path to the first .jar file</li>
     * <li>(more .jar as needed)</li>
     * </ul>
     */
    private static String[] getTargetPaths(IAndroidTarget target) {
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(target.getPath(IAndroidTarget.ANDROID_JAR));
        paths.add(target.getPath(IAndroidTarget.SOURCES));
        paths.add(AdtPlugin.getUrlDoc());
        IOptionalLibrary[] libraries = target.getOptionalLibraries();
        if (libraries != null) {
            String targetDocPath = target.getPath(IAndroidTarget.DOCS);
            if (targetDocPath != null) {
                paths.add(ProjectHelper.getJavaDocPath(targetDocPath));
            } else {
                paths.add("");
            }
            HashSet<String> visitedJars = new HashSet<String>();
            for (IOptionalLibrary library : libraries) {
                String jarPath = library.getJarPath();
                if (visitedJars.contains(jarPath) == false) {
                    visitedJars.add(jarPath);
                    paths.add(jarPath);
                }
            }
        }
        return paths.toArray(new String[paths.size()]);
    }
}
