package com.mongus.stripes;

import java.util.List;
import java.util.Vector;
import org.apache.commons.fileupload.ProgressListener;

public class UploadProgress implements ProgressListener {

    private long bytesRead, contentLength;

    private int items;

    private boolean reported = false, complete = false;

    private long startTime = System.currentTimeMillis(), endTime = 0;

    private String error;

    private List<String> filenames;

    public void update(long pBytesRead, long pContentLength, int pItems) {
        this.bytesRead = pBytesRead;
        this.contentLength = pContentLength;
        this.items = pItems;
        if (bytesRead == contentLength) setComplete(true);
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public int getItems() {
        return items;
    }

    public double getPercentage() {
        if (contentLength == 0) return 0;
        return bytesRead / (double) contentLength;
    }

    public boolean isReported() {
        if (reported) return true;
        reported = true;
        return false;
    }

    public boolean isComplete() {
        return complete;
    }

    public long getElapsedTime() {
        return (endTime != 0 ? endTime : System.currentTimeMillis()) - startTime;
    }

    public void setComplete(boolean complete) {
        endTime = System.currentTimeMillis();
        this.complete = complete;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getFilenames() {
        if (filenames == null) filenames = new Vector<String>();
        return filenames;
    }
}
