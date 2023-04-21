package ch.sahits.codegen.core.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import ch.sahits.codegen.core.util.WorkspaceFragmentProvider;

/**
 * This class loader searches first in the classpath of
 * the current project and then in the classpath of all other
 * projects of java nature on the workspace.
 * 
 * This ClassLoader should only be extended by a ClassLoader that
 * tries to load classes from the bundles or the projects in some specific
 * manner or from a third location
 * @author Andi Hotz
 * @since 1.2.0
 */
public class HeadlessProjectClassLoader extends ClassLoader {

    /** project for which the class should be loaded */
    private IJavaProject project;

    /**
	 * Initialize the project for witch the class should be loaded
	 * @param jProject
	 */
    public HeadlessProjectClassLoader(IJavaProject jProject) {
        super(HeadlessProjectClassLoader.class.getClassLoader());
        project = jProject;
    }

    /**
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        List<URL> urls = getCPURLs4Project(project);
        URL[] url = toArray(urls);
        try {
            return new URLClassLoader(url).loadClass(name);
        } catch (ClassNotFoundException e) {
            List<IJavaProject> projects = WorkspaceFragmentProvider.getOpenJavaProjects();
            for (IJavaProject p : projects) {
                if (!p.getElementName().equals(project.getElementName())) {
                    try {
                        urls = getCPURLs4Project(p);
                        url = toArray(urls);
                        return new URLClassLoader(url).loadClass(name);
                    } catch (ClassNotFoundException e1) {
                        continue;
                    }
                }
            }
        }
        throw new ClassNotFoundException("The class " + name + " could not be found in any project");
    }

    /**
	 * Retrieve all classpahs urls for the project
	 * @param jProject project
	 * @return List of URLSs
	 */
    private List<URL> getCPURLs4Project(IJavaProject jProject) {
        List<URL> urls = new ArrayList<URL>();
        try {
            urls = getCP4Container(jProject);
        } catch (JavaModelException e1) {
        } catch (MalformedURLException e1) {
        }
        try {
            urls.addAll(getCP4Library(jProject));
        } catch (JavaModelException e1) {
        } catch (MalformedURLException e1) {
        }
        try {
            urls.addAll(getCP4variable(jProject));
        } catch (MalformedURLException e1) {
        } catch (JavaModelException e1) {
        }
        try {
            urls.addAll(getCP4Project(jProject));
        } catch (JavaModelException e1) {
        } catch (MalformedURLException e1) {
        }
        IPath p;
        try {
            p = jProject.getOutputLocation();
            String prefix = WorkspaceFragmentProvider.getAbsolutWorkspacePath() + File.separator + p.toOSString() + File.separator;
            File f = new File(prefix);
            try {
                urls.add(0, f.toURI().toURL());
            } catch (MalformedURLException e) {
            }
        } catch (JavaModelException e1) {
        }
        return urls;
    }

