package org.imogene.web.contrib.builder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.imogene.studio.contrib.tools.FileHelper;
import com.google.gwt.eclipse.core.runtime.GWTRuntime;

@SuppressWarnings("restriction")
public class GWTJarBuilder extends IncrementalProjectBuilder {

    private static String LIBRARY_PATH = "war/WEB-INF/lib/";

    @Override
    protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor) throws CoreException {
        if (kind == FULL_BUILD) {
            IJavaProject project = JavaCore.create(getProject());
            GWTRuntime runtime = GWTRuntime.findSdkFor(project);
            for (File f : Arrays.asList(runtime.getWebAppClasspathFiles())) {
                IFolder library = (IFolder) project.getProject().findMember(LIBRARY_PATH);
                File folder = new File(library.getLocation().toOSString());
                try {
                    FileHelper.copyFile(f, new File(folder, f.getName()));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            project.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
        }
        return null;
    }

    @SuppressWarnings("unused")
    private boolean shouldCopy(File sdk, File lib) {
        if (lib.exists()) return false;
        if (sdk.length() == lib.length()) return false;
        return true;
    }
}
