package org.parallelj.mda.controlflow.diagram.extension.tools;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * Miscellaneous tools.
 * 
 * @author Atos Worldline
 * 
 */
public class Tools {

    /**
	 * Converts EMF URI into Eclipse Path
	 * 
	 * @param uri
	 *            : EMF URI
	 * @return Eclipse IPath
	 */
    public static IPath convertURIToPath(URI uri) {
        URI tempURI = uri.trimFragment();
        String scheme = tempURI.scheme();
        if (scheme != null && scheme.endsWith("platform")) {
            String segment0 = tempURI.segment(0);
            if (segment0 != null && segment0.equals("resource")) {
                URI deresolver = URI.createPlatformResourceURI("/", false);
                String pathAsString = tempURI.deresolve(deresolver).toString();
                return new Path(pathAsString);
            }
        }
        return new Path(tempURI.toString());
    }

    /**
	 * Retrieves the Eclipse file containing the Element specified by the EMF
	 * URI
	 * 
	 * @param uri
	 *            : EMF URI
	 * @return IFile
	 */
    public static IFile getFileForURI(URI uri) {
        IPath path = Tools.convertURIToPath(uri);
        if (path == null) return null;
        return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
    }

    /**
	 * Retrieves the Eclipse Project containing the Element specified by the EMF
	 * URI
	 * 
	 * @param uri
	 *            : EMF URI
	 * @return IProject
	 */
    public static IProject getProjectForURI(URI uri) {
        IFile file = Tools.getFileForURI(uri);
        if (file == null) return null;
        return file.getProject();
    }

    /**
	 * Retrieves the JavaProject containing this EObject
	 * 
	 * @param eObj
	 *            input EObject
	 * 
	 * @return javaProject Associated java project
	 */
    public static final IJavaProject getJavaProjectFromEObject(EObject eObj) {
        IProject projectForURI = Tools.getProjectForURI(eObj.eResource().getURI());
        if (projectForURI == null) return null;
        return JavaCore.create(projectForURI);
    }
}
