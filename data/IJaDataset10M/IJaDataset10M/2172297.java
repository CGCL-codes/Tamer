package org.webical.plugin.file;

import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.webical.plugin.file.FileUtils;
import org.webical.plugin.file.ZipFileExtractor;
import junit.framework.TestCase;

/**
 * Tests the zipfile extraction utility
 * @author ivo
 *
 */
public class ZipFileExtractorTest extends TestCase {

    private static final String TEST_NORMALFILE = "src/test/resources/org/webical/plugin/file/test.normalfile";

    private static final String TEST_ZIP = "src/test/resources/org/webical/plugin/file/test.zip";

    private static Log log = LogFactory.getLog(ZipFileExtractorTest.class);

    private static FileSystemResource workdir = new FileSystemResource("workingdirectory");

    /**
	 * Tests with a proper zipfile and working directory
	 * @throws IOException
	 */
    public void testProperZipfile() throws IOException {
        File fileToExtract = new FileSystemResource(TEST_ZIP).getFile();
        ZipFileExtractor.unpackZipFile(fileToExtract, workdir.getFile());
    }

    /**
	 * Tests with a inproper zipfile and proper working directory
	 */
    public void testOtherFile() {
        File fileToExtract = new FileSystemResource(TEST_NORMALFILE).getFile();
        try {
            ZipFileExtractor.unpackZipFile(fileToExtract, workdir.getFile());
            fail("Should have thrown an exception");
        } catch (Exception e) {
        }
    }

    /**
	 * Tests with a proper zipfile and inproper working directory
	 */
    public void testNonexistingExtractionDir() {
        File fileToExtract = new FileSystemResource(TEST_ZIP).getFile();
        try {
            ZipFileExtractor.unpackZipFile(fileToExtract, new File(workdir.getFile().getAbsolutePath(), "subdir"));
            fail("Should have thrown an exception");
        } catch (Exception e) {
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        cleanup();
    }

    /**
	 * Cleans the working directory
	 */
    private void cleanup() {
        log.debug("Cleaning up");
        FileUtils.cleanupDirectory(workdir.getFile());
    }
}
