package org.pixory.pxmodel;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.util.Locale;
import java.util.Map;

public class PXAlbumArchiveTestCase extends TestCase {

    private static final Log LOG = LogFactory.getLog(PXAlbumArchiveTestCase.class);

    public PXAlbumArchiveTestCase(String name) {
        super(name);
    }

    public void testDescription() throws Exception {
        File testAlbumDirectory = PXModelTestSuite.getTestAlbumDirectory();
        PXAlbumContent albumContent = PXAlbumContent.albumAtPath(testAlbumDirectory);
        PXAlbumArchive archive = new PXAlbumArchive(albumContent);
        Map archiveDescription = archive.getDescriptionMap(Locale.getDefault());
        assertNotNull(archiveDescription);
        LOG.debug("archiveDescription: " + archiveDescription);
        assertEquals("4", archiveDescription.get("# images"));
        assertEquals("'test.album.zip'", archiveDescription.get("archive name"));
        String localizedKey = archive.getLocalizedDescriptionKey(PXAlbumArchive.DESCRIPTION_ARCHIVE_NAME_KEY, Locale.getDefault());
        assertEquals("archive name", localizedKey);
        assertEquals("'test.album.zip'", archive.getDescriptionValue(PXAlbumArchive.DESCRIPTION_ARCHIVE_NAME_KEY));
    }
}
