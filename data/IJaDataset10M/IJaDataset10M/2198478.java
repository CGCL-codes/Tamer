package supersync.tree2;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.Charset;
import java.io.File;
import java.text.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/** This class tests the TreeFile class.
 *
 * @author Brandon Drake
 */
public class TreeFileTest {

    /** Get the test data directory that stores the test data.
     */
    public static File getTestDataDirectory() {
        return new File("Test Data");
    }

    public TreeFileTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of createNew method, of class TreeFile.
     */
    @Test
    public void testCreateNew() throws Exception {
        File tempFile = File.createTempFile("test File", ".dat");
        File tempFolder = new File(getTestDataDirectory(), "Test Folder XX");
        FileLeaf root = new FileLeaf(tempFolder);
        FileTreeFile tree = new FileTreeFile();
        TreeWritter treeWritter = new TreeWritter();
        treeWritter.createTree(tree, root, tempFile);
        tree.close();
        tree = new FileTreeFile();
        tree.open(tempFile);
        TreeFileLeaf[] leafs = tree.getRoot().getChildren();
        int filesFound = 0;
        for (TreeFileLeaf leaf : leafs) {
            FileLeaf fileLeaf = (FileLeaf) leaf.getLeaf();
            String fileName = fileLeaf.file.getName();
            if (fileName.equals("Folder 2")) {
                filesFound++;
                TreeFileLeaf[] leafs2 = leaf.getChildren();
                for (TreeFileLeaf leaf2 : leafs2) {
                    fileLeaf = (FileLeaf) leaf2.getLeaf();
                    fileName = fileLeaf.file.getName();
                    if (fileName.equals("Folder 3")) {
                        filesFound++;
                        TreeFileLeaf[] leafs3 = leaf2.getChildren();
                        for (TreeFileLeaf leaf3 : leafs3) {
                            fileLeaf = (FileLeaf) leaf3.getLeaf();
                            fileName = fileLeaf.file.getName();
                            if (fileName.equals("Test XX.rtf")) {
                                filesFound++;
                            } else {
                                fail("Unexpected file found in folder");
                            }
                        }
                    } else {
                        fail("Unexpected file found in folder");
                    }
                }
            } else if (fileName.equals("Test Description.rtf")) {
                filesFound++;
            } else {
                fail("Unexpected file found in folder");
            }
        }
        if (filesFound != 4) {
            fail("Some files were not found in the tree leaf file.");
        }
        tree.close();
        tempFile.delete();
    }

    @Test
    public void testFileInfo() throws Exception {
        File tempFile = File.createTempFile("test File", ".dat");
        File tempFolder = new File(getTestDataDirectory(), "Test Folder XX");
        FileLeaf root = new FileLeaf(tempFolder);
        FileTreeFile tree = new FileTreeFile();
        TreeWritter treeWritter = new TreeWritter();
        treeWritter.createTree(tree, root, tempFile);
        byte[] actualFileInfo = new byte[] { 0, 27, 127, -37, 13, 96, 13, 26, 93 };
        tree.setFileInfo(actualFileInfo);
        tree.close();
        tree = new FileTreeFile();
        tree.open(tempFile);
        if (false == Arrays.equals(actualFileInfo, tree.getFileInfo())) {
            fail("Incorrect file info.");
        }
        actualFileInfo = new byte[] { 10, 27, 17, -37, -13, 96, 113, 26, 93, 28, 46, 29 };
        tree.setFileInfo(actualFileInfo);
        if (false == Arrays.equals(actualFileInfo, tree.getFileInfo())) {
            fail("Incorrect file info.");
        }
        tree.close();
        tree = new FileTreeFile();
        tree.open(tempFile);
        if (false == Arrays.equals(actualFileInfo, tree.getFileInfo())) {
            fail("Incorrect file info.");
        }
        tree.close();
    }

    /** This is a tree file for recording a file structure.  This is a simple implementation used for testing purposes.
     */
    protected class FileTreeFile extends TreeFile {

        @Override
        protected EditableLeaf getNewLeaf(byte[] l_leafValue, TreeFileLeaf l_treeFileLeaf) throws ParseException {
            return new FileLeaf(new File(new String(l_leafValue, FileLeaf.leafCharset)));
        }
    }

    /** This class is a simple implementation of a writable leaf that can be used for testing purposes.  The leafs are files.
     */
    protected static class FileLeaf implements WritableLeaf {

        File file = null;

        public static Charset leafCharset = Charset.forName("UTF-16");

        public FileLeaf(File l_file) {
            this.file = l_file;
        }

        @Override
        public byte[] getLeafValue() {
            return file.getAbsolutePath().getBytes(leafCharset);
        }

        @Override
        public boolean isBranch() {
            return file.isDirectory();
        }

        @Override
        public WritableLeaf[] getChildren() throws IOException {
            File[] childFiles = file.listFiles();
            ArrayList<File> filteredFiles = new ArrayList<File>();
            for (File child : childFiles) {
                if (false == child.getName().equals(".DS_Store") && false == child.getName().equals(".svn") && false == child.getName().equals("Desktop.ini")) {
                    filteredFiles.add(child);
                }
            }
            FileLeaf[] children = new FileLeaf[filteredFiles.size()];
            for (int fileIndex = 0; fileIndex < filteredFiles.size(); fileIndex++) {
                children[fileIndex] = new FileLeaf(filteredFiles.get(fileIndex));
            }
            return children;
        }
    }
}
