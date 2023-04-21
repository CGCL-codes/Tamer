package picasatagstopictures.scan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import picasatagstopictures.gui.LoggingBoard;

/**
 *
 * @author Tom Wiedenhoeft, GPL v3
 */
public class ExiftoolCaller extends Thread {

    public static final String ERR_STREAM = "ERR.STREAM";

    private Logger logger;

    private String command;

    private IFeedback feedback;

    private InputStream err = null;

    private InputStream in = null;

    private LoggingBoard loggingBoard;

    private static int threadNumber = -1;

    public ExiftoolCaller(IFeedback feedbackObject) {
        threadNumber++;
        this.logger = Logger.getLogger(this.getClass().getName());
        this.feedback = feedbackObject;
        this.loggingBoard = LoggingBoard.getInstance();
    }

    /**
     * @return the threadNumber
     */
    public static int getThreadNumber() {
        return threadNumber;
    }

    @Override
    public void run() {
        this.runCommandAndWaitFor(true);
    }

    private void runCommandAndWaitFor(boolean useTaskKill) {
        logger.log(Level.FINE, "Thread Nr. {0} - About to execute command ''{1}'' ...", new Object[] { threadNumber, this.command });
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(this.command);
            this.err = proc.getErrorStream();
            ActionLogger errorLogger = new ActionLogger(err, true, this.feedback);
            errorLogger.start();
            this.in = proc.getInputStream();
            ActionLogger inLogger = new ActionLogger(in, false, this.feedback);
            inLogger.start();
            proc.waitFor();
            int exitValue = proc.exitValue();
            if (exitValue != 0) {
                this.logger.log(Level.WARNING, "Thread Nr. {0}" + " - Command was not executed successfully. " + "Exit Value was: {1} Try to execute the command from the command line. The command was: {2}", new Object[] { threadNumber, exitValue, this.command });
            }
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, "Thread Nr. " + threadNumber + " - Failed to execute command '" + command + "'.", e);
            this.loggingBoard.logMessage("Thread Nr. " + threadNumber + " - Failed to execute command '" + command + "'.");
        } finally {
            this.cleanUp(useTaskKill);
        }
    }

    /** Creates a new instance of CommandAction */
    public void setCommand(String command) {
        this.command = command;
    }

    private void cleanUp(boolean useTaskKill) {
        this.logger.log(Level.FINER, "Thread Nr. {0} - Cleaning up...", threadNumber);
        if (this.in != null) {
            try {
                this.in.close();
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, "Thread Nr. " + threadNumber + " - ", ex);
            }
        }
        if (this.err != null) {
            try {
                this.err.close();
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, "Thread Nr. " + threadNumber + " - ", ex);
            }
        }
        if (useTaskKill) {
            this.killExiftool();
        }
    }

    /**
     * This works for Windows only.
     */
    private void killExiftool() {
        this.setCommand("taskkill /F /FI \"IMAGENAME eq exiftool*\"");
        this.runCommandAndWaitFor(false);
    }

    public boolean canExecuteCommand(String cmd) {
        logger.log(Level.FINE, "Thread Nr. {0} - About to execute command to test it ''{1}'' ...", new Object[] { threadNumber, this.command });
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(this.command);
            proc.waitFor();
            int exitValue = proc.exitValue();
            if (exitValue != 0) {
                this.logger.log(Level.WARNING, "Thread Nr. {0}" + " - Command was not executed successfully.\n" + "Exit Value was: {1}\nTry to execute the command from the command line.", new Object[] { threadNumber, exitValue });
                return false;
            }
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Thread Nr. " + threadNumber + " - Failed to execute command '" + command + "'.", e.getMessage());
            this.loggingBoard.logMessage("Failed to execute command '" + command + "'. " + e.getMessage());
            return false;
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, "Thread Nr. " + threadNumber + " - Failed to execute command '" + command + "'.", e.getMessage());
            this.loggingBoard.logMessage("Failed to execute command '" + command + "'. " + e.getMessage());
            return false;
        } finally {
            this.cleanUp(false);
        }
        return true;
    }

    public class ActionLogger extends Thread {

        private BufferedReader buf;

        private IFeedback feedback;

        boolean isErrStream;

        private Logger logger = Logger.getLogger(this.getClass().getName());

        public ActionLogger(InputStream in, boolean isErrStream, IFeedback feedbackObject) {
            logger.log(Level.FINE, "Thread Nr. {0} - Intializing ActionLogger...", threadNumber);
            this.buf = new BufferedReader(new InputStreamReader(in));
            this.feedback = feedbackObject;
            this.isErrStream = isErrStream;
        }

        @Override
        public void run() {
            String what = "SYSTEM.OUT";
            if (this.isErrStream) {
                what = ExiftoolCaller.ERR_STREAM;
            }
            logger.log(Level.FINER, "Thread Nr. {0} - Start logging ''{1}'' of command...", new Object[] { threadNumber, what });
            loggingBoard.logMessage("Called '" + command + "' by Thread Nr. " + threadNumber);
            String line = "";
            try {
                while ((line = buf.readLine()) != null) {
                    loggingBoard.logMessage(what + " - " + line);
                    if (this.isErrStream) {
                        this.feedback.receiveFeedback(what + " - " + line);
                    } else {
                        this.feedback.receiveFeedback(line);
                    }
                }
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, "Thread Nr. " + threadNumber + " - Error reading a line from " + what + ". ", ex.getMessage());
            } catch (Exception ex) {
                this.logger.log(Level.SEVERE, "Thread Nr. " + threadNumber + " - Error reading a line from " + what + ". ", ex.getMessage());
            } finally {
            }
        }
    }
}
