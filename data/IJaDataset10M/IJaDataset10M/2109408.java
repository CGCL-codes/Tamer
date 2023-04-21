package fr.sonictools.jgrisbicatcleaner.tool;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import fr.sonictools.jgrisbicatcleaner.JgrisbicatcleanerSuite;

public class FileUtilsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCopyFile() throws IOException {
        String inputFilePath = ApplicationLogger.getLogger().getPathLogFile();
        String outputFilePath = "./output.test";
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        ApplicationLogger.getLogger().log(Level.SEVERE, "Log testCopyFile");
        FileUtils.copyFile(inputFile, outputFile);
        assertEquals(true, FileUtils.compareFiles(inputFile, outputFile));
    }

    @Test
    public void testIsStringExists() {
        String inputFilePath = ApplicationLogger.getLogger().getPathLogFile();
        File inputFile = new File(inputFilePath);
        String log = "Log testIsStringExists";
        ApplicationLogger.getLogger().log(Level.SEVERE, log);
        assertEquals(true, FileUtils.isStringExists(inputFile, log));
    }

    @Test
    public void testCompareFilesOK() {
        String inputFilePath = ApplicationLogger.getLogger().getPathLogFile();
        File inputFile = new File(inputFilePath);
        String log = "Log testIsStringExists";
        ApplicationLogger.getLogger().log(Level.SEVERE, log);
        assertEquals(true, FileUtils.compareFiles(inputFile, inputFile));
    }

    @Test
    public void testCompareFilesKO() {
        File file1 = new File(JgrisbicatcleanerSuite.outputExpectedFullUpdateGrisbiFilePath);
        File file2 = new File(JgrisbicatcleanerSuite.outputExpectedOnlyNotAssociatedOperationsGrisbiFilePath);
        assertEquals(false, FileUtils.compareFiles(file1, file2));
    }
}
