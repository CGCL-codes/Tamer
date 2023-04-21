package net.sourceforge.pebble.domain;

import junit.framework.TestCase;
import java.util.Date;

/**
 * Tests for the FileMetaData class.
 *
 * @author    Simon Brown
 */
public class FileMetaDataTest extends TestCase {

    private FileMetaData file;

    public void testConstructionOfRootFile() {
        file = new FileMetaData(null, null);
        assertEquals("/", file.getPath());
        assertEquals("", file.getName());
        file = new FileMetaData(null, "");
        assertEquals("/", file.getPath());
        assertEquals("", file.getName());
        file = new FileMetaData(null, "/");
        assertEquals("/", file.getPath());
        assertEquals("", file.getName());
    }

    public void testConstructionOfDirectory() {
        file = new FileMetaData(null, "/directory");
        assertEquals("/", file.getPath());
        assertEquals("directory", file.getName());
        file = new FileMetaData(null, "/directory/");
        assertEquals("/", file.getPath());
        assertEquals("directory", file.getName());
    }

    public void testConstructionOfSubDirectory() {
        file = new FileMetaData(null, "/directory/subdirectory");
        assertEquals("/directory", file.getPath());
        assertEquals("subdirectory", file.getName());
        file = new FileMetaData(null, "/directory/subdirectory/");
        assertEquals("/directory", file.getPath());
        assertEquals("subdirectory", file.getName());
    }

    public void testDirectoriesAreNotEditable() {
        file = new FileMetaData(null, "/directory");
        assertFalse(file.isEditable());
    }

    public void testTextFilesAreEditable() {
        file = new FileMetaData(null, "/somefile.txt");
        assertTrue(file.isEditable());
        file = new FileMetaData(null, "/somefile.jsp");
        assertTrue(file.isEditable());
        file = new FileMetaData(null, "/somefile.jspf");
        assertTrue(file.isEditable());
        file = new FileMetaData(null, "/somefile.html");
        assertTrue(file.isEditable());
        file = new FileMetaData(null, "/somefile.htm");
        assertTrue(file.isEditable());
        file = new FileMetaData(null, "/somefile.css");
        assertTrue(file.isEditable());
        file = new FileMetaData(null, "/somefile.xml");
        assertTrue(file.isEditable());
    }

    public void testBinaryFilesAreNotEditable() {
        file = new FileMetaData(null, "/somefile.gif");
        assertFalse(file.isEditable());
        file = new FileMetaData(null, "/somefile.jpg");
        assertFalse(file.isEditable());
        file = new FileMetaData(null, "/somefile.png");
        assertFalse(file.isEditable());
        file = new FileMetaData(null, "/somefile.zip");
        assertFalse(file.isEditable());
    }

    public void testAllDirectoriesAreNotEditable() {
        file = new FileMetaData(null, "/somedirectory");
        file.setDirectory(true);
        assertFalse(file.isEditable());
    }

    public void testName() {
        file = new FileMetaData(null, "/");
        file.setName("somename");
        assertEquals("somename", file.getName());
    }

    public void testLastModifiedDate() {
        Date date = new Date();
        file = new FileMetaData(null, "/");
        file.setLastModified(date);
        assertEquals(date, file.getLastModified());
    }

    public void testDirectory() {
        file = new FileMetaData(null, "/");
        file.setDirectory(true);
        assertTrue(file.isDirectory());
        file.setDirectory(false);
        assertFalse(file.isDirectory());
    }

    public void testAbsolutePath() {
        file = new FileMetaData(null, "/somedirectory/somefile.gif");
        assertEquals("/somedirectory/somefile.gif", file.getAbsolutePath());
    }

    public void testUrl() {
        file = new FileMetaData(null, "/somefile.txt");
        file.setType(FileMetaData.BLOG_IMAGE);
        assertEquals("images/somefile.txt", file.getUrl());
        file.setType(FileMetaData.BLOG_FILE);
        assertEquals("files/somefile.txt", file.getUrl());
        file.setType(FileMetaData.THEME_FILE);
        assertEquals("theme/somefile.txt", file.getUrl());
    }

    public void testSize() {
        file = new FileMetaData(null, "/");
        file.setSize(123456789);
        assertEquals(123456789, file.getSize());
        assertEquals(123456789 / 1024, file.getSizeInKB(), 1.0);
    }
}
