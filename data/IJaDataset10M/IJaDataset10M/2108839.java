package org.apache.commons.vfs.provider.gridftp.cogjglobus.test;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.MultiThreadedTests;
import org.apache.commons.vfs.abstractclasses.AbstractTestClass;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import junit.framework.JUnit4TestAdapter;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: pmak
 * Date: Aug 19, 2009
 * Time: 12:45:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridFTPMultiThreadedTest extends AbstractTestClass implements MultiThreadedTests {

    FileObject relativeToFO;

    DefaultFileSystemManager fsManager;

    private final int TEN_MB = 1024 * 1024 * 10;

    public GridFTPMultiThreadedTest() {
    }

    /**
     * to run junit 4 as junit 3.8
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(GridFtpProviderCreateDeleteRenameTest.class);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("one time setup");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        this.loadProperties();
        this.loadCertificate();
        this.fsManager = this.getFsManager();
        this.relativeToFO = this.setupGridFtpFileObjectGsiAuth1(fsManager);
        FileObject tmpFolder = fsManager.resolveFile("file:///tmp/");
        FileObject unitTestFolder = tmpFolder.resolveFile("UnitTestFolder");
        if (!unitTestFolder.exists()) {
            unitTestFolder.createFolder();
        }
        for (int i = 0; i < 10; i++) {
            FileObject file = unitTestFolder.resolveFile("TestFile" + i + ".txt");
            if (!file.exists()) {
                file.createFile();
                String content = "";
                while (content.length() < TEN_MB) {
                    content += "this is a nonsense file, blah blah blah.  More text.  Blha.  More bl" + "ah....aafadsfadsfadsfadsfadfadf ah....aafadsfadsfadsfadsfadfadfah....aafa" + "ah....aafadsfadsfadsfadsfadfadfdsfadsfadsfadsfadfadfah....aafadsfadsfadsf" + "ah....aafadsfadsfadsfadsfadfadfah....aafadsfadsfadsfadsfadfadfah....aafad" + "ah....aafadsfadsfadsfadsfadfadfah....aafadsfadsfadsfadsfadfadfah....aafadsf" + "ah....aafadsfadsfadsfadsfadfadfah....aafadsfadsfadsfadsfadfadfadsfadsfadsfad" + "ah....aafadsfadsfadsfadsfadfadffadfsfadsfadsfadsfadfadfadsfadfadf";
                }
                OutputStream os = null;
                try {
                    byte[] bytes = content.getBytes("utf-8");
                    os = file.getContent().getOutputStream();
                    os.write(bytes);
                } finally {
                    if (os != null) {
                        os.close();
                    }
                    file.getContent().close();
                }
            }
        }
    }

    @Test
    public void runMultiThreadedUpload() throws Exception {
    }

    @Test
    public void runMultiThreadedDownload() throws Exception {
    }
}
