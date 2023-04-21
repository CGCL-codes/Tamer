package org.nightlabs.jfire.testsuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import org.apache.log4j.Logger;
import org.nightlabs.ModuleException;
import org.nightlabs.jfire.security.SecurityReflector;
import org.nightlabs.jfire.testsuite.TestSuite.Status;
import org.nightlabs.util.IOUtil;
import org.nightlabs.util.Util;
import org.nightlabs.xml.NLDOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The default {@link JFireTestListener} gathers all {@link Test} data
 * and is capable of generating a XML file from the results and
 * also send the test results as email.
 * <p>
 * The listener accepts the following configuration properties (file references are relative to the ear directory)
 * <ul>
 *   <li>xmlReport.enabled: Whether an xml report should be stored to file</li>
 *   <li>xmlReport.fileName: The xml reports file name, default jfire-test-report.xml</li>
 *   <li>mail.alwaysSend.enabled: Whether an email notification should be send for every test run.</li>
 *   <li>mail.onFailure.enabled: Whether an email notification should be send, when at least one suite fails.</li>
 *   <li>mail.onSkip.enabled: Whether an email notification should be send, when at least one suite is skipped.</li>
 *   <li>mail.smtp.host: The smtp host to send mail with.</li>
 *   <li>mail.from: The sender of the mail, default info@jfire.org</li>
 *   <li>mail.to: The recipients of the mail (,-separated list).</li>
 *   <li>mail.subject: The recipients of the mail (,-separated list), default "JFireTestSuite Testreport"</li>
 *   <li>mail.htmlReportXSL: The stylesheet to use to render the xml to the html mail body, default "htmlReport.xls"</li>
 * </ul>
 *
 * To override some settings without modifing the properties file you can alternatively
 * set the following environment variables on your system.
 * 'jfiretestsuite.mail.smtp.host' to override 'mail.smtp.host'
 * 'jfiretestsuite.mail.to' to override 'mail.to'
 * 'jfiretestsuite.mail.from' to override 'mail.from'
 *
 * <p>
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 * @author marco schulze - marco at nightlabs dot de
 */
public class DefaultTestListener implements JFireTestListener {

    public static final String PROPERTY_KEY_SMTP_HOST = "mail.smtp.host";

    public static final String PROPERTY_KEY_MAIL_TO = "mail.to";

    public static final String PROPERTY_KEY_MAIL_FROM = "mail.from";

    public static final String PROPERTY_KEY_MAIL_SUBJECT = "mail.subject";

    public static final String PROPERTY_KEY_SMTP_AUTH = "mail.smtp.auth";

    public static final String PROPERTY_KEY_SMTP_USER = "mail.smtp.user";

    /**
	 * The password to be used for authentication. Note that this is not understood by
	 * the java mail api as property, but needs to be handled manually (see code below).
	 */
    public static final String PROPERTY_KEY_SMTP_PASSWORD = "mail.smtp.password";

    /**
	 * Log4J Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(DefaultTestListener.class);

    /**
	 * Date format used for output.
	 */
    private static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    /**
	 * Created and populated by this listener for all {@link TestSuite}s that are run.
	 */
    public static class TestSuiteResult {

        private TestSuite suite;

        private Status status;

        private Date startTime;

        private Date endTime;

        private boolean hasFailures;

        private Throwable suiteStartError;

        private List<TestCaseResult> testCaseResults = new LinkedList<TestCaseResult>();

        /**
		 * Returns the endTime of this DefaultTestListener.TestSuiteResult.
		 * @return the endTime.
		 */
        public Date getEndTime() {
            return endTime;
        }

        /**
		 * Sets the endTime of this DefaultTestListener.TestSuiteResult.
		 * @param endTime the endTime to set.
		 */
        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        /**
		 * Returns the hasFailures of this DefaultTestListener.TestSuiteResult.
		 * @return the hasFailures.
		 */
        public boolean isHasFailures() {
            return hasFailures;
        }

        /**
		 * Sets the hasFailures of this DefaultTestListener.TestSuiteResult.
		 * @param hasFailures the hasFailures to set.
		 */
        public void setHasFailures(boolean hasFailures) {
            this.hasFailures = hasFailures;
        }

