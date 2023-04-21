package org.spark.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataSource;

public class StreamDataSource implements DataSource {

    public static final int BUFFER_SIZE = 512;

    private ByteArrayOutputStream baos;

    private String type = "application/octet-stream";

    public StreamDataSource(byte[] data, String mimeType) throws IOException {
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(data);
            this.byteArrayDataSource(bis, mimeType);
        } catch (IOException ioex) {
            throw ioex;
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }

    public StreamDataSource(InputStream aIs, String mimeType) throws IOException {
        this.byteArrayDataSource(aIs, mimeType);
    }

    public StreamDataSource(String data, String mimeType) throws IOException {
        this.type = mimeType;
        try {
            baos = new ByteArrayOutputStream();
            baos.write(data.getBytes());
            baos.flush();
            baos.close();
        } catch (UnsupportedEncodingException uex) {
            throw new IOException("The Character Encoding is not supported.");
        } finally {
            if (baos != null) {
                baos.close();
            }
        }
    }

    public StreamDataSource(String data, String encoding, String mimeType) throws IOException {
        this.type = mimeType;
        try {
            baos = new ByteArrayOutputStream();
            baos.write(data.getBytes(encoding));
            baos.flush();
            baos.close();
        } catch (UnsupportedEncodingException uex) {
            throw new IOException("The Character Encoding is not supported.");
        } finally {
            if (baos != null) {
                baos.close();
            }
        }
    }

    private void byteArrayDataSource(InputStream aIs, String mimeType) throws IOException {
        this.type = mimeType;
        BufferedInputStream bis = null;
        BufferedOutputStream osWriter = null;
        try {
            int length = 0;
            byte[] buffer = new byte[StreamDataSource.BUFFER_SIZE];
            bis = new BufferedInputStream(aIs);
            baos = new ByteArrayOutputStream();
            osWriter = new BufferedOutputStream(baos);
            while ((length = bis.read(buffer)) != -1) {
                osWriter.write(buffer, 0, length);
            }
            osWriter.flush();
            osWriter.close();
        } catch (IOException ioex) {
            throw ioex;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (baos != null) {
                baos.close();
            }
            if (osWriter != null) {
                osWriter.close();
            }
        }
    }

    public String getContentType() {
        return type == null ? "application/octet-stream" : type;
    }

    public InputStream getInputStream() throws IOException {
        if (baos == null) {
            throw new IOException("no data");
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public String getName() {
        return "ByteArrayDataSource";
    }

    public OutputStream getOutputStream() {
        baos = new ByteArrayOutputStream();
        return baos;
    }
}
