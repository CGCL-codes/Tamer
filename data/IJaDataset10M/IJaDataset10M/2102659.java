package com.mitester.executor;

import static com.mitester.executor.ExecutorConstants.COMMA_SEPARATOR;
import static com.mitester.executor.ExecutorConstants.DEFAULT_TEST_INTERVAL;
import static com.mitester.executor.ExecutorConstants.DISCARD_WAIT_TIME_SEC;
import static com.mitester.executor.ExecutorConstants.SEC;
import static com.mitester.executor.ExecutorConstants.SUT_WAIT_TIME;
import static com.mitester.executor.ExecutorConstants.SUT_WAIT_TIME_SEC;
import static com.mitester.executor.ExecutorConstants.TEST_INTERVAL;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import com.mitester.adapter.Adapter;
import com.mitester.jaxbparser.client.PARAM;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;

/**
 * This class automates SIP client(SUT) functionalities. It sends the client
 * actions and receives the client notifications(client status change)through
 * the TCP channel established between SUT and miTester and it controls the
 * client actions according to the flow described in the test script. It
 * implements Timer threads which are used to suspend the client actions
 * temporarily for the specified time duration and sets the maximum time to the
 * controller will wait for receiving notifications from SUT.
 * 
 */
public class ClientScriptRunner {

    private static final Logger LOGGER = MiTesterLog.getLogger(ClientScriptRunner.class.getName());

    private final int MAXIMUM_SUT_WAIT_TIME;

    private final int EXECUTION_INTERVAL;

    private ScheduledExecutorService clientTimerScheduler = Executors.newScheduledThreadPool(1);

    private volatile boolean isClientTestStart = false;

    private volatile boolean isClientTestSucceed = false;

    private volatile boolean isTimerExpired = false;

    private volatile ScheduledFuture<?> clientTestEndTimer = null;

    private String actName = null;

    private TestExecutor testExecutor = null;

    private Adapter adapter = null;

    /**
	 * It initializes the adapter,testExecutor and MAXIMUM_SUT_WAIT_TIME
	 * instance variables
	 * 
	 * @param adapter
	 *            Adapter interface
	 * @param testExecutor
	 *            TestExecutor class object
	 * 
	 */
    public ClientScriptRunner(Adapter adapter, TestExecutor testExecutor) {
        this.adapter = adapter;
        this.testExecutor = testExecutor;
        if (CONFIG_INSTANCE.isKeyExists(SUT_WAIT_TIME)) {
            MAXIMUM_SUT_WAIT_TIME = Integer.parseInt(CONFIG_INSTANCE.getValue(SUT_WAIT_TIME));
        } else {
            MAXIMUM_SUT_WAIT_TIME = SUT_WAIT_TIME_SEC;
        }
        if (CONFIG_INSTANCE.isKeyExists(TEST_INTERVAL)) {
            EXECUTION_INTERVAL = Integer.parseInt(CONFIG_INSTANCE.getValue(TEST_INTERVAL)) * 1000;
        } else {
            EXECUTION_INTERVAL = DEFAULT_TEST_INTERVAL;
        }
    }

    /**
	 * Execute the client or SUT Test
	 * 
	 * @param clientTest
	 *            consists of set of client actions
	 * 
	 * @return Runnable interface
	 */
    public Runnable frameClientRunnable(final com.mitester.jaxbparser.client.TEST clientTest) {
        cleanUpClientTest();
        return new Runnable() {

            public void run() {
                try {
                    int actionCount = 0;
                    com.mitester.jaxbparser.client.ACTION action = null;
                    com.mitester.jaxbparser.client.WAIT wait = null;
                    List<Object> clientActions = clientTest.getACTIONOrWAIT();
                    boolean isWAIT = false;
                    int noOfClientActions = clientActions.size();
                    if (startSUT()) {
                        isClientTestStart = true;
                        for (Object object : clientActions) {
                            if (object instanceof com.mitester.jaxbparser.client.ACTION) {
                                action = (com.mitester.jaxbparser.client.ACTION) object;
                                isWAIT = false;
                            } else {
                                wait = (com.mitester.jaxbparser.client.WAIT) object;
                                isWAIT = true;
                            }
                            if (isWAIT) {
                                TestUtility.printMessage("client action in sleeping...");
                                LOGGER.info("client action in sleeping...");
                                waitClientAction(wait);
                                TestUtility.printMessage("resumed the client action...");
                                LOGGER.info("resumed the client action...");
                            } else if (action.getRECV() != null) {
                                setSUTActionName(action.getValue());
                                receiveMsgFromSUT(action.getValue());
                            } else if (action.getSEND() != null) {
                                setSUTActionName(action.getValue());
                                sendMsgtoSUT(action);
                            } else if (action.getDISCARD() != null) {
                                if (!waitForDiscardMsg(action.getValue())) break;
                            } else {
                                break;
                            }
                            actionCount++;
                        }
                    } else {
                        TestUtility.printMessage("error while starting SUT");
                        LOGGER.error("error while starting SUT");
                    }
                    if ((actionCount >= noOfClientActions) && (noOfClientActions > 0)) {
                        isClientTestSucceed = true;
                    }
                } catch (com.mitester.executor.ControllerException ex) {
                    if (!isTimerExpired) LOGGER.error(ex.getMessage());
                    if (testExecutor.getIsValidationFailed()) TestResult.setFailReason(testExecutor.getBufferedFailString() + ", " + ex.getMessage()); else TestResult.setFailReason(ex.getMessage());
                } catch (Exception ex) {
                    if (!isTimerExpired) LOGGER.error(ex.getMessage());
                    if (testExecutor.getIsValidationFailed()) TestResult.setFailReason(testExecutor.getBufferedFailString() + ", error while executing client test script"); else TestResult.setFailReason("error while executing client test script");
                } finally {
                    stopClientTimer();
                    if (!isClientTestSucceed) {
                        if (!adapter.isStopCalled()) {
                            LOGGER.info("stopping the client as the call flow of test execution is incomplete");
                            if (adapter.stop()) LOGGER.info("SUT is closed"); else LOGGER.info("closing or stopping SUT is failed");
                        }
                    }
                    startClientWaitTimer((long) EXECUTION_INTERVAL);
                    if (isClientTestSucceed) {
                        TestUtility.printMessage("SUT actions completed...");
                        LOGGER.info("SUT actions completed...");
                    } else LOGGER.info("incomplete client scenario...");
                    if (testExecutor.getClientTestCountDownLatch().getCount() == 1) {
                        testExecutor.getClientTestCountDownLatch().countDown();
                    }
                }
            }
        };
    }

