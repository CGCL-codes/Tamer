package awilkins.eclipse.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * JDTUtils
 */
public class JDTUtils {

    /**
   *  
   */
    private JDTUtils() {
        super();
    }

    private static final Logger log = Logger.getLogger(JDTUtils.class);

    public static IJavaElement findTypesInPackageFragmentRoot(IPackageFragmentRoot root, String packageName, String className) {
        IPackageFragment frag = root.getPackageFragment(packageName);
        String classFileName = className;
        IJavaElement res = frag.getCompilationUnit(classFileName + ".java");
        if (res == null || !res.exists()) {
            res = frag.getClassFile(classFileName + ".class");
        }
        if (res.exists()) {
            return res;
        }
        return null;
    }

    /**
   * @param packageName
   * @param className
   * @param jproject
   * @return
   * @throws JavaModelException
   */
    public static IType findTypesInProject(String packageName, String className, IJavaProject jproject) throws JavaModelException {
        IPackageFragmentRoot[] packageFragmentRoots = jproject.getPackageFragmentRoots();
        for (int j = 0; j < packageFragmentRoots.length; j++) {
            IPackageFragmentRoot root = packageFragmentRoots[j];
            if (root.exists()) {
                IJavaElement javaElement = findTypesInPackageFragmentRoot(root, packageName, className);
                if (javaElement != null && javaElement.exists()) {
                    if (javaElement.getElementType() == IJavaElement.CLASS_FILE) {
                        IClassFile classFile = (IClassFile) javaElement;
                        return classFile.getType();
                    }
                    if (javaElement.getElementType() == IJavaElement.TYPE) {
                        return (IType) javaElement;
                    }
                    if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
                        ICompilationUnit compilationUnit = (ICompilationUnit) javaElement;
                        return compilationUnit.findPrimaryType();
                    }
                    return null;
                }
            }
        }
        return null;
    }

    /**
   * @param jetEmitter
   * @param url
   * @param classLoader
   *                 TODO
   * @return
   */
    public static ClassLoader getClassLoader(URL url, ClassLoader classLoader) {
        return getClassLoader(new URL[] { url }, classLoader);
    }

    /**
   * @param jetEmitter
   * @param url
   * @param classLoader
   *                 TODO
   * @return
   */
    public static ClassLoader getClassLoader(URL[] url, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = JDTUtils.class.getClassLoader();
        }
        URLClassLoader theClassLoader = new URLClassLoader(url, classLoader);
        return theClassLoader;
    }

    /**
   * @param paths
   * @return
   */
    public static File[] getFiles(IPath[] paths) {
        List converted = new ArrayList();
        for (int i = 0; i < paths.length; i++) {
            IPath entry = paths[i];
            File path = entry.toFile();
            if (path != null) {
                converted.add(path);
            }
        }
        File[] result = new File[converted.size()];
        converted.toArray(result);
        return result;
    }

    /**
   * @param javaProject
   * @return
   * @throws JavaModelException
   */
    public static IPackageFragmentRoot getFirstSourcePackageFragmentRoot(IJavaProject javaProject) throws JavaModelException {
        IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
        IPackageFragmentRoot sourcePackageFragmentRoot = null;
        for (int j = 0; j < packageFragmentRoots.length; ++j) {
            IPackageFragmentRoot packageFragmentRoot = packageFragmentRoots[j];
            if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
                sourcePackageFragmentRoot = packageFragmentRoot;
                break;
            }
        }
        return sourcePackageFragmentRoot;
    }

    /**
   * @param progressMonitor
   * @param workspace
   * @param project
   * @param classpathEntries
   *                 TODO
   * @return
   * @throws CoreException
   * @throws JavaModelException
   */
    public static IJavaProject getOrCreateJavaProject(IProgressMonitor progressMonitor, final IWorkspace workspace, final IProject project, List classpathEntries) throws CoreException, JavaModelException {
        IJavaProject javaProject;
        if (!project.exists() || !project.isOpen()) {
            if (project.exists()) {
                try {
                    project.delete(true, false, progressMonitor);
                } catch (CoreException e) {
                    e.toString();
                }
            }
            progressMonitor.subTask("JET creating project " + project.getName());
            project.create(new SubProgressMonitor(progressMonitor, 1));
            progressMonitor.subTask(Messages.getInstance().getFormattedString("_UI_JETCreatingProject_message", new Object[] { project.getName() }));
            IProjectDescription description = workspace.newProjectDescription(project.getName());
            description.setNatureIds(new String[] { JavaCore.NATURE_ID });
            description.setLocation(null);
            project.open(new SubProgressMonitor(progressMonitor, 1));
            project.setDescription(description, new SubProgressMonitor(progressMonitor, 1));
            javaProject = JavaCore.create(project);
            progressMonitor.subTask(Messages.getInstance().getFormattedString("_UI_JETInitializingProject_message", new Object[] { project.getName() }));
            IFolder sourceFolder = project.getFolder(new Path("src"));
            if (!sourceFolder.exists()) {
                sourceFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
            }
            IFolder runtimeFolder = project.getFolder(new Path("runtime"));
            if (!runtimeFolder.exists()) {
                runtimeFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
            }
            javaProject.setOutputLocation(new Path("/" + project.getName() + "/runtime"), new SubProgressMonitor(progressMonitor, 1));
            setClasspath(progressMonitor, classpathEntries, javaProject, true);
        } else {
            project.open(new SubProgressMonitor(progressMonitor, 5));
            javaProject = JavaCore.create(project);
            javaProject.open(progressMonitor);
        }
        return javaProject;
    }

    /**
   * TODO COMMENT
   * @param progressMonitor
   * @param classpathEntries
   * @param javaProject
   * @param addBasicClasspath
   * @throws JavaModelException
   */
    public static void setClasspath(IProgressMonitor progressMonitor, List classpathEntries, IJavaProject javaProject, boolean addBasicClasspath) throws JavaModelException {
        javaProject.setRawClasspath(new IClasspathEntry[0], progressMonitor);
        List classpath = new ArrayList();
        if (addBasicClasspath) {
            addBasicJavaProjectClasspathEntries(javaProject, classpath);
        }
        if (classpathEntries != null) {
            classpath.addAll(classpathEntries);
        }
        ensureClasspathEntriesExist(progressMonitor, javaProject, classpath);
    }

    /**
   * TODO COMMENT
   * 
   * @param project
   * @param classpath
   */
    public static void addBasicJavaProjectClasspathEntries(final IJavaProject project, List classpath) {
        IClasspathEntry srcClasspathEntry = getSourcePathEntry(project);
        classpath.add(srcClasspathEntry);
        IClasspathEntry jreClasspathEntry = getJREClasspathEntry();
        classpath.add(jreClasspathEntry);
    }

    /**
   * TODO COMMENT
   * 
   * @param project
   * @return
   */
    private static IClasspathEntry getSourcePathEntry(final IJavaProject project) {
        IClasspathEntry srcClasspathEntry = JavaCore.newSourceEntry(new Path("/" + project.getProject().getName() + "/src"));
        return srcClasspathEntry;
    }

    /**
   * TODO COMMENT
   * 
   * @return
   */
    private static IClasspathEntry getJREClasspathEntry() {
        IClasspathEntry jreClasspathEntry = JavaCore.newVariableEntry(new Path(JavaRuntime.JRELIB_VARIABLE), new Path(JavaRuntime.JRESRC_VARIABLE), new Path(JavaRuntime.JRESRCROOT_VARIABLE));
        return jreClasspathEntry;
    }

    /**
   * @param progressMonitor
   * @param javaProject
   * @param classpath
   * @throws JavaModelException
   */
    public static void ensureClasspathEntriesExist(IProgressMonitor progressMonitor, IJavaProject javaProject, List classpath) throws JavaModelException {
        List currentClasspath = Arrays.asList(javaProject.getRawClasspath());
        if (currentClasspath.equals(classpath)) {
            return;
        }
        ensureCorrespondingEntriesExist(classpath, currentClasspath);
        IClasspathEntry[] classpathEntryArray = (IClasspathEntry[]) classpath.toArray(new IClasspathEntry[classpath.size()]);
        javaProject.setRawClasspath(classpathEntryArray, new SubProgressMonitor(progressMonitor, 1));
    }

    /**
   * @param classpathToModify
   * @param desiredClasspathEntries
   */
    public static void ensureCorrespondingEntriesExist(List classpathToModify, List desiredClasspathEntries) {
        for (Iterator iter = desiredClasspathEntries.iterator(); iter.hasNext(); ) {
            IClasspathEntry entry = (IClasspathEntry) iter.next();
            IClasspathEntry newClasspathEntry = getCorrespondingClasspathEntry(classpathToModify, entry);
            if (newClasspathEntry == null) {
                classpathToModify.add(entry);
            }
        }
    }

    public static IClasspathEntry getCorrespondingClasspathEntry(List entries, IClasspathEntry entry) {
        File entryFile = entry.getPath().toFile().getAbsoluteFile();
        IClasspathEntry correspondingEntry = null;
        for (Iterator iter = entries.iterator(); iter.hasNext() && correspondingEntry == null; ) {
            IClasspathEntry existingEntry = (IClasspathEntry) iter.next();
            File existingEntryFile = existingEntry.getPath().toFile().getAbsoluteFile();
            if (existingEntryFile.equals(entryFile)) {
                correspondingEntry = existingEntry;
            }
        }
        return correspondingEntry;
    }

    /**
   * @param progressMonitor
   * @param workspace
   * @param project
   * @param classpathEntries
   *                 TODO
   * @return
   * @throws CoreException
   * @throws JavaModelException
   */
    public static IJavaProject getOrCreateJavaProject(IProgressMonitor progressMonitor, final IWorkspace workspace, final String projectName, List classpathEntries) throws CoreException, JavaModelException {
        final IProject project = workspace.getRoot().getProject(projectName);
        return getOrCreateJavaProject(progressMonitor, workspace, project, classpathEntries);
    }

    public static String[] getPackageAndClassName(Class clazz) {
        String qName = clazz.getName();
        return getPackageAndClassName(qName);
    }

    /**
   * @param qName
   * @return
   */
    public static String[] getPackageAndClassName(String qName) {
        int lastDot = qName.lastIndexOf('.');
        if (lastDot < 0) {
            return new String[] { null, qName };
        }
        String c = qName.substring(lastDot + 1);
        String p = qName.substring(0, lastDot);
        return new String[] { p, c };
    }

    /**
   * @param entries
   * @return
   */
    public static IPath[] getPaths(IClasspathEntry[] entries) {
        List converted = new ArrayList();
        for (int i = 0; i < entries.length; i++) {
            IClasspathEntry entry = entries[i];
            IPath path = entry.getPath();
            if (path != null) {
                converted.add(path);
            }
        }
        IPath[] result = new IPath[converted.size()];
        converted.toArray(result);
        return result;
    }

    /**
   * @param jetEmitter
   * @return
   */
    public static ClassLoader getProjectClassLoader(IJavaProject javaProject, ClassLoader classLoader) {
        try {
            IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
            IPath[] paths = getPaths(entries);
            File[] files = getFiles(paths);
            URL[] urls = getURLs(files);
            classLoader = getClassLoader(urls, classLoader);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        URL projectUrl = getProjectOutputRootURL(javaProject);
        classLoader = getClassLoader(projectUrl, classLoader);
        return classLoader;
    }

    /**
   * @param project
   * @param javaProject
   * @return
   * @throws MalformedURLException
   * @throws JavaModelException
   */
    public static URL getProjectOutputRootURL(IJavaProject javaProject) {
        URL url;
        try {
            url = new File(javaProject.getProject().getLocation().toFile(), javaProject.getOutputLocation().removeFirstSegments(1) + "/").toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (JavaModelException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    public static IType getType(IJavaSearchScope scope, Class clazz) throws JavaModelException {
        if (clazz == null) {
            return null;
        }
        String[] packageAndClassName = getPackageAndClassName(clazz);
        return getType(scope, packageAndClassName[0], packageAndClassName[1]);
    }

    public static IType getType(IJavaSearchScope scope, String className) throws JavaModelException {
        if (className == null || className.length() == 0) {
            return null;
        }
        String[] packageAndClassName = getPackageAndClassName(className);
        return getType(scope, packageAndClassName[0], packageAndClassName[1]);
    }

    public static IType getType(IJavaSearchScope scope, String packageName, String className) throws JavaModelException {
        if (scope == null) {
            scope = SearchEngine.createWorkspaceScope();
        }
        if (packageName == null) {
            packageName = "";
        }
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("className must not be null");
        }
        IJavaModel jmodel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
        IPath[] enclosedPaths = scope.enclosingProjectsAndJars();
        IType[] typeArray = findTypesInProject(packageName, className, jmodel, enclosedPaths);
        return typeArray.length == 0 ? null : typeArray[0];
    }

    /**
   * @param paths
   * @return
   */
    public static URL[] getURLs(File[] paths) {
        List converted = new ArrayList();
        for (int i = 0; i < paths.length; i++) {
            File entry = paths[i];
            try {
                URL path = entry.toURL();
                converted.add(path);
            } catch (MalformedURLException e) {
                log.error(e, e);
            }
        }
        URL[] result = new URL[converted.size()];
        converted.toArray(result);
        return result;
    }

    /**
   * @param type
   * @param modelSuperType
   * @return
   * @throws JavaModelException
   */
    public static boolean isTypeOf(IType type, IType modelSuperType) throws JavaModelException {
        if (modelSuperType != null) {
            ITypeHierarchy hierarchy = type.newSupertypeHierarchy(null);
            return hierarchy.contains(modelSuperType);
        }
        return false;
    }

    /**
   * @param type
   * @param isValidType
   * @param modelSuperTypeName
   * @param scope
   *                 TODO
   * @return
   * @throws JavaModelException
   */
    public static boolean isTypeOf(IType type, String modelSuperTypeName, IJavaSearchScope scope) throws JavaModelException {
        IType modelSuperType = getType(scope, modelSuperTypeName);
        return isTypeOf(type, modelSuperType);
    }

    /**
   * @param mostSevere
   * @param status
   * @return
   */
    public static IStatus mostSevere(IStatus mostSevere, IStatus status) {
        int severity = IStatus.OK;
        int mostSevereSeverity = IStatus.OK;
        if (status != null) {
            severity = status.getSeverity();
        }
        if (mostSevere != null) {
            mostSevereSeverity = mostSevere.getSeverity();
        }
        if (severity > mostSevereSeverity) {
            mostSevere = status;
        }
        return mostSevere;
    }

    /**
   * @param packageName
   * @param className
   * @param jmodel
   * @param enclosedPaths
   * @return
   * @throws JavaModelException
   */
    private static IType[] findTypesInProject(String packageName, String className, IJavaModel jmodel, IPath[] enclosedPaths) throws JavaModelException {
        IType type = null;
        List types = new ArrayList();
        for (int i = 0; i < enclosedPaths.length; i++) {
            IPath curr = enclosedPaths[i];
            if (curr.segmentCount() == 1) {
                IJavaProject jproject = jmodel.getJavaProject(curr.segment(0));
                type = findTypesInProject(packageName, className, jproject);
                types.add(type);
            }
        }
        IJavaProject[] projects = jmodel.getJavaProjects();
        for (int i = 0; i < projects.length; i++) {
            IJavaProject jproject = projects[i];
            type = findTypesInProject(packageName, className, jproject);
            types.add(type);
        }
        IType[] typeArray = new IType[types.size()];
        types.toArray(typeArray);
        return typeArray;
    }
}
