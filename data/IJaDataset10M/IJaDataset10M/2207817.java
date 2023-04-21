package org.pwsafe.lib.file;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pwsafe.lib.datastore.PwsEntryStore;
import org.pwsafe.lib.exception.InvalidPassphraseException;

public class PwsFileFactoryTest extends TestCase {

    private static final Log LOGGER = LogFactory.getLog(PwsFileFactoryTest.class);

    private static final String testV2Filename = "password_file_2.dat";

    private static final String PASSPHRASE = "THEFISH";

    public void testLoadFile() throws Exception {
        PwsFile theFile = PwsFileFactory.loadFile(testV2Filename, new StringBuilder(PASSPHRASE));
        assertNotNull(theFile);
        assertTrue(theFile instanceof PwsFileV2);
        assertEquals(1, theFile.getRecordCount());
        PwsEntryStore theStore = PwsFileFactory.getStore(theFile);
        assertNotNull(theStore);
        assertEquals(1, theStore.getSparseEntries().size());
        try {
            theFile = PwsFileFactory.loadFile(testV2Filename, new StringBuilder("wrong passphrase"));
            fail("Wrong passphrase should lead to an InvalidPassphraseException");
        } catch (InvalidPassphraseException e) {
            LOGGER.info(e.toString());
        }
    }

    public void testReadOnly() throws Exception {
        PwsFile pwsFile = PwsFileFactory.loadFile(testV2Filename, new StringBuilder(PASSPHRASE));
        pwsFile.setReadOnly(true);
        try {
            pwsFile.save();
            fail("save on Read only file without exception");
        } catch (IOException anEx) {
        }
    }

    public void testConcurrentMod() throws Exception {
        PwsFile pwsFile = PwsFileFactory.loadFile(testV2Filename, new StringBuilder(PASSPHRASE));
        File file = new File(testV2Filename);
        file.setLastModified(System.currentTimeMillis() + 1000);
        pwsFile.setModified();
        try {
            pwsFile.save();
            fail("save concurrently modified file without exception");
        } catch (ConcurrentModificationException e) {
        }
        file.setLastModified(System.currentTimeMillis() + 2000);
        pwsFile.setModified();
        try {
            pwsFile.save();
            fail("save concurrently modified file without exception");
        } catch (ConcurrentModificationException e) {
        }
    }

    public void testNewFile() {
        PwsFile theFile = PwsFileFactory.newFile();
        assertNotNull(theFile);
        assertEquals(PwsFileV3.class, theFile.getClass());
    }
}
