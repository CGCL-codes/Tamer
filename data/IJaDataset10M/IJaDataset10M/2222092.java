package astcentric.structure.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class RealFileTest extends MockObjectTestCase {

    private static final String FILE_TYP = "txt";

    private static final String FILE_NAME = "file." + FILE_TYP;

    private static final String TMP_DIR = "temporaryTestDirectory";

    private static final File TMP = new File(TMP_DIR);

    private RealFile _root;

    @Override
    protected void setUp() throws Exception {
        _root = new RealFile(TMP);
        assertTrue(TMP.mkdir());
    }

    @Override
    protected void tearDown() throws Exception {
        delete(TMP);
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delete(child);
            }
        }
        assertTrue(file.delete());
    }

    public void testCreateDirectory() throws IOException {
        VirtualFile f = _root.createFile("folder", true);
        File file = new File(TMP, "folder");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        assertTrue(f.isDirectory());
        assertFalse(f.isReadOnly());
        assertEquals("folder", f.getName());
    }

    public void testCreateFile() throws IOException {
        VirtualFile f = _root.createFile(FILE_NAME, false);
        File file = new File(TMP, FILE_NAME);
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        assertFalse(f.isDirectory());
        assertFalse(f.isReadOnly());
        assertEquals(FILE_NAME, f.getName());
        assertEquals(file.lastModified(), f.getLastModifiedDate().getTime());
    }

    public void testCreateFileTwice() throws IOException {
        _root.createFile(FILE_NAME, false);
        try {
            _root.createFile(FILE_NAME, false);
            fail("IOException expected because the same file can't created twice.");
        } catch (IOException e) {
            assertTrue(e.toString(), e.getMessage().indexOf(FILE_NAME) >= 0);
        }
    }

    public void testCreateFileAsChildOfAPlainFile() throws IOException {
        VirtualFile plainFile = _root.createFile(FILE_NAME, false);
        try {
            plainFile.createFile("f", false);
        } catch (IOException e) {
            assertTrue(e.toString(), e.getMessage().indexOf(FILE_NAME) >= 0);
        }
    }

    public void testCreateAndWriteFile() throws IOException {
        VirtualFile f = _root.createFile(FILE_NAME, false);
        OutputStream outputStream = f.createOutputStream();
        outputStream.write(new byte[] { 42, 43, 44 });
        outputStream.close();
        Mock mock = mock(FileHandler.class);
        mock.expects(once()).method("handle").with(eq(f)).will(returnValue(true));
        _root.traverse((FileHandler) mock.proxy());
        checkData(new FileInputStream(new File(TMP, FILE_NAME)));
        checkData(f.createInputStream());
    }

    private void checkData(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[10];
        assertEquals(3, inputStream.read(bytes));
        assertEquals(42, bytes[0]);
        assertEquals(43, bytes[1]);
        assertEquals(44, bytes[2]);
        inputStream.close();
    }

    public void testGetFileType() throws IOException {
        assertEquals("", _root.getFileType());
        assertEquals(FILE_TYP, _root.createFile(FILE_NAME, false).getFileType());
    }

    public void testTraverse() throws IOException {
        VirtualFile file = _root.createFile("file", false);
        VirtualFile folder = _root.createFile("folder", true);
        Picker folderPicker = new Picker(true);
        _root.traverse(folderPicker);
        assertEquals(folder, folderPicker.pickedFile);
        Picker plainFilePicker = new Picker(false);
        _root.traverse(plainFilePicker);
        assertEquals(file, plainFilePicker.pickedFile);
        assertTrue(plainFilePicker.counter != folderPicker.counter);
    }

    private static final class Picker implements FileHandler {

        private final boolean _directory;

        private int counter;

        private VirtualFile pickedFile;

        public Picker(boolean directory) {
            _directory = directory;
        }

        public boolean handle(VirtualFile file) {
            counter++;
            boolean result = (file.isDirectory() == _directory);
            if (result) {
                pickedFile = file;
            }
            return result;
        }
    }
}
