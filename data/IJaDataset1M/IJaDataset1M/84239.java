package org.teiid.cdk.core.sdk;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * Concrete implementation for classpath container initializer.
 * 
 * @see ClasspathContainerInitializer
 * 
 * @author Sanjay Chaudhuri <email2sanjayc@gmail.com>
 */
public class CdkClasspathContainerInitializer extends ClasspathContainerInitializer {

    public CdkClasspathContainerInitializer() {
    }

    public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
        setClasspathContainer(containerPath, project, null);
    }

    public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
        return CdkClasspathContainer.CONTAINER_ID.equals(containerPath.segment(0));
    }

    public String getDescription(IPath containerPath, IJavaProject project) {
        if (containerPath.segmentCount() <= 0) throw new AssertionError();
        CdkSdk cdkSdk = CdkToolkitManager.instance.getCdkFromContainerPath(containerPath);
        return cdkSdk == null ? "Teiid-CDK [Connector Plugins  missing !!!]" : cdkSdk.getDescription();
    }

    public void requestClasspathContainerUpdate(IPath containerPath, IJavaProject project, IClasspathContainer containerSuggestion) throws CoreException {
        setClasspathContainer(containerPath, project, containerSuggestion.getClasspathEntries());
    }

    public void setClasspathContainer(IPath containerPath, IJavaProject project, IClasspathEntry[] updatedClassPathEntries) throws CoreException {
        CdkSdk cdkSdk = CdkToolkitManager.instance.getCdkFromContainerPath(containerPath);
        CdkClasspathContainer cdkClasspathContainer = null;
        if (cdkSdk != null) {
            if (updatedClassPathEntries != null) {
                cdkSdk.setClasspathEntries(updatedClassPathEntries);
            }
            cdkClasspathContainer = new CdkClasspathContainer(cdkSdk, containerPath, project);
            JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { cdkClasspathContainer }, new NullProgressMonitor());
        }
    }
}
