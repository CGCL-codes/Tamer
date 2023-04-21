package com.germinus.portlet.file_directory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.germinus.xpression.cms.CmsConfig;
import com.germinus.xpression.cms.directory.MaxSizeExceededException;
import com.germinus.xpression.cms.web.TemporaryFilesHandler;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import com.germinus.xpression.cms.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Downloader {

    private static Log log = LogFactory.getLog(Downloader.class);

    private HttpClient httpClient;

    private String contentType;

    private int size;

    private ProgressListener progressListener;

    public Downloader() {
        this.httpClient = new HttpClient();
    }

    public Downloader(ProgressListener progressListener) {
        this();
        this.progressListener = progressListener;
    }

    public File downloadToFile(String remoteUrl) throws MaxSizeExceededException {
        File downloadedFile = null;
        HttpMethod method = new GetMethod(remoteUrl);
        OutputStream os = null;
        boolean biggerThanLimit = Boolean.FALSE;
        try {
            httpClient.executeMethod(method);
            InputStream responseBodyAsStream = method.getResponseBodyAsStream();
            Header contentType = method.getResponseHeader("content-type");
            Header contentLengthHeader = method.getResponseHeader("Content-Length");
            long contentLength = contentLengthHeader != null ? Long.valueOf(contentLengthHeader.getValue()) : 0;
            long maxFileSize = CmsConfig.getMaxSizeForRegularFile();
            if (contentLength > maxFileSize) {
                biggerThanLimit = Boolean.TRUE;
                log.warn("Remote file is bigger than the limit.Aborting");
                return null;
            }
            if (contentType != null) this.contentType = contentType.getValue();
            File tempFile = File.createTempFile("file", "temp");
            TemporaryFilesHandler.register(null, tempFile);
            os = new FileOutputStream(tempFile);
            this.size = FileUtil.dumpInputStreamToOutputStream(responseBodyAsStream, os, true, this.progressListener, contentLength);
            downloadedFile = tempFile;
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (biggerThanLimit) throw new MaxSizeExceededException();
            if (os != null) try {
                os.close();
            } catch (IOException e) {
            }
            method.releaseConnection();
        }
        return downloadedFile;
    }

    public String getContentType() {
        return contentType;
    }

    public int getSize() {
        return size;
    }
}
