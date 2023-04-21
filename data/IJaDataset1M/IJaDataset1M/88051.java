package junit.extensions.repo.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipInputStream;
import junit.extensions.repo.TempFileBuilder;

public class TempFileBuilderTest extends TestCase {

    public TempFileBuilderTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TempFileBuilderTest.class);
    }

    public void testCreateTempDirectory() throws IOException {
        File dir = TempFileBuilder.createTempDirectory();
        assertNotNull(dir);
        assertEquals(true, dir.exists());
        assertEquals(true, dir.isDirectory());
    }

    public void testBuildFromInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.zip");
        assertNotNull(in);
        File file = TempFileBuilder.buildFromInputStream("foo.zip", in);
        assertEquals("foo.zip", file.getName());
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertEquals(988, file.length());
    }

    public void testBuildFromZipInputStream() throws IOException {
        ZipInputStream in = new ZipInputStream(getClass().getResourceAsStream("test.zip"));
        assertNotNull(in);
        File dir = TempFileBuilder.buildFromZipInputStream(in);
        assertNotNull(dir);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
        File[] children1 = dir.listFiles();
        assertNotNull(children1);
        assertEquals(2, children1.length);
        Arrays.sort(children1, new FileComparator());
        assertEquals("New Folder", children1[0].getName());
        assertEquals("a.txt", children1[1].getName());
        assertTrue(children1[0].isDirectory());
        assertTrue(children1[1].isFile());
        assertEquals(0, children1[1].length());
        File[] children2 = children1[0].listFiles();
        assertNotNull(children2);
        assertEquals(4, children2.length);
        Arrays.sort(children2, new FileComparator());
        assertEquals("Empty", children2[0].getName());
        assertEquals("xxx", children2[1].getName());
        assertEquals("New Text Document.txt", children2[2].getName());
        assertEquals("test.txt", children2[3].getName());
        assertTrue(children2[0].isDirectory());
        assertTrue(children2[1].isDirectory());
        assertTrue(children2[2].isFile());
        assertTrue(children2[3].isFile());
        assertEquals(86, children2[2].length());
        assertEquals(16, children2[3].length());
        assertNotNull(children2[0].listFiles());
        assertEquals(0, children2[0].listFiles().length);
        File[] children3 = children2[1].listFiles();
        assertNotNull(children3);
        assertEquals(1, children3.length);
        assertEquals("test", children3[0].getName());
        assertTrue(children3[0].isDirectory());
        File[] children4 = children3[0].listFiles();
        assertNotNull(children4);
        assertEquals(1, children4.length);
        assertEquals("foo.txt", children4[0].getName());
        assertTrue(children4[0].isFile());
        assertEquals(3, children4[0].length());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException x) {
        }
    }

    private static class FileComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            File f1 = (File) o1;
            File f2 = (File) o2;
            if (f1.isDirectory() && f2.isFile()) {
                return -1;
            } else if (f1.isFile() && f2.isDirectory()) {
                return 1;
            } else {
                return f1.getName().compareTo(f2.getName());
            }
        }
    }
}
