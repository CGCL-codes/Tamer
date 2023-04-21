package weka.ptolemy.actor;

import ptolemy.actor.TypedAtomicActor;
import ptolemy.data.BooleanToken;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import weka.ptolemy.data.expr.ExtFileParameter;
import java.io.File;
import java.util.Date;

/**
 * A logging actor. Logs stdout and stderr to a specified file.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 152 $
 */
public class OutputLogger extends TypedAtomicActor {

    /** for serialization. */
    private static final long serialVersionUID = 4806559450181940834L;

    /**
   * The class that performs the logging of stdout and stderr.
   * 
   * @author  fracpete (fracpete at waikato dot ac dot nz)
   * @version $Revision: 152 $
   */
    public static class KeplerLogger extends weka.core.logging.OutputLogger {

        /** whether logging is enabled or not. */
        protected boolean m_Enabled;

        /**
     * Initializes the logging.
     * 
     * @param logfile	the log file to use
     */
        public KeplerLogger(File logfile) {
            super();
            m_LogFile = logfile;
            m_Enabled = true;
        }

        /**
     * Sets whether logging is enabled or not.
     * 
     * @param value	if true then logging will be enabled
     */
        public void setEnabled(boolean value) {
            m_Enabled = value;
        }

        /**
     * Returns whether logging is enabled.
     * 
     * @return		true if logging is enabled
     */
        public boolean isEnabled() {
            return m_Enabled;
        }

        /**
     * Sets the new logfile to use.
     * 
     * @param value	the file to use
     */
        public void setLogFile(File value) {
            m_LogFile = value;
        }

        /**
     * Returns the current logfile.
     * 
     * @return		the file in use
     */
        public File getLogFile() {
            return m_LogFile;
        }

        /**
     * Performs the actual logging. 
     * 
     * @param level	the level of the message
     * @param msg		the message to log
     * @param cls		the classname originating the log event
     * @param method	the method originating the log event
     * @param lineno	the line number originating the log event
     */
        protected void doLog(Level level, String msg, String cls, String method, int lineno) {
            if (!m_Enabled) return;
            super.doLog(level, msg, cls, method, lineno);
        }

        /**
     * Restores the original stdout/stderr setup.
     */
        public synchronized void restore() {
            System.setErr(m_StdErr.getDefault());
            System.setOut(m_StdOut.getDefault());
        }
    }

    /** the file to direct the logging information to. */
    protected ExtFileParameter logFile;

    /** whether logging is enabled or not. */
    protected Parameter enabled;

    /** whether to append to the log instead of deleting it. */
    protected Parameter append;

    /** the logger used for logging. */
    protected KeplerLogger _logger;

    /**
   * Constructor.
   * 
   * @param container 	The container.
   * @param name 	The name of this actor.
   */
    public OutputLogger(CompositeEntity container, String name) throws NameDuplicationException, IllegalActionException {
        super(container, name);
        logFile = new ExtFileParameter(this, "logFile");
        logFile.setExpression("%t" + File.separator + "keplerweka.log");
        enabled = new Parameter(this, "enabled", new BooleanToken(true));
        enabled.setTypeEquals(BaseType.BOOLEAN);
        append = new Parameter(this, "append", new BooleanToken(false));
        append.setTypeEquals(BaseType.BOOLEAN);
    }

    /**
   * Initializes the actor.
   * 
   * @throws IllegalActionException	not here
   */
    public void initialize() throws IllegalActionException {
        File logfile;
        boolean appnd;
        boolean enabld;
        super.initialize();
        logfile = logFile.asFile();
        appnd = ((BooleanToken) append.getToken()).booleanValue();
        enabld = ((BooleanToken) enabled.getToken()).booleanValue();
        if (logfile.exists() && !appnd) logfile.delete();
        if (_logger != null) _logger.setLogFile(logfile); else _logger = new KeplerLogger(logfile);
        _logger.setEnabled(enabld);
        if (enabld && appnd) System.out.println("--- Log-trace: " + new Date());
    }

    /** 
   * Post fire the actor. Return false to indicate that the
   * process has finished. If it returns true, the process will
   * continue indefinitely.
   * 
   * @return		always false
   */
    public boolean postfire() {
        return false;
    }
}
