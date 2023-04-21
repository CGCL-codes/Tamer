package eu.jbart.bunit.test.cli;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import eu.jbart.bunit.BackupFile;
import eu.jbart.bunit.FileBackupFile;

/**
 * @author Bart Frackiewicz <mail@jbart.eu>
 *
 */
public class FileBackupFileTest {

    private BackupFile backupFile;

    public static final String TEST_FILE = "src/test/data/License.txt";

    @Before
    public void setUp() {
        backupFile = new FileBackupFile(TEST_FILE);
    }

    @Test
    public void testSetup() {
        assertNotNull(backupFile);
    }

    @Test
    public void testLength() {
        assertEquals(628, backupFile.length());
    }

    @Test
    public void testLastModified() {
        assertTrue(backupFile.lastModified() != 0);
    }

    @Test
    public void testGetWorkingFile() {
        final File workingFile = backupFile.getWorkingFile();
        assertNotNull(workingFile);
        assertTrue(workingFile.getAbsolutePath().startsWith(System.getProperty("java.io.tmpdir")));
    }
}
