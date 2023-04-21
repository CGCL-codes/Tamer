package net.grinder.engine.process;

import net.grinder.plugininterface.GrinderContext;
import net.grinder.util.FilenameFactory;
import net.grinder.util.GrinderProperties;

/**
 * Package scope.
 */
class GrinderContextImplementation implements GrinderContext {

    private final GrinderProperties m_parameters;

    private final String m_hostIDString;

    private final String m_processIDString;

    private final int m_threadID;

    private final FilenameFactory m_filenameFactory;

    private boolean m_aborted;

    private boolean m_abortedCycle;

    private boolean m_errorOccurred;

    private long m_startTime;

    private long m_elapsedTime;

    public GrinderContextImplementation(GrinderProperties parameters, String hostIDString, String processIDString, int threadID) {
        m_parameters = parameters;
        m_hostIDString = hostIDString;
        m_processIDString = processIDString;
        m_threadID = threadID;
        m_filenameFactory = new FilenameFactory(processIDString, Integer.toString(threadID));
        reset();
    }

    public void reset() {
        m_aborted = false;
        m_abortedCycle = false;
        m_errorOccurred = false;
    }

    public boolean getAbortedCycle() {
        return m_abortedCycle;
    }

    public boolean getAborted() {
        return m_aborted;
    }

    public long getElapsedTime() {
        return m_elapsedTime;
    }

    public String getHostIDString() {
        return m_hostIDString;
    }

    public String getProcessIDString() {
        return m_processIDString;
    }

    public int getThreadID() {
        return m_threadID;
    }

    public FilenameFactory getFilenameFactory() {
        return m_filenameFactory;
    }

    public void abort() {
        m_aborted = true;
    }

    public void abortCycle() {
        m_abortedCycle = true;
    }

    public GrinderProperties getParameters() {
        return m_parameters;
    }

    public void startTimer() {
        m_startTime = System.currentTimeMillis();
        m_elapsedTime = 0;
    }

    public void stopTimer() {
        if (m_elapsedTime == 0) {
            m_elapsedTime = System.currentTimeMillis() - m_startTime;
        }
    }
}