        /**
		 * Returns the startTime of this DefaultTestListener.TestSuiteResult.
		 * @return the startTime.
		 */
        public Date getStartTime() {
            return startTime;
        }

        /**
		 * Sets the startTime of this DefaultTestListener.TestSuiteResult.
		 * @param startTime the startTime to set.
		 */
        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        /**
		 * Returns the status of this DefaultTestListener.TestSuiteResult.
		 * @return the status.
		 */
        public Status getStatus() {
            return status;
        }

        /**
		 * Sets the status of this DefaultTestListener.TestSuiteResult.
		 * @param status the status to set.
		 */
        public void setStatus(Status status) {
            this.status = status;
        }

        /**
		 * Returns the suite of this DefaultTestListener.TestSuiteResult.
		 * @return the suite.
		 */
        public TestSuite getSuite() {
            return suite;
        }

        /**
		 * Sets the suite of this DefaultTestListener.TestSuiteResult.
		 * @param suite the suite to set.
		 */
        public void setSuite(TestSuite suite) {
            this.suite = suite;
        }

        /**
		 * Returns the testCaseResults of this DefaultTestListener.TestSuiteResult.
		 * @return the testCaseResults.
		 */
        public List<TestCaseResult> getTestCaseResults() {
            return testCaseResults;
        }

        /**
		 * Sets the testCaseResults of this DefaultTestListener.TestSuiteResult.
		 * @param testCaseResults the testCaseResults to set.
		 */
        public void setTestCaseResults(List<TestCaseResult> testCaseResults) {
            this.testCaseResults = testCaseResults;
        }

        /**
		 * Returns the suiteStartError of this DefaultTestListener.TestSuiteResult.
		 * @return the suiteStartError.
		 */
        public Throwable getSuiteStartError() {
            return suiteStartError;
        }

        /**
		 * Sets the suiteStartError of this DefaultTestListener.TestSuiteResult.
		 * @param suiteStartError the suiteStartError to set.
		 */
        public void setSuiteStartError(Throwable suiteStartError) {
            this.suiteStartError = suiteStartError;
        }
    }

    /**
	 * Created and populated by this listener for all {@link TestSuite}s that are run.
	 */
    public static class TestCaseResult {

        private TestSuiteResult testSuiteResult;

        private Class<? extends Test> testCaseClass;

        private Date startTime;

        private Date endTime;

        private boolean hasFailures;

        private List<TestResult> testResults = new LinkedList<TestResult>();

        /**
		 * Returns the endTime of this DefaultTestListener.TestCaseResult.
		 * @return the endTime.
		 */
        public Date getEndTime() {
            return endTime;
        }

        /**
		 * Sets the endTime of this DefaultTestListener.TestCaseResult.
		 * @param endTime the endTime to set.
		 */
        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        /**
		 * Returns the hasFailures of this DefaultTestListener.TestCaseResult.
		 * @return the hasFailures.
		 */
        public boolean isHasFailures() {
            return hasFailures;
        }

        /**
		 * Sets the hasFailures of this DefaultTestListener.TestCaseResult.
		 * @param hasFailures the hasFailures to set.
		 */
        public void setHasFailures(boolean hasFailures) {
            this.hasFailures = hasFailures;
            if (hasFailures) {
                if (testSuiteResult != null) testSuiteResult.setHasFailures(true);
            }
        }

        /**
		 * Returns the startTime of this DefaultTestListener.TestCaseResult.
		 * @return the startTime.
		 */
        public Date getStartTime() {
            return startTime;
        }

        /**
		 * Sets the startTime of this DefaultTestListener.TestCaseResult.
		 * @param startTime the startTime to set.
		 */
        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        /**
		 * Returns the testCaseClass of this DefaultTestListener.TestCaseResult.
		 * @return the testCaseClass.
		 */
        public Class<? extends Test> getTestCaseClass() {
            return testCaseClass;
        }

        /**
		 * Sets the testCaseClass of this DefaultTestListener.TestCaseResult.
		 * @param testCaseClass the testCaseClass to set.
		 */
        public void setTestCaseClass(Class<? extends Test> testCaseClass) {
            this.testCaseClass = testCaseClass;
        }

        /**
		 * Returns the testResults of this DefaultTestListener.TestCaseResult.
		 * @return the testResults.
		 */
        public List<TestResult> getTestResults() {
            return testResults;
        }

