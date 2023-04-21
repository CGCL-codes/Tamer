package server.execution;

import server.common.Network;

public abstract class AbstractExecution implements Runnable {

    protected int m_ID = -1;

    protected Network m_network = null;

    protected String m_methodName = null;

    protected double m_progress = 0;

    protected int m_success = -1;

    protected volatile boolean m_done = false;

    public static final int PHASE_NONCOMPLETE = 0;

    public static final int PHASE_COMPLETE = 1;

    public static final int PHASE_SUCCESS = 0;

    public static final int PHASE_FAILURE = -1;

    public AbstractExecution() {
    }

    public abstract void run();

    public abstract Object getResult();

    public synchronized void cancel() {
        m_done = true;
    }

    public synchronized boolean isDone() {
        return m_done;
    }

    public synchronized double getProgress() {
        return m_progress;
    }

    public synchronized void setProgress(double progress) {
        this.m_progress = progress;
    }

    public synchronized int getSuccess() {
        return m_success;
    }

    public synchronized void setSuccess(int success) {
        this.m_success = success;
    }

    protected void reportSuccess(int success, double progress) {
        setProgress(progress);
        setSuccess(success);
    }

    public void setID(int ID) {
        m_ID = ID;
    }
}
