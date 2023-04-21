package uk.co.brunella.osgi.bdt.runner.formatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import uk.co.brunella.osgi.bdt.runner.result.OSGiTestResult;

public class JUnitTestResultFormatter implements TestResultFormatter {

    private File outputDirectory;

    public void setOutputDirectory(String directory) {
        outputDirectory = new File(directory);
        outputDirectory.mkdirs();
    }

    public void formatTestResults(List<OSGiTestResult> testResults, String out, String err) {
        if (outputDirectory == null) {
            throw new RuntimeException("Output directory not set");
        }
        for (int i = 0; i < testResults.size(); i++) {
            if (testResults.get(i).isInfo()) {
                process(testResults, i, out, err);
            }
        }
    }

    private void process(List<OSGiTestResult> testResults, int start, String out, String err) {
        OSGiTestResult info = testResults.get(start);
        List<OSGiTestResult> testCases = new ArrayList<OSGiTestResult>();
        for (int i = start + 1; i < testResults.size(); i++) {
            OSGiTestResult result = testResults.get(i);
            if (result.isInfo()) {
                break;
            } else {
                testCases.add(result);
            }
        }
        File outputFile = new File(outputDirectory, "TEST-" + info.getMessage() + ".xml");
        try {
            outputFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(outputFile);
            writeXMLHeader(fos);
            writeTestSuite(fos, info, testCases, out, err);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeXMLHeader(FileOutputStream fos) throws IOException {
        fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n".getBytes("UTF-8"));
    }

    private void writeTestSuite(FileOutputStream fos, OSGiTestResult info, List<OSGiTestResult> tests, String out, String err) throws UnsupportedEncodingException, IOException {
        int passed = 0;
        int failed = 0;
        int errored = 0;
        for (OSGiTestResult result : tests) {
            if (result.hasPassed()) passed++;
            if (result.hasFailed()) failed++;
            if (result.hasErrored()) errored++;
        }
        String testSuiteStartTag = "<testsuite " + attr("name", info.getMessage()) + attr("tests", Integer.toString(passed + failed + errored)) + attr("errors", Integer.toString(errored)) + attr("failures", Integer.toString(failed)) + attr("time", formatTime(info.getTime())) + attr("hostname", "???") + ">\n";
        String testSuiteEndTag = "</testsuite>\n";
        fos.write(testSuiteStartTag.getBytes("UTF-8"));
        fos.write("<properties />\n".getBytes("UTF-8"));
        for (OSGiTestResult result : tests) {
            writeTestCase(fos, result);
        }
        writeSystemOut(fos, out);
        writeSystemErr(fos, err);
        fos.write(testSuiteEndTag.getBytes("UTF-8"));
    }

    private void writeTestCase(FileOutputStream fos, OSGiTestResult testCase) throws UnsupportedEncodingException, IOException {
        String testCaseStartTag = "<testcase " + attr("classname", testCase.getClassName()) + attr("name", testCase.getMethodName()) + attr("time", formatTime(testCase.getTime())) + ">\n";
        String testCaseEndTag = "</testcase>\n";
        fos.write(testCaseStartTag.getBytes("UTF-8"));
        if (testCase.hasFailed()) {
            writeFailure(fos, "failure", testCase);
        } else if (testCase.hasErrored()) {
            writeFailure(fos, "error", testCase);
        }
        fos.write(testCaseEndTag.getBytes("UTF-8"));
    }

    private String formatTime(long time) {
        return Double.toString((double) time / 1000);
    }

    private void writeFailure(FileOutputStream fos, String tag, OSGiTestResult testCase) throws UnsupportedEncodingException, IOException {
        String startTag = "<" + tag + " " + attr("message", testCase.getMessage()) + attr("type", testCase.getThrowable().getClass().getName()) + ">\n";
        String endTag = "</" + tag + ">\n";
        fos.write(startTag.getBytes("UTF-8"));
        fos.write(endTag.getBytes("UTF-8"));
    }

    private void writeSystemOut(FileOutputStream fos, String out) throws UnsupportedEncodingException, IOException {
        fos.write("<system-out><![CDATA[".getBytes("UTF-8"));
        fos.write(out.getBytes("UTF-8"));
        fos.write("]]></system-out>\n".getBytes("UTF-8"));
    }

    private void writeSystemErr(FileOutputStream fos, String err) throws UnsupportedEncodingException, IOException {
        fos.write("<system-err><![CDATA[".getBytes("UTF-8"));
        fos.write(err.getBytes("UTF-8"));
        fos.write("]]></system-err>\n".getBytes("UTF-8"));
    }

    private String escape(String message) {
        if (message == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '<') {
                sb.append("&lt;");
            } else if (c == '>') {
                sb.append("&gt;");
            } else sb.append(c);
        }
        return sb.toString();
    }

    private String attr(String name, String value) {
        return name + "=\"" + escape(value) + "\" ";
    }
}
