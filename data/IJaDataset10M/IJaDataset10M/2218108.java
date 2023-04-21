package org.fressia.reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.fressia.util.Constants;

/**
 *
 * @author Alvaro Egana
 *
 */
public class FressiaSuiteExecutionInfo {

    private static final String LS = System.getProperty("line.separator");

    private String suiteId;

    private HashMap<String, FressiaTestExecutionInfo> testsInfo;

    private StringBuffer logBuff;

    public FressiaSuiteExecutionInfo(String suiteId) {
        testsInfo = new HashMap<String, FressiaTestExecutionInfo>();
        logBuff = new StringBuffer();
        this.suiteId = suiteId;
    }

    public void setSuiteAsStarted() {
        logln("[starting suite: " + suiteId + "]");
    }

    public void setSuiteAsFinished() {
        logln("[suite finished: " + suiteId + "]");
    }

    public void setTestAsStarted(String fressiaTestId) {
        String msg = "    [starting test: " + fressiaTestId + "]";
        FressiaTestExecutionInfo info = new FressiaTestExecutionInfo(fressiaTestId);
        testsInfo.put(fressiaTestId, info);
        logln(msg);
    }

    public void setTestEventAsStarted(FressiaTestExecutionInfo.TestEventType eventType, String fressiaTestId) {
        String msg = "";
        if (eventType == FressiaTestExecutionInfo.TestEventType.ACTION) {
            msg = "        executing action ... ";
        }
        if (eventType == FressiaTestExecutionInfo.TestEventType.ASSERT) {
            msg = "        evaluating asserts ... ";
        }
        setEventStatus(eventType, fressiaTestId, msg, TestEventExecutionInfo.Status.RUNNING);
        log(msg);
    }

    public void setTestEventStatus(FressiaTestExecutionInfo.TestEventType eventType, String fressiaTestId, TestEventExecutionInfo.Status status, double elapsedTime, Throwable exception, List<String> log) {
        if (testsInfo.containsKey(fressiaTestId)) {
            String currMsg = getTestEventCurrMsg(eventType, fressiaTestId);
            String msg = "";
            FressiaTestExecutionInfo.Status testStatus = FressiaTestExecutionInfo.Status.FAILED;
            if (status == TestEventExecutionInfo.Status.PASSED) {
                if (eventType == FressiaTestExecutionInfo.TestEventType.ACTION) {
                    msg = "[ok]";
                }
                if (eventType == FressiaTestExecutionInfo.TestEventType.ASSERT) {
                    msg = "[true]";
                }
                testStatus = FressiaTestExecutionInfo.Status.PASSED;
            }
            if (status == TestEventExecutionInfo.Status.FAILED) {
                if (eventType == FressiaTestExecutionInfo.TestEventType.ACTION) {
                    msg = "[error]";
                }
                if (eventType == FressiaTestExecutionInfo.TestEventType.ASSERT) {
                    msg = "[false]";
                }
            }
            if (status == TestEventExecutionInfo.Status.SKIPPED) {
                if (eventType == FressiaTestExecutionInfo.TestEventType.ACTION) {
                    testStatus = FressiaTestExecutionInfo.Status.SKIPPED;
                    msg = "[skipped]";
                } else {
                    testStatus = getTestStatus(fressiaTestId);
                    if (testStatus == FressiaTestExecutionInfo.Status.PASSED) {
                        msg = "[passed]";
                    }
                    if (testStatus == FressiaTestExecutionInfo.Status.FAILED) {
                        msg = "[failed]";
                    }
                }
            }
            currMsg += msg;
            setEventStatus(eventType, fressiaTestId, currMsg, status, elapsedTime, exception, log);
            setTestStatus(fressiaTestId, testStatus);
            logln(msg);
        }
    }

