package org.apache.commons.vfs.provider.sftp.jsch;

import org.apache.commons.vfs.CreateDeleteRenameTests;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.vfs.abstractclasses.AbstractTestClass;
import static org.junit.Assert.*;

/**
 *
 * @author David Meredith
 */
public class SftpProviderCreateDeleteRenameTest extends AbstractTestClass implements CreateDeleteRenameTests {

    FileObject relativeToFO;

    DefaultFileSystemManager fsManager;

    public SftpProviderCreateDeleteRenameTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        this.loadProperties();
        this.fsManager = this.getFsManager();
        this.relativeToFO = this.setupSftpFileObject(fsManager);
    }

    @After
    public void tearDown() throws Exception {
        this.cleanUp(relativeToFO, fsManager);
    }

    @Test
    public void testCreateAndDeleteNewFileAndFolder() throws Exception {
        vfsTestHelp.testCreateAndDeleteNewFileAndFolder(fsManager, relativeToFO, dummyFileDirName);
    }

    @Test
    public void testRenameFile() throws Exception {
        vfsTestHelp.testRenameFile(relativeToFO, dummyFileDirName, dummyFileDirName + "Renamed", this.assertContentInWriteTests);
    }

    @Test
    public void testRenameFolder() throws Exception {
        vfsTestHelp.testRenameFolder(relativeToFO, dummyFileDirName, dummyFileDirName + "Renamed");
    }

    @Test
    public void testCreateDirectoryHeirarchyAndRecursiveDelete() throws Exception {
        vfsTestHelp.testCreateDirHierarchyAndDeleteAll(relativeToFO, dummyFileDirName, this.assertContentInWriteTests);
    }

    @Test
    public void testNewFileAddedToFolderAfterRefresh() throws Exception {
        System.out.println("testNewFileAddedToFolderAfterRefresh");
        vfsTestHelp.testNewFileAddedToFolderAfterRefresh(relativeToFO, dummyFileDirName);
    }
}
