package org.thesandbox.itask.tasks;

import org.thesandbox.itask.ITaskApp;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Shutdown ITask. This class this be extended with platform specific
 * implementations that spawn process to shutdown/logoff a system.
 *
 * Created by IntelliJ IDEA.
 *
 * Date: 12-Dec-2009
 * @author jrxrs
 */
public class Shutdown extends ITask {

    private static Logger logger = Logger.getLogger(Shutdown.class.getCanonicalName());

    public enum Option {

        SHUTDOWN(" -s", " "), RESTART(" -r", " "), LOG_OFF(" -l", " "), SLEEP(" PowrProf.dll,SetSuspendState Sleep", " "), HIBERNATE(" PowrProf.dll,SetSuspendState", " "), LOCK(" user32.dll,LockWorkStation", " ");

        private String winArg;

        private String unixArg;

        private Option(String winArg, String unixArg) {
            this.winArg = winArg;
            this.unixArg = unixArg;
        }

        public String getWinArg() {
            return winArg;
        }

        public String getUnixArg() {
            return unixArg;
        }
    }

    public Shutdown(String taskName) {
        super(taskName);
    }

    private Option opt;

    private StringBuffer optString;

    private String winCmd;

    private final String winComment = " -c \"iTask Shutdown\"";

    private final String unixCmd = "shutdown";

    public Shutdown(Option opt) {
        super("holder");
        final String winCmdShutdown = "shutdown.exe -f";
        final String winCmdRunDLL = "Rundll32.exe";
        this.opt = opt;
        optString = new StringBuffer();
        switch(opt) {
            case SHUTDOWN:
                setTaskName("Shutdown");
                winCmd = winCmdShutdown;
                break;
            case RESTART:
                setTaskName("Restart");
                winCmd = winCmdShutdown;
                break;
            case LOG_OFF:
                setTaskName("Log Off");
                winCmd = winCmdShutdown;
                break;
            case SLEEP:
                setTaskName("Sleep");
                winCmd = winCmdRunDLL;
                break;
            case HIBERNATE:
                setTaskName("Hibernate");
                winCmd = winCmdRunDLL;
                break;
            case LOCK:
                setTaskName("Lock");
                winCmd = winCmdRunDLL;
                break;
            default:
                break;
        }
    }

    public void run() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            winShutdown();
        } else if (os.contains("Linux")) {
            unixShutdown();
        } else if (os.contains("Mac")) {
        } else {
            logger.log(Level.SEVERE, "Operating System: " + os + " is not supported!");
        }
    }

    private void winShutdown() {
        logger.log(Level.INFO, "Execute Task: " + getTaskName());
        try {
            Runtime rt = Runtime.getRuntime();
            optString.append(opt.getWinArg());
            String exec = winCmd + optString.toString() + winComment;
            logger.log(Level.INFO, "Command: " + exec);
            Process p = rt.exec(exec);
            try {
                p.waitFor();
            } catch (InterruptedException ie) {
                logger.log(Level.WARNING, "Shutdown Interrupted", ie);
            }
            ITaskApp.getApplication().exit();
        } catch (IOException ioe) {
            logger.log(Level.WARNING, "IO Error shutting down!", ioe);
        }
    }

    private void unixShutdown() {
        logger.log(Level.INFO, "Execute Task: " + getTaskName());
        try {
            Runtime rt = Runtime.getRuntime();
            optString.append(opt.getUnixArg());
            logger.log(Level.INFO, "Command: " + winCmd + optString.toString() + winComment);
            Process p = rt.exec(winCmd + optString.toString() + winComment);
            try {
                p.waitFor();
            } catch (InterruptedException ie) {
                logger.log(Level.WARNING, "Shutdown Interrupted", ie);
            }
            ITaskApp.getApplication().exit();
        } catch (IOException ioe) {
            logger.log(Level.WARNING, "IO Error shutting down!", ioe);
        }
    }
}