        /**
		 * Sets the testResults of this DefaultTestListener.TestCaseResult.
		 * @param testResults the testResults to set.
		 */
        public void setTestResults(List<TestResult> testResults) {
            this.testResults = testResults;
        }

        /**
		 * Returns the testSuiteResult of this DefaultTestListener.TestCaseResult.
		 * @return the testSuiteResult.
		 */
        public TestSuiteResult getTestSuiteResult() {
            return testSuiteResult;
        }

        /**
		 * Sets the testSuiteResult of this DefaultTestListener.TestCaseResult.
		 * @param testSuiteResult the testSuiteResult to set.
		 */
        public void setTestSuiteResult(TestSuiteResult testSuiteResult) {
            this.testSuiteResult = testSuiteResult;
        }
    }

    /**
	 * Created and populated by this listener for all {@link TestSuite}s that are run.
	 */
    private static class TestResult {

        private TestCaseResult testCaseResult;

        private String testName;

        private Date startTime;

        private Date endTime;

        private boolean success = true;

        private Throwable error;

        /**
		 * Returns the endTime of this DefaultTestListener.TestResult.
		 * @return the endTime.
		 */
        public Date getEndTime() {
            return endTime;
        }

        /**
		 * Sets the endTime of this DefaultTestListener.TestResult.
		 * @param endTime the endTime to set.
		 */
        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        /**
		 * Returns the error of this DefaultTestListener.TestResult.
		 * @return the error.
		 */
        public Throwable getError() {
            return error;
        }

        /**
		 * Sets the error of this DefaultTestListener.TestResult.
		 * @param error the error to set.
		 */
        public void setError(Throwable error) {
            this.error = error;
        }

        /**
		 * Returns the startTime of this DefaultTestListener.TestResult.
		 * @return the startTime.
		 */
        public Date getStartTime() {
            return startTime;
        }

        /**
		 * Sets the startTime of this DefaultTestListener.TestResult.
		 * @param startTime the startTime to set.
		 */
        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        /**
		 * Returns the success of this DefaultTestListener.TestResult.
		 * @return the success.
		 */
        public boolean isSuccess() {
            return success;
        }

        /**
		 * Sets the success of this DefaultTestListener.TestResult.
		 * @param success the success to set.
		 */
        public void setSuccess(boolean success) {
            this.success = success;
            if (!success) {
                if (testCaseResult != null) testCaseResult.setHasFailures(true);
            }
        }

        /**
		 * Returns the test of this DefaultTestListener.TestResult.
		 * @return the test.
		 */
        public String getTestName() {
            return testName;
        }

        /**
		 * Sets the test of this DefaultTestListener.TestResult.
		 * @param test the test to set.
		 */
        public void setTestName(String testName) {
            this.testName = testName;
        }

        /**
		 * Returns the testCaseResult of this DefaultTestListener.TestResult.
		 * @return the testCaseResult.
		 */
        public TestCaseResult getTestCaseResult() {
            return testCaseResult;
        }

        /**
		 * Sets the testCaseResult of this DefaultTestListener.TestResult.
		 * @param testCaseResult the testCaseResult to set.
		 */
        public void setTestCaseResult(TestCaseResult testCaseResult) {
            this.testCaseResult = testCaseResult;
        }
    }

    /**
	 * The gatherd results.
	 */
    private List<TestSuiteResult> testSuiteResults;

    /**
	 * Used while running.
	 */
    private Date startTime;

    /**
	 * Used while running.
	 */
    private Date endTime;

    /**
	 * Used while running.
	 */
    private TestSuiteResult currSuiteResult;

    /**
	 * Used while running.
	 */
    private TestCaseResult currTestCaseResult;

    /**
	 * Used while running.
	 */
    private TestResult currTestResult;

    /**
	 * The configuration of this listener.
	 */
    private Properties config = null;

    private String organisationID;

    /**
	 * Default constructor used by the framework to instantiate {@link JFireTestListener}s.
	 */
    public DefaultTestListener() {
        organisationID = SecurityReflector.getUserDescriptor().getOrganisationID();
    }

    /**
	 * {@inheritDoc}
	 * @see org.nightlabs.jfire.testsuite.JFireTestListener#configure(java.util.Properties)
	 */
    public void configure(Properties config) {
        this.config = config;
    }

