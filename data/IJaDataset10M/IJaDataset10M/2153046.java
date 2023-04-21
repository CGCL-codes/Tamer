package com.sortedunderbelly.appengineunit;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.TaskHandle;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.sortedunderbelly.appengineunit.model.Failure;
import com.sortedunderbelly.appengineunit.model.FailureData;
import com.sortedunderbelly.appengineunit.model.Run;
import com.sortedunderbelly.appengineunit.model.RunStatus;
import com.sortedunderbelly.appengineunit.model.Status;
import com.sortedunderbelly.appengineunit.model.Test;
import com.sortedunderbelly.appengineunit.model.TestResult;
import com.sortedunderbelly.appengineunit.model.TestStatus;
import com.sortedunderbelly.appengineunit.spi.TestHarness;
import com.sortedunderbelly.appengineunit.spi.TestHarnessConfig;
import com.sortedunderbelly.appengineunit.spi.TestRun;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestHarnessManager is the brains of the test harness.  It handles the high
 * level operations requested by the servlet and delegates to the {@link TestHarnessDao}
 * for all persistence operations.
 *
 * @author Max Ross <max.ross@gmail.com>
 */
public class TestHarnessManager {

    private final TestHarnessConfig harnessConfig;

    private final TestHarnessDao dao;

    private final Logger logger = Logger.getLogger(getClass().getName());

    public TestHarnessManager(TestHarnessConfig harnessConfig, TestHarnessDao dao) {
        this.harnessConfig = harnessConfig;
        this.dao = dao;
    }

    public long createNewRun() {
        logger.fine("Creating new run.");
        Run run = dao.newRun();
        TestRun testRun = harnessConfig.newTestRun();
        List<TaskHandle> handles = scheduleExecution(run.getId(), testRun);
        run.setNumTests(handles.size());
        dao.updateRun(run);
        logger.fine("Created new run with id " + run.getId());
        return run.getId();
    }

    public RunStatus getRunStatus(long runId, boolean loadFailures) {
        logger.fine("Getting status for run " + runId);
        Run run = dao.findRunById(runId);
        int numTestsStarted = dao.getNumTestsStartedForRun(runId);
        int numTestsInProgress = dao.getNumTestsInProgressForRun(runId);
        int numTestsFailedForRun = dao.getNumTestsFailedForRun(runId);
        Iterable<Test> failures = numTestsFailedForRun == 0 || !loadFailures ? Collections.<Test>emptyList() : dao.getFailedTestsForRun(runId);
        Iterable<Test> inProgress = dao.getTestsInProgressForRun(runId);
        logger.fine("Retrieved status for run " + runId);
        return new RunStatus(run, numTestsStarted, numTestsInProgress, numTestsFailedForRun, failures, inProgress);
    }

    private List<TaskHandle> scheduleExecution(long runId, TestRun testRun) {
        logger.fine("Scheduling execution for run " + runId);
        List<TaskHandle> handles = new ArrayList<TaskHandle>();
        Queue q = harnessConfig.getQueue(runId);
        for (String testId : testRun.getTestIds(runId)) {
            handles.add(q.add(buildTaskOptionsForTestRun(runId, testId)));
        }
        logger.fine("Scheduled execution of " + handles.size() + " tests for run " + runId);
        return handles;
    }

    private TaskOptions buildTaskOptionsForTestRun(long runId, String testId) {
        return TaskOptions.Builder.method(TaskOptions.Method.POST).url(harnessConfig.getBaseURL() + runId + "/" + testId + "/run");
    }

    private TaskOptions buildTaskOptionsForRunCompletionNotification(long runId, String testId) {
        return TaskOptions.Builder.method(TaskOptions.Method.POST).url(harnessConfig.getBaseURL() + runId + "/" + testId + "/completionNotification");
    }

    public TestStatus getTestStatus(long runId, String testId) {
        return new TestStatus(runId, testId);
    }

    public Failure getFailure(long runId, String testId, String failureId) {
        return dao.getFailure(runId, testId, failureId);
    }

    public TestResult runTest(long runId, String testId) {
        logger.fine("Running test " + testId + " in run " + runId);
        Run run = dao.findRunById(runId);
        Test test = dao.newTest(run, testId);
        TestHarness harness = harnessConfig.getTestHarness();
        TestResult result = null;
        Throwable thrown = null;
        try {
            result = harness.runTest(harnessConfig, test);
            test.setStatus(result.getStatus());
            logger.fine("Test " + testId + " in run " + runId + " completed with status " + result.getStatus());
        } catch (Throwable t) {
            thrown = t;
        } finally {
            if (result == null) {
                test.setStatus(Status.FAILURE);
                String msg;
                if (thrown != null) {
                    msg = "Test " + testId + " in run " + runId + " threw an exception of type " + thrown.getClass().getName();
                    result = new TestResult(runId, testId, Status.FAILURE, -1, Collections.singletonList(new FailureData("Harness Error", msg + ": " + thrown)));
                    logger.log(Level.SEVERE, msg, thrown);
                } else {
                    msg = "Test " + testId + " in run " + runId + " has an error without an exception.";
                    result = new TestResult(runId, testId, Status.FAILURE, -1, Collections.singletonList(new FailureData("Harness Error", msg)));
                    logger.log(Level.SEVERE, msg);
                }
            }
            addResultToTest(test, result);
            test.setEndTime(new Date());
            dao.updateTest(test);
            final boolean includeFailureData = false;
            RunStatus runStatus = getRunStatus(runId, includeFailureData);
            if (runStatus.getStatus() == RunStatus.Status.FINISHED) {
                scheduleCompletionNotification(runId, testId);
            }
        }
        return result;
    }

    private void scheduleCompletionNotification(long runId, String testId) {
        logger.fine("Scheduling completion notification for run " + runId);
        Queue q = harnessConfig.getQueue(runId);
        TaskOptions opts = buildTaskOptionsForRunCompletionNotification(runId, testId);
        q.add(opts);
        logger.fine("Scheduled completion notification for run " + runId);
    }

    private void addResultToTest(Test test, TestResult result) {
        for (FailureData data : result.getFailureData()) {
            test.getFailures().add(new Failure(data.getId(), test.getId(), test.getRun().getId(), data.getData()));
        }
    }

    public void doCompletionCheck(long runId, String serverURL) {
        if (dao.createCompletionRecordIfNotAlreadyPresent(runId)) {
            harnessConfig.getTestRunListener().onCompletion(serverURL + harnessConfig.getBaseURL() + runId, runId);
        } else {
        }
    }
}
