package com.codestreet.bugunit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.codestreet.bugunit.utils.Utils;

/**
 * An abstract bug tracker that implements the common assertion tracking.
 * Subclass for specific formatting of bug and cross-references.
 * 
 * @author Axelp
 */
class DefaultBugTracker implements BugTracker {

    private static DefaultBugTracker s_instance = null;

    private static final Object classLock = DefaultBugTracker.class;

    private static Reporter[] s_reporter = null;

    private IssuesStore issueStore = null;

    private String xmlFileName = null;

    private static final String BUGMAP_XML_FILENAME = "issues.xml";

    private IssuesImpl issues;

    public class SnapshotHook extends Thread {

        public void run() {
            BugUnit.snapshot();
        }
    }

    private DefaultBugTracker() {
        issues = new IssuesImpl();
        issueStore = IssueStoreFactory.createIssuesStore();
        this.xmlFileName = BugUnitProperties.getInstance().getBaseDir() + java.io.File.separator + BUGMAP_XML_FILENAME;
        startReporter();
        Runtime.getRuntime().addShutdownHook(new SnapshotHook());
    }

    /**
   * Initializes the Reporter.
   */
    private static synchronized void startReporter() {
        try {
            BugUnitProperties props = BugUnitProperties.getInstance();
            String reporterClassNames = props.getReporterClasses();
            String[] classes = Utils.split(reporterClassNames, ",");
            s_reporter = new Reporter[classes.length];
            for (int i = 0; i < s_reporter.length; i++) {
                String className = classes[i];
                Class c = null;
                try {
                    c = Class.forName(className);
                } catch (ClassNotFoundException cnfe) {
                    className = "com.codestreet.bugunit." + className;
                    c = Class.forName(className);
                }
                if (c == null) {
                    throw new ClassNotFoundException("Could not find the configured Report class: " + classes[i]);
                }
                s_reporter[i] = (Reporter) c.newInstance();
                s_reporter[i].initialize();
                BugUnitLogger.debug(BugUnitLogger.BugUnit, "initialized reporter: " + s_reporter[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Returns the singleton default Bugtracker.
   */
    public static DefaultBugTracker getInstance() {
        synchronized (classLock) {
            if (s_instance == null) {
                s_instance = new DefaultBugTracker();
            }
            return s_instance;
        }
    }

    /** Intialize an instance of the Abstract BugTracker from the specified report base directory.
   * 
   * @param basedir
   * @throws RuntimeException in case this method fails
   */
    public void initialize() {
        try {
            if (BugUnitProperties.getInstance().isTrackAccumulate()) {
                issues = issueStore.read(xmlFileName);
            } else {
            }
        } catch (SAXException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            BugUnitLogger.debug(BugUnitLogger.Tracker, "reading XML bug map (not found)");
            persist();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /** Returns the XML file name.
   * 
   * @return
   */
    public String getXmlFileName() {
        return xmlFileName;
    }

    /** Persists the current issues to XML.
   * @throws RuntimeException in case this method fails
   */
    public void persist() {
        try {
            BugUnitLogger.debug(BugUnitLogger.Tracker, "saving XML bug map to " + this.xmlFileName);
            issueStore.write(this.issues, this.xmlFileName);
            if (BugUnitProperties.getInstance().isTrackBugs()) {
                BugUnitLogger.debug(BugUnitLogger.Tracker, "Bugs Tracked: " + issues.getNumBugsTracked() + ", Bugs Open: " + issues.getTotalBugsOpen() + " - Bug Tests: " + issues.getTotalBugTests() + ", Bug Failures: " + issues.getTotalBugFailures() + " - Deadlocks: " + issues.getNumDeadlocks());
            }
            for (int i = 0; i < s_reporter.length; i++) {
                if (s_reporter[i] != null) s_reporter[i].process(issues);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /** Adds a bug as being fixed.
   * An assert testing this bug has succeed.
   * @param bugID the ID of the bug that was tested, if set to <CODE>null</CODE> or empty, do nothing
   * @param message the assertion message that would have been given on failure
   */
    public void addBugSuccess(String bugID, String message) {
        TrackedAssertion assertion = new TrackedAssertionImpl(message);
        issues.trackBugAssertion(bugID, assertion);
    }

    /** Adds a bug as being unresolved.
   * 
   * @param bugID the ID of the bug that was tested, if set to <CODE>null</CODE> or empty throw AssertionFailedError
   * @param message the assertion message that would have been given on failure
   * @param e the tracked assertion
   */
    public void addBugFailure(String bugID, TrackedAssertion assertion) {
        issues.trackBugAssertion(bugID, assertion);
    }

    /** Adds a deadlock.
   * @param testClassPackageName the package name of the test case that had to be interrupted
   * @param testMethodName the name of the test method that had to be interrupted
   */
    public void addDeadlock(String testClassPackageName, String testMethodName) {
        issues.addDeadlock(testClassPackageName, testMethodName);
    }

    /** Returns the current map of tracked bugs.
   * 
   * @return the current map of tracked bugs
   */
    public Issues getIssues() {
        return issues;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DefaultBugTracker:");
        buffer.append(" xmlFileName: ");
        buffer.append(xmlFileName);
        buffer.append("]");
        return buffer.toString();
    }
}
