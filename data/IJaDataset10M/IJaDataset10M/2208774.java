package com.burry.ajax.upload;

import javax.servlet.http.HttpServletRequest;

public class UploadListener implements OutputStreamListener {

    private HttpServletRequest request;

    private long delay = 0;

    private long startTime = 0;

    private int totalToRead = 0;

    private int totalBytesRead = 0;

    private int totalFiles = -1;

    public UploadListener(HttpServletRequest request, long debugDelay) {
        this.request = request;
        this.delay = debugDelay;
        totalToRead = request.getContentLength();
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        totalFiles++;
        updateUploadInfo("start");
    }

    public void bytesRead(int bytesRead) {
        totalBytesRead = totalBytesRead + bytesRead;
        updateUploadInfo("progress");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void error(String message) {
        updateUploadInfo("error");
    }

    public void done() {
        updateUploadInfo("done");
    }

    public long getDelta() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    private void updateUploadInfo(String status) {
        long delta = (System.currentTimeMillis() - startTime) / 1000;
        request.getSession().setAttribute("uploadInfo", new UploadInfo(totalFiles, totalToRead, totalBytesRead, delta, status));
    }
}
