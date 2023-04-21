package org.maximodeveloper.views;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.maximodeveloper.core.MaxProperties;

public class ClassFolderCreator {

    public void create(String maxDir) {
        String trim = maxDir.trim();
        if ("".equals(trim) || !(maxDir + MaxProperties.fileSeparator).equals(MaxProperties.getMaximoDirectory())) {
            IFolder psdi = MaxProperties.getCurrProject().getProject().getFolder("maximoPsdi");
            IFolder psdiWeb = MaxProperties.getCurrProject().getProject().getFolder("maximoPsdiWeb");
            try {
                resetRawClassPath(MaxProperties.getCurrProject());
                psdi.delete(true, null);
                psdiWeb.delete(true, null);
                MaxProperties.setMaximoDir(maxDir);
                if ("".equals(trim)) {
                    return;
                }
            } catch (CoreException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        create();
    }

    public void create() {
        IJavaProject jp = MaxProperties.getCurrProject();
        IProject proj = jp.getProject();
        try {
            create(proj, jp);
        } catch (JavaModelException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void create(IProject projectForSelectedResource, IJavaProject javaProject) throws JavaModelException {
        IClasspathEntry[] existing = javaProject.getRawClasspath();
        for (int i = 0; i < existing.length; i++) {
            IClasspathEntry classpathEntry = existing[i];
            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                IPath outputLocation = classpathEntry.getOutputLocation();
                IPath sourceLocation = classpathEntry.getPath();
                IPath excl1 = new Path("maximoPsdi/");
                IPath excl2 = new Path("maximoPsdiWeb/");
                classpathEntry = JavaCore.newSourceEntry(sourceLocation, new IPath[] { excl1, excl2 }, outputLocation);
                existing[i] = classpathEntry;
            }
        }
        javaProject.setRawClasspath(existing, null);
        IFolder psdi = projectForSelectedResource.getFolder("maximoPsdi");
        boolean psdiExisted = psdi.exists();
        if (!psdiExisted) {
            try {
                psdi.createLink(new Path(MaxProperties.getMaximoClassesDirectory()), IResource.BACKGROUND_REFRESH, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        IFolder psdiWeb = projectForSelectedResource.getFolder("maximoPsdiWeb");
        boolean psdiWebExisted = psdiWeb.exists();
        if (!psdiWebExisted) {
            try {
                psdiWeb.createLink(new Path(MaxProperties.getMaximoWebClassesDirectory()), IResource.BACKGROUND_REFRESH, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        if (!psdiExisted || !psdiWebExisted) {
            setRawClassPath(javaProject, psdi, psdiWeb);
        }
    }

    private void setRawClassPath(IJavaProject javaProject, IFolder psdi, IFolder psdiWeb) throws JavaModelException {
        IClasspathEntry cpe = JavaCore.newLibraryEntry(psdi.getFullPath(), null, null);
        IClasspathEntry cpeWeb = JavaCore.newLibraryEntry(psdiWeb.getFullPath(), null, null);
        IClasspathEntry servletJar = JavaCore.newLibraryEntry(new Path(MaxProperties.getServletJarLocation()), null, null);
        IClasspathEntry[] existing = javaProject.getRawClasspath();
        IClasspathEntry[] newCpe = new IClasspathEntry[existing.length + 3];
        System.arraycopy(existing, 0, newCpe, 0, existing.length);
        newCpe[existing.length] = cpe;
        newCpe[existing.length + 1] = cpeWeb;
        newCpe[existing.length + 2] = servletJar;
        javaProject.setRawClasspath(newCpe, new NullProgressMonitor());
    }

    private void resetRawClassPath(IJavaProject javaProject) throws JavaModelException {
        IClasspathEntry[] prevoiusClassPath = javaProject.getRawClasspath();
        IClasspathEntry[] newClassPath = new IClasspathEntry[prevoiusClassPath.length - 3];
        System.arraycopy(prevoiusClassPath, 0, newClassPath, 0, prevoiusClassPath.length - 3);
        javaProject.setRawClasspath(newClassPath, new NullProgressMonitor());
    }
}
