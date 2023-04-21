package com.phloc.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.resource.ClassPathResource;

/**
 * Test class for class {@link SimpleFileIO}.
 * 
 * @author philip
 */
public final class SimpleFileIOTest {

    @Test
    public void testReadFileBytes() {
        final String s = "äöü text";
        final File f = new File("dummy.txt");
        assertTrue(SimpleFileIO.writeFile(f, s.getBytes()).isSuccess());
        try {
            final byte[] aBytes = SimpleFileIO.readFileBytes(f);
            assertNotNull(aBytes);
            assertTrue(Arrays.equals(aBytes, s.getBytes()));
            assertNull(SimpleFileIO.readFileBytes(null));
            assertNull(SimpleFileIO.readFileBytes(new File("non existing file")));
        } finally {
            FileOperations.deleteFile(f);
        }
    }

    @Test
    public void testReaFileLines() {
        assertNull(SimpleFileIO.readFileLines(null, CCharset.CHARSET_ISO_8859_1));
        assertNull(SimpleFileIO.readFileLines(new File("ha ha said the clown"), CCharset.CHARSET_ISO_8859_1));
        final File aFile = ClassPathResource.getAsFile("streamutils-lines");
        assertTrue(aFile.exists());
        final List<String> lines = SimpleFileIO.readFileLines(aFile, CCharset.CHARSET_ISO_8859_1);
        assertEquals(10, lines.size());
    }

    @Test
    public void testReadFileAsString() {
        final File aFile = new File("umlaut-tests.txt");
        final String s = "defäöüabc";
        assertEquals("Source encoding of the Java file must be UTF-8!", 9, s.length());
        assertNull(SimpleFileIO.readFileAsString(null, CCharset.CHARSET_ISO_8859_1));
        assertTrue(SimpleFileIO.writeFile(aFile, s, CCharset.CHARSET_UTF_8).isSuccess());
        try {
            final String t = SimpleFileIO.readFileAsString(aFile, CCharset.CHARSET_UTF_8);
            assertEquals(s, t);
        } finally {
            assertTrue(FileOperations.deleteFile(aFile).isSuccess());
        }
        assertTrue(SimpleFileIO.writeFile(aFile, s, CCharset.CHARSET_ISO_8859_1).isSuccess());
        try {
            final String t = SimpleFileIO.readFileAsString(aFile, CCharset.CHARSET_ISO_8859_1);
            assertEquals(s, t);
        } finally {
            assertTrue(FileOperations.deleteFile(aFile).isSuccess());
        }
    }

    @Test
    public void testWriteFile() {
        final File aFile = new File("hahatwf.txt");
        try {
            assertTrue(SimpleFileIO.writeFile(aFile, new byte[10]).isSuccess());
            assertTrue(SimpleFileIO.writeFile(aFile, new byte[10], 0, 5).isSuccess());
            assertTrue(SimpleFileIO.writeFile(aFile, "abc", CCharset.CHARSET_ISO_8859_1).isSuccess());
        } finally {
            FileOperations.deleteFile(aFile);
        }
        try {
            SimpleFileIO.writeFile(null, new byte[10]);
            fail();
        } catch (final NullPointerException ex) {
        }
        try {
            SimpleFileIO.writeFile(null, new byte[10], 0, 5);
            fail();
        } catch (final NullPointerException ex) {
        }
        try {
            SimpleFileIO.writeFile(null, "abc", CCharset.CHARSET_ISO_8859_1);
            fail();
        } catch (final NullPointerException ex) {
        }
    }
}