    /**
	 * This method called during the client test execution for suspending client
	 * action
	 * 
	 * @param time
	 *            amount of time(in milliseconds) to wait
	 * 
	 */
    private void startClientWaitTimer(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException ex) {
        }
    }

    /**
	 * It stops the client wait timer
	 * 
	 */
    private void stopClientTimer() {
        if (clientTestEndTimer != null) {
            clientTestEndTimer.cancel(true);
            clientTestEndTimer = null;
            LOGGER.info("wait timer is stopped");
        }
    }

    /**
	 * 
	 * This method called when tool is started to wait for expected
	 * notification(SUT state change) from client. It starts timer, wait for
	 * specified time duration if the expected notification not received in
	 * specified time, forcefully ends the client test.
	 * 
	 * @param adapter
	 *            Adapter interface
	 */
    private void startClientTimer(final Adapter adapter) {
        if (clientTestEndTimer != null) {
            clientTestEndTimer.cancel(true);
            clientTestEndTimer = null;
        }
        LOGGER.info("client wait timer is started");
        clientTestEndTimer = clientTimerScheduler.schedule(new Runnable() {

            public void run() {
                isTimerExpired = true;
                if (testExecutor.getIsValidationFailed()) {
                    String errStr = testExecutor.getBufferedFailString() + ", expected '" + getSUTActionName() + "' message not received from SUT";
                    TestResult.setFailReason(errStr);
                    LOGGER.error(errStr);
                } else {
                    String errStr = "expected '" + getSUTActionName() + "' message is not received from SUT";
                    TestResult.setFailReason(errStr);
                    LOGGER.error(errStr);
                }
                LOGGER.info("stopping the client as the client timer is expired");
                stopClientTestExecution();
            }
        }, MAXIMUM_SUT_WAIT_TIME, TimeUnit.SECONDS);
    }

    /**
	 * It returns the client start status
	 * 
	 * @return true if client is started
	 */
    public boolean getClientTestStart() {
        return isClientTestStart;
    }

    /**
	 * It cleans the ClientScriptRunner instance variables
	 * 
	 */
    private void cleanUpClientTest() {
        isClientTestStart = false;
        isClientTestSucceed = false;
        isTimerExpired = false;
        clientTestEndTimer = null;
        actName = null;
    }

    /**
	 * It returns the client test result status
	 * 
	 * @return client test result status
	 */
    public boolean getClientTestResult() {
        return isClientTestSucceed;
    }

    /**
	 * It returns the ScheduledExecutorService
	 * 
	 * @returns ScheduledExecutorService
	 */
    public ScheduledExecutorService getClientScheduledExecutorService() {
        return clientTimerScheduler;
    }

    /**
	 * It sends the action to SUT
	 * 
	 * @param action
	 *            is a com.mitester.jaxbparser.client.ACTION object consists of
	 *            action details
	 * @throws ControllerException
	 */
    private void sendMsgtoSUT(com.mitester.jaxbparser.client.ACTION action) throws ControllerException {
        List<PARAM> paramList = null;
        String clientActionValue = null;
        try {
            paramList = action.getPARAM();
            StringBuilder tempBuf = new StringBuilder();
            if (paramList.size() > 0) {
                for (PARAM param : paramList) {
                    tempBuf.append(param.getValue());
                    tempBuf.append(COMMA_SEPARATOR);
                }
                clientActionValue = action.getValue() + COMMA_SEPARATOR + tempBuf.toString();
            } else {
                clientActionValue = action.getValue();
            }
            adapter.send(clientActionValue);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new com.mitester.executor.ControllerException("Error while sending '" + getSUTActionName() + "' ACTION message to SUT");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new com.mitester.executor.ControllerException("Error while sending '" + getSUTActionName() + "' ACTION message to SUT");
        }
    }

    /**
	 * It used to suspend the client test execution
	 * 
	 * @param wait
	 *            is a com.mitester.jaxbparser.client.WAIT object consists of
	 *            the Time related informations
	 */
    private void waitClientAction(com.mitester.jaxbparser.client.WAIT wait) {
        BigDecimal time;
        if (wait.getUnit().equals(SEC)) {
            time = new BigDecimal(wait.getValue().toString());
            time = time.multiply(new BigDecimal("1000"));
        } else {
            time = new BigDecimal(wait.getValue().toString());
        }
        startClientWaitTimer(time.longValueExact());
    }

    /**
	 * It waiting for discarded message from SUT
	 * 
	 * @return true if client doesn't accept the discarded SIP message
	 * @throws ControllerException
	 */
    private boolean waitForDiscardMsg(String clientActionValue) throws ControllerException {
        try {
            adapter.getSocket().setSoTimeout((DISCARD_WAIT_TIME_SEC * 1000));
            String receivedMsg = null;
            try {
                long timetoWait = DISCARD_WAIT_TIME_SEC * 1000;
                do {
                    long startTime = System.currentTimeMillis();
                    receivedMsg = adapter.receive();
                    TestUtility.printMessage("MSG from SUT <==" + receivedMsg);
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    if (elapsedTime < timetoWait) {
                        timetoWait = timetoWait - elapsedTime;
                    } else {
                        timetoWait = DISCARD_WAIT_TIME_SEC * 1000;
                    }
                    adapter.getSocket().setSoTimeout((int) timetoWait);
                } while (!(receivedMsg.equals(clientActionValue)));
                TestUtility.printMessage("SUT is accepted the discarded SIP message");
                LOGGER.error("SUT is accepted the discarded SIP message");
                TestResult.setFailReason("SUT is accepted the discarded SIP message");
                LOGGER.info("stopping the SUT as the SUT accepted the discarded sip message");
                stopClientTestExecution();
                return false;
            } catch (SocketTimeoutException ex) {
                LOGGER.info("SUT is not accepting the discarded message");
                adapter.getSocket().setSoTimeout(0);
                return true;
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new com.mitester.executor.ControllerException("TCP communication channel is disconnected between SUT and miTester");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new com.mitester.executor.ControllerException("Error while receiving discardable message from SUT");
        }
    }

    /**
	 * It receives and process the message from SUT
	 * 
	 * @param clientActionValue
	 *            specifies message is being expected from client
	 * @throws ControllerException
	 */
    private void receiveMsgFromSUT(String clientActionValue) throws ControllerException {
        try {
            String receivedMsg = null;
            startClientTimer(adapter);
            do {
                receivedMsg = adapter.receive();
                TestUtility.printMessage("MSG from SUT <==" + receivedMsg);
            } while (!(receivedMsg.equals(clientActionValue)));
            stopClientTimer();
        } catch (IOException ex) {
            if (!isTimerExpired) LOGGER.error(ex.getMessage());
            throw new com.mitester.executor.ControllerException("TCP communication channel is disconnected between SUT and miTester");
        } catch (Exception ex) {
            if (!isTimerExpired) LOGGER.error(ex.getMessage());
            throw new com.mitester.executor.ControllerException("Error while receiving '" + getSUTActionName() + "' message from SUT");
        }
    }

    /**
	 * starts the SUT
	 * 
	 * @return true if client started successfully
	 */
    private boolean startSUT() {
        if (!adapter.isConnected()) {
            LOGGER.info("SUT is not connected");
            if (!adapter.start()) {
                return false;
            }
        } else {
            TestUtility.printMessage("SUT is started");
        }
        return true;
    }

    /**
	 * stops the client test execution
	 */
    public void stopClientTestExecution() {
        if (!System.getProperty("os.name").startsWith("SunOS") && !System.getProperty("os.name").startsWith("Solaris")) LOGGER.info("called stopClientTestExecution");
        if (clientTestEndTimer != null) clientTestEndTimer.cancel(true);
        clientTestEndTimer = null;
        if (!adapter.stop()) {
            TestUtility.printMessage("closing or stopping SUT is failed");
            LOGGER.error("closing or stopping SUT is failed");
        } else {
            if (!System.getProperty("os.name").startsWith("SunOS") && !System.getProperty("os.name").startsWith("Solaris")) LOGGER.info("SUT is closed");
        }
    }

    /**
	 * return the ACTION name
	 * 
	 * @return the current ACTION name
	 */
    private String getSUTActionName() {
        return actName;
    }

    /**
	 * set the SUT ACTION name
	 * 
	 * @param actName
	 *            name of the current ACTION
	 */
    private void setSUTActionName(String actName) {
        this.actName = actName;
    }
}
