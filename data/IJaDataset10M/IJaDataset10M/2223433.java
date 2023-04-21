package werkzeugkasten.weblauncher.launch;

import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import werkzeugkasten.common.jdt.ClasspathEntryUtil;
import werkzeugkasten.launcher.LibraryConfigurator;
import werkzeugkasten.launcher.impl.AbstractConfigurationFacet;
import werkzeugkasten.weblauncher.variable.Variable;

public class Servlet24Jsp20 extends AbstractConfigurationFacet implements LibraryConfigurator {

    public boolean hasLibrary(IJavaProject project) throws CoreException {
        Map<IPath, IClasspathEntry> m = ClasspathEntryUtil.toClasspathMap(project);
        return m.containsKey(Variable.SERVLET_API_2_4) && m.containsKey(Variable.JSP_API_2_0);
    }

    public void addLibrary(IJavaProject project) throws CoreException {
        ClasspathEntryUtil.addClasspathEntry(project, JavaCore.newVariableEntry(Variable.SERVLET_API_2_4, null, new Path("./")));
        ClasspathEntryUtil.addClasspathEntry(project, JavaCore.newVariableEntry(Variable.JSP_API_2_0, null, new Path("./")));
    }

    public void removeLibrary(IJavaProject project) throws CoreException {
        ClasspathEntryUtil.removeClasspathEntry(project, Variable.SERVLET_API_2_4);
        ClasspathEntryUtil.removeClasspathEntry(project, Variable.JSP_API_2_0);
    }
}
