package net.sf.refactorit.test.netbeans.vfs;

import net.sf.refactorit.netbeans.common.FileNotFoundReason;
import net.sf.refactorit.netbeans.common.RefactorItActions;
import net.sf.refactorit.netbeans.common.vfs.NBSource;
import net.sf.refactorit.test.Utils;
import org.openide.filesystems.JarFileSystem;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

/**
 * @author risto
 */
public class FileNotFoundReasonFsCapabilitiesTest extends NbTestCase {

    private NBSource jarFile;

    public void mySetUp() throws IOException, PropertyVetoException {
        if (RefactorItActions.isNetBeansFour()) {
            return;
        }
        JarFileSystem fs = TestFileCreator.mountJarFile(new File(new File(Utils.getTestProjectsDirectory(), "NbIntegrationTests"), "library.jar"));
        jarFile = NBSource.getSource(fs.getRoot());
    }

    public void testBadFsCapabilities() {
        if (RefactorItActions.isNetBeansFour()) {
            return;
        }
        assertEquals(FileNotFoundReason.WRONG_FS_CAPABILITIES, FileNotFoundReason.getFor(jarFile.getFileObject()));
    }
}
