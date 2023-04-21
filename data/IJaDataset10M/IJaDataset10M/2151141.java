package com.ekeyman.securesavelib.dto;

import java.io.InputStream;

public class FileDownloadResponse {

    private InputStream fileInputStream;

    private String filename;

    private String filetype;

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }
}
