package org.mandiwala;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mandiwala.subtest.JunitTestSuite;
import org.mandiwala.utils.StreamUtils;
import org.mandiwala.utils.ZipFileComparator;

/**
 * The Class JunitTestSuiteIT.
 */
public class JunitTestSuiteIT {

    private static final Log LOG = LogFactory.getLog("MANDIWALA");

    private static final String DATE_COMMENT_IN_PROPERTIES_PATTERN = "#\\w\\w\\w \\w\\w\\w \\d\\d \\d\\d:\\d\\d:\\d\\d .*? \\d\\d\\d\\d";

    private static final String DATE_IN_REPORT_PATTERN = "\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d";

    private static final String GET_NEW_BROWSER_SESSION = "getNewBrowserSession\\([^)]+\\)\\s+\\-&gt;\\s+[a-zA-Z0-9]+";

    private static final String SOURCE_LINE_IN_STACK_TRACE = "\\([A-Z][a-zA-Z0-9]+\\.java:\\d+\\)";

    private static final String TIME_IN_CSV_REPORT = "\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d;\\d+";

    /**
     * Execute test.
     */
    @BeforeClass
    public static void executeJunitTestSuite() {
        Result result = JUnitCore.runClasses(JunitTestSuite.class);
        for (Failure failure : result.getFailures()) {
            LOG.info("Got a failure while executing subtest", failure.getException());
        }
        Assert.assertTrue(result.wasSuccessful());
    }

    /**
     * Should generate detailed reports.
     * 
     * @throws ArchiverException
     *             the archiver exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldGenerateReports() throws ArchiverException, IOException {
        ZipArchiver archiver = new ZipArchiver();
        archiver.addDirectory(new File("target/mandiwala/org.mandiwala.subtest.JunitTestSuite/output/reports"));
        File generated = File.createTempFile("generatedReports", ".zip");
        File base = File.createTempFile("baseReports", ".zip");
        try {
            StreamUtils.copyAndCloseStream(getClass().getResourceAsStream("junitTestSuiteBaseReport.zip"), new FileOutputStream(base));
            archiver.setDestFile(generated);
            archiver.createArchive();
            ZipFileComparator zipFileComparator = new ZipFileComparator(Arrays.asList(DATE_COMMENT_IN_PROPERTIES_PATTERN, DATE_IN_REPORT_PATTERN, GET_NEW_BROWSER_SESSION, SOURCE_LINE_IN_STACK_TRACE, TIME_IN_CSV_REPORT), null, false, false);
            assertTrue(zipFileComparator.compare(new ZipFile(generated), new ZipFile(base)) == 0);
        } finally {
            generated.delete();
            base.delete();
        }
    }
}
