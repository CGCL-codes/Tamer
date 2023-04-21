package org.sf.dctmutils.common.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.sf.dctmutils.common.FileHelper;
import org.sf.dctmutils.common.exception.MissingParameterException;
import org.sf.dctmutils.common.test.DctmUtilsTestCase;

/**
 * Unit tests for the FileHelper class.
 *
 * @author <a href="mailto:luther@lebsvcs.com">Luther E. Birdzell</a>
 * @version 1.0
 */
public class FileHelperTest extends DctmUtilsTestCase {

    private static Logger logger = Logger.getLogger(FileHelperTest.class);

    /**
     * Set up the TestSuite.
     *
     * @return a <code>Test</code> value
     */
    public static Test suite() {
        return new TestSuite(FileHelperTest.class);
    }

    /**
     * Test recursiveRemoveDirectory.
     */
    public void testRecursiveRemoveDirectory() {
        try {
            String rootTestDirStr = "junit-filehelper-test-dir";
            File rootTestDir = new File(rootTestDirStr);
            File path = new File(rootTestDirStr + "/foo/bar/junit/test");
            path.mkdirs();
            File testFile = new File(rootTestDirStr + "/foo/bar/test-file");
            testFile.createNewFile();
            File testFile2 = new File(rootTestDirStr + "/foo/test-file");
            testFile2.createNewFile();
            File testFile3 = new File(rootTestDirStr + "/foo/test-file2");
            testFile3.createNewFile();
            FileHelper.recursiveRemoveDirectory(rootTestDir);
            assertFalse(rootTestDir.exists());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail("testRecursiveRemoveDirectory failed.  " + e.getMessage());
        }
    }

