package com.jsystem.j2autoit.history;

import java.io.File;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import com.jsystem.j2autoit.logger.Log;

/**
 * @author Kobi Gana
 *
 */
public class HistoryFileThread extends Thread {

    private Vector<String> pail = new Vector<String>();

    private int history_Size = 1000;

    private boolean active = true;

    private TimeUnit sleepTimeUnit = TimeUnit.MINUTES;

    private long sleepTimeOut = 5;

    public HistoryFileThread() {
    }

    @Override
    public void run() {
        while (active) {
            if (history_Size < pail.size()) {
                deleteVector(pail.subList(0, pail.size() - (history_Size + 1)));
            }
            try {
                sleepTimeUnit.sleep(sleepTimeOut);
            } catch (InterruptedException exception) {
            }
        }
    }

    public boolean containEntries() {
        return !pail.isEmpty();
    }

    public void shutdownNow() {
        this.active = false;
        interrupt();
    }

    public void setSleepTime(TimeUnit sleepTimeUnit, long sleepTimeOut) {
        this.sleepTimeUnit = sleepTimeUnit;
        this.sleepTimeOut = sleepTimeOut;
    }

    public void addFile(String fileName) {
        pail.add(fileName);
    }

    public int getHistory_Size() {
        return this.history_Size;
    }

    public void setHistory_Size(int size) {
        this.history_Size = size;
    }

    public void forceDeleteAll() {
        deleteVector(pail);
    }

    private void deleteVector(Collection<String> list) {
        File currentFile = null;
        for (String currentFileName : list) {
            try {
                if ((currentFile = new File(currentFileName)).delete()) {
                    Log.infoLog(currentFileName + " deleted successfully\n");
                } else {
                    if (currentFile.exists()) {
                        Log.errorLog(currentFileName + " failed to deleted\n");
                    } else {
                        Log.warningLog(currentFileName + " file not exist\n");
                    }
                }
            } catch (Exception exception) {
                Log.throwableLog(exception.getMessage() + "\n", exception);
            }
            currentFile = null;
        }
        list.clear();
    }
}
