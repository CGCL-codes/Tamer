package net.sf.agentopia.util;

import java.io.IOException;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the byte file.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 2008
 */
public class ByteFileTest {

    /**
     * @return A suite with this class.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ByteFileTest.class);
    }

    /**
     * @throws Exception If something failed.
     */
    @Test
    public void testByteFileLoading() throws Exception {
        final int JAVA_SRC_FILE_LENGTH = 8126;
        ByteFile byteFile = new ByteFile("src/net/sf/agentopia", "util/ByteFile.java", null);
        byteFile.load();
        byte[] fileBytes = byteFile.getFileBytes();
        assertNotNull(fileBytes);
        assertEquals(JAVA_SRC_FILE_LENGTH, fileBytes.length);
        assertEquals(0, Logger.getLogger().getWarningCount());
    }

    /**
     * @throws Exception If something failed.
     */
    @Test
    public void testByteFileSaving() throws Exception {
        final String TEST_STRING = "Hello, this is a ByteFile test";
        ByteFile byteFile = new ByteFile();
        byteFile.setFileBytes(new String(TEST_STRING).getBytes());
        try {
            byteFile.save();
            assertTrue(false);
        } catch (IOException exc) {
        }
        byteFile.setFileName("bin/Test.txt");
        byteFile.save();
        assertTrue(byteFile.getFile().exists());
        byteFile.load();
        String loadedString = new String(byteFile.getFileBytes());
        assertEquals(TEST_STRING, loadedString);
        byteFile.deleteFromDisk();
        assertFalse(byteFile.getFile().exists());
        assertEquals(0, Logger.getLogger().getWarningCount());
    }
}
