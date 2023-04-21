package org.lc.support.ajaxupload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;

public class UploadListener implements OutputStreamListener {

    private HttpServletRequest request;

    private long delay = 0;

    private long startTime = 0;

    private int totalToRead = 0;

    private int totalBytesRead = 0;

    private int totalFiles = -1;

    private static final Log log = LogFactory.getLog(UploadListener.class);

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
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void error(String message) {
        updateUploadInfo("error");
    }

    public void done() {
        updateUploadInfo("done");
    }

    private void updateUploadInfo(String status) {
        long delta = (System.currentTimeMillis() - startTime) / 1000;
        request.getSession().setAttribute("uploadInfo", new UploadInfo(totalFiles, totalToRead, totalBytesRead, delta, status));
    }
}