    /**
	 * Retrieve the class paths URLs from project
	 * @param p project for which the container is to be lookedup
	 * @return List of classpath URLs
	 * @throws MalformedURLException
	 * @throws JavaModelException
	 */
    private List<URL> getCP4Container(IJavaProject p) throws JavaModelException, MalformedURLException {
        List<URL> url = new ArrayList<URL>();
        IClasspathEntry[] entries = p.getRawClasspath();
        Vector<IClasspathEntry> v = new Vector<IClasspathEntry>();
        for (IClasspathEntry entry : entries) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                IClasspathContainer cont = JavaCore.getClasspathContainer(entry.getPath(), project);
                IClasspathEntry[] entries2 = cont.getClasspathEntries();
                for (IClasspathEntry e2 : entries2) {
                    v.add(e2);
                }
            }
        }
        return retrieveURLsFromClasspath(url, v);
    }

    /**
	 * Retrieve the class paths URLs from project
	 * @param p project for which the library is to be lookedup
	 * @return List of classpath URLs
	 * @throws MalformedURLException
	 * @throws JavaModelException
	 */
    private List<URL> getCP4Library(IJavaProject p) throws JavaModelException, MalformedURLException {
        int kind = IClasspathEntry.CPE_LIBRARY;
        return retrieveURLsFromProject(p, kind);
    }

    /**
	 * Retrieve the class paths URLs from project by classpath entry
	 * @param p project for which the variable is to be lookedup
	 * @param kind Kind of the Container
	 * @return List of classpath URLs
	 * @throws MalformedURLException
	 * @throws JavaModelException
	 */
    private List<URL> retrieveURLsFromProject(IJavaProject p, int kind) throws JavaModelException, MalformedURLException {
        List<URL> url = new ArrayList<URL>();
        IClasspathEntry[] entries = p.getRawClasspath();
        Vector<IClasspathEntry> v = new Vector<IClasspathEntry>();
        for (IClasspathEntry entry : entries) {
            if (entry.getEntryKind() == kind) {
                v.add(entry);
            }
        }
        return retrieveURLsFromClasspath(url, v);
    }

    /**
	 * Retrieve the list of classpaths
	 * @param url List of classpathes
	 * @param v Vector of Classpath entries
	 * @return List of URLs
	 * @throws MalformedURLException
	 */
    private List<URL> retrieveURLsFromClasspath(List<URL> url, Vector<IClasspathEntry> v) throws MalformedURLException {
        IPath path;
        String path2Workspace = WorkspaceFragmentProvider.getAbsolutWorkspacePath();
        for (IClasspathEntry entry : v) {
            path = entry.getPath();
            URL u = null;
            if (isWorkspaceRelative(path)) {
                u = new File(path2Workspace + path.toOSString()).toURI().toURL();
            } else {
                u = new File(path.toOSString()).toURI().toURL();
            }
            url.add(u);
        }
        return url;
    }

    /**
	 * Retrieve the class paths URLs from project
	 * @param p project for which the project is to be lookedup
	 * @return List of classpath URLs
	 * @throws MalformedURLException
	 * @throws JavaModelException
	 */
    private List<URL> getCP4Project(IJavaProject p) throws JavaModelException, MalformedURLException {
        int kind = IClasspathEntry.CPE_PROJECT;
        return retrieveURLsFromProject(p, kind);
    }

    /**
	 * Retrieve the class paths URLs from project
	 * @param p project for which the variable is to be lookedup
	 * @return List of classpath URLs
	 * @throws MalformedURLException
	 * @throws JavaModelException
	 */
    private List<URL> getCP4variable(IJavaProject p) throws MalformedURLException, JavaModelException {
        List<URL> url = new ArrayList<URL>();
        IClasspathEntry[] entries = p.getRawClasspath();
        Vector<IClasspathEntry> v = new Vector<IClasspathEntry>();
        for (IClasspathEntry entry : entries) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
                v.add(JavaCore.getResolvedClasspathEntry(entry));
            }
        }
        return retrieveURLsFromClasspath(url, v);
    }

    /**
	 * Check wether the path can be seolved within the workspace
	 * @param p Path to be checked
	 * @return true if the path exists relative to the workspace
	 */
    private boolean isWorkspaceRelative(IPath p) {
        assert (p != null);
        IWorkspaceRoot root = WorkspaceFragmentProvider.getWorkspace().getRoot();
        return root.exists(p);
    }

    /**
	 * Convert a list of URLs into an array of URLs
	 * @param urls List of URLs
	 * @return Array of the same URLs
	 */
    private URL[] toArray(List<URL> urls) {
        URL[] url;
        url = new URL[urls.size()];
        for (int i = 0; i < url.length; i++) {
            url[i] = urls.get(i);
        }
        return url;
    }

    /**
	 * Remove any generic stuff befor trying to load the class
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.indexOf("<") > 0) {
            name = name.substring(0, name.indexOf("<"));
        }
        try {
            return super.loadClass(name);
        } catch (NoClassDefFoundError e) {
            return findClass(name);
        }
    }
}
