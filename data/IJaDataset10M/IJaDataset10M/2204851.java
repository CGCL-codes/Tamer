package jmud.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jmud.engine.core.JMudStatics;
import jmud.logging.LoggingUtil;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoggingTest {

    private static Logger LOGGER = Logger.getLogger(LoggingTest.class);

    private static final String TEST_LOG_MESSAGE = "Test Log Message";

    private static final String TEST_LOG_FILE_NAME = "testLog4j.log";

    private void clearLogFile() throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(TEST_LOG_FILE_NAME));
            bufferedWriter.write(new char[] {});
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    private void deleteLogFile() {
        File testLogFile = new File(TEST_LOG_FILE_NAME);
        System.out.println("Current directory: " + System.getProperty("user.dir"));
        System.out.println("Test log file name: " + testLogFile.getName());
        System.out.println("Test log file path: " + testLogFile.getAbsolutePath());
        System.out.println("Test log file exists: " + testLogFile.exists());
        System.out.println("Test log file writable: " + testLogFile.canWrite());
        if (!testLogFile.canWrite()) {
            throw new RuntimeException("Test log file could not be deleted, write protected: " + TEST_LOG_FILE_NAME);
        }
        if (!testLogFile.delete()) {
            throw new RuntimeException("Test log file not deleted");
        }
    }

    private List<String> readLogFileByLines() throws IOException {
        List<String> linesFromFile = new ArrayList<String>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(TEST_LOG_FILE_NAME));
            String lineFromFile;
            while ((lineFromFile = bufferedReader.readLine()) != null) {
                linesFromFile.add(lineFromFile);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return linesFromFile;
    }

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
        try {
            clearLogFile();
        } catch (IOException e) {
            System.out.println("Could not clear test log file: " + e.getMessage());
        }
    }

    @Test
    public void testDefaultConfigurationLogging() {
        LoggingUtil.resetConfiguration();
        LoggingUtil.configureLogging();
        List<String> logFileLines = new ArrayList<String>();
        LOGGER.info(TEST_LOG_MESSAGE);
        try {
            logFileLines = readLogFileByLines();
        } catch (IOException e) {
            Assert.fail("Failed to read log file: " + e.getMessage());
        }
        Assert.assertNotNull("Failed to read log file", logFileLines);
        Assert.assertEquals("Incorrect number of log file lines read; expected 1, read " + logFileLines.size(), 1, logFileLines.size());
        Assert.assertTrue("Expected text not found; expected " + TEST_LOG_MESSAGE, logFileLines.get(0).contains(TEST_LOG_MESSAGE));
    }

    @Test
    public void testFileConfigurationLogging() {
        LoggingUtil.resetConfiguration();
        LoggingUtil.configureLogging(JMudStatics.log4jConfigFile);
        List<String> logFileLines = new ArrayList<String>();
        LOGGER.info(TEST_LOG_MESSAGE);
        try {
            logFileLines = readLogFileByLines();
        } catch (IOException e) {
            Assert.fail("Failed to read log file: " + e.getMessage());
        }
        Assert.assertNotNull("Failed to read log file", logFileLines);
        Assert.assertEquals("Incorrect number of log file lines read; expected 1, read " + logFileLines.size(), 1, logFileLines.size());
        Assert.assertTrue("Expected text not found; expected " + TEST_LOG_MESSAGE, logFileLines.get(0).contains(TEST_LOG_MESSAGE));
    }
}
