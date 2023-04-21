package org.langkiss.gui;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.langkiss.util.FileUtil;
import org.langkiss.util.TimeStamp;

/**
 *
 * @author tom
 */
public class LoggingBoard {

    private static LoggingBoard instance;

    private JTextArea jTextArea;

    private String text = new String();

    private String logFile;

    private boolean isLogFileBusy = false;

    private int messageCounter;

    /**
     * @return the logFile as absolute path
     */
    public String getLogFile() {
        return logFile;
    }

    /**
     * @param aLogFile the logFile as absolute path
     */
    public void setLogFile(String aLogFile) {
        logFile = aLogFile;
    }

    private LoggingBoard() {
    }

    public static LoggingBoard getInstance() {
        if (instance == null) {
            instance = new LoggingBoard();
        }
        return instance;
    }

    /**
     * 
     * @param message
     */
    public synchronized void logMessage(String message) {
        Logger.getLogger(LoggingBoard.class.getName()).log(Level.FINER, "Got message: {0}", message);
        String timeStamp = TimeStamp.getFullTimstamp();
        if (this.messageCounter++ < 10000) {
            if (text.length() > 0) {
                text = text + "\n" + timeStamp + " - " + message;
            } else {
                text = timeStamp + " - " + message;
            }
        } else {
            this.messageCounter = 0;
            text = timeStamp + " - " + message;
        }
        if (jTextArea != null) {
            Logger.getLogger(LoggingBoard.class.getName()).finer("Set the text to the jTextArea...");
            jTextArea.setText(text);
            jTextArea.setCaretPosition(text.length());
        }
        if (logFile != null) {
            if (this.isLogFileBusy == true) {
                Logger.getLogger(LoggingBoard.class.getName()).log(Level.WARNING, "Logfile is busy at the moment. Could not write the message ''{0}'' to file ''{1}''. This is not a good thing but anyway this feature is only used for junit testing. It might only cause errors while tessting.", new Object[] { message, logFile });
                return;
            }
            Logger.getLogger(LoggingBoard.class.getName()).log(Level.FINER, "Writing the text to file ''{0}''...", logFile);
            FileUtil fUtil = new FileUtil();
            try {
                this.isLogFileBusy = true;
                fUtil.appendLineToFile(logFile, timeStamp + " - " + message);
            } catch (Exception ex) {
                Logger.getLogger(LoggingBoard.class.getName()).log(Level.SEVERE, null, ex);
                Logger.getLogger(LoggingBoard.class.getName()).log(Level.SEVERE, "The Logging Board stopps to write messages to file ''{0}'' because it failed to write at least one message.", logFile);
                logFile = null;
            } finally {
                this.isLogFileBusy = false;
            }
        }
    }

    /**
     * Is only used for junit testing.
     * 
     * @return null if something went wrong (file might be busy or reading has
     * thrown an exception). Return an empty string if the file is empty
     */
    public synchronized String getLogFileContent() {
        if (logFile == null) {
            return "";
        }
        if (this.isLogFileBusy == true) {
            Logger.getLogger(LoggingBoard.class.getName()).log(Level.WARNING, "Logfile is busy at the moment. Could not read the messages from file ''{0}''. This is not a good thing but anyway this feature is only used for junit testing. It might only cause errors while tessting.", logFile);
            return null;
        }
        FileUtil fUtil = new FileUtil();
        String fileContent = null;
        try {
            this.isLogFileBusy = true;
            fileContent = fUtil.getFileAsString(new File(this.logFile));
        } catch (Exception ex) {
            Logger.getLogger(LoggingBoard.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.isLogFileBusy = false;
        }
        return fileContent;
    }

    /**
     * Clears the text text area used for log messages.
     */
    public void clear() {
        Logger.getLogger(LoggingBoard.class.getName()).finer("Clearing the jTextArea.");
        text = "";
        if (jTextArea != null) {
            jTextArea.setText(text);
        }
        if (logFile != null) {
            File f = new File(logFile);
            if (!f.exists()) {
                Logger.getLogger(LoggingBoard.class.getName()).finer("No file to cleanup because the file does not exist. (This is not an error.)");
            } else {
                boolean wasDeleted = f.delete();
                if (!wasDeleted) {
                    Logger.getLogger(LoggingBoard.class.getName()).log(Level.WARNING, "File ''{0}'' could not be deleted at cleanup.", logFile);
                } else {
                    Logger.getLogger(LoggingBoard.class.getName()).log(Level.FINER, "File ''{0}'' was deleted at cleanup.", logFile);
                }
            }
        }
    }

    /**
     * @param jTextArea the jTextArea to set
     */
    public void setjTextArea(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }
}
