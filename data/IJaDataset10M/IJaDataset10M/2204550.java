package com.lzy.jmail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import org.apache.commons.fileupload.FileItem;

public class UploadFileDataSource implements DataSource {

    private FileItem uploadfileitem;

    public UploadFileDataSource() {
    }

    public UploadFileDataSource(FileItem uploadfileitem) {
        this.uploadfileitem = uploadfileitem;
    }

    public String getContentType() {
        return uploadfileitem.getContentType();
    }

    public InputStream getInputStream() throws IOException {
        return uploadfileitem.getInputStream();
    }

    public String getName() {
        return uploadfileitem.getName();
    }

    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    public FileItem getUploadfileitem() {
        return uploadfileitem;
    }

    public void setUploadfileitem(FileItem uploadfileitem) {
        this.uploadfileitem = uploadfileitem;
    }
}
