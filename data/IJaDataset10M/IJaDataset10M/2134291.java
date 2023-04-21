package org.protesting.jft.tcgen.testsuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.util.List;

/**
 * User: ab83625
 * Date: 25.03.2008
 * Time: 10:40:24
 */
public abstract class TestSuite {

    protected static Log logger = LogFactory.getLog(TestSuite.class);

    protected List testCases;

    protected File testSuiteFile;

    protected FileOutputStream testSuiteFileOutputStream;

    public TestSuite(List testCases, File testSuiteFile) {
        this.testCases = testCases;
        this.testSuiteFile = testSuiteFile;
    }

    public TestSuite(List testCases, FileOutputStream testSuiteFileOutputStream) {
        this.testCases = testCases;
        this.testSuiteFileOutputStream = testSuiteFileOutputStream;
    }

    public void create() {
        createTestSuite();
        printToFile();
    }

    public void createFile() {
        createTestSuite();
    }

    public OutputStream createStream() {
        createTestSuite();
        return printToStream();
    }

    protected abstract OutputStream printToStream();

    protected abstract void printToFile();

    protected abstract void createTestSuite();

    public List getTestCases() {
        return testCases;
    }

    public File getTestSuiteFile() {
        return testSuiteFile;
    }
}
