package org.wikiup.modules.fileupload.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import org.apache.commons.fileupload.FileItem;

public class UploadFileItemBlob implements Blob {

    private FileItem fileItem;

    public UploadFileItemBlob(FileItem item) {
        fileItem = item;
    }

    public long length() throws SQLException {
        return fileItem.getSize();
    }

    public byte[] getBytes(long pos, int length) throws SQLException {
        return fileItem.get();
    }

    public InputStream getBinaryStream() throws SQLException {
        try {
            return fileItem.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    public long position(byte pattern[], long start) throws SQLException {
        return 0;
    }

    public long position(Blob pattern, long start) throws SQLException {
        return 0;
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException {
        return 0;
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        return 0;
    }

    public OutputStream setBinaryStream(long pos) throws SQLException {
        try {
            return fileItem.getOutputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    public void truncate(long len) throws SQLException {
    }

    public void free() throws SQLException {
    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        return null;
    }
}
