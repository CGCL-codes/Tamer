package com.rcreations.timeout;

/**
 * Utility class to wait for process termination with timeout.
 * <br>
 * For example:<code><pre>
 *		int	iExitVal = TimeoutProcess.waitFor( process );
 *	</pre></code>
 */
public class TimeoutProcess extends TimeoutTemplateReturnObject {

    /**
    * Process.
    */
    protected Process m_process;

    /**
    * For convenience, you should call the static method waitFor() instead.
    * <br><br>
    * Constructor.
    * @see #waitFor
    */
    protected TimeoutProcess(Process process) {
        super();
        m_process = process;
    }

    /**
    * For convenience, you should call the static method waitFor() instead.
    * <br><br>
    * This method waits for process exit in a background thread.
    * @see #waitFor
    */
    public void runInBackground() throws Exception {
        m_object = Integer.valueOf(m_process.waitFor());
    }

    /**
    * Static method to wait for process exit with timeout.
    * @see java.lang.Process
    */
    public static Integer waitFor(Process process, long lMsTimeout) throws InterruptedException {
        TimeoutProcess tProcess = new TimeoutProcess(process);
        tProcess.runWithTimeout(lMsTimeout);
        return (Integer) tProcess.m_object;
    }
}
