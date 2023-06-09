package org.apache.roller.presentation;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.roller.RollerException;
import org.apache.roller.RollerTestBase;
import org.apache.roller.business.FileManagerTest;
import org.apache.roller.pojos.RollerConfigData;
import org.apache.roller.presentation.velocity.ExportRssTest;
import com.mockrunner.mock.web.MockServletContext;

/**
 * TODO: revisit this class once Atom 1.0 support comes to Rome
 * @author lance.lavandowska
 */
public class ArchiveParserTest extends RollerTestBase {

    MockServletContext mContext = null;

    RollerConfigData rollerConfig = null;

    private static String FILE_LOCATION = "./build/junk/";

    private static String RSS_ARCHIVE = "export-test.rss.xml";

    private static String ATOM_ARCHIVE = "export-test.atom.xml";

    public void _testAtomParsing() throws RollerException {
        File archiveFile = new File(FILE_LOCATION + mWebsite.getHandle() + "/" + ATOM_ARCHIVE);
        parseFile(archiveFile);
    }

    public void _testRssParsing() throws RollerException {
        File archiveFile = new File(FILE_LOCATION + mWebsite.getHandle() + "/" + RSS_ARCHIVE);
        parseFile(archiveFile);
    }

    /**
     * @param archiveFile
     * @throws RollerException
     */
    private void parseFile(File archiveFile) throws RollerException {
        if (archiveFile.exists()) {
            String result = null;
            getRoller().flush();
            assertTrue(result.length() > 0);
            System.out.println(result);
        } else {
            ExportRssTest exportTest = new ExportRssTest();
            try {
                exportTest.setUp();
                exportTest.testExportRecent();
                exportTest.tearDown();
                parseFile(archiveFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!archiveFile.exists()) {
                fail(archiveFile.getAbsolutePath() + " does not exist.");
            }
        }
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(ArchiveParserTest.class);
    }
}
