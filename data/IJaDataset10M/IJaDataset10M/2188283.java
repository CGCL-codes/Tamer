package org.grandtestauto.test;

import org.apache.commons.io.*;
import org.grandtestauto.*;
import java.io.*;

/**
 * @author Tim Lavers
 */
public class ResultsLoggerTest {

    private ResultsLogger resultsLogger;

    private File resultsFile;

    public boolean constructorTest() throws IOException {
        initWithResultsFile(true);
        String message1 = "message1";
        resultsLogger.log(message1, null);
        assert FileUtils.readFileToString(resultsFile).contains(message1);
        cleanup();
        initWithResultsFile(false);
        String message2 = "message2";
        resultsLogger.log(message2, null);
        assert FileUtils.readFileToString(resultsFile).contains(message2);
        assert !FileUtils.readFileToString(resultsFile).contains(message1);
        cleanup();
        initWithoutResultsFile(false);
        assert !resultsFile.exists();
        cleanup();
        return true;
    }

    public boolean logTest() throws Exception {
        initWithResultsFile(false);
        String message1 = "Hey diddle diddle";
        StringBuilder expected = new StringBuilder();
        resultsLogger.log(message1, null);
        expected.append(message1);
        expected.append(Helpers.NL);
        String message2 = "The cat and the fiddle";
        resultsLogger.log(message2, null);
        expected.append(message2);
        expected.append(Helpers.NL);
        String message3 = "The cow jumped over the moon";
        resultsLogger.log(message3, null);
        expected.append(message3);
        expected.append(Helpers.NL);
        assert FileUtils.readFileToString(resultsFile).equals(expected.toString());
        cleanup();
        initWithResultsFile(false);
        expected = new StringBuilder();
        resultsLogger.log(message1, new NullPointerException("NPE"));
        expected.append(message1);
        expected.append(Helpers.NL);
        resultsLogger.log(message2, new AssertionError("AE"));
        expected.append(message2);
        expected.append(Helpers.NL);
        resultsLogger.log(message3, new Throwable("T"));
        expected.append(message3);
        expected.append(Helpers.NL);
        String logged = FileUtils.readFileToString(resultsFile);
        int index1 = logged.indexOf(message1);
        int index2 = logged.indexOf("java.lang.NullPointerException: NPE");
        int index3 = logged.indexOf(message2);
        int index4 = logged.indexOf("java.lang.AssertionError: AE");
        int index5 = logged.indexOf(message3);
        int index6 = logged.indexOf("java.lang.Throwable: T");
        assert index1 >= 0;
        assert index2 > index1;
        assert index3 > index2;
        assert index4 > index3;
        assert index5 > index4;
        assert index6 > index5;
        cleanup();
        return true;
    }

    public boolean closeLoggerTest() throws IOException {
        initWithResultsFile(false);
        resultsLogger.log("dsfsf", null);
        assert resultsFile.exists();
        assert !resultsFile.delete();
        resultsLogger.closeLogger();
        assert resultsFile.delete();
        return true;
    }

    private void initWithResultsFile(boolean logToConsole) throws IOException {
        Helpers.cleanTempDirectory();
        resultsFile = new File(Helpers.tempDirectory(), "ResultsLoggerTest.txt");
        resultsLogger = new ResultsLogger(resultsFile.getPath(), logToConsole);
    }

    private void initWithoutResultsFile(boolean logToConsole) throws IOException {
        Helpers.cleanTempDirectory();
        resultsFile = new File(Helpers.tempDirectory(), "ResultsLoggerTest.txt");
        resultsLogger = new ResultsLogger(null, logToConsole);
    }

    private void cleanup() {
        resultsLogger.closeLogger();
        if (resultsFile.exists()) {
            assert resultsFile.delete() : "Could not cleanup results file!";
        }
    }
}
