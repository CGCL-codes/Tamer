package net.sf.refactorit.netbeans.v4.vfs;

import net.sf.refactorit.common.util.AppRegistry;
import net.sf.refactorit.netbeans.common.projectoptions.FileObjectUtil;
import net.sf.refactorit.netbeans.common.vfs.NBClassPathElement;
import net.sf.refactorit.utils.RefactorItConstants;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.ClassPath.Entry;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

public class ClassPathElementTest extends TestCase {

    public ClassPathElementTest(String name) {
        super(name);
    }

    interface FileObjectVisitor {

        void accept(Entry entry) throws Exception;
    }

    private void visitProjectsClassPathes(FileObjectVisitor callback) {
        Project[] projects = OpenProjects.getDefault().getOpenProjects();
        try {
            for (int i = 0; i < projects.length; i++) {
                FileObject fo = projects[i].getProjectDirectory();
                ClassPath cp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
                List list = cp.entries();
                for (Iterator it = list.iterator(); it.hasNext(); ) {
                    ClassPath.Entry entry = (ClassPath.Entry) it.next();
                    if (entry.getRoot().isRoot() && entry.isValid()) {
                        callback.accept(entry);
                    }
                }
            }
        } catch (Exception e) {
            AppRegistry.getExceptionLogger().error(e, this);
        }
    }

    public void test_getFileSystem() {
        FileObjectVisitor visitor = new FileObjectVisitor() {

            public void accept(Entry entry) throws Exception {
                FileSystem fs = entry.getRoot().getFileSystem();
                NBClassPathElement element = new NBClassPathElement(fs);
                assertEquals("FileSystem of the NBClassPathElement shall be the same," + " what was used during its creation.", fs, element.getFileSystem());
            }
        };
        visitProjectsClassPathes(visitor);
    }

    public void test_getAbsolutePath() {
        FileObjectVisitor visitor = new FileObjectVisitor() {

            public void accept(Entry entry) throws Exception {
                FileSystem fs = entry.getRoot().getFileSystem();
                NBClassPathElement element = new NBClassPathElement(fs);
                File file = FileObjectUtil.getFileOrNull(entry.getRoot());
                String path = "";
                if (file != null) {
                    path = file.getAbsolutePath();
                } else {
                    path = entry.getRoot().getPath();
                }
                assertEquals("Absolute path displayed is correct.", path, element.getAbsolutePath());
            }
        };
        visitProjectsClassPathes(visitor);
    }

    public void test_ClassPathEntry() {
        if (!RefactorItConstants.runNotImplementedTests) {
            return;
        }
        FileObjectVisitor visitor = new FileObjectVisitor() {

            public void accept(Entry entry) throws Exception {
                FileSystem fs = entry.getRoot().getFileSystem();
                NBClassPathElement element = new NBClassPathElement(fs);
                net.sf.refactorit.vfs.ClassPath.Entry classpathEntry = element.getEntry(getName(entry));
                assertTrue("Entry must be located on the classPath: " + getName(entry), classpathEntry != null);
                assertEquals("Length must be correct: ", fs.findResource(getName(entry)).getSize(), classpathEntry.length());
            }
        };
        visitProjectsClassPathes(visitor);
    }

    private static String getName(Entry entry) {
        Enumeration data = entry.getRoot().getData(true);
        while (data.hasMoreElements()) {
            Object obj = data.nextElement();
            if (obj instanceof FileObject) {
                if (((FileObject) obj).getPath().indexOf(".class") != -1) {
                    return ((FileObject) obj).getPath();
                }
            }
        }
        return "";
    }
}
