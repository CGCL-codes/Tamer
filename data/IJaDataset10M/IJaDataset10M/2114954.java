package net.sourceforge.dalutils4j.eclipse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;

public class CodeGeneratorCopyUtil {

    private static final String REQUIRED_FILES[] = { "net.sourceforge.dalutils4j.CodeGenerator|java", "net.sourceforge.dalutils4j.CRUD_SQL|vm", "net.sourceforge.dalutils4j.DAO_class|vm", "net.sourceforge.dalutils4j.DAO_method_create|vm", "net.sourceforge.dalutils4j.DAO_method_query|vm", "net.sourceforge.dalutils4j.DAO_method_update|vm", "net.sourceforge.dalutils4j.DTO_class|vm", "net.sourceforge.dalutils4j.VM_common|vm", "net.sourceforge.dalutils4j.jaxb.dao.Crud|java", "net.sourceforge.dalutils4j.jaxb.dao.CrudAuto|java", "net.sourceforge.dalutils4j.jaxb.dao.DaoClass|java", "net.sourceforge.dalutils4j.jaxb.dao.Query|java", "net.sourceforge.dalutils4j.jaxb.dao.QueryBean|java", "net.sourceforge.dalutils4j.jaxb.dao.QueryBeanList|java", "net.sourceforge.dalutils4j.jaxb.dao.QueryList|java", "net.sourceforge.dalutils4j.jaxb.dao.TypeCrud|java", "net.sourceforge.dalutils4j.jaxb.dao.Update|java", "net.sourceforge.dalutils4j.jaxb.dao.TypeMethod|java", "net.sourceforge.dalutils4j.jaxb.dao.ObjectFactory|java", "net.sourceforge.dalutils4j.jaxb.dto.DtoClass|java", "net.sourceforge.dalutils4j.jaxb.dto.DtoClasses|java", "net.sourceforge.dalutils4j.jaxb.dto.ObjectFactory|java" };

    private static final String VELOCITY_JAR = "velocity-1.7-dep.jar";

    public static void copyTo(IProject project, String srcRelPath, Shell shell) {
        try {
            String newLine = System.getProperty("line.separator");
            List<String> existingFiles = enumExistingFiles(project, srcRelPath);
            String msg = "";
            if (existingFiles.size() > 0) {
                msg = "The following files will be replaced:" + newLine;
                for (String s : existingFiles) {
                    s = project.getFile(s).getFullPath().toPortableString();
                    msg += "\t" + s + newLine;
                }
            }
            String velocityJAR = checkVelocityJAR(project);
            boolean skipVelocity = false;
            if (velocityJAR != null) {
                if (velocityJAR.compareTo(VELOCITY_JAR) != 0) {
                    msg += "'" + velocityJAR + "' is detected in the project build path. Using of version diffrent than '" + VELOCITY_JAR + "' may cause incorrect behavior." + newLine;
                }
                skipVelocity = true;
            }
            if (msg.length() > 0) {
                msg += "Proceed?";
                if (InternalHelpers.showConfirmation(msg) == false) {
                    return;
                }
            }
            copyFiles(project, srcRelPath);
            if (skipVelocity == false) {
                copyVelocityJAR(project);
            }
            InternalHelpers.refreshProject(project);
        } catch (Exception e) {
            e.printStackTrace();
            InternalHelpers.showError(e);
        }
    }

    private static void copyVelocityJAR(IProject project) throws Exception {
        IJavaProject javaProject = JavaCore.create(project);
        IPath jarFolderPath = project.getFolder("lib").getFullPath();
        {
            IPath projectPath = project.getFullPath();
            String relFilePath = jarFolderPath.makeRelativeTo(projectPath).append(VELOCITY_JAR).toPortableString();
            extractResourceToFile(VELOCITY_JAR, project, relFilePath);
        }
        {
            IPath relFilePath = jarFolderPath.append(VELOCITY_JAR);
            addLibrary(javaProject, relFilePath, null, null);
        }
    }

    private static IPackageFragmentRoot addLibrary(IJavaProject jproject, IPath path, IPath sourceAttachPath, IPath sourceAttachRoot) throws JavaModelException {
        IClasspathEntry cpe = JavaCore.newLibraryEntry(path, sourceAttachPath, sourceAttachRoot);
        addToClasspath(jproject, cpe);
        return jproject.getPackageFragmentRoot(path.toString());
    }

    private static void addToClasspath(IJavaProject jproject, IClasspathEntry cpe) throws JavaModelException {
        IClasspathEntry[] oldEntries = jproject.getRawClasspath();
        for (int i = 0; i < oldEntries.length; i++) {
            if (oldEntries[i].equals(cpe)) {
                return;
            }
        }
        int nEntries = oldEntries.length;
        IClasspathEntry[] newEntries = new IClasspathEntry[nEntries + 1];
        System.arraycopy(oldEntries, 0, newEntries, 0, nEntries);
        newEntries[nEntries] = cpe;
        jproject.setRawClasspath(newEntries, null);
    }

    private static InputStream getBundleResourceAsStream(String relPath) throws IOException {
        URL fileURL = new URL("platform:/plugin/net.sourceforge.dalutils4j/" + relPath);
        InputStream inputStream = fileURL.openConnection().getInputStream();
        return inputStream;
    }

    private static void extractResourceToFile(String resPath, IProject project, String relFilePath) throws Exception {
        InputStream is = getBundleResourceAsStream(resPath);
        if (is == null) {
            throw new Exception("Resource not found: " + relFilePath);
        }
        try {
            String fileName = project.getFile(relFilePath).getLocation().toOSString();
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            file.getParentFile().mkdirs();
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            try {
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    out.write(data, 0, nRead);
                }
            } finally {
                out.flush();
                out.close();
            }
        } finally {
            is.close();
        }
    }

    private static String checkVelocityJAR(IProject project) throws JavaModelException {
        IJavaProject javaProject = JavaCore.create(project);
        IPackageFragmentRoot[] roots = javaProject.getPackageFragmentRoots();
        for (IPackageFragmentRoot fr : roots) {
            if (fr.getKind() == IPackageFragmentRoot.K_BINARY) {
                IClasspathEntry classpathEntry = fr.getRawClasspathEntry();
                if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
                    IPath p = classpathEntry.getPath();
                    if (p.lastSegment().startsWith("velocity-")) {
                        return p.lastSegment();
                    }
                }
            }
        }
        return null;
    }

    private static List<String> enumExistingFiles(IProject project, String srcRelPath) throws Exception {
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < REQUIRED_FILES.length; i++) {
            String basePath = "/" + REQUIRED_FILES[i].replace('.', '/').replace('|', '.');
            String relFilePath = srcRelPath + basePath;
            IFile f = project.getFile(relFilePath);
            if (f.exists()) {
                res.add(relFilePath);
            }
        }
        return res;
    }

    private static void copyFiles(IProject project, String srcRelPath) throws Exception {
        for (int i = 0; i < REQUIRED_FILES.length; i++) {
            String basePath = "/" + REQUIRED_FILES[i].replace('.', '/').replace('|', '.');
            String relFilePath = srcRelPath + basePath;
            String resPath = "src" + basePath;
            extractResourceToFile(resPath, project, relFilePath);
        }
    }
}