    /**
     * Test recursiveRemoveDirectory.
     */
    public void testRecursiveRemoveDirectoryNull() {
        try {
            FileHelper.recursiveRemoveDirectory(null);
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test recursiveEmptyDirectory.
     */
    public void testRecursiveEmptyDirectoryNull() {
        try {
            FileHelper.recursiveEmptyDirectory(null);
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test isXmlFile().
     */
    public void testIsXmlFile() {
        assertTrue(FileHelper.isXmlFile("foo.xml"));
        assertTrue(FileHelper.isXmlFile("FooBar.xml"));
        assertTrue(FileHelper.isXmlFile("foobar.XML"));
        assertFalse(FileHelper.isXmlFile("foobar.html"));
        assertFalse(FileHelper.isXmlFile("foobar.doc"));
    }

    /**
     * Test isXmlFile().
     */
    public void testIsXmlFileNull() {
        try {
            FileHelper.isXmlFile(null);
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test isXmlFile().
     */
    public void testIsXmlFileEmptyString() {
        try {
            FileHelper.isXmlFile("");
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test replaceBackSlashWithSlash.
     */
    public void testReplaceBackSlashWithSlash() {
        assertTrue(FileHelper.replaceBackSlashWithSlash("C:\\foo\\bar").equals("C:/foo/bar"));
    }

    /**
     * Test replaceBackSlashWithSlash.
     */
    public void testReplaceBackSlashWithSlashNull() {
        try {
            FileHelper.replaceBackSlashWithSlash(null);
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test replaceBackSlashWithSlash.
     */
    public void testReplaceBackSlashWithSlashEmptyString() {
        try {
            FileHelper.replaceBackSlashWithSlash("");
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test removeFileName.
     */
    public void testRemoveFileName() {
        assertTrue(FileHelper.removeFileName("/foo/bar/test.txt").equals("/foo/bar"));
        assertTrue(FileHelper.removeFileName("/foo/bar/README").equals("/foo/bar/README"));
    }

    /**
     * Test removeFileName.
     */
    public void testRemoveFileNameNull() {
        try {
            FileHelper.removeFileName(null);
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test removeFileName.
     */
    public void testRemoveFileNameEmptyString() {
        try {
            FileHelper.removeFileName("");
        } catch (MissingParameterException mpe) {
            return;
        }
        fail("expected MissingParameterException");
    }

    /**
     * Test FileHelper.getFileName()
     */
    public void testGetFileName() {
        assertTrue(FileHelper.getFileName(null) == null);
        assertTrue(FileHelper.getFileName("").equals(""));
        assertTrue(FileHelper.getFileName("foo.xml").equals("foo.xml"));
        assertTrue(FileHelper.getFileName("README").equals("README"));
        assertTrue(FileHelper.getFileName(".emacs").equals(".emacs"));
        assertTrue(FileHelper.getFileName("foo.xml").equals("foo.xml"));
        assertTrue(FileHelper.getFileName("c:\\foo.xml").equals("foo.xml"));
        assertTrue(FileHelper.getFileName("c:\\temp\\test\\foo.xml").equals("foo.xml"));
        assertTrue(FileHelper.getFileName("/foo.xml").equals("foo.xml"));
        assertTrue(FileHelper.getFileName("/tmp/test/foo.xml").equals("foo.xml"));
    }

    /**
     * Test FileHelper.removeFileExtension()
     */
    public void testRemoveFileExtension() {
        assertTrue(FileHelper.removeFileExtension(null) == null);
        assertTrue(FileHelper.removeFileExtension("").equals(""));
        assertTrue(FileHelper.removeFileExtension("foo").equals("foo"));
        assertTrue(FileHelper.removeFileExtension("foo.xml").equals("foo"));
        assertTrue(FileHelper.removeFileExtension("FooBar.xml").equals("FooBar"));
        assertTrue(FileHelper.removeFileExtension("foobar.XML").equals("foobar"));
        assertTrue(FileHelper.removeFileExtension("my.foobar.XML").equals("my.foobar"));
    }

    /**
     * Test FileHelper.getFileExtension()
     */
    public void testGetFileExtension() {
        assertTrue(FileHelper.getFileExtension(null) == null);
        assertTrue(FileHelper.getFileExtension("") == null);
        assertTrue(FileHelper.getFileExtension("foo.xml").equals("xml"));
        assertTrue(FileHelper.getFileExtension("FOO.xml").equals("xml"));
        assertTrue(FileHelper.getFileExtension("FOO.properties").equals("properties"));
        assertTrue(FileHelper.getFileExtension("my.foo.bar.FOO.properties").equals("properties"));
        assertTrue(FileHelper.getFileExtension(".emacs") == null);
        assertTrue(FileHelper.getFileExtension("README") == null);
    }

    /**
     * Test FileHelper.getFileAsString()
     */
    public void testGetFileAsStringFile() {
        try {
            String testText = "testGetFileAsString() test text";
            File file = new File("testGetFileAsString.txt");
            FileWriter writer = new FileWriter(file);
            writer.write(testText, 0, testText.length());
            writer.flush();
            writer.close();
            String fileString = FileHelper.getFileAsString(file);
            assertTrue(fileString.equals(testText));
            file.delete();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * Test FileHelper.getFileAsString()
     */
    public void testGetFileAsStringPath() {
        try {
            String testText = "testGetFileAsString() test text";
            File file = new File("testGetFileAsString.txt");
            FileWriter writer = new FileWriter(file);
            writer.write(testText, 0, testText.length());
            writer.flush();
            writer.close();
            String path = file.getPath();
            String fileString = FileHelper.getFileAsString(path);
            assertTrue(fileString.equals(testText));
            file.delete();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * Test getFileAsStringFileWithNullFileName.
     */
    public void testGetFileAsStringFileWithNullFileName() {
        try {
            String file = FileHelper.getFileAsString((File) null);
        } catch (MissingParameterException mpe) {
            logger.debug("Caught expected Exception. " + mpe.getMessage());
            return;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            fail("testGetFileAsStringWithNullFileName() failed!");
        }
        fail("Expected MissingParameterException to be thrown.");
    }

    /**
     * Test getFileAsStringPathWithNullFileName.
     */
    public void testGetFileAsStringPathWithNullFileName() {
        try {
            String file = FileHelper.getFileAsString((String) null);
        } catch (MissingParameterException mpe) {
            logger.debug("Caught expected Exception. " + mpe.getMessage());
            return;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            fail("testGetFileAsStringWithNullFileName() failed!");
        }
        fail("Expected MissingParameterException to be thrown.");
    }

    /**
     * Test getFileAsStringPathWithNullFileName.
     */
    public void testGetFileAsStringWithEmptyStringPath() {
        try {
            String file = FileHelper.getFileAsString("");
        } catch (MissingParameterException mpe) {
            logger.debug("Caught expected Exception. " + mpe.getMessage());
            return;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            fail("testGetFileAsStringWithNullFileName() failed!");
        }
        fail("Expected MissingParameterException to be thrown.");
    }

    /**
     * Test FileHelper.getFileAsStringFromClassPath().
     *
     */
    public void testGetFileAsStringFromClassPath() {
        try {
            String file = FileHelper.getFileAsStringFromClassPath("unitTest.xml");
            logger.debug("file = " + file);
            assertTrue(file != null);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            fail("testGetFileAsStringFromClassPath failed! " + ioe.getMessage());
        }
    }

    /**
     * Test FileHelper.getFileAsStringFromClassPath().
     *
     */
    public void testGetFileAsStringFromClassPathFakeFile() {
        try {
            String file = FileHelper.getFileAsStringFromClassPath("I h0p3 there's no F1l3 with this N@m3");
        } catch (RuntimeException re) {
            logger.debug("caught expected exception");
            return;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            fail("testGetFileAsStringFromClassPath failed! " + ioe.getMessage());
        }
        fail("Expected RuntimeException with fake file");
    }

    /**
     * Test FileHelper.getFileAsStringFromClassPath().
     *
     */
    public void testGetFileAsStringFromClassPathNull() {
        try {
            FileHelper.getFileAsStringFromClassPath(null);
        } catch (MissingParameterException mpe) {
            logger.debug("Caught expected Exception. " + mpe.getMessage());
            return;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            fail("testGetFileAsStringFromClassPathNull() failed!");
        }
        fail("Expected MissingParameterException to be thrown.");
    }

    /**
     * Test FileHelper.getFileAsStringFromClassPath().
     *
     */
    public void testGetFileAsStringFromClassPathEmpthString() {
        try {
            FileHelper.getFileAsStringFromClassPath("");
        } catch (MissingParameterException mpe) {
            logger.debug("Caught expected Exception. " + mpe.getMessage());
            return;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            fail("testGetFileAsStringFromClassPathNull() failed!");
        }
        fail("Expected MissingParameterException to be thrown.");
    }
}
