package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import javax.naming.InitialContext;
import org.fsconnector.cci.CreateException;
import org.fsconnector.cci.DeleteException;
import org.fsconnector.cci.FSConnection;
import org.fsconnector.cci.FSConnectionFactory;
import org.fsconnector.cci.FSFile;
import org.fsconnector.cci.FilenameFilter;
import org.fsconnector.cci.RenameException;
import org.fsconnector.cci.SetException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class FSFileTest {

    private static final String rootDirname = "/devel/testing";

    private static final String multiLevel = "/L1/L2";

    private FSConnection conn;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        FSConnectionFactory factory = (FSConnectionFactory) new InitialContext().lookup("java:FSConnector");
        conn = factory.getConnection();
        boolean success = new File(rootDirname, multiLevel).mkdirs();
        if (!success) throw new Exception("Failed to create test directory");
    }

    @After
    public void tearDown() {
        if (conn != null) conn.close();
        deleteFiles(new File(rootDirname).listFiles());
    }

    private static void deleteFiles(File[] files) {
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                deleteFiles(f.listFiles());
            }
            f.delete();
        }
    }

    private void createTestFile(final String fname) throws IOException {
        File file = new File(rootDirname, fname);
        boolean success = file.createNewFile();
        if (!success) throw new IOException("Failed to create test file");
    }

    private void createTestDir(final String dname) throws IOException {
        File file = new File(rootDirname, dname);
        boolean success = file.mkdir();
        if (!success) throw new IOException("Failed to create test directory");
    }

    @Test
    public void testCanReadTrue() throws Exception {
        String fname = "/testCanReadTrue.txt";
        File f1 = new File(rootDirname.concat(fname));
        boolean success = f1.createNewFile();
        if (!success) throw new Exception("Failed to create test file");
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertTrue(file.canRead());
    }

    @Test
    public void testCanReadFalse() throws Exception {
        String fname = "/testCanReadFalse.txt";
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertFalse(file.canRead());
    }

    @Test
    public void testCanReadMultiLevelTrue() throws Exception {
        String fname = multiLevel.concat("/testCanReadMultiLevelTrue.txt");
        File f1 = new File(rootDirname.concat(fname));
        boolean success = f1.createNewFile();
        if (!success) throw new Exception("Failed to create test file");
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertTrue(file.canRead());
    }

    @Test
    public void testCanReadMultiLevelFalse() throws Exception {
        String fname = multiLevel.concat("/testCanReadMultiLevelFalse.txt");
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertFalse(file.canRead());
    }

    @Test
    public void testCanWriteTrue() throws Exception {
        String fname = "/testCanWriteTrue.txt";
        File f1 = new File(rootDirname.concat(fname));
        boolean success = f1.createNewFile();
        if (!success) throw new Exception("Failed to create test file");
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertTrue(file.canWrite());
    }

    @Test
    public void testCanWriteFalse() throws Exception {
        String fname = "/testCanWriteFalse.txt";
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertFalse(file.canWrite());
    }

    @Test
    public void testCanWriteMultiLevelTrue() throws Exception {
        String fname = multiLevel.concat("/testCanWriteMultiLevelTrue.txt");
        File f1 = new File(rootDirname.concat(fname));
        boolean success = f1.createNewFile();
        if (!success) throw new Exception("Failed to create test file");
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertTrue(file.canWrite());
    }

    @Test
    public void testCanWriteMultiLevelFalse() throws Exception {
        String fname = multiLevel.concat("/testCanWriteMultiLevelFalse.txt");
        FSFile file = conn.getFile(fname);
        assertNotNull("file is null", file);
        assertFalse(file.canWrite());
    }

    @Test
    public void testCompareToLess() {
        FSFile f1 = conn.getFile("aaa.txt");
        FSFile f2 = conn.getFile("bbb.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testCompareToEqual() {
        FSFile f1 = conn.getFile("aaa.txt");
        FSFile f2 = conn.getFile("aaa.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertTrue(f1.compareTo(f2) == 0);
    }

    @Test
    public void testCompareToGreater() {
        FSFile f1 = conn.getFile("bbb.txt");
        FSFile f2 = conn.getFile("aaa.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertTrue(f1.compareTo(f2) > 0);
    }

    @Test
    public void testCompareToDiffDirLess() {
        FSFile f1 = conn.getFile("aaa/aaa.txt");
        FSFile f2 = conn.getFile("bbb/aaa.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testCompareToDiffDirEqual() {
        FSFile f1 = conn.getFile("aaa/aaa.txt");
        FSFile f2 = conn.getFile("aaa/aaa.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertTrue(f1.compareTo(f2) == 0);
    }

    @Test
    public void testCompareToDiffDirGreater() {
        FSFile f1 = conn.getFile("bbb/aaa.txt");
        FSFile f2 = conn.getFile("aaa/aaa.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertTrue(f1.compareTo(f2) > 0);
    }

    @Test
    public void testCreateNewFileSuccess() throws IOException {
        String fname = "testCreateNewFileSuccess";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.createNewFile();
        File file = new File(rootDirname, fname);
        assertTrue("file doesn not exist", file.exists());
    }

    @Test(expected = IOException.class)
    public void testCreateNewFileFailure() throws IOException {
        FSFile f1 = conn.getFile(">");
        assertNotNull("f1 is null", f1);
        f1.createNewFile();
    }

    @Test
    public void testCreateNewFileMultiLevelSuccess() throws IOException {
        String fname = multiLevel.concat("/testCreateNewFileMultiLevelSuccess.txt");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.createNewFile();
        File file = new File(rootDirname, fname);
        assertTrue("file doesn not exist", file.exists());
    }

    @Test(expected = IOException.class)
    public void testCreateNewFileMultiLevelFailure() throws IOException {
        String fname = multiLevel.concat("/>");
        FSFile f1 = conn.getFile(fname);
        assertTrue("f1 is null", f1 != null);
        f1.createNewFile();
        File file = new File(rootDirname, fname);
        assertFalse("file exist when it should not", file.exists());
    }

    @Test
    public void testDeleteSuccess() throws IOException, DeleteException {
        String fname = "testDeleteSuccess.txt";
        File file = new File(rootDirname, fname);
        boolean success = file.createNewFile();
        if (!success) throw new IOException("Failed to create test file");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.delete();
        assertFalse("file still exists", file.exists());
    }

    @Test(expected = DeleteException.class)
    public void testDeleteFailure() throws DeleteException {
        String fname = "testDeleteFailure.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.delete();
    }

    @Test
    public void testDeleteMulitLevelSuccess() throws IOException, DeleteException {
        String fname = multiLevel.concat("/testDeleteMultiLevelSuccess.txt");
        File file = new File(rootDirname, fname);
        boolean success = file.createNewFile();
        if (!success) throw new IOException("Failed to create test file");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.delete();
        assertFalse("file still exists", file.exists());
    }

    @Test(expected = DeleteException.class)
    public void testDeleteMultiLevelFailure() throws DeleteException {
        String fname = multiLevel.concat("/testDeleteMultiLevelFailure.txt");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.delete();
    }

    @Test
    public void testEqualsTrue() {
        FSFile f1 = conn.getFile("aaa.txt");
        FSFile f2 = conn.getFile("aaa.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsFalse() {
        FSFile f1 = conn.getFile("aaa.txt");
        FSFile f2 = conn.getFile("aaaa.txt");
        assertNotNull("f1 is null", f1);
        assertNotNull("f2 is null", f2);
        assertFalse(f1.equals(f2));
    }

    @Test
    public void testEqualsNull() {
        FSFile f1 = conn.getFile("aaa.txt");
        assertNotNull("f1 is null", f1);
        assertFalse(f1.equals(null));
    }

    @Test
    public void testExistsTrue() throws IOException {
        String fname = "testExistsTrue.txt";
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.exists());
    }

    @Test
    public void testExistsFalse() {
        String fname = "testExistsFalse.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.exists());
    }

    @Test
    public void testExistsMultiLevelTrue() throws IOException {
        String fname = multiLevel.concat("/testExistsMultiLevelTrue.txt");
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.exists());
    }

    @Test
    public void testExistsMultiLevelFalse() {
        String fname = multiLevel.concat("/testExistsMultiLevelFalse.txt");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.exists());
    }

    @Test
    public void testGetName() {
        String fname = "/testGetName.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.getName().equals("testGetName.txt"));
    }

    @Test
    public void testGetNameMultiLevel() {
        String fname = multiLevel.concat("/testGetNameMulitLevel.txt");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.getName().equals("testGetNameMulitLevel.txt"));
    }

    @Test
    public void testGetParent() {
        String fname = "testGetParent.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        String parent = f1.getParent();
        assertTrue("Was expecting / but got " + parent, parent.equals("/"));
    }

    @Test
    public void testGetParentMultiLevel() {
        String fname = multiLevel.concat("/testGetParentMultiLevel.txt");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        String parent = f1.getParent();
        assertTrue("Was expecting " + multiLevel + " but got " + parent, parent.equals(multiLevel));
    }

    @Test
    public void testGetParentDirectory() {
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        String parent = f1.getParent();
        assertTrue("Was expecting /L1 but got " + parent, parent.equals("/L1"));
    }

    @Test
    public void testGetParentNull() {
        String fname = "/";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        String parent = f1.getParent();
        assertNull("Was expecting null but got " + parent, parent);
    }

    @Test
    public void testGetParentFile() {
        String fname = "testGetParentFile.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        FSFile parent = f1.getParentFile();
        assertTrue("Was expecting / but got " + parent, parent.equals(conn.getFile("/")));
    }

    @Test
    public void testGetParentFileMultiLevel() {
        String fname = multiLevel.concat("/testGetParentFileMultiLevel.txt");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        FSFile parent = f1.getParentFile();
        assertTrue("Was expecting " + multiLevel + " but got " + parent, parent.equals(conn.getFile(multiLevel)));
    }

    @Test
    public void testGetParentFileDirectory() {
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        FSFile parent = f1.getParentFile();
        assertTrue("Was expecting /L1 but got " + parent, parent.equals(conn.getFile("/L1")));
    }

    @Test
    public void testGetParentFileNull() {
        String fname = "/";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        FSFile parent = f1.getParentFile();
        assertNull("Was expecting null but got " + parent, parent);
    }

    @Test
    public void testGetPath() {
        String fname = "testGetPath.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.getPath().equals("/testGetPath.txt"));
    }

    @Test
    public void testGetPathMultiLevel() {
        String fname = multiLevel.concat("/testGetPathMulitLevel.txt");
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.getPath().equals(multiLevel.concat("/testGetPathMulitLevel.txt")));
    }

    @Test
    public void testGetPathFileFromParent() {
        String fname = "testGetPathFileFromParent.txt";
        FSFile fRoot = conn.getFile("/");
        assertTrue(fRoot.getPath().equals("/"));
        FSFile f1 = conn.getFile(fRoot, fname);
        assertTrue("Expecting \"/" + fname + "\" but got \"" + f1.getPath() + "\"", f1.getPath().equals("/" + fname));
        FSFile f2 = conn.getFile(fRoot, "/" + fname);
        assertTrue("Expecting \"/" + fname + "\" but got \"" + f2.getPath() + "\"", f2.getPath().equals("/" + fname));
    }

    @Test
    public void testIsDirectoryTrue() throws IOException {
        String fname = "testIsDirectoryTrue";
        createTestDir(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.isDirectory());
    }

    @Test
    public void testIsDirectoryFalse() throws IOException {
        String fname = "testIsDirectoryFalse.txt";
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.isDirectory());
    }

    @Test
    public void testIsDirectoryMultiLevelTrue() throws IOException {
        String fname = multiLevel.concat("/testIsDirectoryMultiLevelTrue");
        createTestDir(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.isDirectory());
    }

    @Test
    public void testIsDirectoryMultiLevelFalse() throws IOException {
        String fname = multiLevel.concat("/testIsDirectoryMultiLevelFalse.txt");
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.isDirectory());
    }

    @Test
    public void testIsFileTrue() throws IOException {
        String fname = "testIsFileTrue.txt";
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.isFile());
    }

    @Test
    public void testIsFileFalse() throws IOException {
        String fname = "testIsFileFalse";
        createTestDir(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.isFile());
    }

    @Test
    public void testIsFileMultiLevelTrue() throws IOException {
        String fname = multiLevel.concat("testIsFileMultiLevelTrue.txt");
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.isFile());
    }

    @Test
    public void testIsFileMultiLevelFalse() throws IOException {
        String fname = multiLevel.concat("testIsFileMultiLevelFalse");
        createTestDir(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.isFile());
    }

    @Ignore
    @Test
    public void testIsHiddenTrue() throws IOException {
        fail("Don't know how to test this");
    }

    @Test
    public void testIsHiddenFalse() throws IOException {
        String fname = "testIsHiddenFalse.txt";
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.isHidden());
    }

    @Test
    public void testLastModified() throws IOException {
        String fname = "testLastModified.txt";
        File file = new File(rootDirname, fname);
        boolean success = file.createNewFile();
        if (!success) throw new IOException("Failed to create test file");
        long lastMod = file.lastModified();
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.lastModified() == lastMod);
    }

    @Test
    public void testLengthTrue() throws IOException {
        String fname = "testLengthTrue.txt";
        File file = new File(rootDirname, fname);
        boolean success = file.createNewFile();
        if (!success) throw new IOException("Failed to create test file");
        byte[] data = new byte[1024];
        for (int i = 0; i < data.length; ++i) {
            data[i] = 'a';
        }
        OutputStream os = new FileOutputStream(file);
        try {
            os.write(data);
        } finally {
            try {
                os.close();
            } catch (Exception e) {
            }
        }
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.length() == 1024);
    }

    @Test
    public void testLengthFalse() throws IOException {
        String fname = "testLengthFalse.txt";
        File file = new File(rootDirname, fname);
        boolean success = file.createNewFile();
        if (!success) throw new IOException("Failed to create test file");
        byte[] data = new byte[1025];
        for (int i = 0; i < data.length; ++i) {
            data[i] = 'a';
        }
        OutputStream os = new FileOutputStream(file);
        try {
            os.write(data);
        } finally {
            try {
                os.close();
            } catch (Exception e) {
            }
        }
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertFalse(f1.length() == 1024);
    }

    @Test
    public void testList() throws IOException {
        String fname1 = "testList1.txt";
        String fname2 = "testList2.txt";
        String fname3 = "testList3.txt";
        createTestFile(fname1);
        createTestFile(fname2);
        createTestFile(fname3);
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        String[] files = f1.list();
        Arrays.sort(files);
        assertTrue("Expecting 4 entries but got " + files.length, files.length == 4);
        assertTrue("Expecting " + fname1 + " but got " + files[1], files[1].equals(fname1));
        assertTrue("Expecting " + fname2 + " but got " + files[2], files[2].equals(fname2));
        assertTrue("Expecting " + fname3 + " but got " + files[3], files[3].equals(fname3));
    }

    @Test
    public void testListMultiLevel() throws IOException {
        String fname1 = "testListMultiLevel1.txt";
        String fname2 = "testListMultiLevel2.txt";
        String fname3 = "testListMultiLevel3.txt";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        String mname3 = multiLevel.concat("/").concat(fname3);
        createTestFile(mname1);
        createTestFile(mname2);
        createTestFile(mname3);
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        String[] files = f1.list();
        Arrays.sort(files);
        assertTrue("Expecting 3 entries but got " + files.length, files.length == 3);
        assertTrue("Expecting " + fname1 + " but got " + files[0], files[0].equals(fname1));
        assertTrue("Expecting " + fname2 + " but got " + files[1], files[1].equals(fname2));
        assertTrue("Expecting " + fname3 + " but got " + files[2], files[2].equals(fname3));
    }

    @Test
    public void testListFilenameFilterNoHits() throws IOException {
        String fname1 = "testListFilenameFilterNoHits1.txt";
        String fname2 = "testListFilenameFilterNoHits2.data";
        String fname3 = "testListFilenameFilterNoHits3.data";
        String fname4 = "testListFilenameFilterNoHits4.data";
        createTestFile(fname1);
        createTestFile(fname2);
        createTestFile(fname3);
        createTestFile(fname4);
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        String[] files = f1.list(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.endsWith(".csv");
            }
        });
        assertTrue("Expecting 0 entries but got " + files.length, files.length == 0);
    }

    @Test
    public void testListFilenameFilterOneHit() throws IOException {
        String fname1 = "testListFilenameFilterOneHit1.txt";
        String fname2 = "testListFilenameFilterOneHit2.data";
        String fname3 = "testListFilenameFilterOneHit3.data";
        String fname4 = "testListFilenameFilterOneHit4.data";
        createTestFile(fname1);
        createTestFile(fname2);
        createTestFile(fname3);
        createTestFile(fname4);
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        String[] files = f1.list(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.endsWith(".txt");
            }
        });
        assertTrue("Expecting 1 entries but got " + files.length, files.length == 1);
        assertTrue("Expecting " + fname1 + " but got " + files[0], files[0].equals(fname1));
    }

    @Test
    public void testListFilenameFilterMultipleHits() throws IOException {
        String fname1 = "testListFilenameFilterMultipleHits1.txt";
        String fname2 = "testListFilenameFilterMultipleHits2.data";
        String fname3 = "testListFilenameFilterMultipleHits3.data";
        String fname4 = "testListFilenameFilterMultipleHits4.data";
        createTestFile(fname1);
        createTestFile(fname2);
        createTestFile(fname3);
        createTestFile(fname4);
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        String[] files = f1.list(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.endsWith(".data");
            }
        });
        Arrays.sort(files);
        assertTrue("Expecting 3 entries but got " + files.length, files.length == 3);
        assertTrue("Expecting " + fname2 + " but got " + files[0], files[0].equals(fname2));
        assertTrue("Expecting " + fname3 + " but got " + files[1], files[1].equals(fname3));
        assertTrue("Expecting " + fname4 + " but got " + files[2], files[2].equals(fname4));
    }

    @Test
    public void testListFilenameFilterMultiLevelNoHits() throws IOException {
        String fname1 = "txt.testListFilenameFilterMultiLevelNoHits1";
        String fname2 = "data.testListFilenameFilterMultiLevelNoHits2";
        String fname3 = "data.testListFilenameFilterMultiLevelNoHits3";
        String fname4 = "data.testListFilenameFilterMultiLevelNoHits4";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        String mname3 = multiLevel.concat("/").concat(fname3);
        String mname4 = multiLevel.concat("/").concat(fname4);
        createTestFile(mname1);
        createTestFile(mname2);
        createTestFile(mname3);
        createTestFile(mname4);
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        String[] files = f1.list(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.startsWith("csv.");
            }
        });
        assertTrue("Expecting 0 entries but got " + files.length, files.length == 0);
    }

    @Test
    public void testListFilenameFilterMultiLevelOneHit() throws IOException {
        String fname1 = "txt.testListFilenameFilterMultiLevelOneHit1";
        String fname2 = "data.testListFilenameFilterMultiLevelOneHit2";
        String fname3 = "data.testListFilenameFilterMultiLevelOneHit3";
        String fname4 = "data.testListFilenameFilterMultiLevelOneHit4";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        String mname3 = multiLevel.concat("/").concat(fname3);
        String mname4 = multiLevel.concat("/").concat(fname4);
        createTestFile(mname1);
        createTestFile(mname2);
        createTestFile(mname3);
        createTestFile(mname4);
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        String[] files = f1.list(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.startsWith("txt.");
            }
        });
        assertTrue("Expecting 1 entries but got " + files.length, files.length == 1);
        assertTrue("Expecting " + fname1 + " but got " + files[0], files[0].equals(fname1));
    }

    @Test
    public void testListFilenameFilterMultiLevelMultipleHits() throws IOException {
        String fname1 = "txt.testListFilenameFilterMultiLevelMultipleHits1";
        String fname2 = "data.testListFilenameFilterMultiLevelMultipleHits2";
        String fname3 = "data.testListFilenameFilterMultiLevelMultipleHits3";
        String fname4 = "data.testListFilenameFilterMultiLevelMultipleHits4";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        String mname3 = multiLevel.concat("/").concat(fname3);
        String mname4 = multiLevel.concat("/").concat(fname4);
        createTestFile(mname1);
        createTestFile(mname2);
        createTestFile(mname3);
        createTestFile(mname4);
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        String[] files = f1.list(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.startsWith("data.");
            }
        });
        Arrays.sort(files);
        assertTrue("Expecting 3 entries but got " + files.length, files.length == 3);
        assertTrue("Expecting " + fname2 + " but got " + files[0], files[0].equals(fname2));
        assertTrue("Expecting " + fname3 + " but got " + files[1], files[1].equals(fname3));
        assertTrue("Expecting " + fname4 + " but got " + files[2], files[2].equals(fname4));
    }

    @Test
    public void testListFilesNoHites() {
        deleteFiles(new File(rootDirname).listFiles());
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles();
        assertTrue("Expecting 0 entries but got " + files.length, files.length == 0);
    }

    @Test
    public void testListFilesMultipleHits() throws IOException {
        String fname1 = "testListFilesMultipleHits1.txt";
        String fname2 = "testListFilesMultipleHits2";
        String fname3 = "testListFilesMultipleHits3.txt";
        createTestFile(fname1);
        createTestDir(fname2);
        createTestFile(fname3);
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles();
        Arrays.sort(files);
        assertTrue("Expecting 4 entries but got " + files.length, files.length == 4);
        assertTrue("Expecting " + fname1 + " but got " + files[1], files[1].equals(conn.getFile(fname1)));
        assertTrue("Expecting " + fname2 + " but got " + files[2], files[2].equals(conn.getFile(fname2)));
        assertTrue("Expecting " + fname3 + " but got " + files[3], files[3].equals(conn.getFile(fname3)));
    }

    @Test
    public void testListFilesMultiLevelNoHites() {
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles();
        assertTrue("Expecting 0 entries but got " + files.length, files.length == 0);
    }

    @Test
    public void testListFilesMultiLevelMultipleHits() throws IOException {
        String fname1 = "testListFilesMultiLevelMultipleHits1.txt";
        String fname2 = "testListFilesMultiLevelMultipleHits2.txt";
        String fname3 = "testListFilesMultiLevelMultipleHits3.txt";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        String mname3 = multiLevel.concat("/").concat(fname3);
        createTestFile(mname1);
        createTestFile(mname2);
        createTestFile(mname3);
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles();
        Arrays.sort(files);
        assertTrue("Expecting 3 entries but got " + files.length, files.length == 3);
        assertTrue("Expecting " + mname1 + " but got " + files[0], files[0].equals(conn.getFile(mname1)));
        assertTrue("Expecting " + mname2 + " but got " + files[1], files[1].equals(conn.getFile(mname2)));
        assertTrue("Expecting " + mname3 + " but got " + files[2], files[2].equals(conn.getFile(mname3)));
    }

    @Test
    public void testListFilesFilenameFilterNoHites() throws IOException {
        String fname1 = "testListFilenameFilter1.txt";
        String fname2 = "testListFilenameFilter2.data";
        String fname3 = "testListFilenameFilter3.data";
        String fname4 = "testListFilenameFilter4.data";
        createTestFile(fname1);
        createTestFile(fname2);
        createTestFile(fname3);
        createTestFile(fname4);
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.endsWith(".csv");
            }
        });
        assertTrue("Expecting 0 entries but got " + files.length, files.length == 0);
    }

    @Test
    public void testListFilesFilenameFilterMultipleHites() throws IOException {
        String fname1 = "testListFilesFilenameFilterMultipleHites1.txt";
        String fname2 = "testListFilesFilenameFilterMultipleHites2.data";
        String fname3 = "testListFilesFilenameFilterMultipleHites3.data";
        String fname4 = "testListFilesFilenameFilterMultipleHites4.data";
        createTestFile(fname1);
        createTestFile(fname2);
        createTestDir(fname3);
        createTestFile(fname4);
        FSFile f1 = conn.getFile("/");
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.endsWith(".data");
            }
        });
        Arrays.sort(files);
        assertTrue("Expecting 3 entries but got " + files.length, files.length == 3);
        assertTrue("Expecting " + fname2 + " but got " + files[0], files[0].equals(conn.getFile(fname2)));
        assertTrue("Expecting " + fname3 + " but got " + files[1], files[1].equals(conn.getFile(fname3)));
        assertTrue("Expecting " + fname4 + " but got " + files[2], files[2].equals(conn.getFile(fname4)));
    }

    @Test
    public void testListFilesFilnameFilterMultiLevelNoHits() throws IOException {
        String fname1 = "txt.testListFilesFilnameFilterMultiLevelNoHits1";
        String fname2 = "data.testListFilesFilnameFilterMultiLevelNoHits2";
        String fname3 = "data.testListFilesFilnameFilterMultiLevelNoHits3";
        String fname4 = "data.testListFilesFilnameFilterMultiLevelNoHits4";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        String mname3 = multiLevel.concat("/").concat(fname3);
        String mname4 = multiLevel.concat("/").concat(fname4);
        createTestFile(mname1);
        createTestFile(mname2);
        createTestFile(mname3);
        createTestFile(mname4);
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.startsWith("csv.");
            }
        });
        assertTrue("Expecting 0 entries but got " + files.length, files.length == 0);
    }

    @Test
    public void testListFilesFilnameFilterMultiLevelMultipleHits() throws IOException {
        String fname1 = "txt.testListFilesFilnameFilterMultiLevelMultipleHits1";
        String fname2 = "data.testListFilesFilnameFilterMultiLevelMultipleHits2";
        String fname3 = "data.testListFilesFilnameFilterMultiLevelMultipleHits3";
        String fname4 = "data.testListFilesFilnameFilterMultiLevelMultipleHits4";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        String mname3 = multiLevel.concat("/").concat(fname3);
        String mname4 = multiLevel.concat("/").concat(fname4);
        createTestFile(mname1);
        createTestFile(mname2);
        createTestFile(mname3);
        createTestFile(mname4);
        FSFile f1 = conn.getFile(multiLevel);
        assertNotNull("f1 is null", f1);
        FSFile[] files = f1.listFiles(new FilenameFilter() {

            public boolean accept(String filename) {
                return filename.startsWith("data.");
            }
        });
        assertTrue("Expecting 3 entries but got " + files.length, files.length == 3);
        assertTrue("Expecting " + mname2 + " but got " + files[0], files[0].equals(conn.getFile(mname2)));
        assertTrue("Expecting " + mname3 + " but got " + files[1], files[1].equals(conn.getFile(mname3)));
        assertTrue("Expecting " + mname4 + " but got " + files[2], files[2].equals(conn.getFile(mname4)));
    }

    @Test
    public void testMkDirSuccess() throws CreateException {
        String fname = "testMkDirSuccess";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.mkDir();
        File file = new File(rootDirname, fname);
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
    }

    @Test(expected = CreateException.class)
    public void testMkDirFailure() throws IOException {
        String fname = ">";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.mkDir();
    }

    @Test
    public void testMkDirMultiLevelSuccess() throws CreateException {
        String fname = "testMkDirMultiLevelSuccess";
        String mname = multiLevel.concat("/").concat(fname);
        FSFile f1 = conn.getFile(mname);
        assertNotNull("f1 is null", f1);
        f1.mkDir();
        File file = new File(rootDirname, mname);
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
    }

    @Test(expected = CreateException.class)
    public void testMkDirMultiLevelFailure() throws CreateException {
        String fname = ">";
        String mname = multiLevel.concat("/").concat(fname);
        FSFile f1 = conn.getFile(mname);
        assertNotNull("f1 is null", f1);
        f1.mkDir();
    }

    @Test
    public void testMkDirsSuccess() throws CreateException {
        String fname = "testMkDirsSuccess/testMkDirsSuccess";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.mkDirs();
        File file = new File(rootDirname, fname);
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
    }

    @Test(expected = CreateException.class)
    public void testMkDirsFailure() throws CreateException {
        String fname = "testMkDirsFailure/>";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.mkDirs();
    }

    @Test
    public void testMkDirsMultiLevelSuccess() throws IOException {
        String dname = "testMkDirsMultiLevelSuccess";
        String fname = "testMkDirsMultiLevelSuccess/testMkDirsMultiLevelSuccess";
        createTestDir(dname);
        FSFile f1 = conn.getFile(dname);
        assertNotNull("f1 is null", f1);
        f1 = conn.getFile(f1, fname);
        f1.mkDirs();
        File file = new File(rootDirname, dname);
        file = new File(file, fname);
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
    }

    @Test(expected = CreateException.class)
    public void testMkDirsMultiLevelFailure() throws IOException {
        String dname = "testMkDirsMultiLevelFailure";
        String fname = "testMkDirsMultiLevelFailure/>";
        createTestDir(dname);
        FSFile f1 = conn.getFile(dname);
        assertNotNull("f1 is null", f1);
        f1 = conn.getFile(f1, fname);
        f1.mkDirs();
    }

    @Test
    public void testRenameToSuccess() throws IOException, RenameException {
        String fname1 = "testRenameToSuccess1.txt";
        String fname2 = "testRenameToSuccess2.txt";
        createTestFile(fname1);
        FSFile f1 = conn.getFile(fname1);
        assertNotNull("f1 is null", f1);
        FSFile f2 = conn.getFile(fname2);
        assertNotNull("f2 is null", f2);
        f1.renameTo(f2);
        File file1 = new File(rootDirname, fname1);
        File file2 = new File(rootDirname, fname2);
        assertFalse(file1.exists());
        assertTrue(file2.exists());
    }

    @Test(expected = RenameException.class)
    public void testRenameToFailure() throws IOException, RenameException {
        String fname1 = "testRenameToFailure1.txt";
        String fname2 = ">.txt";
        createTestFile(fname1);
        FSFile f1 = conn.getFile(fname1);
        assertNotNull("f1 is null", f1);
        FSFile f2 = conn.getFile(fname2);
        assertNotNull("f2 is null", f2);
        RenameException e = null;
        try {
            f1.renameTo(f2);
        } catch (RenameException re) {
            e = re;
        }
        File file1 = new File(rootDirname, fname1);
        File file2 = new File(rootDirname, fname2);
        assertTrue(file1.exists());
        assertFalse(file2.exists());
        if (e != null) throw e;
    }

    @Test
    public void testRenameToMultiLevelSuccess() throws IOException, RenameException {
        String fname1 = "testRenameToMultiLevelSuccess1.txt";
        String fname2 = "testRenameToMultiLevelSuccess2.txt";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        createTestFile(mname1);
        FSFile mult = conn.getFile(multiLevel);
        FSFile f1 = conn.getFile(mult, fname1);
        assertNotNull("f1 is null", f1);
        FSFile f2 = conn.getFile(mult, fname2);
        assertNotNull("f2 is null", f2);
        f1.renameTo(f2);
        File file1 = new File(rootDirname, mname1);
        File file2 = new File(rootDirname, mname2);
        assertFalse(file1.exists());
        assertTrue(file2.exists());
    }

    @Test(expected = RenameException.class)
    public void testRenameToMultiLevelFailure() throws IOException, RenameException {
        String fname1 = "testRenameToMultiLevelFailure1.txt";
        String fname2 = ">.txt";
        String mname1 = multiLevel.concat("/").concat(fname1);
        String mname2 = multiLevel.concat("/").concat(fname2);
        createTestFile(mname1);
        FSFile mult = conn.getFile(multiLevel);
        FSFile f1 = conn.getFile(mult, fname1);
        assertNotNull("f1 is null", f1);
        FSFile f2 = conn.getFile(mult, fname2);
        assertNotNull("f2 is null", f2);
        RenameException e = null;
        try {
            f1.renameTo(f2);
        } catch (RenameException re) {
            e = re;
        }
        File file1 = new File(rootDirname, mname1);
        File file2 = new File(rootDirname, mname2);
        assertTrue(file1.exists());
        assertFalse(file2.exists());
        if (e != null) throw e;
    }

    @Test
    public void testSetLastModified() throws IOException, SetException {
        String fname = "testSetLastModified.txt";
        long time = System.currentTimeMillis() - 60000;
        createTestFile(fname);
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        f1.setLastModified(time);
        File file = new File(rootDirname, fname);
        long ttime = file.lastModified();
        assertEquals(time, ttime);
    }

    @Ignore
    @Test
    public void testSetReadOnly() {
        fail("Don't know how to test this");
    }

    @Test
    public void testToString() {
        String fname = "testToString.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
        assertTrue(f1.toString().equals("/".concat(fname)));
    }

    @Test
    public void testToStringMultiLevel() {
        String fname = "/testToString.txt";
        String mname = multiLevel.concat(fname);
        FSFile f1 = conn.getFile(mname);
        assertNotNull("f1 is null", f1);
        assertTrue("Expecting " + mname + " but got " + f1.toString(), f1.toString().equals(mname));
    }

    @Ignore
    @Test
    public void testGetFile() {
        String fname = "testGetFile.txt";
        FSFile f1 = conn.getFile(fname);
        assertNotNull("f1 is null", f1);
    }
}
