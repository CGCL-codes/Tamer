package org.pixory.pxfoundation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PXFileUtilityTestCase extends PXTestCase {

    private static final Log LOG = LogFactory.getLog(PXFileUtilityTestCase.class);

    private static final int TEST_BYTES_SIZE = 1024;

    private static final String TEST_FILE_NAME = "_PXFileUtilityTestCase_test_";

    private static final String TEST_COPY_FILE_NAME = "filetocopy";

    private File _testFile;

    private byte[] _testBytes;

    public PXFileUtilityTestCase() {
    }

    protected void setUp() {
        File currentDir = PXFileUtility.getCurrentDirectory();
        _testFile = new File(currentDir, TEST_FILE_NAME);
        Random aRandom = new Random();
        _testBytes = new byte[TEST_BYTES_SIZE];
        aRandom.nextBytes(_testBytes);
    }

    protected void tearDown() {
        if (_testFile.exists()) {
            _testFile.delete();
        }
        _testFile = null;
        _testBytes = null;
    }

    public void runTests() throws Exception {
        this.testCopy();
        this.testMove();
        this.testBaseName();
        this.testCopyFile();
        this.testCopyFiles();
        this.testDeleteMatching();
        this.testEmptyDirectory();
        this.testEnsureParentDirectory();
        this.testExtension();
        this.testFindFilenamed();
        this.testIsParent();
        this.testPathDifference();
        this.testReadContents();
        this.testReadWriteContents();
        this.testReadWriteString();
        this.testRecursiveDelete();
        this.testTempDirectory();
        this.testWriteContents();
        this.testEnsureHidden();
    }

    public void testCopy() throws Exception {
        File currentDir = PXFileUtility.getCurrentDirectory();
        File destinationDir = new File(currentDir, "copied");
        File testDataDir = new File(currentDir, PXFoundationTestSuite.TEST_DATA_DIR_NAME);
        File sourceDir = new File(testDataDir, "toCopy");
        if (destinationDir.exists()) {
            assertTrue(PXFileUtility.delete(destinationDir));
        }
        assertTrue(sourceDir.isDirectory());
        boolean copied = PXFileUtility.copy(sourceDir, destinationDir, null);
        assertTrue(copied);
        List sourcePaths = getAllRelativePathsUnder(sourceDir);
        List destinationPaths = getAllRelativePathsUnder(destinationDir);
        assertEquals(sourcePaths, destinationPaths);
    }

    public void testMove() throws Exception {
        File currentDir = PXFileUtility.getCurrentDirectory();
        File destinationDir = new File(currentDir, "toMove");
        File testDataDir = new File(currentDir, PXFoundationTestSuite.TEST_DATA_DIR_NAME);
        File originalSourceDir = new File(testDataDir, "toCopy");
        if (destinationDir.exists()) {
            assertTrue(PXFileUtility.delete(destinationDir));
        }
        assertTrue(originalSourceDir.isDirectory());
        boolean copied = PXFileUtility.copy(originalSourceDir, destinationDir, null);
        assertTrue(copied);
        assertTrue(destinationDir.exists());
        File sourceDir = destinationDir;
        destinationDir = new File(currentDir, "moved");
        if (destinationDir.exists()) {
            assertTrue(PXFileUtility.delete(destinationDir));
        }
        boolean moved = PXFileUtility.move(sourceDir, destinationDir, null);
        assertTrue(moved);
        assertTrue(destinationDir.exists());
        assertTrue(!sourceDir.exists());
        List sourcePaths = getAllRelativePathsUnder(originalSourceDir);
        List destinationPaths = getAllRelativePathsUnder(destinationDir);
        assertEquals(sourcePaths, destinationPaths);
    }

    /**
	 * @return Set of Strings, which are paths _relative_ to the startingPath_
	 */
    private static List getAllFilesUnder(File startingPath_) {
        List getAllFilesUnder = null;
        if ((startingPath_ != null) && (startingPath_.isDirectory())) {
            File[] filesUnder = startingPath_.listFiles();
            if ((filesUnder != null) && (filesUnder.length > 0)) {
                getAllFilesUnder = new ArrayList(filesUnder.length);
                for (int i = 0; i < filesUnder.length; i++) {
                    File file = filesUnder[i];
                    getAllFilesUnder.add(file);
                    if (file.isDirectory()) {
                        List subfiles = getAllFilesUnder(file);
                        if (subfiles != null) {
                            getAllFilesUnder.addAll(subfiles);
                        }
                    }
                }
            }
        }
        return getAllFilesUnder;
    }

    private static List getAllRelativePathsUnder(File startingPath_) {
        List getAllRelativePathsUnder = null;
        if (startingPath_ != null) {
            List filesUnder = getAllFilesUnder(startingPath_);
            if ((filesUnder != null) && (filesUnder.size() > 0)) {
                getAllRelativePathsUnder = new ArrayList(filesUnder.size());
                Iterator fileIterator = filesUnder.iterator();
                while (fileIterator.hasNext()) {
                    File fileUnder = (File) fileIterator.next();
                    String relativePath = PXFileUtility.getPathDifference(startingPath_, fileUnder);
                    getAllRelativePathsUnder.add(relativePath);
                }
            }
        }
        return getAllRelativePathsUnder;
    }

    public void testReadContents() throws IOException {
        try {
            PXFileUtility.readContents(_testFile);
            fail("Shoulda thrown MissingResourceException");
        } catch (IOException anException) {
        }
        FileOutputStream aFileStream = new FileOutputStream(_testFile);
        BufferedOutputStream aBufferedStream = new BufferedOutputStream(aFileStream);
        for (int i = 0; i < _testBytes.length; i++) {
            aBufferedStream.write(_testBytes[i]);
        }
        aBufferedStream.flush();
        aBufferedStream.close();
        byte[] someContents = PXFileUtility.readContents(_testFile);
        assertTrue(Arrays.equals(someContents, _testBytes));
        assertTrue(_testFile.delete());
    }

    public void testReadWriteContents() throws IOException {
        _testFile.createNewFile();
        try {
            PXFileUtility.writeContents(_testFile, _testBytes);
        } catch (Exception anException) {
            assertTrue(anException instanceof IOException);
        }
        _testFile.delete();
        PXFileUtility.writeContents(_testFile, _testBytes);
        byte[] someContents = PXFileUtility.readContents(_testFile);
        assertTrue(Arrays.equals(someContents, _testBytes));
        _testFile.delete();
    }

    public void testReadWriteString() throws IOException {
        String aTestString = "aaaaaaaaa bbbbbbbb cccccccc ddd\n\t\r(*&(^%%";
        String aTestFileName = "_remove_me_";
        String aWorkingPath = System.getProperty("user.dir");
        File aWorkingDirectory = new File(aWorkingPath);
        File aTestFile = new File(aWorkingDirectory, aTestFileName);
        PXFileUtility.writeContentsFromString(aTestFile, aTestString);
        String aResultString = PXFileUtility.readContentsAsString(aTestFile);
        assertEquals(aTestString, aResultString);
        aTestFile.delete();
    }

    public void testWriteContents() throws IOException {
        _testFile.delete();
        PXFileUtility.writeContents(_testFile, new byte[0]);
        assertTrue(_testFile.exists());
        assertEquals(_testFile.length(), 0);
        _testFile.delete();
        PXFileUtility.writeContents(_testFile, null);
        assertTrue(!_testFile.exists());
    }

    public void testCopyFile() throws IOException {
        String aSourceFilePath = PXPathUtility.pathByAppendingPathComponent(PXFoundationTestSuite.TEST_DATA_DIR_NAME, TEST_COPY_FILE_NAME);
        File aSourceFile = new File(aSourceFilePath);
        String aDestinationFileName = "_copyfile_";
        File aDestinationFile = new File(aDestinationFileName);
        byte[] someSourceBytes = PXFileUtility.readContents(aSourceFile);
        PXFileUtility.copyFile(aSourceFile, aDestinationFile);
        byte[] someDestinationBytes = PXFileUtility.readContents(aDestinationFile);
        assertTrue(Arrays.equals(someSourceBytes, someDestinationBytes));
        aDestinationFile.delete();
    }

    public void testCopyFiles() throws IOException {
        String aCurrentPath = System.getProperty("user.dir");
        File aCurrentDir = new File(aCurrentPath);
        String aDestinationDirName = "_testCopyFiles_";
        File aDestinationDir = new File(aCurrentDir, aDestinationDirName);
        File aSourceDir = new File(aCurrentDir, PXFoundationTestSuite.TEST_DATA_DIR_NAME);
        PXFileUtility.copyFilesFromDirectory(aSourceDir, aDestinationDir, null);
        FileFilter aFileFilter = new FileFileFilter();
        File[] someSourceFiles = aSourceDir.listFiles(aFileFilter);
        File[] someDestinationFiles = aDestinationDir.listFiles(aFileFilter);
        List aSourceList = new ArrayList();
        List aDestinationList = new ArrayList();
        for (int i = 0; i < someSourceFiles.length; i++) {
            aSourceList.add(someSourceFiles[i].getName());
        }
        for (int i = 0; i < someDestinationFiles.length; i++) {
            aDestinationList.add(someDestinationFiles[i].getName());
        }
        assertEquals(aSourceList, aDestinationList);
        PXFileUtility.delete(aDestinationDir);
    }

    public void testBaseName() {
        File aFile = new File("");
        assertEquals(PXFileUtility.getBaseName(aFile), "");
        aFile = new File("a");
        assertEquals(PXFileUtility.getBaseName(aFile), "a");
        aFile = new File(".");
        assertEquals(PXFileUtility.getBaseName(aFile), ".");
        aFile = new File(".asd");
        assertEquals(PXFileUtility.getBaseName(aFile), ".asd");
        aFile = new File("asd.");
        assertEquals(PXFileUtility.getBaseName(aFile), "asd");
        aFile = new File("asd.jpg");
        assertEquals(PXFileUtility.getBaseName(aFile), "asd");
    }

    public void testExtension() {
        assertNull(PXFileUtility.getExtension(null));
        assertNull(PXFileUtility.getExtension(new File("")));
        assertNull(PXFileUtility.getExtension(new File("aaaa")));
        assertNull(PXFileUtility.getExtension(new File(".")));
        assertNull(PXFileUtility.getExtension(new File("aaaa.")));
        assertEquals(PXFileUtility.getExtension(new File("aaaa.bbbb")), "bbbb");
        assertEquals(PXFileUtility.getExtension(new File(".bbbb")), "bbbb");
    }

    public void testRecursiveDelete() throws IOException {
        String aTestDirName = "_test_";
        String theTestPath = System.getProperty("user.dir");
        File theCurrentDirectory = new File(theTestPath);
        for (int i = 0; i < 3; i++) {
            theTestPath = PXPathUtility.pathByAppendingPathComponent(theTestPath, aTestDirName);
            File aTestDir = new File(theTestPath);
            aTestDir.mkdir();
        }
        String aSourceFilePath = PXPathUtility.pathByAppendingPathComponent(PXFoundationTestSuite.TEST_DATA_DIR_NAME, TEST_COPY_FILE_NAME);
        String aTestFilePath = PXPathUtility.pathByAppendingPathComponent(theTestPath, TEST_COPY_FILE_NAME);
        File aSourceFile = new File(aSourceFilePath);
        File aTestFile = new File(aTestFilePath);
        PXFileUtility.copyFile(aSourceFile, aTestFile);
        File aTestDir = new File(aTestDirName);
        assertTrue(PXFileUtility.delete(aTestDir));
    }

    public void testFindFilenamed() throws IOException {
        String aCurrentPath = System.getProperty("user.dir");
        File aCurrentDir = new File(aCurrentPath);
        File aSourceDir = new File(aCurrentDir, PXFoundationTestSuite.TEST_DATA_DIR_NAME);
        File aFileNamed = PXFileUtility.findFileNamed(aSourceDir, "RandomJunk");
        assertNull(aFileNamed);
        aFileNamed = PXFileUtility.findFileNamed(null, TEST_COPY_FILE_NAME);
        assertNull(aFileNamed);
        aFileNamed = PXFileUtility.findFileNamed(aSourceDir, null);
        assertNull(aFileNamed);
        aFileNamed = PXFileUtility.findFileNamed(null, null);
        assertNull(aFileNamed);
        aFileNamed = PXFileUtility.findFileNamed(aSourceDir, TEST_COPY_FILE_NAME);
        assertTrue(aFileNamed.getName().equals(TEST_COPY_FILE_NAME));
    }

    public void testEmptyDirectory() throws IOException {
        String aTestDirName = "_test.dir_";
        String aCurrentPath = System.getProperty("user.dir");
        File aCurrentDir = new File(aCurrentPath);
        File aTestDir = new File(aCurrentDir, aTestDirName);
        aTestDir.mkdir();
        assertTrue(PXFileUtility.isDirectoryEmpty(aTestDir));
        File aTestFile = new File(aTestDir, "_testfile_");
        aTestFile.createNewFile();
        assertTrue(!PXFileUtility.isDirectoryEmpty(aTestDir));
        PXFileUtility.delete(aTestDir);
    }

    private static class FileFileFilter extends Object implements FileFilter {

        private static final Log LOG = LogFactory.getLog(FileFileFilter.class);

        public boolean accept(File file) {
            boolean accept = false;
            if ((file != null) && (file.isFile())) {
                accept = true;
            }
            return accept;
        }
    }

    public void testDeleteMatching() throws IOException {
        String aCurrentPath = System.getProperty("user.dir");
        File aCurrentDir = new File(aCurrentPath);
        String aTestDirName = "_testDeleteMatching_";
        File aTestDir = new File(aCurrentDir, aTestDirName);
        aTestDir.mkdir();
        ArrayList someFilenames = new ArrayList(8);
        someFilenames.add("xxxfilteryyy.zzz");
        someFilenames.add("filteryyy.zzz");
        someFilenames.add("filte.zzz");
        someFilenames.add("xxx.filter");
        Iterator aFilenameIterator = someFilenames.iterator();
        while (aFilenameIterator.hasNext()) {
            File aTestFile = new File(aTestDir, (String) aFilenameIterator.next());
            aTestFile.createNewFile();
        }
        String aRegex = ".*filter.*";
        assertTrue(PXFileUtility.deleteMatching(aTestDir, aRegex));
        String[] someFiles = aTestDir.list();
        assertTrue(someFiles.length == 1);
        assertEquals("filte.zzz", someFiles[0]);
        PXFileUtility.delete(aTestDir);
    }

    /**
	 * N.B. getPathDifference(String,String) uses canonical paths, which are
	 * sensitive to platform specifics. So need different test for different
	 * platform path types
	 *  
	 */
    public void testPathDifference() {
        if (SystemUtils.IS_OS_WINDOWS) {
            File aParentPath = new File("c:\\aaa\\bbb");
            File aChildPath = new File("c:\\aaa\\bbb\\ccc\\ddd");
            String aDifference = PXFileUtility.getPathDifference(aParentPath, aChildPath);
            assertEquals("ccc\\ddd", aDifference);
            aChildPath = new File("c:\\aaa\\bbb");
            aDifference = PXFileUtility.getPathDifference(aParentPath, aChildPath);
            assertNull(aDifference);
        } else {
            File aParentPath = new File("/aaa/bbb");
            File aChildPath = new File("/aaa/bbb/ccc/ddd");
            String aDifference = PXFileUtility.getPathDifference(aParentPath, aChildPath);
            assertEquals("ccc/ddd", aDifference);
            aChildPath = new File("/aaa/bbb");
            aDifference = PXFileUtility.getPathDifference(aParentPath, aChildPath);
            LOG.debug("aDifference: " + aDifference);
            assertNull(aDifference);
        }
    }

    public void testTempDirectory() throws IOException {
        File aTempFile = File.createTempFile("hello", null);
        File aTempParent = aTempFile.getParentFile();
        assertTrue(aTempParent.equals(PXFileUtility.getTempDirectory()));
    }

    public void testIsParent() throws IOException {
        assertTrue(PXFileUtility.isParent(null, null));
        File theCurrentDir = PXFileUtility.getCurrentDirectory();
        assertTrue(!PXFileUtility.isParent(theCurrentDir, null));
        assertTrue(!PXFileUtility.isParent(null, theCurrentDir));
        assertTrue(PXFileUtility.isParent(theCurrentDir, new File(theCurrentDir.getCanonicalPath())));
        File aSubdir = new File(theCurrentDir, "testing");
        assertTrue(PXFileUtility.isParent(aSubdir, theCurrentDir));
    }

    public void testEnsureParentDirectory() throws IOException {
        File aCurrentDir = PXFileUtility.getCurrentDirectory();
        assertTrue(PXFileUtility.ensureParentDirectory(aCurrentDir));
        File aNonExistentSubDir = new File(aCurrentDir, "does_not_exist");
        File aTargetFile = new File(aNonExistentSubDir, "testEnsureParent");
        assertTrue(PXFileUtility.ensureParentDirectory(aTargetFile));
        assertTrue(aNonExistentSubDir.isDirectory());
        assertTrue(PXFileUtility.delete(aNonExistentSubDir));
    }

    public void testEnsureHidden() throws IOException {
        File aCurrentDir = PXFileUtility.getCurrentDirectory();
        File aTestFile = new File(aCurrentDir, ".pixory");
        aTestFile.createNewFile();
        assertTrue(PXFileUtility.ensureHidden(aTestFile));
        aTestFile.delete();
        aTestFile = new File(aCurrentDir, "pixory");
        aTestFile.createNewFile();
        if (SystemUtils.IS_OS_WINDOWS) {
            assertTrue(PXFileUtility.ensureHidden(aTestFile));
        } else {
            assertTrue(!PXFileUtility.ensureHidden(aTestFile));
        }
        aTestFile.delete();
    }
}
