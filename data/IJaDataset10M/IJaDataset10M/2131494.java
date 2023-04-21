package com.alexmcchesney.operations;

/**
 * Dummy operation for testing the async operation handler.
 * @author amcchesney
 *
 */
public class TestOperation implements IAsynchronousOperation {

    /** Flag indicating that the operation has been cancelled */
    private boolean m_bCancelled;

    /** The name of the operation */
    private String m_sName;

    /** Flag indicating that we'd like the operation to simulate a failure */
    private boolean m_bFail = false;

    /** Current status */
    private TestOperationStatus m_status = new TestOperationStatus();

    /** Test operation status class */
    private class TestOperationStatus extends OperationStatus {

        public void setStatus(int iStatus) {
            m_iStatus = iStatus;
        }

        public void appendToLog(String sMsg) {
            m_log.add(sMsg);
        }

        public void setError(Throwable error) {
            m_error = error;
            m_iStatus = OperationStatus.FAILED;
        }

        public void setCompletionPercentage(int iPercentage) {
            m_iCompletionPercentage = iPercentage;
        }
    }

    /**
	 * Constructor
	 * @param sName	Name of the operation
	 * @param bFail	If true, the operation will "fail" at some point
	 */
    public TestOperation(String sName, boolean bFail) {
        m_sName = sName;
        m_bFail = bFail;
    }

    public void cancel() {
        m_bCancelled = true;
    }

    public String getOperationDescription() {
        return m_sName;
    }

    public OperationStatus getStatus() {
        return m_status;
    }

    public void run() {
        m_status.setStatus(OperationStatus.RUNNING);
        for (int i = 0; i < 10; i++) {
            if (m_bCancelled) {
                System.out.println("Operation got cancellation message");
                m_status.setStatus(OperationStatus.CANCELLED);
                return;
            }
            m_status.appendToLog("Doing thing #" + i);
            if (i == 5 && m_bFail) {
                m_status.setError(new Exception("Test failure."));
                break;
            }
            m_status.setCompletionPercentage(i * 10);
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            ;
        }
        m_status.setStatus(OperationStatus.SUCCEEDED);
    }
}
