package org.t2framework.confeito.contexts.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.t2framework.confeito.contexts.UploadFile;
import org.t2framework.confeito.util.CloseableUtil;
import org.t2framework.confeito.util.StreamUtil;

/**
 * <#if locale="en">
 * <p>
 * This class depends on Apache commons fileupload.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public class StreamUploadFileImpl implements UploadFile {

    protected String contentType;

    protected String name;

    protected InputStream is;

    public StreamUploadFileImpl(InputStream is, String contentType, String name) throws IOException {
        this.is = is;
        this.contentType = contentType;
        this.name = name;
    }

    public byte[] get() {
        byte[] ret = new byte[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            StreamUtil.copy(this.is, baos);
            ret = baos.toByteArray();
        } finally {
            CloseableUtil.close(baos);
        }
        return ret;
    }

    public String getContentType() {
        return this.contentType;
    }

    public InputStream getInputStream() {
        return this.is;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return get().length;
    }

    public String getString() throws UnsupportedEncodingException {
        return getString("UTF-8");
    }

    public String getString(String encoding) throws UnsupportedEncodingException {
        return new String(get(), encoding);
    }

    public String toString() {
        return getName();
    }

    protected String getBaseFileName(String filePath) {
        String fileName = new File(filePath).getName();
        int colonIndex = fileName.indexOf(":");
        if (colonIndex == -1) {
            colonIndex = fileName.indexOf("\\\\");
        }
        int backslashIndex = fileName.lastIndexOf("\\");
        if (-1 < colonIndex && -1 < backslashIndex) {
            fileName = fileName.substring(backslashIndex + 1);
        }
        return fileName;
    }

    @Override
    public void close() {
        if (this.is != null) {
            CloseableUtil.close(is);
        }
    }
}
