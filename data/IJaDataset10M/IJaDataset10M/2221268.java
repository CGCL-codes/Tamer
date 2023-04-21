package net.sf.refactorit.test.netbeans;

import java.util.Arrays;
import java.util.List;
import net.sf.refactorit.common.exception.SystemException;
import net.sf.refactorit.common.util.AppRegistry;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.exception.ErrorCodes;
import net.sf.refactorit.netbeans.common.RefactorItActions;
import net.sf.refactorit.netbeans.common.VersionSpecific;
import net.sf.refactorit.netbeans.common.projectoptions.PathItemReference;
import net.sf.refactorit.netbeans.common.vfs.NBFileObjectClassPathElement;
import net.sf.refactorit.netbeans.common.vfs.NBSource;
import net.sf.refactorit.test.TempFileCreator;
import net.sf.refactorit.test.Utils;
import net.sf.refactorit.test.netbeans.vfs.NbTestCase;
import net.sf.refactorit.test.netbeans.vfs.TestFileCreator;
import net.sf.refactorit.utils.FileUtil;
import net.sf.refactorit.vfs.SourcePath;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author risto
 */
public class PathItemReferenceTest extends NbTestCase {

    public static Test suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(PathItemReferenceTest.class);
        result.addTestSuite(PathItemReferenceSerializationTest.class);
        return result;
    }

    public void testLocalFile() {
        final File localFile = createLocalFile();
        final PathItemReference reference = new PathItemReference(localFile);
        assertTrue(reference.isValid());
        assertTrue(reference.isLocalFile());
        assertFalse(reference.isFileObject());
        assertFalse(reference.isFreeform());
        assertEquals(localFile, reference.getFile());
        assertEquals(localFile.getAbsolutePath(), reference.getAbsolutePath());
        assertEquals(localFile.getAbsolutePath(), reference.getDisplayNameLong());
        assertEquals(localFile.getAbsolutePath(), reference.getClassPathElement().getAbsolutePath());
        assertEquals(localFile.getName(), reference.getDisplayNameWithoutFolders());
        assertEquals(localFile.getParentFile(), reference.getParent().getFile());
        assertEquals(localFile, reference.getSource().getFileOrNull());
        localFile.delete();
        assertFalse(reference.isValid());
        assertEquals(localFile, reference.getFile());
        assertEquals(localFile.getAbsolutePath(), reference.getAbsolutePath());
        assertEquals(localFile.getAbsolutePath() + PathItemReference.INVALID_ITEM_SUFFIX, reference.getDisplayNameLong());
        assertEquals(localFile.getName() + PathItemReference.INVALID_ITEM_SUFFIX, reference.getDisplayNameWithoutFolders());
    }

    public void testFileObject() throws IOException {
        final FileObject fileObject = createFileObject();
        final PathItemReference reference = new PathItemReference(fileObject);
        final String absolutePath = getAbsolutePath(fileObject);
        assertTrue(reference.isValid());
        assertTrue(reference.isFileObject());
        assertFalse(reference.isLocalFile());
        assertFalse(reference.isFreeform());
        assertEquals(fileObject, reference.getFileObject());
        assertEquals(absolutePath, reference.getAbsolutePath());
        assertEquals(absolutePath, reference.getDisplayNameLong());
        assertEquals(fileObject, ((NBFileObjectClassPathElement) reference.getClassPathElement()).getFileObject());
        assertEquals(fileObject.getNameExt(), reference.getDisplayNameWithoutFolders());
        assertEquals(fileObject.getParent(), reference.getParent().getFileObject());
        assertEquals(fileObject, ((NBSource) reference.getSource()).getFileObject());
        fileObject.delete();
        assertFalse(reference.isValid());
        assertNull(reference.getFileObject());
        assertEquals(absolutePath + " [GOT: " + reference.getAbsolutePath() + "]", absolutePath, reference.getAbsolutePath());
        assertEquals(fileObject.getNameExt() + PathItemReference.INVALID_ITEM_SUFFIX, reference.getDisplayNameWithoutFolders());
        assertEquals(absolutePath + PathItemReference.INVALID_ITEM_SUFFIX, reference.getDisplayNameLong());
        try {
            reference.getParent();
            fail("Expected exception");
        } catch (RuntimeException expected) {
        }
        ;
    }

    public void testStringForm() {
        String str = "Something.complex:õäöü$€½";
        PathItemReference reference = new PathItemReference(str);
        assertTrue(reference.isValid());
        assertTrue(reference.isFreeform());
        assertFalse(reference.isFileObject());
        assertFalse(reference.isLocalFile());
        assertEquals(str, reference.getFreeform());
        assertEquals(str, reference.getDisplayNameLong());
        assertEquals(str, reference.getDisplayNameWithoutFolders());
        assertEquals(null, reference.getParent());
    }

    public void testGetAbsolutePathOfJarFileRoot() throws IOException, PropertyVetoException {
        File jarFile = Utils.getSomeJarFile();
        FileObject jarRoot = TestFileCreator.mountJarFile(jarFile).getRoot();
        assertEquals(jarFile.getAbsolutePath(), new PathItemReference(jarRoot).getAbsolutePath());
    }

    public void testGetDisplayNameOfJarFileRoot() throws IOException, PropertyVetoException {
        File jarFile = Utils.getSomeJarFile();
        FileObject jarRoot = TestFileCreator.mountJarFile(jarFile).getRoot();
        assertEquals(jarFile.getAbsolutePath(), new PathItemReference(jarRoot).getDisplayNameLong());
    }

    public void testGetDisplayNameWithoutFolders_Root() throws FileStateInvalidException {
        final FileObject root = createFileObject().getFileSystem().getRoot();
        final String longDisplayName = VersionSpecific.getInstance().getLongDisplayName(root);
        final String shortDisplayName = RefactorItActions.isNetBeansFour() ? longDisplayName : FileUtil.extractFileNameFromPath(longDisplayName, File.separatorChar);
        assertEquals(shortDisplayName, new PathItemReference(root).getDisplayNameWithoutFolders());
        assertEquals(longDisplayName, new PathItemReference(root).getDisplayNameLong());
        assertEquals(longDisplayName, new PathItemReference(root).getAbsolutePath());
    }

    public void testEquals() {
        final FileObject fileObject = createFileObject();
        final File file = createLocalFile();
        assertTrue(new PathItemReference(fileObject).equals(new PathItemReference(fileObject)));
        assertFalse(new PathItemReference(fileObject).equals(new PathItemReference(fileObject.getParent())));
        assertTrue(new PathItemReference(file).equals(new PathItemReference(file)));
        assertFalse(new PathItemReference(file).equals(new PathItemReference(file.getParentFile())));
        assertFalse(new PathItemReference(file).equals(new PathItemReference(fileObject)));
    }

    public void testIsFolder() {
        FileObject fileObject = createFileObject();
        assertFalse(new PathItemReference(fileObject).isFolder());
        assertTrue(new PathItemReference(fileObject.getParent()).isFolder());
        File localFile = createLocalFile();
        assertFalse(new PathItemReference(localFile).isFolder());
        assertTrue(new PathItemReference(localFile.getParentFile()).isFolder());
    }

    public void testNoNpeOnGetParent_fileObject() {
        FileObject fileObject = createFileObject();
        PathItemReference reference = new PathItemReference(fileObject);
        while (reference != null) {
            reference = reference.getParent();
        }
    }

    public void testNoNpeOnGetParent_localFile() {
        File localFile = createLocalFile();
        PathItemReference reference = new PathItemReference(localFile);
        while (reference != null) {
            reference = reference.getParent();
        }
    }

    static String getAbsolutePath(final FileObject fo) {
        return NBSource.getSource(fo).getAbsolutePath();
    }

    static File createLocalFile() {
        return TempFileCreator.getInstance().createRootFile().getFileOrNull();
    }

    static FileObject createFileObject() {
        FileObject fileObject = ((NBSource) IDEController.getInstance().getActiveProject().getPaths().getSourcePath().getRootSources()[0]).getFileObject();
        try {
            return fileObject.createData("justSomeTestFileToBeDeleted.txt");
        } catch (IOException e) {
            AppRegistry.getExceptionLogger().error(e, PathItemReferenceTest.class);
            throw new SystemException(ErrorCodes.INTERNAL_ERROR, e);
        }
    }
}
