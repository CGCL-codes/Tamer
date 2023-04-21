package com.worldware.misc;

import java.io.File;
import java.io.*;
import com.worldware.misc.Log;

public class LogFile extends Log {

    File m_logFile = null;

    static String lf;

    /** Creates a log object. This class logs to a file. */
    public LogFile(File logFile) {
        lf = System.getProperty("line.separator", "\n");
        m_logFile = logFile;
    }

    public synchronized void write(String logMsg) {
        boolean rc = true;
        DataOutputStream dos;
        try {
            RandomAccessFile rLog = new RandomAccessFile(m_logFile, "rw");
            rLog.seek(rLog.length());
            rLog.writeBytes(logMsg + lf);
            rLog.close();
        } catch (IOException e) {
            System.out.println("error writing to " + m_logFile + ": " + e);
            System.out.println("LogError + " + logMsg);
        }
    }

    /** Gets the current contents of the log file as a string (first 128 K)*/
    public synchronized String getLogFileContents(int size, long offset) {
        byte[] b = new byte[size];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(m_logFile);
            if (offset != 0) offset = fis.skip(offset);
            int count = fis.read(b);
            if (count == -1) return "no data";
            return new String(b, 0, count);
        } catch (IOException ioe) {
            return "LogFile.getLogFileContents: " + ioe.toString();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ioe) {
            }
        }
    }

    /** Gets the current contents of the log file as a string (first 128 K)*/
    public synchronized long getLogFileSize() {
        return m_logFile.length();
    }

    public synchronized boolean deleteLogFile() {
        return m_logFile.delete();
    }
}