    public void setTestAsFinished(String fressiaTestId) {
        if (testsInfo.containsKey(fressiaTestId)) {
            String msg = "";
            if (testsInfo.get(fressiaTestId).getStatus() == FressiaTestExecutionInfo.Status.PASSED) {
                msg = "(passed) ";
            }
            if (testsInfo.get(fressiaTestId).getStatus() == FressiaTestExecutionInfo.Status.FAILED) {
                msg = "(failed) ";
            }
            if (testsInfo.get(fressiaTestId).getStatus() == FressiaTestExecutionInfo.Status.SKIPPED) {
                msg = "(skipped) ";
            }
            logln("    [" + msg + "test finished: " + fressiaTestId + "]");
        }
    }

    public int getPassedTestsCount() {
        return countByStatus(FressiaTestExecutionInfo.Status.PASSED);
    }

    public int getFailedTestsCount() {
        return countByStatus(FressiaTestExecutionInfo.Status.FAILED);
    }

    public int getSkippedTestsCount() {
        return countByStatus(FressiaTestExecutionInfo.Status.SKIPPED);
    }

    public Status getStatus() {
        int skippedCount = 0;
        Status status = Status.PASSED;
        for (FressiaTestExecutionInfo test : testsInfo.values()) {
            if (test.getStatus() == FressiaTestExecutionInfo.Status.FAILED) {
                return Status.FAILED;
            }
            if (test.getStatus() == FressiaTestExecutionInfo.Status.SKIPPED) {
                skippedCount++;
            }
        }
        if (skippedCount == testsInfo.size()) {
            return Status.SKIPPED;
        }
        return status;
    }

    public Collection<FressiaTestExecutionInfo> getTestExecutionInfo() {
        return testsInfo.values();
    }

    public StringBuffer getLog() {
        return logBuff;
    }

    private int countByStatus(FressiaTestExecutionInfo.Status status) {
        int c = 0;
        for (FressiaTestExecutionInfo test : testsInfo.values()) {
            if (test.getStatus() == status) {
                c++;
            }
        }
        return c;
    }

    private String getTestEventCurrMsg(FressiaTestExecutionInfo.TestEventType eventType, String fressiaTestId) {
        if (testsInfo.containsKey(fressiaTestId)) {
            if (eventType == FressiaTestExecutionInfo.TestEventType.ACTION) {
                return testsInfo.get(fressiaTestId).getActionInfo().getCurrentMsg();
            }
            if (eventType == FressiaTestExecutionInfo.TestEventType.ASSERT) {
                return testsInfo.get(fressiaTestId).getAssertEvalInfo().getCurrentMsg();
            }
        }
        return "";
    }

    private void setEventStatus(FressiaTestExecutionInfo.TestEventType eventType, String fressiaTestId, String temporaryMsg, TestEventExecutionInfo.Status status, double elapsedTime, Throwable exception, List<String> log) {
        if (testsInfo.containsKey(fressiaTestId)) {
            if (eventType == FressiaTestExecutionInfo.TestEventType.ACTION) {
                testsInfo.get(fressiaTestId).setActionInfo(temporaryMsg, status, elapsedTime, exception, log);
            }
            if (eventType == FressiaTestExecutionInfo.TestEventType.ASSERT) {
                testsInfo.get(fressiaTestId).setAssertEvalInfo(temporaryMsg, status, elapsedTime, exception, log);
            }
        }
    }

    private void setEventStatus(FressiaTestExecutionInfo.TestEventType eventType, String fressiaTestId, String temporaryMsg, TestEventExecutionInfo.Status status) {
        setEventStatus(eventType, fressiaTestId, temporaryMsg, status, -1, null, null);
    }

    private void setTestStatus(String fressiaTestId, FressiaTestExecutionInfo.Status status) {
        if (testsInfo.containsKey(fressiaTestId)) {
            testsInfo.get(fressiaTestId).setStatus(status);
        }
    }

    private FressiaTestExecutionInfo.Status getTestStatus(String fressiaTestId) {
        if (testsInfo.containsKey(fressiaTestId)) {
            return testsInfo.get(fressiaTestId).getStatus();
        }
        return null;
    }

