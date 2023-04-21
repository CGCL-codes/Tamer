package uk.co.akademy.Downloader;

import java.io.*;
import java.net.*;
import java.util.*;

public class Download extends Observable implements Runnable {

    private static final int MAX_BUFFER_SIZE = 1024;

    public static final String STATUSES[] = { "Downloading", "Paused", "Complete", "Cancelled", "Error", "Waiting" };

    public static final int DOWNLOADING = 0;

    public static final int PAUSED = 1;

    public static final int COMPLETE = 2;

    public static final int CANCELLED = 3;

    public static final int ERROR = 4;

    public static final int WAITING = 5;

    private String _downloadFolder;

    private String _storeFolder;

    private URL _url;

    private int size;

    private int downloaded;

    private int status;

    public Download(URL url, String downloadFolder, String storeFolder) {
        this._url = url;
        this._downloadFolder = downloadFolder;
        this._storeFolder = storeFolder;
        size = -1;
        downloaded = 0;
        status = WAITING;
    }

    public String getUrl() {
        return _url.toString();
    }

    public int getSize() {
        return size;
    }

    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    public int getStatus() {
        return status;
    }

    public boolean isComplete() {
        return (status == COMPLETE);
    }

    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    public String getDownloadedFilePosition() {
        return _downloadFolder + getFileName(_url);
    }

    public String getStoreFilePosition() {
        return _storeFolder + getFileName(_url);
    }

    public void pause() {
        status = PAUSED;
        stateChanged();
    }

    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }

    private void error() {
        status = ERROR;
        stateChanged();
    }

    public void download() {
        status = DOWNLOADING;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }
            file = new RandomAccessFile(_downloadFolder + getFileName(_url), "rw");
            file.seek(downloaded);
            stream = connection.getInputStream();
            while (status == DOWNLOADING) {
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }
                int read = stream.read(buffer);
                if (read == -1) break;
                file.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}
