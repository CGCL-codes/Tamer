package org.dataminx.dts.batch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import org.dataminx.dts.common.DtsConstants;
import org.proposal.dmi.schemas.dts.x2010.dmiCommon.DataCopyActivityDocument;

/**
 * Some stuff that is usefull in all tests.
 * @author David Meredith
 */
public class TestUtils {

    /**
     * Test that the test environment has been set up ok, inc. setting of the
     * -Ddataminx.dir, jobsteps dir, testfiles.
     * @throws IllegalStateException if the test environment is not setup ok.
     */
    protected static void assertTestEnvironmentOk() throws IllegalStateException {
        try {
            if (!System.getProperties().containsKey(DtsConstants.DATAMINX_CONFIGURATION_KEY)) {
                throw new IllegalStateException("Please specify full path of your dataminx.dir using: " + "'mvn -Ddataminx.dir=/full/path/to/.dataminx.dir test'");
            }
            File configdir = new File(System.getProperty(DtsConstants.DATAMINX_CONFIGURATION_KEY));
            if (!configdir.exists() || !configdir.isDirectory() || !configdir.canWrite()) {
                throw new IllegalStateException(String.format(" Invalid DataMINX configuration folder: '%s'.  Check your configuration", configdir.getAbsolutePath()));
            }
            File jobStepsDir = new File(configdir, "jobsteps");
            if (!jobStepsDir.exists() || !jobStepsDir.isDirectory() || !jobStepsDir.canWrite()) {
                throw new IllegalStateException(String.format(" Invalid DataMINX jobStepsDir folder: '%s'.  Check your configuration", jobStepsDir.getAbsolutePath()));
            }
            File testFilesDir = new File(System.getProperty("user.home"), "testfiles");
            if (!testFilesDir.exists() || !testFilesDir.isDirectory() || !testFilesDir.canRead()) {
                throw new IllegalStateException(String.format(" Invalid testfiles folder: '%s'.  Please unpack the 'testfiles.zip' resource in your home directory to run tests", testFilesDir.getAbsolutePath()));
            }
        } catch (IllegalStateException ex) {
            throw new IllegalStateException("=================================================================\n" + ex.getMessage() + "\n=================================================================");
        }
    }

    /**
     * Return the jobDefDoc from the given file. Perform required filtering of test docs. 
     * @param f
     * @return
     * @throws Exception
     */
    protected static DataCopyActivityDocument getTestDataCopyActivityDocument(final File f) throws Exception {
        String docString = TestUtils.readFileAsString(f.getAbsolutePath());
        String homeDir = System.getProperty("user.home").replaceAll("\\\\", "/");
        docString = docString.replaceAll("@home.dir.replacement@", homeDir);
        final DataCopyActivityDocument dtsJob = DataCopyActivityDocument.Factory.parse(docString);
        return dtsJob;
    }

    /**
     * Return the file contents as a string
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    private static String readFileAsString(String filePath) throws java.io.IOException {
        byte[] buffer = new byte[(int) new File(filePath).length()];
        BufferedInputStream f = null;
        try {
            f = new BufferedInputStream(new FileInputStream(filePath));
            f.read(buffer);
        } finally {
            if (f != null) {
                f.close();
            }
        }
        return new String(buffer);
    }
}
