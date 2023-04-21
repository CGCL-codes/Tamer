package org.vikulin.ui;

import java.io.File;
import java.util.Observable;
import javax.swing.JOptionPane;
import org.jdamico.jhu.components.FilePartition;
import org.jdamico.jhu.utils.Helper;
import org.vikulin.utils.Constants;

public class Upload extends Observable implements Runnable {

    public static final String STATUSES[] = { "Uploading", "Paused", "Complete", "Cancelled", "Error", "Splitting", "Joining", "Calculating checksums" };

    public static final int UPLOADING = 0;

    public static final int PAUSED = 1;

    public static final int COMPLETE = 2;

    public static final int CANCELLED = 3;

    public static final int ERROR = 4;

    public static final int SPLITTING = 5;

    public static final int JOINING = 6;

    public static final int CALCULATION_CHECKSUMS = 7;

    private File file;

    private long size;

    private int status;

    private int progress;

    private UploadManager uploadManager;

    private volatile Thread thread;

    public Upload(File file, UploadManager uploadManager) {
        this.file = file;
        this.uploadManager = uploadManager;
        size = -1l;
        upload();
    }

    public String getUrl() {
        return file.toString();
    }

    public long getSize() {
        return size;
    }

    public float getProgress() {
        return progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (this.status == ERROR) return;
        this.status = status;
        if (status != COMPLETE) setProgress(0); else stateChanged();
    }

    public void pause() {
        status = PAUSED;
        stateChanged();
    }

    public void resume() {
        status = UPLOADING;
        stateChanged();
        upload();
    }

    public void cancel() {
        status = CANCELLED;
        if (thread != null) thread.interrupt();
        stateChanged();
    }

    public void error() {
        status = ERROR;
        stateChanged();
    }

    public void error(String message) {
        status = ERROR;
        stateChanged();
        JOptionPane.showMessageDialog(uploadManager, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void upload() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        if (size == -1) {
            size = file.length();
            stateChanged();
        }
        FilePartition partitioning = new FilePartition(this);
        try {
            int partSize = Constants.conf.getChunkSize();
            System.out.println("Running as client [Remote Host: " + Helper.getServerAddress());
            partitioning.start(file, partSize);
        } catch (Exception e) {
            System.out.println("Unable to run client with these parameters.");
            e.printStackTrace();
        }
    }

    private void stateChanged() {
        setChanged();
        notifyObservers();
    }

    public synchronized void setProgress(int percent) {
        this.progress = percent;
        stateChanged();
    }

    public UploadManager getUploadManager() {
        return uploadManager;
    }
}
