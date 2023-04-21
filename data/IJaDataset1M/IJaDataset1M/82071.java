package org.grandtestauto.distributed;

import org.apache.commons.io.*;
import org.grandtestauto.*;
import org.grandtestauto.util.*;
import java.io.*;
import java.rmi.*;
import java.util.logging.*;

/**
 * A test agent looks for a server from which to get tasks.
 * When assigned a task, it spawns a test JVM using. The results
 * of the test are reported to the server, and another task
 * requested.
 *
 * @author Tim Lavers
 */
public class TestAgent implements TestLauncher.Controller {

    private Distributor distributor;

    private Thread runner;

    private volatile boolean keepGoing = true;

    public static long PAUSE_TIME = 1000;

    private TestAgentSettings settings;

    private String distributorLookup;

    private File testsBaseDir;

    private File logsDir;

    private Logger logger;

    private FileHandler fileHandler;

    public static void main(String[] args) throws IOException {
        TestAgentSettings tas = new TestAgentSettings(args[0]);
        new TestAgent(tas);
    }

    public TestAgent(TestAgentSettings settings) throws IOException {
        try {
            PAUSE_TIME = Long.parseLong(System.getProperty("org.grandtestauto.AgentPauseTime"));
        } catch (Exception e) {
            PAUSE_TIME = 1000;
        }
        this.settings = settings;
        testsBaseDir = new File(settings.baseDir(), "tests");
        testsBaseDir.mkdirs();
        logsDir = new File(settings.baseDir(), "logs");
        logsDir.mkdirs();
        logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        fileHandler = new FileHandler(logsDir.getPath() + "/TestAgent.log", 1024 * 1024, 10, false);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        distributorLookup = "//" + settings.serverAddress().getHostAddress() + ":" + settings.serverPort() + "/" + Distributor.SERVICE_NAME;
        log("Will lookup distributor on '" + distributorLookup + "'");
        runner = new Thread("TestAgentRunner_" + settings.name()) {

            public void run() {
                while (keepGoing) {
                    pollForWork();
                    try {
                        Thread.sleep(PAUSE_TIME);
                    } catch (InterruptedException ie) {
                        keepGoing = false;
                        return;
                    }
                }
            }
        };
        log("About to start TestAgentRunner");
        runner.start();
    }

    public void log(String message) {
        logger.log(Level.INFO, message);
        fileHandler.flush();
    }

    public void stop() {
        keepGoing = false;
        runner.interrupt();
        try {
            runner.join(2 * PAUSE_TIME);
        } catch (InterruptedException e) {
        }
        fileHandler.flush();
        fileHandler.close();
    }

    private void pollForWork() {
        log("polling for work...");
        if (distributor == null) {
            lookupDistributor();
        }
        if (distributor == null) return;
        TaskSummary taskSummary;
        try {
            taskSummary = distributor.requestWork(new AgentDetails(settings.name(), settings.maximumGrade()));
        } catch (RemoteException e) {
            log("RemoteException while requesting work.", e);
            return;
        }
        if (taskSummary != null) {
            doWork(taskSummary);
        } else {
            log("No work assigned.");
        }
    }

    private void lookupDistributor() {
        try {
            distributor = (Distributor) Naming.lookup(distributorLookup);
        } catch (Exception e) {
            log("Server not found " + e.getMessage());
        }
    }

    private void doWork(TaskSummary taskSummary) {
        String cp = settings.classesRoot().getAbsolutePath();
        cp = cp + ";" + taskSummary.classpath();
        TestLauncher launcher = new TestLauncher(this, cp, testsBaseDir, settings.jvmOptionsList());
        try {
            log("About to launch testJVM for: " + taskSummary);
            Process testJVM = launcher.startTestJVM(taskSummary.packageName(), settings.classesRoot());
            log("Test JVM launched...");
            String[] output = ProcessReader.readProcess(testJVM);
            log("TestJ VM output...");
            log("sout: '" + output[0] + "'");
            log("serr: '" + output[1] + "'");
            TAResult result = new TAResult(taskSummary, settings.name(), output);
            File resultsFile = new File(testsBaseDir, Settings.DEFAULT_LOG_FILE_NAME);
            String resultFileContents = FileUtils.readFileToString(resultsFile);
            result.setResultsFileContents(resultFileContents);
            log("Results file contents: '" + resultFileContents + "'");
            if (distributor == null) {
                lookupDistributor();
            }
            if (distributor == null) {
                log("Not reporting results as distributor not found.");
            } else {
                log("About to report results");
                distributor.reportResult(result);
                log("Results reported");
            }
        } catch (Exception e) {
            log("Error running tests or reporting results.", e);
        }
    }

    private void log(String message, Exception exception) {
        logger.log(Level.WARNING, message, exception);
    }
}