    private void log(String msg) {
        String msgNew = new String(msg);
        msgNew = msgNew.replaceAll(Constants.INT_SUITE_SEP, Constants.PRINT_INT_SUITE_SEP);
        logBuff.append(msgNew);
        System.out.print(msgNew);
    }

    private void logln(String msg) {
        log(msg + FressiaSuiteExecutionInfo.LS);
    }

    public static class FressiaTestExecutionInfo {

        ActionExecutionInfo actionInfo;

        AssertExecutionInfo assertInfo;

        Status status;

        String testId;

        public FressiaTestExecutionInfo(String testId) {
            status = Status.INITIATED;
            this.testId = testId;
            actionInfo = new ActionExecutionInfo();
            assertInfo = new AssertExecutionInfo();
        }

        public String getId() {
            return testId;
        }

        public Status getStatus() {
            return status;
        }

        public AssertExecutionInfo getAssertEvalInfo() {
            return assertInfo;
        }

        public ActionExecutionInfo getActionInfo() {
            return actionInfo;
        }

        public double getElapsedTime() {
            return actionInfo.getElapsedTime() + assertInfo.getElapsedTime();
        }

        public void setAssertEvalInfo(String temporaryMsg, TestEventExecutionInfo.Status status, double elapsedTime, Throwable exception, List<String> log) {
            assertInfo.setTemporaryMsg(temporaryMsg);
            assertInfo.setStatus(status);
            assertInfo.setElapsedTime(elapsedTime);
            assertInfo.setException(exception);
            assertInfo.setLog(log);
        }

        public void setActionInfo(String temporaryMsg, TestEventExecutionInfo.Status status, double elapsedTime, Throwable exception, List<String> log) {
            actionInfo.setTemporaryMsg(temporaryMsg);
            actionInfo.setStatus(status);
            actionInfo.setElapsedTime(elapsedTime);
            actionInfo.setException(exception);
            actionInfo.setLog(log);
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public static enum TestEventType {

            ACTION, ASSERT
        }

        public static enum Status {

            INITIATED, PASSED, FAILED, SKIPPED
        }
    }

    public static class TestEventExecutionInfo {

        private String temporaryMsg;

        private Status status;

        private double elapsedTime;

        private Throwable exception;

        private List<String> log;

        public TestEventExecutionInfo() {
        }

        public TestEventExecutionInfo(String currentMsg, Status status) {
            this.temporaryMsg = currentMsg;
            this.status = status;
        }

        public String getCurrentMsg() {
            return temporaryMsg;
        }

        public void setTemporaryMsg(String temporaryMsg) {
            this.temporaryMsg = temporaryMsg;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        /**
         * @return the time
         */
        public double getElapsedTime() {
            return elapsedTime;
        }

        /**
         * @param time the time to set
         */
        public void setElapsedTime(double time) {
            this.elapsedTime = time;
        }

        /**
         * @param exception the exception to set
         */
        public void setException(Throwable exception) {
            this.exception = exception;
        }

        /**
         * @param log the log to set
         */
        public void setLog(List<String> log) {
            this.log = log;
        }

        /**
         * @return the log
         */
        public List<String> getOutput() {
            ArrayList<String> output = new ArrayList<String>();
            if (log != null) {
                output.addAll(log);
            }
            if (exception != null) {
                output.add(exception.getMessage());
                for (StackTraceElement elem : exception.getStackTrace()) {
                    output.add(elem.toString());
                }
            }
            return output;
        }

        public static enum Status {

            RUNNING, PASSED, FAILED, SKIPPED
        }
    }

    protected static class ActionExecutionInfo extends TestEventExecutionInfo {

        public ActionExecutionInfo() {
            super();
        }

        public ActionExecutionInfo(String currentMsg, Status status) {
            super(currentMsg, status);
        }
    }

    protected static class AssertExecutionInfo extends TestEventExecutionInfo {

        public AssertExecutionInfo() {
            super();
        }

        public AssertExecutionInfo(String currentMsg, Status status) {
            super(currentMsg, status);
        }
    }

    public static enum Status {

        PASSED, FAILED, SKIPPED
    }
}
