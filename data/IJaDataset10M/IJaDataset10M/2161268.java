package com.litt.core.file.ajaxupload;

public class UploadInfo {

    private long totalSize = 0;

    private long bytesRead = 0;

    private long elapsedTime = 0;

    private String status = "done";

    private int fileIndex = 0;

    public UploadInfo() {
        this.status = "start";
    }

    public UploadInfo(int fileIndex, long totalSize, long bytesRead, long elapsedTime, String status) {
        this.fileIndex = fileIndex;
        this.totalSize = totalSize;
        this.bytesRead = bytesRead;
        this.elapsedTime = elapsedTime;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setInProgress(boolean b) {
    }

    public boolean getInProgress() {
        return "progress".equals(status) || "start".equals(status);
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }
}
