package xplanetconfigurator.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom
 */
public class XPlanetTimer extends Thread {

    private static int instanceNumber;

    private Logger logger;

    private MainFrame caller;

    private int seconds;

    private boolean run = true;

    private String command;

    private FeedbackReceiver feedbackReceiver;

    /**
     *
     * @param caller
     * @param seconds to sleep until the next call of the command. If seconds < 1: run the command once and quit.
     * @param command
     */
    public XPlanetTimer(MainFrame caller, int seconds, String command) {
        this.caller = caller;
        this.seconds = seconds;
        this.command = command;
        this.logger = Logger.getLogger(this.getClass().getName());
        instanceNumber++;
        this.logger.finer("Created XPlanetTimer. (Instance '" + instanceNumber + "')");
    }

    public void stopRuning() {
        this.run = false;
        this.logger.finer("XPlanetTimer was stopped. (Instance '" + instanceNumber + "')");
        this.logger.finer("Setting caller (MainFrame) to null...");
        this.caller = null;
    }

    public void exec() {
        this.logger.fine("About to execute command '" + this.command + "' ... (Instance '" + instanceNumber + "')");
        Runtime runtime = Runtime.getRuntime();
        try {
            this.logger.finest("Creating Process for command: " + this.command);
            Process proc = runtime.exec(this.command);
            this.logger.finest("Getting input stream for process...");
            InputStream in = proc.getInputStream();
            ActionLogger inLogger = new ActionLogger(in, false);
            inLogger.run();
            this.logger.finest("Getting error stream for process...");
            InputStream err = proc.getErrorStream();
            ActionLogger errorLogger = new ActionLogger(err, true);
            errorLogger.run();
            this.logger.finer("Start to wait for process for command: " + this.command);
            proc.waitFor();
            int i = proc.exitValue();
            this.logger.finest("Exit value of process was: " + i);
            if (i != 0) {
                this.logger.warning("Command was not executed successfully.\n" + "Exit Value was: " + i + "\nTry to execute the command from the command line.");
                this.stopRuning();
            } else {
                this.logger.fine("Command was  executed successfully.\n" + "Exit Value was: " + i);
                this.sendFeedback();
            }
            if (this.seconds < 1) {
                this.logger.fine("Timer was stopped after one execution of XPlanet because the seconds where set to " + this.seconds);
                this.stopRuning();
            }
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, "Failed to execute command '" + command + "'. (Instance '" + instanceNumber + "')", e);
            this.logger.log(Level.SEVERE, "Exception is: '" + e.toString() + "'", e);
            LoggingBoard.logXPlanetMessage("Failed to execute command '" + command + "'. (Instance '" + instanceNumber + "')");
            LoggingBoard.logXPlanetMessage("Exception is: '" + e.toString() + "'");
            this.stopRuning();
        } finally {
        }
    }

    @Override
    public void run() {
        this.logger.finer("Start running...  (Instance '" + instanceNumber + "')");
        try {
            while (this.run) {
                if (this.run) {
                    this.exec();
                    this.logger.finer("Waiting " + this.seconds + " seconds... (Instance '" + instanceNumber + "')");
                    sleep(seconds * 1000);
                    this.logger.finer("Timer woke up after sleeping " + this.seconds + " seconds... (Instance '" + instanceNumber + "')");
                } else {
                    this.logger.finer("Was stopped befor next execution of XPlanet. About to stop this timer... (Instance '" + instanceNumber + "')");
                }
            }
        } catch (InterruptedException ex) {
            logger.log(Level.FINEST, null, ex);
        }
    }

    /**
     * Send a feedback the GUI
     * @param o
     */
    private void sendFeedback() {
        if (this.getFeedbackReceiver() != null) {
            this.logger.finer("Sending feedback to the GUI...");
            this.getFeedbackReceiver().receiveXPlanetFeedback();
        }
    }

    /**
     * Used by the GUI only
     *
     * @return the feedbackReceiver
     */
    public FeedbackReceiver getFeedbackReceiver() {
        return feedbackReceiver;
    }

    /**
     * Used by the GUI only
     *
     * @param feedbackReceiver the feedbackReceiver to set
     */
    public void setFeedbackReceiver(FeedbackReceiver feedbackReceiver) {
        this.feedbackReceiver = feedbackReceiver;
    }

    public class ActionLogger extends Thread {

        private BufferedReader buf;

        boolean isErrStream;

        public ActionLogger(InputStream in, boolean isErrStream) {
            this.buf = new BufferedReader(new InputStreamReader(in));
            this.isErrStream = isErrStream;
            logger.finest("Created ActionLogger... (Instance '" + instanceNumber + "')");
        }

        @Override
        public void run() {
            String what = "system.out";
            if (this.isErrStream) {
                what = "system.err";
            }
            logger.finer("Start logging " + what + " of command '" + command + "'... (Instance '" + instanceNumber + "')");
            String line = "";
            try {
                logger.finest("Start reading the input stream... (Instance '" + instanceNumber + "')");
                while ((line = buf.readLine()) != null) {
                    if (this.isErrStream) {
                        logger.finest("Did read line from error stream: " + line);
                        LoggingBoard.logXPlanetMessage("std.err - " + line);
                    } else {
                        logger.finest("Did read line from input stream: " + line);
                        LoggingBoard.logXPlanetMessage("std.out - " + line);
                    }
                }
                logger.finest("Stopped reading the input stream. Input stream is at end  (Instance '" + instanceNumber + "')");
                this.buf.close();
                this.buf = null;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } finally {
                if (this.buf != null) {
                    try {
                        this.buf.close();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
