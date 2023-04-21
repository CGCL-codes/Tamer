package frost.fcp.fcp07.messagetransfer;

import java.io.*;
import frost.fcp.*;

public class MessageTransferTask {

    public static final int MODE_DOWNLOAD = 1;

    public static final int MODE_UPLOAD = 2;

    private final int transferMode;

    private final String identifier;

    private final String key;

    private final File file;

    private final int maxSize;

    private final int priority;

    private final int maxRetries;

    private boolean taskFinished = false;

    FcpResultPut putResult = null;

    FcpResultGet getResult = null;

    /**
     * Construct task for DOWNLOAD
     */
    public MessageTransferTask(final String id, final String key, final File targetFile, final int prio, final int maxSize, final int maxRetries) {
        transferMode = MODE_DOWNLOAD;
        identifier = id;
        this.key = key;
        this.file = targetFile;
        this.maxSize = maxSize;
        this.priority = prio;
        this.maxRetries = maxRetries;
    }

    /**
     * Construct task for UPLOAD
     */
    public MessageTransferTask(final String id, final String key, final File sourceFile, final int prio) {
        transferMode = MODE_UPLOAD;
        identifier = id;
        this.key = key;
        this.file = sourceFile;
        this.maxSize = -1;
        this.priority = prio;
        this.maxRetries = -1;
    }

    public String getIdentifier() {
        return identifier;
    }

    public File getFile() {
        return file;
    }

    public String getKey() {
        return key;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Called by MessageThread to wait for a message.
     */
    public synchronized void waitForFinished() {
        if (taskFinished) {
            return;
        }
        try {
            wait();
        } catch (final InterruptedException e) {
        }
    }

    /**
     * Called by NodeMessageHandler when task is finished.
     */
    public synchronized void setFinished() {
        taskFinished = true;
        notifyAll();
    }

    public boolean isModeUpload() {
        return transferMode == MODE_UPLOAD;
    }

    public boolean isModeDownload() {
        return transferMode == MODE_DOWNLOAD;
    }

    public FcpResultPut getFcpResultPut() {
        return putResult;
    }

    public void setFcpResultPut(final FcpResultPut putResult) {
        this.putResult = putResult;
    }

    public FcpResultGet getFcpResultGet() {
        return getResult;
    }

    public void setFcpResultGet(final FcpResultGet getResult) {
        this.getResult = getResult;
        if (!getResult.isSuccess()) {
            if (getFile().isFile()) {
                getFile().delete();
            }
        }
    }

    /**
     * Quickly fail because socket was disconnected.
     */
    public void setFailed() {
        if (isModeDownload()) {
            this.getResult = FcpResultGet.RESULT_FAILED;
        } else {
            this.putResult = FcpResultPut.NO_CONNECTION_RESULT;
        }
    }

    public int getPriority() {
        return priority;
    }
}