    /**
	 * Write the gathered test data to XML.
	 *
	 * @param out The {@link OutputStream} the XML should be written to.
	 * @throws ParserConfigurationException
	 */
    protected void writeReportAsXML(OutputStream out) throws ParserConfigurationException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootNode = doc.createElement("JFireServerTestResult");
        rootNode.setAttribute("startTime", ISO_DATE_FORMAT.format(startTime));
        rootNode.setAttribute("endTime", ISO_DATE_FORMAT.format(endTime));
        rootNode.setAttribute("organisationID", organisationID);
        for (TestSuiteResult suiteResult : testSuiteResults) {
            Element suiteElement = doc.createElement("TestSuiteResult");
            suiteElement.setAttribute("suiteClass", suiteResult.getSuite().getClass().getName());
            suiteElement.setAttribute("suiteName", suiteResult.getSuite().getName() != null ? suiteResult.getSuite().getName() : suiteResult.getSuite().getClass().getSimpleName());
            suiteElement.setAttribute("suiteStatus", suiteResult.getStatus().toString());
            suiteElement.setAttribute("startTime", ISO_DATE_FORMAT.format(suiteResult.getStartTime()));
            suiteElement.setAttribute("endTime", ISO_DATE_FORMAT.format(suiteResult.getEndTime()));
            suiteElement.setAttribute("executionTime", Long.toString(suiteResult.getEndTime().getTime() - suiteResult.getStartTime().getTime()));
            suiteElement.setAttribute("hasFailures", Boolean.toString(suiteResult.isHasFailures()));
            if (suiteResult.getSuiteStartError() != null) {
                Element exceptionElement = doc.createElement("TestSuiteResultDetail");
                exceptionElement.setAttribute("message", suiteResult.getSuiteStartError().getLocalizedMessage());
                Node stackTrace = doc.createCDATASection(Util.getStackTraceAsString(suiteResult.getSuiteStartError()));
                exceptionElement.appendChild(stackTrace);
                suiteElement.appendChild(exceptionElement);
            }
            rootNode.appendChild(suiteElement);
            for (TestCaseResult caseResult : suiteResult.getTestCaseResults()) {
                Element caseElement = doc.createElement("TestCaseResult");
                caseElement.setAttribute("testCaseClass", caseResult.getTestCaseClass().getName());
                caseElement.setAttribute("startTime", ISO_DATE_FORMAT.format(caseResult.getStartTime()));
                caseElement.setAttribute("endTime", ISO_DATE_FORMAT.format(caseResult.getEndTime()));
                caseElement.setAttribute("executionTime", Long.toString(caseResult.getEndTime().getTime() - caseResult.getStartTime().getTime()));
                caseElement.setAttribute("hasFailures", Boolean.toString(caseResult.isHasFailures()));
                suiteElement.appendChild(caseElement);
                for (TestResult testResult : caseResult.getTestResults()) {
                    Element testElement = doc.createElement("TestResult");
                    testElement.setAttribute("testName", testResult.getTestName());
                    testElement.setAttribute("startTime", ISO_DATE_FORMAT.format(testResult.getStartTime()));
                    testElement.setAttribute("endTime", ISO_DATE_FORMAT.format(testResult.getEndTime()));
                    testElement.setAttribute("executionTime", Long.toString(testResult.getEndTime().getTime() - testResult.getStartTime().getTime()));
                    testElement.setAttribute("success", Boolean.toString(testResult.isSuccess()));
                    if (testResult.getError() != null) {
                        Element exceptionElement = doc.createElement("TestResultDetail");
                        exceptionElement.setAttribute("message", testResult.getError().getLocalizedMessage());
                        Node stackTrace = doc.createCDATASection(Util.getStackTraceAsString(testResult.getError()));
                        exceptionElement.appendChild(stackTrace);
                        testElement.appendChild(exceptionElement);
                    }
                    caseElement.appendChild(testElement);
                }
            }
        }
        doc.appendChild(rootNode);
        NLDOMUtil.writeDocument(doc, out, "UTF-8");
    }

    /**
	 * {@inheritDoc}
	 * @see org.nightlabs.jfire.testsuite.JFireTestListener#endTestRun()
	 */
    public void endTestRun() throws Exception {
        endTime = new Date();
        processTestRun();
    }

    /**
	 * {@inheritDoc}
	 * @see org.nightlabs.jfire.testsuite.JFireTestListener#startTestRun()
	 */
    public void startTestRun() throws Exception {
        testSuiteResults = new LinkedList<TestSuiteResult>();
        startTime = new Date();
    }

    /**
	 * {@inheritDoc}
	 * @see org.nightlabs.jfire.testsuite.JFireTestListener#testSuiteStatus(org.nightlabs.jfire.testsuite.TestSuite, org.nightlabs.jfire.testsuite.TestSuite.Status)
	 */
    public void testSuiteStatus(TestSuite suite, Status status) throws Exception {
        if (status == Status.START || status == Status.SKIP) {
            TestSuiteResult suiteResult = new TestSuiteResult();
            suiteResult.setStartTime(new Date());
            suiteResult.setStatus(status);
            if (status == Status.SKIP) {
                suiteResult.setEndTime(new Date());
            }
            testSuiteResults.add(suiteResult);
            suiteResult.setSuite(suite);
            currSuiteResult = suiteResult;
        } else if (status == Status.END) {
            if (currSuiteResult == null) return;
            currSuiteResult.setEndTime(new Date());
        }
    }

    /**
	 * {@inheritDoc}
	 * @see org.nightlabs.jfire.testsuite.JFireTestListener#addSuiteStartError(org.nightlabs.jfire.testsuite.TestSuite, java.lang.Throwable)
	 */
    public void addSuiteStartError(TestSuite suite, Throwable t) {
        if (currSuiteResult != null) {
            currSuiteResult.setSuiteStartError(t);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see junit.framework.TestListener#addError(junit.framework.Test, java.lang.Throwable)
	 */
    public void addError(Test test, Throwable t) {
        if (currTestResult == null) return;
        currTestResult.setSuccess(false);
        currTestResult.setError(t);
    }

    /**
	 * {@inheritDoc}
	 * @see junit.framework.TestListener#addFailure(junit.framework.Test, junit.framework.AssertionFailedError)
	 */
    public void addFailure(Test test, AssertionFailedError t) {
        if (currTestResult == null) return;
        currTestResult.setSuccess(false);
        currTestResult.setError(t);
    }

    /**
	 * {@inheritDoc}
	 * @see junit.framework.TestListener#endTest(junit.framework.Test)
	 */
    public void endTest(Test test) {
        if (currTestResult == null) return;
        if (currTestCaseResult != null) currTestCaseResult.setEndTime(new Date());
        currTestResult.setEndTime(new Date());
    }

    /**
	 * {@inheritDoc}
	 * @see junit.framework.TestListener#startTest(junit.framework.Test)
	 */
    public void startTest(Test test) {
        if (currTestCaseResult == null || !currTestCaseResult.getTestCaseClass().equals(test.getClass())) {
            if (currTestCaseResult != null) {
                currTestCaseResult.setEndTime(new Date());
            }
            currTestCaseResult = new TestCaseResult();
            currTestCaseResult.setTestSuiteResult(currSuiteResult);
            currSuiteResult.getTestCaseResults().add(currTestCaseResult);
            currTestCaseResult.setTestCaseClass(test.getClass());
            currTestCaseResult.setStartTime(new Date());
        }
        if (currTestCaseResult == null) return;
        currTestResult = new TestResult();
        currTestResult.setTestCaseResult(currTestCaseResult);
        currTestCaseResult.getTestResults().add(currTestResult);
        currTestResult.setStartTime(new Date());
        String name = test.toString();
        String testName = name.substring(0, name.indexOf('('));
        currTestResult.setTestName(testName);
    }

    private File tempDir = null;

    protected File getTempDir() {
        if (tempDir == null) {
            File tmpDir = IOUtil.getUserTempDir("JFireTestSuite.", null);
            if (!tmpDir.exists()) tmpDir.mkdirs();
            tempDir = tmpDir;
        }
        return tempDir;
    }

    protected String getTempFilePrefix() {
        if (tempFilePrefix == null) tempFilePrefix = Long.toString(System.currentTimeMillis(), 36) + '-' + Long.toString(System.identityHashCode(this)) + '-';
        return tempFilePrefix;
    }

    private String tempFilePrefix = null;

    private File xmlFile = null;

    private File htmlFile = null;

    protected File getXmlFile() throws IOException, ParserConfigurationException {
        if (xmlFile == null) {
            File tmpFileXml = new File(getTempDir(), getTempFilePrefix() + "report.xml");
            tmpFileXml.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tmpFileXml);
            try {
                writeReportAsXML(out);
            } finally {
                out.close();
            }
            xmlFile = tmpFileXml;
        }
        return xmlFile;
    }

    protected File getHtmlFile() throws IOException, ModuleException, ParserConfigurationException, TransformerException {
        if (htmlFile == null) {
            String xslFileName = getProperty("mail.htmlReportXSL", "htmlReport.xsl");
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new File(JFireTestSuiteEAR.getEARDir(), xslFileName)));
            File tmpFileXml = getXmlFile();
            String html;
            {
                StringWriter writer = new StringWriter();
                transformer.transform(new StreamSource(tmpFileXml), new StreamResult(writer));
                writer.close();
                html = writer.toString();
            }
            File tmpFileHtml = new File(getTempDir(), getTempFilePrefix() + "report.html");
            tmpFileHtml.deleteOnExit();
            IOUtil.writeTextFile(tmpFileHtml, html);
            htmlFile = tmpFileHtml;
        }
        return htmlFile;
    }

    /**
	 * According to the contract of {@link JFireTestListener}, an instance is created for each test run.
	 * Therefore, we assert it really is this way and we have no cached data yet.
	 */
    protected void assertClearCache() {
        if (xmlFile != null) throw new IllegalStateException("xmlFile != null");
        if (htmlFile != null) throw new IllegalStateException("htmlFile != null");
        if (tempFilePrefix != null) throw new IllegalStateException("tempFilePrefix != null");
        if (tempDir != null) throw new IllegalStateException("tempDir != null");
    }

    /**
	 * Checks whether the results should be written to XML and does so if desired.
	 * Also checks whether the results should be send by email and does so.
	 */
    protected void processTestRun() {
        assertClearCache();
        boolean xmlReportEnabled = getProperty("xmlReport.enabled", false);
        boolean htmlReportEnabled = getProperty("htmlReport.enabled", false);
        if (xmlReportEnabled) {
            try {
                getXmlFile();
            } catch (Exception e) {
                logger.error("Creating XML report failed!", e);
            }
        }
        if (htmlReportEnabled) {
            try {
                getHtmlFile();
            } catch (Exception e) {
                logger.error("Creating HTML report failed!", e);
            }
        }
        boolean sendMailOnFailure = getProperty("mail.onFailure.enabled", false);
        boolean sendMailOnSuccess = getProperty("mail.alwaysSend.enabled", false);
        boolean sendMailOnSkip = getProperty("mail.onSkip.enabled", false);
        boolean failure = false;
        boolean skipped = false;
        for (TestSuiteResult suiteResult : testSuiteResults) {
            if (suiteResult.isHasFailures()) {
                failure = true;
            } else if (suiteResult.getStatus() == Status.SKIP) {
                skipped = true;
            }
        }
        boolean sendMail = sendMailOnSuccess || (failure && sendMailOnFailure) || (skipped && sendMailOnSkip);
        if (sendMail) {
            try {
                sendReportAsMail();
            } catch (Exception e) {
                logger.error("Failed sending notification email!", e);
            }
        }
    }

    /**
	 * Get the listeners boolean property with the given key.
	 * Return the default value if not set.
	 *
	 * @param key The key of the property.
	 * @param def The properties default value.
	 */
    protected boolean getProperty(String key, boolean def) {
        boolean result = def;
        String str = config.getProperty(key);
        if (str != null && !"".equals(str)) {
            try {
                result = Boolean.parseBoolean(str);
            } catch (Exception e) {
                logger.error("Wrong boolean property for " + key + " = " + str);
                result = def;
            }
        }
        return result;
    }

    /**
	 * Get the listeners String property with the given key.
	 * Return the default value if not set.
	 *
	 * @param key The key of the property.
	 * @param def The properties default value.
	 */
    protected String getProperty(String key, String def) {
        String result = def;
        String str = config.getProperty(key);
        if (str != null) {
            result = str;
        }
        return result;
    }

    /**
	 * Sends the gathered results as email.
	 * Sender, recipient etc. is configured in the listener properties.
	 *
	 * @throws Exception When it failed.
	 */
    public void sendReportAsMail() throws Exception {
        try {
            File tmpFileXml = getXmlFile();
            String html = IOUtil.readTextFile(getHtmlFile());
            if ("".equals(getProperty(PROPERTY_KEY_SMTP_HOST, ""))) throw new IllegalStateException("There is no SMTP host defined! Check your jfireTestSuite.properties (key suffix " + PROPERTY_KEY_SMTP_HOST + ") and your included properties-files!");
            if ("".equals(getProperty(PROPERTY_KEY_MAIL_FROM, ""))) throw new IllegalStateException("There is no mail-from defined! Check your jfireTestSuite.properties (key suffix " + PROPERTY_KEY_MAIL_FROM + ") and your included properties-files!");
            if ("".equals(getProperty(PROPERTY_KEY_MAIL_TO, ""))) throw new IllegalStateException("There is no mail-to defined! Check your jfireTestSuite.properties (key suffix " + PROPERTY_KEY_MAIL_TO + ") and your included properties-files!");
            Session session = Session.getInstance(config, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getProperty(PROPERTY_KEY_MAIL_FROM, "info@jfire.org")));
            String to = getProperty(PROPERTY_KEY_MAIL_TO, "info@jfire.org");
            if (to.contains(",")) {
                StringTokenizer t = new StringTokenizer(to, ",");
                while (t.hasMoreTokens()) message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(t.nextToken().trim()));
            } else {
                message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            }
            String subject = getProperty(PROPERTY_KEY_MAIL_SUBJECT, "JFireTestSuite Testreport");
            message.setSubject(subject);
            MimeBodyPart mimebodypart = new MimeBodyPart();
            mimebodypart.setContent(html, "text/html");
            MimeMultipart mimemultipart = new MimeMultipart();
            mimemultipart.addBodyPart(mimebodypart);
            mimebodypart = new MimeBodyPart();
            FileDataSource filedatasource = new FileDataSource(tmpFileXml);
            mimebodypart.setDataHandler(new DataHandler(filedatasource));
            mimebodypart.setFileName("jfire-test-report.xml");
            mimemultipart.addBodyPart(mimebodypart);
            message.setContent(mimemultipart);
            message.saveChanges();
            boolean authenticate = "true".equals(config.getProperty(PROPERTY_KEY_SMTP_AUTH, "false"));
            String smtpHost = config.getProperty(PROPERTY_KEY_SMTP_HOST);
            if (!authenticate) {
                logger.info("sendReportAsMail: Sending TestSuite report email without authentication via SMTP host " + smtpHost + " to: " + to);
                Transport.send(message);
            } else {
                logger.info("sendReportAsMail: Sending TestSuite report email with authentication via SMTP host " + smtpHost + " to: " + to);
                String smtpUsername = config.getProperty(PROPERTY_KEY_SMTP_USER);
                String smtpPassword = config.getProperty(PROPERTY_KEY_SMTP_PASSWORD);
                if (smtpUsername == null || "".equals(smtpUsername)) logger.warn("sendReportAsMail: property " + PROPERTY_KEY_SMTP_AUTH + " has been set to 'true', but there is no user name defined! You should add a user name using the property " + PROPERTY_KEY_SMTP_USER + "!");
                if (smtpPassword == null || "".equals(smtpPassword)) logger.warn("sendReportAsMail: property " + PROPERTY_KEY_SMTP_AUTH + " has been set to 'true', but there is no password defined! You should add a password using the property " + PROPERTY_KEY_SMTP_PASSWORD + "!");
                if (logger.isTraceEnabled()) {
                    logger.trace("sendReportAsMail: properties:");
                    for (Map.Entry<?, ?> me : config.entrySet()) logger.trace("sendReportAsMail:   * " + me.getKey() + '=' + me.getValue());
                }
                Transport tr = session.getTransport("smtp");
                try {
                    tr.connect(smtpHost, smtpUsername, smtpPassword);
                    tr.sendMessage(message, message.getAllRecipients());
                } finally {
                    tr.close();
                }
            }
        } catch (Exception e) {
            logger.error("Sending TestSuite report email failed! Escalating exception...", e);
            throw e;
        }
    }
}
