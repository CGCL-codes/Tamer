package obol.format;

import java.io.EOFException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import obol.tools.Debug;

/** Common format server thread antics.
 * @version $Id: FormatThreadBase.java,v 1.4 2007/03/23 16:48:48 perm Exp $
 */
public abstract class FormatThreadBase extends Thread {

    private static final String __me = "obol.format.FormatThreadBase";

    private volatile boolean keepRunning = true;

    private int maxExceptionCount = 5;

    private PrintStream err = System.err;

    protected Debug log = Debug.getInstance(__me);

    private static long[] threadnamebase = { 0L };

    public FormatThreadBase(ThreadGroup group) {
        super(group, "worker-" + threadnamebase[0]++);
    }

    /** Retrieve the running flag.
     * @return the running flag.
     */
    public boolean getRunningFlag() {
        return this.keepRunning;
    }

    /** Set the running flag.
     * @param run <tt>true</tt> if we're to run, <tt>false</tt> if not.
     * @return old value of the running flag.
     */
    public synchronized boolean setRunningFlag(boolean run) {
        boolean _b = this.getRunningFlag();
        this.keepRunning = run;
        return _b;
    }

    /** Retrieve the maximum number of consecutive exceptions the server
     * loop will tolerate before failing.
     * @return the maximum number of consecutive exceptions.
     */
    public int getMaximumExceptionCount() {
        return this.maxExceptionCount;
    }

    /** Set the maximum number of consequtive exceptions the server loop
     * will tolerate before failing.
     * @param max number of consequtive exceptions to tolerate (0...MAXINT).
     * @throws IllegalArgumentsException if the number is negative.
     */
    public void setMaximumExceptionCount(int max) {
        if (max < 0) {
            throw new IllegalArgumentException(__me + ".setMaximumExceptionCount(): maximum count number (" + max + "is negative!");
        }
        this.maxExceptionCount = max;
    }

    /** Set the error output stream.  Defaults to System.err.
     * @param err PrintStream to use to print errors, or <tt>null</tt> if
     * not to print errors (NOT recommended!);
     */
    public void setErrorStream(PrintStream err) {
        if (null == err) {
            if (null != this.err) {
                this.err.println(__me + ".setErrorStream(): WARNING, disabling error output!");
            }
        }
        this.err = err;
    }

    /** Generic implementation of server thread main loop.
     * Will keep calling the process() method until either running flag is
     * set to false via the setRunningFlag() method, or the number of
     * consecutive caught exceptions has exceeded a maximum, set via the
     * setMaximumExceptionCount() method.
     * There are several overridable exception handlers, see this class'
     * handle*-methods.
     */
    public void run() {
        log.debug(".run():  Worker starting up");
        boolean _exceptionExession = false;
        int _exceptionCount = 0;
        while (this.keepRunning) {
            try {
                this.process();
                if (false == this.handleIteration()) {
                    break;
                }
                _exceptionCount = 0;
            } catch (EOFException e) {
                if (false == this.handleEOFException(e)) {
                    break;
                }
            } catch (SocketTimeoutException e) {
                this.handleSocketTimeout(e);
            } catch (InterruptedException e) {
                this.handleInterruptedException(e);
            } catch (Throwable t) {
                _exceptionCount++;
                this.handleCaughtThrowable(t);
                if (_exceptionCount > this.maxExceptionCount) {
                    if (this.handleMaxExceptionExcession()) {
                        break;
                    }
                }
            }
        }
        log.debug(".run():  Worker terminating");
    }

    /** Handle an iteration, deciding whether to continue or not.
     * This method is called, just <em>after</em> the <tt>process()</tt>
     * method, and by default returns <tt>true</tt>, i.e. that the main
     * loop should continue iterating over <tt>process()</tt>.
     * @return <tt>true</tt> if the main loop should 
     */
    protected boolean handleIteration() {
        return true;
    }

    /** Handle an java.io.EOFException, deciding whether to continue or not.
     * There is a test in the main loop in the <tt>run()</tt> method that
     * looks at this method's return value and exits the loop if it is
     * <tt>false</tt>.
     * The default handling is to ignore the exception by returning
     * <tt>true</tt>, i.e. that the main loop should ignore the exception
     * and continue iterating over <tt>process()</tt>.
     * @return <tt>true</tt> if the main loop should continue, or
     * <tt>false</tt> if it should exit.
     */
    protected boolean handleEOFException(EOFException e) {
        return true;
    }

    /** Handle a SocketTimeoutException caught in the server loop.
     * The default handling is to ignore this.
     */
    protected void handleSocketTimeout(SocketTimeoutException e) {
    }

    /** Handle an InterruptedException caught in the server loop.
     * The default handling is to print a warning to the designated error
     * stream.
     */
    protected void handleInterruptedException(InterruptedException e) {
        if (null != this.err) {
            this.err.println(__me + ".run() [" + this.getThreadContextDescription() + "] interrupted (ignored): " + e);
        }
    }

    /** Handle a Throwable caught in the server loop.
     * The default handling is to print a warning to the designated error
     * stream.
     */
    protected void handleCaughtThrowable(Throwable t) {
        t.printStackTrace();
        if (null != this.err) {
            this.err.println(__me + ".run() [" + this.getThreadContextDescription() + "] caught (and ignored): " + t);
        }
    }

    /** Handle the event of exceeding the maximum number of consecutively
     * caught exceptions, and decide wheter to terminate or continue.
     * The default handling is to print a warning to the designated error
     * stream, and then terminate.
     * @return <tt>true</tt> if the server loop is to terminate because of
     * the excession, <tt>false</tt> if not (in which case the caught
     * exception counter is reset).
     */
    protected boolean handleMaxExceptionExcession() {
        if (null != this.err) {
            this.err.println(__me + ".run() [" + this.getThreadContextDescription() + "] Exceeded maximum number of consequitive " + "exceptions (" + this.getMaximumExceptionCount() + "), terminating thread!");
            this.err.println(__me + ".run() [" + this.getThreadContextDescription() + "] Ugly workaround: termination!");
            System.exit(1);
        }
        return true;
    }

    /** Obtain a thread context description for various error and logging
     * output.
     * The description should be informative enough, so that it is possible
     * to figure out what's going on (yeah, this is pretty vague).
     * The default is the Thread.currentThread().toString() output.
     * @return String containing the current thread context description.
     */
    protected String getThreadContextDescription() {
        return Thread.currentThread().toString();
    }

    /** The server loop processing. The implementation should do the actual
     * work of the server loop.
     */
    protected abstract void process() throws Exception;
}
