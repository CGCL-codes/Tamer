package org.kdev.wtf;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.ArrayList;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;

public class LogFileWatcher implements JNotifyListener {

    /**
	 *  Obserwowany plik 
	 */
    private final File logfile;

    /**
	 * Metchod that determine if error is in log line
	 */
    private Method hasLogLineErrorMethod = null;

    /**
	 * Collection with lines with errors
	 */
    private ArrayList<String> errorLines;

    private long filePointer;

    private long fileLength;

    private RandomAccessFile raFile;

    private int watchID;

    private int lineNumber = 0;

    /**
	 * A new line has been added to the tailed log file
	 *
	 * @param line
	 *            The new line that has been added to the tailed log file
	 */
    public void newLogFileLine(String line, int lineNumber) {
        try {
            if ((Boolean) this.hasLogLineErrorMethod.invoke(null, line)) {
                this.errorLines.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error while invoking hasLogLineErrorMethod()");
            e.printStackTrace();
        }
    }

    /**
	 * Creates a new log file tailer
	 *
	 * @param file
	 *            The file to tail
	 * @param interval
	 *            How often to check for updates to the log file (default =
	 *            5000ms)
	 * @param checkErrorMethod
	 *            Method that detremines if error in log line ocour
	 *
	 */
    public LogFileWatcher(File file, long interval, Method hasLogLineError) {
        this.logfile = file;
        this.filePointer = this.logfile.length();
        this.hasLogLineErrorMethod = hasLogLineError;
        this.errorLines = new ArrayList<String>();
        int mask = JNotify.FILE_MODIFIED;
        boolean watchSubtree = false;
        try {
            raFile = new RandomAccessFile(logfile, "r");
            watchID = JNotify.addWatch(logfile.getCanonicalPath(), mask, watchSubtree, this);
            while (raFile.readLine() != null) lineNumber++;
        } catch (Exception e) {
            System.err.println("JNotify Exception");
            e.printStackTrace();
        }
    }

    public Boolean hasLogError() {
        if (this.errorLines.size() > 0) return true;
        return false;
    }

    public void stopWatching() {
        try {
            raFile.close();
            JNotify.removeWatch(watchID);
        } catch (IOException e) {
            System.err.println("LogFileWatcher exception!");
        }
    }

    private void check_tail() {
        try {
            if (!logfile.exists() || raFile.length() < filePointer) {
                raFile = new RandomAccessFile(logfile, "r");
                filePointer = 0;
            } else if (raFile.length() > filePointer) {
                raFile.seek(filePointer);
                String line = raFile.readLine();
                while (line != null) {
                    this.newLogFileLine(line, lineNumber);
                    line = raFile.readLine();
                    lineNumber++;
                }
                filePointer = raFile.getFilePointer();
            }
        } catch (Exception e) {
            System.err.println("LogFileWatcher exception!");
        }
    }

    public void fileCreated(int arg0, String arg1, String arg2) {
    }

    public void fileDeleted(int arg0, String arg1, String arg2) {
    }

    public void fileRenamed(int arg0, String arg1, String arg2, String arg3) {
    }

    public void fileModified(int arg0, String arg1, String arg2) {
        check_tail();
    }
}
