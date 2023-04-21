package osgi.processing.servlet;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

public class ProjectMgr {

    private boolean projectCreated = false;

    private boolean isDirty = false;

    private String source = "";

    public ProjectMgr(String projectName) {
    }

    public boolean createProject(String projectName, String export) {
        IProgressMonitor progressMonitor = new NullProgressMonitor();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject project = root.getProject(projectName);
        try {
            if (!project.exists()) {
                project.create(progressMonitor);
            }
            project.open(progressMonitor);
            IProjectDescription description = project.getDescription();
            description.setNatureIds(new String[] { JavaCore.NATURE_ID });
            project.setDescription(description, progressMonitor);
            IJavaProject javaProject = JavaCore.create(project);
            IFolder binFolder = project.getFolder("bin");
            IFolder outputFolder = project.getFolder(export);
            if (!binFolder.exists()) {
                binFolder.create(false, true, null);
            }
            javaProject.setOutputLocation(outputFolder.getFullPath(), progressMonitor);
            List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
            IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
            LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
            for (LibraryLocation element : locations) {
                entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
            }
            javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
            IFolder sourceFolder = project.getFolder("src");
            if (!sourceFolder.exists()) {
                sourceFolder.create(false, true, null);
            }
            IPackageFragmentRoot rootfolder = javaProject.getPackageFragmentRoot(sourceFolder);
            IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
            IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
            System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
            newEntries[oldEntries.length] = JavaCore.newSourceEntry(rootfolder.getPath());
            javaProject.setRawClasspath(newEntries, null);
            IPackageFragment pack;
            if (rootfolder.getPackageFragment("") == null) {
                pack = rootfolder.createPackageFragment("", true, progressMonitor);
            } else {
                pack = rootfolder.getPackageFragment("");
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("\n");
            buffer.append(source);
            ICompilationUnit cu = pack.createCompilationUnit("ProcessingApplet.java", buffer.toString(), false, null);
            return true;
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
